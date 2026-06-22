package week2.day01;

/*对于某系统的4位数字密码采用加密传输的方式
先得到每一位数,然后每一位数都加上5,再对10取余,最后将所有的数字反转,得到新数字*/

import java.util.Scanner;

public class Encryption {
    static void main() {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入密码");
        int number = sc.nextInt(); //最好使用字符串进行录入
        int count = 0;
        int temp = number;
        while (temp != 0) {
            temp = temp / 10;
            count++;
        }
        // System.out.println(count);

        int[] arr = new int[count];//此处count表示数组长度
        for (int i = count - 1; i > -1; i--) { //让数组录入正确方向的数字
            arr[i] = number % 10;
            number = number / 10;
        }

       /* for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }*/

        extracted(arr);
        extracted1(arr);
        extracted2(arr);
        String result = "";
        for (int i = 0; i < arr.length; i++) {
            result = result + arr[i];
        }
        System.out.println(result);
    }

    public static void extracted2(int[] arr) { //把所有数字反转
        for (int i = 0, j = arr.length - 1; i < j; i++, j--) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    public static void extracted1(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr[i] % 10; //每一位对10取余
        }
    }

    public static void extracted(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr[i] + 5; //对每一位加5
        }
    }
}

