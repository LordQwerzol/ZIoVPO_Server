package ZIoVPO.ZIoVPO_Server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class Ticket {
    private LocalDateTime serverDate;
    private int timeLifeSec;
    private LocalDate activationDate;
    private LocalDate expirationDate;
    private String userEmail;
    private String macAddress;
    private boolean isBlocked;
}

