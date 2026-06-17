package week1.day03;

import java.util.Scanner;

//键盘输入两个数字表示范围,求这个范围内既能被3整除又能被5整除的数字
public class ForPractice {
    static void main() {
        Scanner sc = new Scanner(System.in);
        int f = sc.nextInt();  //表示first number
        int l = sc.nextInt();  //表示last number
        int sum = 0;
        for( int i = f ; i <= l ; i++ ){
            if( i % 3 ==0 && i % 5 == 0){
                sum++;
            }
        }
        System.out.println(sum);
    }
}
