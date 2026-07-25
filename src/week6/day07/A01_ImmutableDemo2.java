package week6.day07;

import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;

public class A01_ImmutableDemo2 {
    public static void main(String[] args) {

        //利用Set接口中的静态of方法创建不可变集合
        //在对Set接口使用此方法时一定要注意添加元素的唯一性,如果存在重复元素,则会抛出IllegalArgumentException

        Set<String> set = Set.of("zhangsan", "lisi", "wangwu");

        for (String s : set) {
            System.out.println(s);
        }
        System.out.println("-----------------------");

        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
        System.out.println("-----------------------");

        set.forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        });
    }
}
