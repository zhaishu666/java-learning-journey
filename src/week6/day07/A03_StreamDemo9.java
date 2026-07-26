package week6.day07;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Function;
import java.util.stream.Collectors;

public class A03_StreamDemo9 {
    public static void main(String[] args) {

        //测试Stream流终结方法collect,将收集到的数据放到集合中

        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list,"流萤-女-18","张雪峰-男-40","张三-男-23",
                "卡夫卡-女-26","雷米埃尔-女-500","张无忌-男-10");

        //收集集合中所有性别为女的人,放入ArrayList集合当中
        list.stream().
                filter(s -> "女".equals(s.split("-")[1]))
                .collect(Collectors.toList())
                .forEach(System.out::println);

        System.out.println("--------------------------------");
        //收集集合中所有性别为男的人,放入HashSet集合当中
        list.stream()
                .filter(s -> "男".equals(s.split("-")[1]))
                .collect(Collectors.toSet())
                .forEach(System.out::println);

        System.out.println("--------------------------------");
        //收集集合中所有性别为女的人,放入HashMap集合当中,键存放名字,值存放年龄,括号中两者都需要指定生成规则
        list.stream()
                .filter(s -> "女".equals(s.split("-")[1]))
                .collect(Collectors.toMap(new Function<String, String>() {
                    @Override
                    public String apply(String s) {
                        return s.split("-")[0];
                    }
                }, new Function<String, Integer>() {
                    @Override
                    public Integer apply(String s) {
                        return Integer.parseInt(s.split("-")[2]);
                    }
                }))
                .entrySet()
                .forEach(System.out::println);

        System.out.println("--------------------------------");
        //简化写法
        list.stream()
                .filter(s -> "女".equals(s.split("-")[1]))
                .collect(Collectors.toMap(s -> s.split("-")[0], s -> Integer.parseInt(s.split("-")[2])))
                .entrySet()
                .forEach(System.out::println);
    }
}
