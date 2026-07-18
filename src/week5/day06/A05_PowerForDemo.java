package week5.day06;

import java.util.ArrayList;

public class A05_PowerForDemo {
    static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");

        for (String s : list) {    //所有的单列集合还有数组才能使用增强型for循环,其底层就是迭代器
            System.out.println(s);
        }

    }
}
