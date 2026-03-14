package ZIoVPO.ZIoVPO_Server.service;

import ZIoVPO.ZIoVPO_Server.entity.LicenseType;
import ZIoVPO.ZIoVPO_Server.repository.LicenseRepository;
import ZIoVPO.ZIoVPO_Server.repository.LicenseTypeRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class LicenseTypeService {
    private final LicenseTypeRepository licenseTypeRepository;

    public LicenseType getLicenseType(@NotBlank() String typeName) {
        return licenseTypeRepository.findByName(typeName);
    }
}
