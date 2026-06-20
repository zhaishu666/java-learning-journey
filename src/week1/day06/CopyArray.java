package week1.day06;

import java.util.Scanner;

//写一个方法copyOfRangle(int[] arr,int form,int to)来复制数组
public class CopyArray {

    public static int[] copyOfRangle(int[] arr,int from,int to){ //方法名的首字母应该小写
        int [] newArr = new int[to - from];//避免写成form
        int j = 0;
        for (int i = from; i < to; i++) {
            newArr[j] = arr[i];
            j++;
        }
        return newArr;
    }

    public static void main(String[] args) {
        int[] arr = {1,2,3,4,5,6,7,8,9};
        Scanner sc = new Scanner(System.in);
        int[] copyArr = copyOfRangle(arr,3,7); //将返回的数组赋值给另一个新数组
        for (int i = 0; i < copyArr.length; i++) {
            System.out.print(copyArr[i] + " ");
        }
    }
}
