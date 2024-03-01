package tinqle.tinqleServer.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomDateUtil {

    public static String resolveElapsedTime(LocalDateTime localDateTime) {
        LocalDateTime now = LocalDateTime.now();

        Duration between = Duration.between(localDateTime, now);

        if (between.toMinutes() == 0) return "방금";
        else if (between.toHours() == 0) return "%d분".formatted(between.toMinutes());
        else if (between.toDays() == 0) return "%d시간".formatted(between.toHours());
        else return localDateTime.format(DateTimeFormatter.ofPattern("M월 dd일"));
    }

    public static String resolveStatusElapsedTime(LocalDateTime localDateTime) {
        LocalDateTime now = LocalDateTime.now();

        Duration between = Duration.between(localDateTime, now);

        if (between.toMinutes() == 0) return "방금";
        else if (between.toHours() == 0) return "%d분".formatted(between.toMinutes());
        else if (between.toDays() == 0) return "%d시간".formatted(between.toHours());
        else return "%d일".formatted(between.toDays());
    }

    public static String resolveDateFromDateTime(LocalDateTime localDateTime) {
        LocalDateTime now = LocalDateTime.now();

        if (now.toLocalDate().isEqual(localDateTime.toLocalDate())) return "오늘";
        else return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

}
