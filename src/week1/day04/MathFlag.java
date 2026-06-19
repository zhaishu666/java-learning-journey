package week1.day04;

import java.util.Scanner;

//控制台输入一个整数,判断这个数是否为质数(只能被1或者它本身整除)
//对于这种循环完全结束才适合确定结果的题,else就不好用了,可以使用flag来记录
public class MathFlag {

    static void main() {
        boolean flag = true;
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入一个整数");
        int num = sc.nextInt();
        for( int i = 2 ; i < num ; i++ ){
            if( num % i == 0 ){
                flag = false;
                break;
            }
        }if(flag){
            System.out.println( num + "是一个质数");
        }else{
            System.out.println(num + "不是一个质数");
        }
    }
}
