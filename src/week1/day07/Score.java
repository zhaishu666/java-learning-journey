package week1.day07;

import java.util.Scanner;

//6个评委给选手打分,分数范围为1~100,去除一个最高分一个最低分后计算平均分
//选中一个字符 shift + F6 全部修改
public class Score {
    static void main() {
        Scanner sc = new Scanner(System.in);
        int[] score = new int[6];
        for (int i = 0; i < score.length; ) {
            System.out.println("请输入评分,一共6位");
            int point = sc.nextInt();
            if(point > 0 && point <=100){  //只有在输入的分数合法时才会执行i++
                score[i] = point;
                i++;
            }
        }
        int min = score[0]; //需将初始化放在循环外面
        int max = score[0];
        int sum = 0;
        max = getMax(score, max);
        min = getMin(score, min);
        double average = getAverage(score, sum, min, max);
        System.out.println("最终得分为" + average);
    }

    private static double getAverage(int[] score, int sum, int min, int max) {
        for (int i = 0; i < score.length; i++) {
            sum += score[i];
        }
        double average = (double)(sum - min - max)/ score.length - 2; //优化代码的可改性
        return average;
    }

    private static int getMin(int[] score, int min) {
        for (int i = 0; i < score.length; i++) {
            if (score[i] < min) {
                min = score[i];
            }
        }
        return min;
    }

    private static int getMax(int[] score, int max) {
        for (int i = 0; i < score.length; i++) {
            if (score[i] > max) {
                max = score[i];
            }
        }
        return max;
    }
}
