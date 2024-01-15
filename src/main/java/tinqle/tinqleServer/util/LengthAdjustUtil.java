package tinqle.tinqleServer.util;

public class LengthAdjustUtil {

    public static final int ZERO_VALUE = 0;
    public static final int FIFTEEN_VALUE = 15;
    public static final String ETC = "..";

    public static String adjustLengthFifteen(String content) {
        if (content.length() < 15) {
            return content;
        }

        return content.substring(ZERO_VALUE, FIFTEEN_VALUE)+ETC;
    }
}
