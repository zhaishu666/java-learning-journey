package week5.day03;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeFormatterDemo {
    static void main(String[] args) {
        //创建带时区的时间对象
        ZonedDateTime time = ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss EE a");
        System.out.println(dtf.format(time));
    }
}
