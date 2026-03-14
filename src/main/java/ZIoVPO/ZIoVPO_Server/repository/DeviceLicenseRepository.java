package ZIoVPO.ZIoVPO_Server.repository;

import ZIoVPO.ZIoVPO_Server.entity.DeviceLicense;
import ZIoVPO.ZIoVPO_Server.entity.License;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeviceLicenseRepository extends JpaRepository<DeviceLicense, UUID> {
    int countByLicense(License license);
}
