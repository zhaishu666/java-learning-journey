package week2.day01;

import java.util.Random;
import java.util.Scanner;

public class DoubleColorBall {
    static void main() {
        Random r = new Random();
        int[] ranRed = new int[6];
        for (int i = 0; i < ranRed.length; ) {//将6个随机数存入数组,并防止出现相同数
            int num = r.nextInt(1, 34);
            boolean flag = true;
            for (int j = 0; j < i; j++) { //将生成的随机num跟前面的值对比,若存在相同则重新生成
                if (num == ranRed[j]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                ranRed[i] = num;
                i++;
            }
        }

       /* for (int i = 0; i < ranRed.length; i++) {
            System.out.print(ranRed[i] + " ");
        }*/

        int ranBlue = r.nextInt(1, 17); //蓝球随机数

        Scanner sc = new Scanner(System.in);
        int[] inputRed = new int[6];
        for (int i = 0; i < inputRed.length; i++) { //遍历存入输入的结果
            System.out.println("请输入第" + (i + 1)  + "个猜的红球数"); // i+1需要加上括号
            inputRed[i] = sc.nextInt();
        }
        System.out.println("请输入猜的蓝球数");
        int blue = sc.nextInt();

        int count = 0;
        int sam = 0;
        for (int i = 0; i < 6; i++) { // 逐个对比,统计红球中奖数
            for (int j = 0; j < 6; j++) {
                if (ranRed[i] == inputRed[j]) {
                    count++;
                    break; //匹配到一次就break防止重复计数
                }
            }
        }
        if (ranBlue == blue) {
            sam = 1;
        }

        if (count >= 2 && sam == 1) {
            System.out.println("中六等奖");
        } else if ((count == 3 && sam == 1) || (count == 4 && sam == 0)) {
            System.out.println("中五等奖");
        } else if ((count == 4 && sam == 1) || (count == 5 && sam == 0)) {
            System.out.println("中四等奖");
        } else if (count == 5 && sam == 1) {
            System.out.println("中三等奖");
        } else if (count == 6 && sam == 0) {
            System.out.println("中二等奖");
        } else if (count == 6 && sam == 1) {
            System.out.println("中一等奖");
        }else{
            System.out.println("没有中奖");
        }
        sc.close();
    }
}
