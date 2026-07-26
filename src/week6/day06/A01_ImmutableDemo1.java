package week6.day06;

import java.util.Iterator;
import java.util.List;

public class A01_ImmutableDemo1 {
    public static void main(String[] args) {
        //通过List,Set,Map中的静态的of方法获得不可变集合

        List<String> list = List.of("zhangsan", "lisi", "wangwu");

        Iterator<String> it = list.iterator();
        while(it.hasNext()) {
            System.out.println(it.next());
        }
        System.out.println("-------------------");

        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
        System.out.println("-------------------");

        list.forEach(System.out::println);

    }
}
