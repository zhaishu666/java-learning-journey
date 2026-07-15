package week5.day03;

import java.util.Calendar;
import java.util.Date;

public class CalendarDemo1 {
    static void main(String[] args) {
        Calendar c = Calendar.getInstance();  //调用方法获取Calendar的对象
        Date date = new Date(0L);
        c.setTime(date);

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;   //月份的范围是0~11,如果要表示某个月,得+1
        int day = c.get(Calendar.DAY_OF_MONTH);

        System.out.println("Year: " + year +" Month: " + month + " Day: " + day );

        c.setTimeInMillis(date.getTime());  //获取date中的日期对象的毫秒值
        System.out.println(c.getTimeInMillis());

        c.set(Calendar.MONTH, 11); //值得注意的是,这里的时间超出限制并不会报错,而是将这个时间计算加上
        //11带表12月
        System.out.println(c.get(Calendar.MONTH));
        c.add(Calendar.DAY_OF_MONTH, -1); //输入负数则会在原有时间上减去对应的时间
    }
}
