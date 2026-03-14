package ZIoVPO.ZIoVPO_Server.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LicenseActivateRequest {

    @NotBlank()
    @Pattern(regexp = "^([0-9A-Za-z]{5}-){3}[0-9A-Za-z]{5}$")
    private String activateCod;

    @NotBlank()
    @Pattern(regexp = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$")
    private String macAddress;

}
