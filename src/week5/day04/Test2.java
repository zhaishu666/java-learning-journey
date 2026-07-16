package week5.day04;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String regex = "[1-9]\\d{0,9}";
        System.out.println("输入一个长度为1~10的整数字符串");
        String str = sc.nextLine();

        //开法中建议先过滤错误的数据
        boolean flag = str.matches(regex);  //验证str是否符合regex正则规则
        if (!flag) {
            System.out.println("输入的字符串不合法");
        }else {
            int result = Integer.parseInt(str);
            System.out.println(result);
        }
    }
}
