package week5.day02;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleDateFormatDemo3{
    static void main(String[] args) throws ParseException {

        String str1 = "2023年11月11日 00:00:00";
        String str2 = "2023年11月11日 00:10:00";
        String str3 = "2023年11月11日 00:01:00";
        String str4 = "2023年11月11日 00:11:00";

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");


        Date date1 = sdf1.parse(str1);
        Date date2 = sdf1.parse(str2);
        Date date3 = sdf1.parse(str3);
        Date date4 = sdf1.parse(str4);

        long time1 = date1.getTime();
        long time2 = date2.getTime();
        long time3 = date3.getTime();
        long time4 = date4.getTime();

        checkTime(time1, time2, time3);
        checkTime(time1, time2, time4);

    }

    public static void checkTime(long time1, long time2 , long time) {
        if (time > time1 && time < time2){
            System.out.println("抢到了");
        }else{
            System.out.println("没抢到");
        }
    }
}
