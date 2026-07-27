package week7.day01;

import java.util.ArrayList;
import java.util.Collections;

public class A01_StreamPraDemo1 {
    public static void main(String[] args) {
        //过滤集合中的奇数,只留下偶数
        ArrayList<Integer> list = new ArrayList<>();
        Collections.addAll(list, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        list.stream().filter(x -> x % 2 == 0).forEach(x -> System.out.println(x));
    }
}
