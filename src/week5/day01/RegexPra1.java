package week5.day01;

import java.util.Scanner;

public class RegexPra1 {

    static void main() {

        Scanner sc = new Scanner(System.in);
        //checkPhoneNumber(sc);
        System.out.println(checkEmail(sc));
        /*String regex = "0\\d{2,3}-?[1-9]\\d{4,9}";
        System.out.println("0553-123456".matches(regex));*/
        String regex = "(1[0-2]|0?[1-9]):[0-5]\\d:[0-5]\\d";
    }

    public static boolean checkPhoneNumber(Scanner sc) {
        System.out.println("请输入您的手机号码");
        String phoneNumber = sc.next();
        if (!phoneNumber.matches("1\\d{10}")) {
            System.out.println("错误手机号,请重新输入");
            return false;
        } else
            System.out.println("正确");
        return true;
    }

    public static boolean checkEmail(Scanner sc) {
        System.out.println("请输入你的qq邮箱");
        String email = sc.next();
        if(email.matches("\\w+@[\\w[^_]]+(\\.[\\w[^_]]+){1,2}")) {
            return true;
        }
        return false;
    }
}
