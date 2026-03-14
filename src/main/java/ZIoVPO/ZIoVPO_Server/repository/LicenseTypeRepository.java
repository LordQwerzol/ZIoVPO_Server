package ZIoVPO.ZIoVPO_Server.repository;

import ZIoVPO.ZIoVPO_Server.entity.LicenseType;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LicenseTypeRepository extends JpaRepository<LicenseType, UUID> {
    LicenseType findByName(@NotBlank() String typeName);
}
