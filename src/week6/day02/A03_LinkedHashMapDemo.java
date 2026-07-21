package week6.day02;

import java.util.LinkedHashMap;

public class A03_LinkedHashMapDemo {
    public static void main(String[] args) {

        //LinkedHashMap是在HashMap上在每一个键值对的基础上添加了双链表来记录存储的顺序
        LinkedHashMap<String,Integer> map = new LinkedHashMap<>();

        map.put("a",111);
        map.put("b",222);
        map.put("c",333);
        map.put("a",123);

        System.out.println(map);
    }
}
