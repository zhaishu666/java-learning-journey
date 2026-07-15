package week5.day02;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleDateFormatDemo2 {
    static void main(String[] args) throws ParseException {
        String str = "2000-11-11";
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        Date p = sdf1.parse(str);  //将字符串解构成date类型

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日");
        String newTime = sdf2.format(p);  //format方法格式化date类型的对象,转化为字符串
        System.out.println(newTime);
    }
}
