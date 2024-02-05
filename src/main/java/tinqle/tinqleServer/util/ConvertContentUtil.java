package tinqle.tinqleServer.util;

public class ConvertContentUtil {

    public static final int ZERO_VALUE = 0;
    public static final int FIFTEEN_VALUE = 15;
    public static final String ETC = "..";
    public static final String ONLY_IMAGE_CONTENT = "내가 올린 이미지";

    public static String convertContent(String content) {
        if (content.isBlank())
            return ONLY_IMAGE_CONTENT;

        if (content.length() < 15) {
            return content;
        }

        return content.substring(ZERO_VALUE, FIFTEEN_VALUE)+ETC;
    }
}
