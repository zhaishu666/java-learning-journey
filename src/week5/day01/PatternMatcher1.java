package week5.day01;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternMatcher1 {
    static void main(String[] args) {
        String str = "自Java问世以来,很多公司都在使用该语言" +
                "现在很多公司都在使用Java8和Java11,但有不少公司都已经在开始使用Java17了";

        //创建p对象来调用Pattern类中compile方法要获取的文本
        Pattern p = Pattern.compile("Java\\d{0,2}");

        //p调用Matcher中的matcher方法,表示m要在str中寻找符合p规则的小串
        Matcher m = p.matcher(str);

        while (m.find()) {   //find方法记录找到小串的开始和结束索引并记录,此时是包头不包尾的
            System.out.println(m.group());  //方法会根据find所记录的索引来进行字符串的截取
        }
    }
}
