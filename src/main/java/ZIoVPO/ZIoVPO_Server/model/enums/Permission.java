package ZIoVPO.ZIoVPO_Server.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Permission {

    READ("read"),

    CREATE("create"),

    UPDATE("update"),

    DELETE("delete");

    private final String permission;
}
