package tinqle.tinqleServer.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.domain.account.exception.AuthException;

@Getter
@AllArgsConstructor
public enum Device {
    ANDROID("1.1.4"),
    APPLE("1.1.4");

    private final String version;

    public static Device toEntity(String deviceType) {
        return switch (deviceType.toUpperCase()) {
            case "ANDROID" -> ANDROID;
            case "APPLE" -> APPLE;
            default -> throw new AuthException(StatusCode.DEVICE_TYPE_ERROR);
        };
    }

}
