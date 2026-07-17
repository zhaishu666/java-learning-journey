package week5.day05;

import java.util.Arrays;

public class A04_ArraysDemo1 {
    static void main(String[] args) {
        int[] arr = {6, 1, 2, 7, 9, 3, 4, 5, 10, 8};

        System.out.println(Arrays.toString(arr));   //将数组转化成字符串打印出来

        System.out.println(Arrays.binarySearch(arr, 8)); //返回-5,代表8应该出现在4索引的位置

        int[] newArr = Arrays.copyOf(arr, 5);
        System.out.println(Arrays.toString(newArr));

        int[] newArr2 = Arrays.copyOfRange(arr, 0, 5);
        System.out.println(Arrays.toString(newArr2));

        /*Arrays.fill(arr, 8);   //将数组中的元素全部转化为8
        System.out.println(Arrays.toString(arr))*/;

        Arrays.sort(arr);   //使用默认的插入排序方法进行排序
        System.out.println(Arrays.toString(arr));
    }
}
