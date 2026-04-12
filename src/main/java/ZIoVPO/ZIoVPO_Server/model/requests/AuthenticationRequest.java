package ZIoVPO.ZIoVPO_Server.model.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationRequest {

    @NotBlank()
    private String email;

    @NotBlank()
    private String password;

    @NotBlank()
    @Pattern(regexp = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$")
    private String macAddress;

}
