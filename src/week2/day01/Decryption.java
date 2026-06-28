package week2.day01;

import java.util.Scanner;

//对于每一位数都加上5,再对10取余,最后将所有的数字反转的加密方法进行解密
public class Decryption {
    static void main() {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入要解密的密码");
        String input = sc.next();
        int count = input.length();
        
        int[] arr = new int[count];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = input.charAt(i) - '0'; //charAt(i) 取出下标i位置的字符,减去0的ASCII码值得到具体数字
        }
        for (int i = 0,j = arr.length - 1; i < j; i++ , j--) { //将所有数字反转回去,注意数组长度
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
        for (int i = 0; i < arr.length; i++) { // 判断对10取余时是否大于10,原数5~9,加5取余后0~4
            if(arr[i] >= 0 && arr[i] <= 4){
                arr[i] = arr[i] + 10;
            }
        }
        for (int i = 0; i < arr.length; i++) { //将加的5减回去
            arr[i] = arr[i] - 5;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
        }
        sc.close();
    }
}
