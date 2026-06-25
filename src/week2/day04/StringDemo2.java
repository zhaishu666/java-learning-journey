package week2.day04;

import java.util.Scanner;

public class StringDemo2 {
    static void main() {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入一个字符串");
        String str = sc.next();

        for (int i = 0; i < str.length(); i++) {  //字符串需要.length().fori
            //i依次表示字符串的每一个索引
            char c = str.charAt(i);
            System.out.println(c);
        }
    }
}
