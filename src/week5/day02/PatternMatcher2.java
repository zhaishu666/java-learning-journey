package week5.day02;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternMatcher2 {
    static void main(String[] args) {
        String str = "来黑马程序员学习Java,电话号码为13112345678,18512508907" +
                "座机号码为01036517895,010-98951256" +
                "联系邮箱: boniu@itcast.cn," +
                "邮箱: bozai@itcast.cn," +
                "热线电话: 400-618-9090, 4006184000";

        String regex = "(1[3-9]\\d{9})|(\\w+@[\\w&&[^_]]{2,6}(\\.[a-zA-Z]{2,3}){1,2})|(0\\d{2,3}-?[1-9]\\d{4,9})|(400-?[1-9]\\d{2}-?[1-9]\\d{3})";
        Pattern p = Pattern.compile(regex);

        Matcher m = p.matcher(str);
        while (m.find()) {
            System.out.println(m.group());
        }
    }
}
