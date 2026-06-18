package week1.day04;

//利用Random随机数完成1~100间的猜数游戏
//注意random的位置以及范围
import java.util.Random;
import java.util.Scanner;

public class Randompra {
    static void main() {
        Random r = new Random();
        int digit = r.nextInt(1,101);
        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("请输入一个1~100的数字");
            int number = sc.nextInt();
            if( number == digit){
                System.out.println("这是正确的数字!");
                break;
            } else if (number > digit) {
                System.out.println("大了");
            } else if (number < digit) {
                System.out.println("小了");
            }

        }
    }
}
