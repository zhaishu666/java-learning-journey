package week5.day02;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternMatcher3 {
    static void main(String[] args) {
        String str = "自Java问世以来,很多公司都在使用该语言" +
                "现在很多公司都在使用Java8和Java11,但有不少公司都已经在开始使用Java17了";

        String regex1 = "((?i)Java)(?=8|11|17)";  //获取所有的Java版本但去掉后面的版本内容
        String regex2 = "((?i)Java)(?:8|11|17)";  //获取所有的Java版本不去掉后面的内容
        String regex3 = "((?i)Java)(?!8|11|17)";  //获取所有不带版本的Java

        Pattern p = Pattern.compile(regex3);
        Matcher m = p.matcher(str);
        while (m.find()) {
            System.out.println(m.group());
        }
    }
}
