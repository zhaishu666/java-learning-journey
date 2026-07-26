package week6.day07;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Function;

public class A02_StreamDemo7 {
    public static void main(String[] args) {

        //测试Stream中中间方法map转化流中的数据类型

        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list,"流萤-18","张雪峰-40","张三-23","卡夫卡-26","雷米埃尔-500","张无忌-10");

        list.stream()
                .map(s-> Integer.parseInt(s.split("-")[1]))
                .forEach(System.out::println);
    }
}
