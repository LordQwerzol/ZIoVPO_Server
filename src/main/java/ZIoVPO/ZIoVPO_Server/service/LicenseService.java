package ZIoVPO.ZIoVPO_Server.service;

import ZIoVPO.ZIoVPO_Server.entity.*;
import ZIoVPO.ZIoVPO_Server.model.records.Ticket;
import ZIoVPO.ZIoVPO_Server.model.requests.LicenseActivateRequest;
import ZIoVPO.ZIoVPO_Server.model.requests.LicenseCheckRequest;
import ZIoVPO.ZIoVPO_Server.model.requests.LicenseCreateRequest;
import ZIoVPO.ZIoVPO_Server.repository.ApplicationUserRepository;
import ZIoVPO.ZIoVPO_Server.repository.DeviceLicenseRepository;
import ZIoVPO.ZIoVPO_Server.repository.LicenseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;


@Service
@Transactional
@RequiredArgsConstructor
public class LicenseService {

    // Для работы с БД
    private final ProductService productService;
    private final LicenseTypeService licenseTypeService;
    private final LicenseHistoryService licenseHistoryService;
    private final DeviceService deviceService;
    private final ApplicationUserRepository applicationUserRepository;
    private final LicenseRepository licenseRepository;
    private final DeviceLicenseRepository deviceLicenseRepository;

    // Переменные для генерации ключа
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int GROUP_SIZE = 5;
    private static final int GROUP_COUNT = 4;
    private static final char SEPARATOR = '-';
    private static final Random RANDOM = new SecureRandom();

    // Переменная для Ticket
    @Value("${ticket.expiration}")
    private int ticketExpiration;

