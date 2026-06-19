package week1.day05;

//生成10个1~100间的随机值,存入数组
//求出所有数的和
//求出所有数的平均值
//统计有多少比平均值小的数
/*
总结:使用random时不要忘了写范围/21,printf前面的内容需要加双引号/34
求average可将分子强转防止丢失数据/28
 */

import java.util.Random;

public class Random2 {
    static void main(String[] args) {
        int[] arr = new int[10];
        int sum = 0;
        int count = 0;
        Random r = new Random();//Random放在循环外面更加规范
        for (int i = 0; i < arr.length; i++) {
            int num = r.nextInt(1,101);//标准写法为 int num = r.nextInt(100) + 1;
            arr[i] = num;
            System.out.printf("%d ",num);
        }
        for (int i = 0; i < arr.length; i++) {
            sum = sum + arr[i];
        }
        double average = (double)sum/arr.length;
        for (int i = 0; i < arr.length; i++) {
            if( arr[i] < average){
                count++;
            }
        }
        System.out.println("所有数的和为" + sum);
        System.out.println("平均数为" + average);
        System.out.println("比平均数小的有" + count + "个");
    }
}
