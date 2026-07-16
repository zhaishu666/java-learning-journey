package week5.day04;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

public class Test4 {
    public static void main(String[] args) {
        //JDK8后的代码实现活了多少天
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime brithDay = LocalDateTime.of(2005, 7, 30, 0, 0, 0);
        System.out.println(ChronoUnit.DAYS.between(brithDay, now));

        //JDK7的代码实现活了多少天
        Calendar now1 = Calendar.getInstance();
        Calendar now2 = Calendar.getInstance();
        now2.set(2005, 7, 30, 0, 0, 0);
        long t1 = now1.getTimeInMillis();
        long t2 = now2.getTimeInMillis();

        long between = t1 - t2;
        int dayBetween = (int) (between / (24 * 60 * 60 * 1000));
        System.out.println(dayBetween);
    }
}
