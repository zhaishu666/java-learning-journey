package week1.day03;

import java.util.Scanner;

//键盘录入星期数,输出工作日,休息日,使用switch语句
/*总结:对于switch-case 可以使用->来简化代码,对于输入的非数字符号会影响代码进行,可以增加一个
if(day.hasNextInt())进行合法判断,switch更适合每一种条件造成结果不同的情况
default和break容易忘写
 */
public class SwitchPractice {
    static void main() {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入星期数");
        int day = sc.nextInt();
        switch(day){
            case 1,2,3,4,5 ->
             System.out.println("工作日");
            case 6,7 ->
             System.out.println("休息日");
            default -> System.out.println("没有这个星期数");

        }
    }
}

