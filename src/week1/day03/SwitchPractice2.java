package week1.day03;

import java.util.Scanner;

//拨打电话订机票服务,电话中语音提示,1 机票查询 2 机票预定 3 机票改签 4 退出服务 其他键也是退出
public class SwitchPractice2 {
    static void main() {
        System.out.println(" 机票查询请按1\n 机票预定请按2\n 机票改签请按3\n 退出服务请按4\n");
        Scanner sc = new Scanner(System.in);
        int number = sc.nextInt();
        switch(number){
            case 1 -> System.out.println("正在查询");
            case 2 -> System.out.println("进行下一步");
            case 3 -> System.out.println("选择下一步");
            case 0,4,5,6,7,8,9 -> System.out.println("退出服务");
            default -> System.out.println("错误按键");
        }
    }
}
