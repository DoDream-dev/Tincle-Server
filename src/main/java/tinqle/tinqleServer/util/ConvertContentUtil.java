package tinqle.tinqleServer.util;

public class ConvertContentUtil {

    public static final int ZERO_VALUE = 0;
    public static final int FIFTEEN_VALUE = 15;
    public static final String ETC = "..";
    public static final String ONLY_IMAGE_CONTENT = "내가 올린 이미지";
    public static final String DOUBLE_QUOTATION_MARKS = "\"";

    public static String convertContent(String content) {
        if (content.isBlank())
            return ONLY_IMAGE_CONTENT;

        if (content.length() < 15) {
            return DOUBLE_QUOTATION_MARKS+content+DOUBLE_QUOTATION_MARKS;
        }

        return DOUBLE_QUOTATION_MARKS+content.substring(ZERO_VALUE, FIFTEEN_VALUE)+ETC+DOUBLE_QUOTATION_MARKS;
    }
}
