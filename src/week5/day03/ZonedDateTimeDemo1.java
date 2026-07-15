package week5.day03;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ZonedDateTimeDemo1 {
    static void main() {
        ZonedDateTime now = ZonedDateTime.now();  //创建一个当前时区的ZonedDateTime对象
        //System.out.println(now);

        //获取指定时间的对象,根据年,月,日,时,分,秒,纳秒,时区
        ZonedDateTime zdt1 = ZonedDateTime.of(2026, 7, 15,
                15, 25, 50, 0, ZoneId.of("Asia/Shanghai"));
        //System.out.println(zdt1);

        //通过Instant加时区的方式获取对象
        Instant instant = Instant.ofEpochMilli(0L);
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt2 = ZonedDateTime.ofInstant(instant, zoneId);
        //System.out.println(zdt2);

        ZonedDateTime zdt3 = zdt1.withYear(2018);//这时是创建了一个全新的对象zdt1的值不会修改
        System.out.println(zdt1);
        System.out.println(zdt3);

        ZonedDateTime zdt4 = zdt1.minusYears(1L);
        ZonedDateTime zdt5 = zdt1.plusYears(1L);
        System.out.println(zdt4);
        System.out.println(zdt5);

    }
}
