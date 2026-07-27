package week7.day01;

import java.util.ArrayList;
import java.util.Collections;


public class A02_MethodRefenceDemo1 {
    public static void main(String[] args) {

        //利用方法引用将集合中的String类型数据转化成int类型
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list, "1", "2", "3", "4", "5");

        list.stream().map(Integer::parseInt).forEach(System.out::println);
    }
}
