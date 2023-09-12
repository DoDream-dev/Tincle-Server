package tinqle.tinqleServer.util.image;

import lombok.AllArgsConstructor;
import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.util.image.exception.ImageException;

@AllArgsConstructor
public enum S3SaveDir {

    FEED("/feed");

    public final String path;

    public static S3SaveDir toEnum(String stringParam) {
        return switch (stringParam.toLowerCase()) {
            case "feed" -> FEED;

            default -> throw new ImageException(StatusCode.S3_FILE_TYPE_INVALID);
        };
    }
}
