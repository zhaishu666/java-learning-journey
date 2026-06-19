package week1.day05;

import java.util.Random;

//定义一个数组,存入1~5,要求打乱数组中所有数据的位置
public class ShuffleArray {
    static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5};
        Random r = new Random();
        for (int i = 0; i < arr.length; i++) {
            int randomIndex = r.nextInt(arr.length);//标准写法
            arr[i] = randomIndex;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
    }
}
