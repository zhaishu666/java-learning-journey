package week6.day07;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.IntFunction;

public class A03_StreamDemo8 {
    public static void main(String[] args) {

        //测试Stream流中的终结方法forEach和count
        //注意: 终结方法运行后stream流就结束了
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list,"流萤","张雪峰","张三","卡夫卡","雷米埃尔","张无忌");

        //list.stream().forEach(System.out::println);  //遍历
        //System.out.println(list.stream().count()); //统计流中的元素数量

        String[] array = list.stream().toArray(new IntFunction<String[]>() {
            @Override
            public String[] apply(int value) {
                return new String[value];
            }
        });
        System.out.println(Arrays.toString(array));

        System.out.println("==============================");

        System.out.println(Arrays.toString(list.stream().toArray(String[]::new)));
    }
}
