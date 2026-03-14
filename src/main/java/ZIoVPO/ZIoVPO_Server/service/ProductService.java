package ZIoVPO.ZIoVPO_Server.service;

import ZIoVPO.ZIoVPO_Server.entity.Product;
import ZIoVPO.ZIoVPO_Server.repository.ProductRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Product getProduct(@NotBlank() String productName) {
        return productRepository.findByName(productName);
    }
}
