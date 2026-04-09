package ZIoVPO.ZIoVPO_Server.model.enums;

import lombok.Getter;

@Getter
public enum ExportType {
    FULL(0),
    INCREMENT(1),
    BY_IDS(2);

    private final int code;

    ExportType(int code) {
        this.code = code;
    }

}