    public String createLicense(LicenseCreateRequest request, UserDetails currentUser) {
        // 1. Блок проверок
        Product product = productService.getProduct(request.getProduct());
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.valueOf(404), "Product not found: " + request.getProduct());
        }
        if (product.isBlocked()) {
            throw new ResponseStatusException(HttpStatus.valueOf(403), "Product is blocked: " + product.getName());
        }
        LicenseType type = licenseTypeService.getLicenseType(request.getLicenseType());
        if (type == null) {
            throw new ResponseStatusException(HttpStatus.valueOf(404), "License type not found: " + request.getLicenseType());
        }
        ApplicationUser owner = applicationUserRepository.findByEmail(request.getOwnerEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Owner not found: " + request.getOwnerEmail()));
        ApplicationUser actor = applicationUserRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Actor not found: " + currentUser.getUsername()));
        // 2. Создание
        License newLicense = createLicense(product, type, owner);
        // 3. Транзакция
        licenseRepository.save(newLicense);
        licenseHistoryService.creation(newLicense, actor);
        return newLicense.getCode();
    }

    public Ticket activateLicense(LicenseActivateRequest request, UserDetails currentUser) {
        // 1. Блок проверок
        ApplicationUser actor = applicationUserRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Actor not found: " + currentUser.getUsername()));
        License license = licenseRepository.findByCode(request.getActivateCod());
        if (license == null) {
            throw new ResponseStatusException(HttpStatus.valueOf(404), "License not found");
        }
        if (!actor.equals(license.getOwner())){
            throw new ResponseStatusException(HttpStatus.valueOf(403), "License owned by another user");
        }
        Device device = deviceService.getDevice(request.getMacAddress());
        if (device == null) {
            device = deviceService.buildDevice(actor, request.getMacAddress());
        }
        // 2. Активация
        boolean isFirst = false;
        LocalDate now = LocalDate.now();
        // Первая активация
        if (license.getUser() == null) {
            LocalDate ending = now.plusDays(license.getType().getDefaultDurationDay());
            license.setUser(actor);
            license.setFirst_activation_date(now);
            license.setEnding_date(ending);
            licenseRepository.save(license);
            isFirst = true;
        }
        // Проверка кол-ва устройств
        if (! (isFirst || checkDeviceLimit(license))) {
            throw new ResponseStatusException(HttpStatus.valueOf(409), "Device limit reached");
        }
        // 3. Транзакция
        DeviceLicense deviceLicense = DeviceLicense.builder().device(device).license(license).activationDate(now).build();
        deviceLicenseRepository.save(deviceLicense);
        licenseHistoryService.activation(license, actor);
        // 4. Формирование тикета
        return buildTicket(license, request.getMacAddress());
    }

    public Ticket renewLicense(LicenseActivateRequest request, UserDetails currentUser) {
        // 1. Блок проверок
        ApplicationUser actor = applicationUserRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Actor not found: " + currentUser.getUsername()));
        License license = licenseRepository.findByCode(request.getActivateCod());
        if (license == null) {
            throw new ResponseStatusException(HttpStatus.valueOf(404), "License not found");
        }
        if (!actor.equals(license.getOwner())){
            throw new ResponseStatusException(HttpStatus.valueOf(403), "License owned by another user");
        }
        // 2. Условие для продления
        if (checkRenewability(license)) {
            throw new ResponseStatusException(HttpStatus.valueOf(409), "Renewal not allowed");
        }
        // 3. Продление
        license.setEnding_date(LocalDate.now().plusDays(license.getType().getDefaultDurationDay()));
        licenseRepository.save(license);
        licenseHistoryService.renewanation(license, actor);
        // 4. Формирование тикета
        return buildTicket(license, request.getMacAddress());
    }

    public Ticket checkLicense(LicenseCheckRequest request, UserDetails currentUser) {
        ApplicationUser actor = applicationUserRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Actor not found: " + currentUser.getUsername()));
        Device device = deviceService.getDevice(request.getMacAddress());
        if (device == null) {
            throw new ResponseStatusException(HttpStatus.valueOf(404), "Device not found");
        }
        Product product = productService.getProduct(request.getProductName());
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.valueOf(404), "Product not found: " + request.getProductName());
        }
        License license = licenseRepository.findActiveByDeviceUserAndProduct(device, actor, product);
        if (license == null) {
            throw new ResponseStatusException(HttpStatus.valueOf(404), "License not found");
        }
        return buildTicket(license, request.getMacAddress());
    }

    private boolean checkRenewability(License license) {
        if (license.isBlocked()) {
            return false;
        }
        LocalDate endingDateActivation = license.getEnding_date();
        if (endingDateActivation == null) {
            return false;
        }
        LocalDate now = LocalDate.now();
        long daysUntil = ChronoUnit.DAYS.between(now, endingDateActivation);
        return daysUntil >= 0 && daysUntil <= 7;
    }

    private Ticket buildTicket(License license, String macAddress) {
        return Ticket.builder()
                .serverDate(LocalDateTime.now())
                .timeLifeSec(ticketExpiration)
                .activationDate(license.getFirst_activation_date())
                .expirationDate(license.getEnding_date())
                .userEmail(license.getOwner().getEmail())
                .macAddress(macAddress)
                .isBlocked(license.isBlocked())
                .build();
    }

    private boolean checkDeviceLimit(License license) {
        return deviceLicenseRepository.countByLicense(license) < license.getDeviceCnt();
    }

    private License createLicense(Product product, LicenseType type, ApplicationUser owner) {
        return License.builder()
                .code(generateCode())
                .user(null)
                .product(product)
                .type(type)
                .isBlocked(false)
                .deviceCnt(5)
                .owner(owner)
                .build();
    }

    private String generateCode() {
        String code;
        do {
            StringBuilder sb = new StringBuilder();
            for (int group = 0; group < GROUP_COUNT; group++) {
                for (int i = 0; i < GROUP_SIZE; i++) {
                    sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
                }
                if (group < GROUP_COUNT - 1) {
                    sb.append(SEPARATOR);
                }
            }
            code = sb.toString();
        } while (licenseRepository.existsByCode(code));
        return code;
    }

}

