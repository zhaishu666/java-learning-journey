package week5.day02;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternMatcher4 {
    static void main() {
        String str = "自Java问世以来,很多公司都在使用该语言,abbbbbbbbbbbaaaaaaaaaa" +
                "现在很多公司都在使用Java8和Java11,但有不少公司都已经在开始使用Java17了";

        String regex1 = "ab+";   //此时是java默认的贪婪爬取,java会尽可能多的获取b
        String regex2 = "ab+?";   //此时是非贪婪爬取,我们只需要在数量词后面加上问号,java就会尽可能少获取b

        Pattern p = Pattern.compile(regex2);
        Matcher m = p.matcher(str);
        while (m.find()) {
            System.out.println(m.group());
        }
    }
}
