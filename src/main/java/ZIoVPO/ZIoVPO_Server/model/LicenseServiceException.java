package ZIoVPO.ZIoVPO_Server.model;

import lombok.Getter;

@Getter
public class LicenseServiceException extends RuntimeException {
    private final int code;

    public LicenseServiceException(String message, int code) {
        super(message);
        this.code = code;
    }

}
