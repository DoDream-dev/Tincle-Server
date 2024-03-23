package tinqle.tinqleServer.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.domain.account.exception.AuthException;

import java.util.List;

@Getter
@AllArgsConstructor
public enum Device {
    ANDROID(List.of("1.1.6")),
    APPLE(List.of("1.1.6"));

    private final List<String> versions;

    public static Device toEntity(String deviceType) {
        return switch (deviceType.toUpperCase()) {
            case "ANDROID" -> ANDROID;
            case "APPLE" -> APPLE;
            default -> throw new AuthException(StatusCode.DEVICE_TYPE_ERROR);
        };
    }

}
