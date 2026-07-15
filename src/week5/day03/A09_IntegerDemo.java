package week5.day03;

import java.util.Scanner;

public class A09_IntegerDemo {
    static void main(String[] args) {
        String s1 = Integer.toBinaryString(100); //获取2进制
        System.out.println(s1); //1100100

        String s2 = Integer.toOctalString(100);  //获取8进制
        System.out.println(s2); //144

        String s3 = Integer.toHexString(100);   //获取16进制
        System.out.println(s3);

        int i = Integer.parseInt(s1,2);
        //8个包装类中除了Character都有对应的parseXxx方法,将其他进制的字符串转化时要在radix标明字符串的进制
        System.out.println(i);

        Scanner sc = new Scanner(System.in);
        System.out.println("请输入一个整数");
        String string = sc.nextLine();  //在以后我们就可以使用nextLine接收输入的值
        int i1 = Integer.parseInt(string); //然后再用对应包装类的parse方法转化成基本数据类型
        System.out.println(i1 + 1);
    }
}
