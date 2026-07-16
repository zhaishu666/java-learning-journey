package week5.day04;

import java.util.Scanner;

public class Test3 {
        public static void main(String[] args) {
            //自己去实现十进制转二进制toBinaryString
            StringBuilder sb = new StringBuilder();
            Scanner sc = new Scanner(System.in);
            System.out.println("请输入要转化成二进制的十进制整数");
            int num;
            if (!sc.hasNextInt()) {
                System.out.println("输入不合法");
            }else {
                num = sc.nextInt();
                System.out.println(numToBinaryString(sb, num));
            }

        }

    private static String numToBinaryString(StringBuilder sb , int num) {
            int remainder;
        while (true) {
            remainder = num % 2;
            sb.insert(0, remainder);   //也可以通过每次添加时将remainder插入首部来避免最后反转
            num = num / 2;
           // sb.append(remainder);
            if (num == 0) break;
        }
       // return sb.reverse().toString();
        return sb.toString();
    }
}
