package week1.day05;

import java.util.Scanner;

//判断质数 要求在main方法中循环输入5个数
//为什么能使用/11? 因为非质数 n = a*b 必有一个小于√n
public class Method3 {

    public static boolean isPrime(int num) {
        boolean flag = true;
        for (int i = 1; i < num; i++) {//对于质数判断,可以使用Math.sqrt(num)
            if (num % i == 0) {
                flag = false;
                break; //不要忘记而导致效率降低
            }
        }
        return flag;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < 5; i++) {
            System.out.println("请输入数");
            int num = sc.nextInt();
            System.out.println(isPrime(num));
        }
    }
}
