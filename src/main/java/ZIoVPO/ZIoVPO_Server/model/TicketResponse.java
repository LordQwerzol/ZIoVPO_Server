package ZIoVPO.ZIoVPO_Server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TicketResponse {
    private Ticket ticket;
    private String signature;
}
