package week6.day07;

import java.util.*;

public class A01_StreamDemo2 {
    public static void main(String[] args) {
        //获得单列集合的Stream流,只需要调用Collection接口中的stream方法
        ArrayList<Integer> list = new ArrayList<>();
        Collections.addAll(list, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        //list.stream().forEach(System.out::println);
        //如果想要获取双列集合的Stream流,可以用keySet或者entrySet转成单列集合再使用
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);

        Set<Integer> keys = map.keySet();
        //keys.stream().forEach(System.out::println);

        Set<Map.Entry<Integer, Integer>> entries = map.entrySet();
        entries.stream().forEach(entry -> {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        });
    }
}
