package week6.day01;

import java.util.Comparator;
import java.util.TreeSet;

public class A03_TreeSetDemo3 {
    static void main(String[] args) {


        //获得两个字符串的长度差
        TreeSet<String> ts = new TreeSet<>(Comparator.comparingInt(String::length).thenComparing(o -> o)
        );

        ts.add("c");
        ts.add("ab");
        ts.add("df");
        ts.add("abcd");

        System.out.println(ts);
    }
}
