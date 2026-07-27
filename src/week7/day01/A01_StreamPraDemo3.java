package week7.day01;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class A01_StreamPraDemo3 {
    public static void main(String[] args) {
        //两个集合分别存储5个男女角色的名字和年龄中间用逗号隔开
        ArrayList<String> boyList = new ArrayList<>();
        ArrayList<String> girlList = new ArrayList<>();
        Collections.addAll(boyList,"刃,500","宗师,100","旅行者,20","法厄同,28","爱雷神,24");
        Collections.addAll(girlList,"卡夫卡,35","流萤,20","雷米埃尔,100","千夏,16","千宫羽,17","千爱芮,18");


        Stream<String> stream1 = boyList.stream()
                .filter(s -> (s.split(",")[0]).length() == 3)
                .limit(2);

        Stream<String> stream2 = girlList.stream()
                .filter(s -> (s.split(",")[0]).startsWith("千"))
                .skip(1);


        List<Actor> actors = Stream.concat(stream1, stream2)
                .map(s -> new Actor(s.split(",")[0], Integer.parseInt(s.split(",")[1])))
                .collect(Collectors.toList());

        System.out.println(actors);
    }
}
