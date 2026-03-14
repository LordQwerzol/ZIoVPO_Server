package ZIoVPO.ZIoVPO_Server.repository;
import ZIoVPO.ZIoVPO_Server.entity.ApplicationUser;
import ZIoVPO.ZIoVPO_Server.entity.Device;
import ZIoVPO.ZIoVPO_Server.entity.License;
import ZIoVPO.ZIoVPO_Server.entity.Product;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LicenseRepository extends JpaRepository<License, UUID> {
    boolean existsByCode(String code);

    License findByCode(@NotBlank() String activateCod);


    @Query("SELECT l FROM License l " +
           "JOIN DeviceLicense dl ON l.id = dl.license.id " +
           "WHERE dl.device = :device " +
           "AND l.user = :user " +
           "AND l.product = :product ")
    License findActiveByDeviceUserAndProduct(@Param("device") Device device,
                                             @Param("user") ApplicationUser user,
                                             @Param("product") Product product);
}
