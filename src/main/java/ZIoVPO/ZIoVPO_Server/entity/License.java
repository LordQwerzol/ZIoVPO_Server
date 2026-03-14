package ZIoVPO.ZIoVPO_Server.entity;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "licenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class License {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private ApplicationUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private LicenseType type;

    private LocalDate first_activation_date;

    private LocalDate  ending_date;

    private boolean isBlocked;

    private int deviceCnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private ApplicationUser owner;

    private String description;

}
