package week5.day02;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleDateFormatDemo1 {
    static void main(String[] args) throws ParseException {
        //利用空参构造创建一个SimpleDateFormat对象,默认格式,也可以传入格式
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date();  //创建一个当前时间的Date对象
        String format = sdf1.format(date);   //利用sdf的format方法格式化date对象
        System.out.println(format);

        //定义一个字符串来表示时间
        String str = "2011-11-11 11:11:11";
        //细节:创建的SimpleDateFormat对象格式应与字符串格式完全一样
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = sdf2.parse(str);  //将str解析成了Date类型
        System.out.println(d);     //解析后我们就可以用getTime方法来获得时间的毫秒值,来进行一些计算
    }
}
