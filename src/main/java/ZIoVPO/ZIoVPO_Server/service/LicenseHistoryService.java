package ZIoVPO.ZIoVPO_Server.service;

import ZIoVPO.ZIoVPO_Server.entity.ApplicationUser;
import ZIoVPO.ZIoVPO_Server.entity.License;
import ZIoVPO.ZIoVPO_Server.entity.LicenseHistory;
import ZIoVPO.ZIoVPO_Server.model.enums.LicenseStatus;
import ZIoVPO.ZIoVPO_Server.repository.LicenseHistoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class LicenseHistoryService {
    private final LicenseHistoryRepository licenseHistoryRepository;

    public void creation(License license, ApplicationUser actor) {
        LicenseHistory record = LicenseHistory.builder()
                .license(license)
                .user(actor)
                .status(LicenseStatus.CREATED)
                .changeDate(LocalDateTime.now())
                .build();
        licenseHistoryRepository.save(record);
    }

    public void activation(License license, ApplicationUser actor) {
        LicenseHistory record = LicenseHistory.builder()
                .license(license)
                .user(actor)
                .status(LicenseStatus.ACTIVE)
                .changeDate(LocalDateTime.now())
                .build();
        licenseHistoryRepository.save(record);
    }

    public void renewanation(License license, ApplicationUser actor) {
        LicenseHistory record = LicenseHistory.builder()
                .license(license)
                .user(actor)
                .status(LicenseStatus.RENEWED)
                .changeDate(LocalDateTime.now())
                .build();
        licenseHistoryRepository.save(record);
    }
}
