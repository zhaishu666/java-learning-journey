package week3.day03;

import java.util.StringJoiner;

// 工具类练习
public class ArrayUtil {

    public ArrayUtil(){}  //需要先写一个空参构造,才可以进行调用

    private static void printArr(int[] arr) {
      /* StringBuilder sb = new StringBuilder();  //方法1
       sb.append('[');
        for (int i = 0; i < arr.length; i++) {
            if (i != arr.length -1) {
                sb.append(arr[i] + ", ");
            }else{
                sb.append(arr[i] + "]");
            }
        }
        String arrString = sb.toString();
        System.out.println(arrString);*/
        StringJoiner sj = new StringJoiner(", ","[","]");
        for (int i = 0; i < arr.length; i++) {
            sj.add(String.valueOf(arr[i])); //性能更好,也可以使用sj.add(arr[i] + "");转化为字符串
        }
        String arrString = sj.toString();
        System.out.println(arrString);
    }
    public static void work(int[] arr){  //对于私有化成员方法可以通过公开方法使其可外部调用
        printArr(arr);
    }

    private static double getAverage(double[] scores){
        double sum = 0;
        for (int i = 0; i < scores.length; i++) {
            sum = sum + scores[i];
        }
        return sum/scores.length;
    }
    public static double average(double[] scores){
       return getAverage(scores);
    }
}
