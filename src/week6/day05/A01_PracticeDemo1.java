package week6.day05;

import java.util.ArrayList;
import java.util.Collections;

public class A01_PracticeDemo1 {
    static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();

        Collections.addAll(list,"zhangsan","lisi","wangwu","zhaoliu");

        Collections.shuffle(list);

        System.out.println(list.getFirst());
    }
}
