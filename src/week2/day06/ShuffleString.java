package week2.day06;

import java.util.Random;
import java.util.Scanner;

//键盘输入一个字符串,并打乱里面的内容
public class ShuffleString {
    static void main() {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入一个字符串");
        String str = sc.next();
        String result = shuffle(str);
        System.out.println(result);
    }

    public static String shuffle(String str){
        char[] arr = str.toCharArray();
        Random r = new Random();
        for (int i = 0; i < arr.length; i++) {
            int ran = r.nextInt(arr.length);
            char temp = arr[i];  //temp要字符型
            arr[i] = arr[ran];
            arr[ran] = temp;
        }
        String newStr = new String(arr);
        return newStr;
    }
}
