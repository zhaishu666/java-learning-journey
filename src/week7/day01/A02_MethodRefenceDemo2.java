package week7.day01;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class A02_MethodRefenceDemo2 {
    public static void main(String[] args) {
        //引用构造方法来将list中的字符串分成name和age封装进Actor对象中
        //格式 构造方法名::new
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list,"卡夫卡,35","流萤,20","雷米埃尔,100","千夏,16","千宫羽,17","千爱芮,18");

        List<Actor> newList = list.stream()
                .map(Actor::new)  //此处使用的是我们手动书写的构造方法
                .collect(Collectors.toList());

        System.out.println(newList);
    }
}
