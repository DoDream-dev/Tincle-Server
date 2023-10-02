package tinqle.tinqleServer.domain.emoticon.model;


import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.domain.emoticon.exception.EmoticonException;

public enum EmoticonType {
    HEART,
    SMILE,
    SAD,
    SURPRISE;

    public static EmoticonType toEnum(String emoticonType) {
        return switch (emoticonType.toUpperCase()) {
            case "HEART" -> HEART;
            case "SMILE" -> SMILE;
            case "SAD" -> SAD;
            case "SURPRISE" -> SURPRISE;

            default -> throw new EmoticonException(StatusCode.NOT_FOUND_EMOTICON_TYPE);
        };
    }
}
