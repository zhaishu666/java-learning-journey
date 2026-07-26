package week6.day07;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Predicate;

public class A02_StreamDemo5 {
    public static void main(String[] args) {

        //测试练习Stream中的中间方法filter,limit,skip方法

        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list,"流萤","张雪峰","张三","卡夫卡","雷米埃尔","张无忌");

        //list.stream().filter(name -> name.startsWith("张")).forEach(System.out::println);

        //list.stream().limit(1).forEach(System.out::println);  //limit(n) n表示获取前几个元素

        //list.stream().skip(3).forEach(System.out::println);  //skip(n) n表示跳过前几个元素

        //如果我现在要获得卡夫卡,如何实现?两种方法
        //list.stream().skip(3).limit(1).forEach(System.out::println);
        list.stream().limit(4).skip(3).forEach(System.out::println);
    }
}
