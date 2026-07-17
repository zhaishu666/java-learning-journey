package week5.day05;

import java.util.Arrays;
import java.util.Comparator;

public class A05_ArraysDemo2 {
    public static void main(String[] args) {
        Integer[] arr = {6, 1, 2, 7, 9, 3, 4, 5, 10, 8};

        Arrays.sort(arr, Comparator.comparingInt(o -> o));
        System.out.println(Arrays.toString(arr));
    }
}
