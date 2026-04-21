package ZIoVPO.ZIoVPO_Server.controller;


import ZIoVPO.ZIoVPO_Server.model.records.Ticket;
import ZIoVPO.ZIoVPO_Server.model.requests.LicenseActivateRequest;
import ZIoVPO.ZIoVPO_Server.model.requests.LicenseCheckRequest;
import ZIoVPO.ZIoVPO_Server.model.requests.LicenseCreateRequest;
import ZIoVPO.ZIoVPO_Server.model.responses.TicketResponse;
import ZIoVPO.ZIoVPO_Server.service.LicenseService;
import ZIoVPO.ZIoVPO_Server.signature.SigningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/license")
public class LicenseController {

    private final LicenseService service;
    private final SigningService signingService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createLicense(@RequestBody LicenseCreateRequest request,
                                           @AuthenticationPrincipal UserDetails currentUser) {
        try {
            String licenseCode = service.createLicense(request, currentUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(licenseCode);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('create')")
    @PostMapping("/activate")
    public ResponseEntity<?> activateLicense(@RequestBody LicenseActivateRequest request,
                                             @AuthenticationPrincipal UserDetails currentUser) {
        try {
            Ticket ticket = service.activateLicense(request, currentUser);
            String signature = signingService.sign(ticket);
            return ResponseEntity.status(HttpStatus.OK).body(TicketResponse.builder().ticket(ticket).signature(signature).build());
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @PreAuthorize("hasAuthority('create')")
    @PostMapping("/renew")
    public ResponseEntity<?> renewLicense(@RequestBody LicenseActivateRequest request,
                                          @AuthenticationPrincipal UserDetails currentUser) {
        try {
            Ticket ticket = service.renewLicense(request, currentUser);
            String signature = signingService.sign(ticket);
            return ResponseEntity.status(HttpStatus.OK).body(TicketResponse.builder().ticket(ticket).signature(signature).build());
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @PreAuthorize("hasAuthority('create')")
    @PostMapping("/check")
    public ResponseEntity<?> checkLicense(@RequestBody LicenseCheckRequest request,
                                          @AuthenticationPrincipal UserDetails currentUser) {
        try {
            Ticket ticket = service.checkLicense(request, currentUser);
            String signature = signingService.sign(ticket);
            return ResponseEntity.status(HttpStatus.OK).body(TicketResponse.builder().ticket(ticket).signature(signature).build());
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

}
