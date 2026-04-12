package ZIoVPO.ZIoVPO_Server.service;

import ZIoVPO.ZIoVPO_Server.entity.ApplicationUser;
import ZIoVPO.ZIoVPO_Server.entity.Device;
import ZIoVPO.ZIoVPO_Server.repository.DeviceRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class DeviceService {
    private final DeviceRepository deviceRepository;

    public Device getDevice(@NotBlank() @Pattern(regexp = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$") String macAddress) {
        return deviceRepository.findByMacAddress(macAddress);
    }

    public Device buildDevice(ApplicationUser actor, @NotBlank() @Pattern(regexp = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$") String macAddress) {

        int currentCount = deviceRepository.countByUser(actor);

        String name = "Device_" + actor.getName() + "_" + (currentCount + 1);

        Device device = Device.builder()
                .user(actor)
                .name(name)
                .macAddress(macAddress)
                .build();
        deviceRepository.save(device);

        return device;
    }
}
