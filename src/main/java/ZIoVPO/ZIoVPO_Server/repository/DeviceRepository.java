package ZIoVPO.ZIoVPO_Server.repository;

import ZIoVPO.ZIoVPO_Server.entity.ApplicationUser;
import ZIoVPO.ZIoVPO_Server.entity.Device;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {
    Device findByMacAddress(@NotBlank() @Pattern(regexp = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$") String macAddress);

    int countByUser(ApplicationUser actor);
}
