package week7.day01;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.Collections;
import java.util.function.IntFunction;

public class A02_MethodRefenceDemo4 {
    public static void main(String[] args) {
        //在一个集合中存储一些整数,将他们收集到数组当中
        ArrayList<Integer> list = new ArrayList<>();
        Collections.addAll(list, 1, 2, 3, 4, 5);

        Integer[] array1 = list.stream().toArray(new IntFunction<Integer[]>() {
            @Override
            public Integer[] apply(int value) {  //此处value表示要创建数组的长度
                return new Integer[value];
            }
        });

        Integer[] array2 = list.stream().toArray(Integer[]::new);
        System.out.println(Arrays.toString(array1));
        System.out.println(Arrays.toString(array2));
    }
}
