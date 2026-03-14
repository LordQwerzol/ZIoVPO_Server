package ZIoVPO.ZIoVPO_Server.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "license_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LicenseType {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    private String name;

    private int defaultDurationDay;

    private String description;
}
