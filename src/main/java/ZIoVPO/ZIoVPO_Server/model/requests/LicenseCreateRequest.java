package ZIoVPO.ZIoVPO_Server.model.requests;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LicenseCreateRequest {

    @NotBlank()
    @Email()
    private String ownerEmail;

    @NotBlank()
    private String licenseType;

    @NotBlank()
    private String product;

}
