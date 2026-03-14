package ZIoVPO.ZIoVPO_Server.repository;
import ZIoVPO.ZIoVPO_Server.entity.ApplicationUser;
import ZIoVPO.ZIoVPO_Server.entity.LicenseHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LicenseHistoryRepository extends JpaRepository<LicenseHistory, UUID> {
}
