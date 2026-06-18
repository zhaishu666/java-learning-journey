package week1.day04;

import java.util.Scanner;

//输入一个整数x,如果这个整数是回文数,则返回true,反之返回false
public class MathPractice {
   public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入一个整数");
        int number = sc.nextInt();
        int temp = number;
        int result = 0;
        while(temp > 0){
            int num = temp%10;
            result = result * 10 + num;
            temp = temp/10;
        }
        if( number == result){
            System.out.println("true");
        }else{
            System.out.println("false");
        }
    }
}
