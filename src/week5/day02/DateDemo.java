package week5.day02;

import java.util.Date;

public class DateDemo {
    static void main() {
        Date date1 = new Date();  //空参构造创建的是当前系统的时间
        System.out.println(date1);

        Date date2 = new Date(0L);  //0表示时间原点,类型为long的毫秒值
        System.out.println(date2);

        long time = 1000L * 60 * 60 * 24 * 365;
        date2.setTime(time);
        System.out.println(date2);
    }
}
