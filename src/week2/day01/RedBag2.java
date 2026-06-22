package week2.day01;

import java.util.Random;

//红包抽奖,奖品分别为{2,588,888,1000,10000},使用代码模拟抽奖,奖金出现顺序随机并不重复
//使用打乱数组的方法
public class RedBag2 {
    static void main() {
        Random r = new Random();
        int[] arr = {2, 588, 888, 1000, 10000};
        shuffleArray(arr, r);
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i] + "已经被抽出");
        }
    }

    public static void shuffleArray(int[] arr, Random r) {
        for (int i = 0; i < arr.length; i++) {
            int randomIndex = r.nextInt(arr.length);
            int temp = arr[i]; //让arr[i]与随机索引的数进行交换
            arr[i] = arr[randomIndex];
            arr[randomIndex] = temp;
        }
    }
}

