package week2.day01;

import java.util.Random;

//红包抽奖,奖品分别为{2,588,888,1000,10000},使用代码模拟抽奖,奖金出现顺序随机并不重复
public class RedBag {
    static void main() {
        int[] arr = {2,588,888,1000,10000};
        int[] copyArr = new int[arr.length]; //使用arr.length提升代码灵活性
        for (int i = 0; i < arr.length; ) {
            Random r  = new Random();
            int ran = r.nextInt(arr.length); //需要一个变量来接收random随机数,易忘
            int prize = arr[ran];           //数组中的某一个数需要先赋值给一个变量再被调用
            if (compareArr(prize,copyArr)){
                System.out.println(arr[ran] +"已经被抽出");
                copyArr[i] = arr[ran];
                //当判断完成后才进行i++
                i++;
            }
        }
    }
    public static boolean compareArr(int arr,int[] copyArr){ //判断某个数是否已经被抽出
        boolean flag = true;
         for (int i = 0; i < copyArr.length; i++) {
             if( copyArr[i] == arr){
                 flag = false;
             }
         }
         return flag;
     }
}
