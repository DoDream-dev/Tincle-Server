package tinqle.tinqleServer.util;

import java.util.Random;

public class UuidGenerateUtil {

    private final static int LENGTH_INT_RADIX = 10;
    private final static int leftLimit = 48; // numeral '0'
    private final static int rightLimit = 122; // letter 'z'

    public static String makeRandomUuid() {

        Random random = new Random();
        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(LENGTH_INT_RADIX)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
