package week5.day02;

import java.util.regex.Pattern;

public class PatternMatcher5 {
    static void main(String[] args) {
        String str = "原神dawytdtah123崩坏:星穹铁道akdaodalLL412绝区零";

        String newStr = str.replaceAll("[\\w&&[^_]]+", "and");  //按照正则表达式的规则进行替换
        System.out.println(newStr);

        String str2 = "原神&崩坏:星穹铁道&绝区零";
        String[] split = str2.split("&");    //按照正则表达式的规则进行切割,并以数组的形式进行返回
        for (String s : split) {
            System.out.println(s);
        }
    }
}
