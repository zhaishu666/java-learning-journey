package week6.day07;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Stream;

public class A02_StreamDemo6 {
    public static void main(String[] args) {

        //测试Stream中的中间方法distinct,concat方法

        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list,"流萤","张雪峰","张雪峰","张雪峰","张三","卡夫卡","雷米埃尔","张无忌");

        ArrayList<String> list2 = new ArrayList<>();
        Collections.addAll(list,"叶顺光","花火");
        //利用distinct方法去重,要注意的是distinct底层是依赖hashSet去重的,对于我们自己定义的对象,需要手动重新equals和hashCode
        list.stream().distinct().forEach(System.out::println);
        System.out.println("=================");

        Stream.concat(list.stream(),list2.stream()).forEach(System.out::println);
    }
}
