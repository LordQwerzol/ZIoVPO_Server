package ZIoVPO.ZIoVPO_Server.repository;

import ZIoVPO.ZIoVPO_Server.entity.Product;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Product findByName(@NotBlank() String product);
}
