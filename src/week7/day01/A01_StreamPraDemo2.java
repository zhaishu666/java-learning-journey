package week7.day01;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class A01_StreamPraDemo2 {
    public static void main(String[] args) {
        //向集合中添加一些字符串,保留年龄大于24岁的人,并将结果收集到Map集合中,姓名为键,年龄为值
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list,"zhangsan, 23","lisi, 24","wangwu, 25");

        Map<String, Integer> collect = list.stream()
                .filter(s -> 24 >= Integer.parseInt(s.split(", ")[1]))
                .collect(Collectors.toMap(s -> s.split(", ")[0]
                        , s -> Integer.parseInt(s.split(", ")[1])));

        System.out.println(collect);
    }
}
