package week2.day05;

import java.util.Scanner;
import java.util.StringJoiner;

//练习StringJoiner的各种方法
public class StringJoinerDemo1 {
    static void main() {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入字符串");
        String str = sc.next();

        StringJoiner sj = new StringJoiner("");
        sj.add(str);
        System.out.println(sj);

        int length = sj.length();
        System.out.println(length);

        String sjStr = sj.toString();
        System.out.println(sjStr);
    }
}
