package week5.day03;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class InstantDemo1 {
    static void main(String[] args) {
        Instant now = Instant.now();  //获取当前时间的标准时间的Instant对象
        //System.out.println(now);

        Instant instant1 = Instant.ofEpochSecond(1L);  //根据秒钟来获取Instant对象,表示过时间原点1秒
        //System.out.println(instant1);

        ZonedDateTime zonedDateTime = now.atZone(ZoneId.of("Asia/Shanghai"));
        //指定时区,可以解决now方法获得的对象与当前时区时间不同步的问题
        System.out.println(zonedDateTime);  //此时为当地时间

        Instant instant2 = Instant.ofEpochMilli(1000L);
        Instant instant3 = Instant.ofEpochMilli(2000L);
        boolean result = instant2.isBefore(instant3);  //isBefore判断instant3时间是否在2之前
        System.out.println(result);

        Instant instant4 = instant2.minusMillis(1000L);  //减去1000毫秒,这种加减不会改变原来的instant2
        Instant instant5 = instant3.plusMillis(2000L);
        System.out.println(instant4);
        System.out.println(instant5);
    }
}
