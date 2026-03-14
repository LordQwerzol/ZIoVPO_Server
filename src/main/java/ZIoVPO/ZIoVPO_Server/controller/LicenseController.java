package ZIoVPO.ZIoVPO_Server.controller;

import ZIoVPO.ZIoVPO_Server.model.*;
import ZIoVPO.ZIoVPO_Server.service.LicenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/license")
public class LicenseController {

    private final LicenseService service;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createLicense(@RequestBody LicenseCreateRequest request,
                                           @AuthenticationPrincipal UserDetails currentUser) {
        try {
            String licenseCode = service.createLicense(request, currentUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(licenseCode);
        } catch (LicenseServiceException e) {
            HttpStatus status = HttpStatus.resolve(e.getCode());
            if (status == null) {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            return ResponseEntity.status(status).body(e.getMessage());
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
            return ResponseEntity.status(HttpStatus.OK).body(ticket);
        } catch (LicenseServiceException e) {
            HttpStatus status = HttpStatus.resolve(e.getCode());
            if (status == null) {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            return ResponseEntity.status(status).body(e.getMessage());
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
            return ResponseEntity.status(HttpStatus.OK).body(ticket);
        } catch (LicenseServiceException e) {
            HttpStatus status = HttpStatus.resolve(e.getCode());
            if (status == null) {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            return ResponseEntity.status(status).body(e.getMessage());
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
            return ResponseEntity.status(HttpStatus.OK).body(ticket);
        } catch (LicenseServiceException e) {
            HttpStatus status = HttpStatus.resolve(e.getCode());
            if (status == null) {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            return ResponseEntity.status(status).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

}
