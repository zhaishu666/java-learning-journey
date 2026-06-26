package week2.day05;

import java.util.Scanner;

//定义一个方法,实现字符串反转
public class StringDemo5 {
    static void main() {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入要反转的字符串");
        String str  = sc.next();
        String newStr = reverseStr(str);
        System.out.println(newStr);
    }

    public static String reverseStr(String str){
        String newStr = "";
        for (int i = str.length() - 1; i >= 0; i--) { //str.length.forr倒着遍历
            newStr = newStr + str.charAt(i);
        }
        return newStr;
    }
}
