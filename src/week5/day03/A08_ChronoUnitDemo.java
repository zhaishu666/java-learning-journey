package week5.day03;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class A08_ChronoUnitDemo {
    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime birthday = LocalDateTime.of(2005, 7, 30, 0, 0, 0);

        System.out.println("相差的年数: " + ChronoUnit.YEARS.between(birthday, now));
        System.out.println("相差的月数: " + ChronoUnit.MONTHS.between(birthday, now));
        System.out.println("相差的天数: " + ChronoUnit.DAYS.between(birthday, now));
        System.out.println("相差的小时数: " + ChronoUnit.HOURS.between(birthday, now));
        System.out.println("相差的分钟数: " + ChronoUnit.MINUTES.between(birthday, now));
        System.out.println("相差的秒数: " + ChronoUnit.SECONDS.between(birthday, now));

    }
}
