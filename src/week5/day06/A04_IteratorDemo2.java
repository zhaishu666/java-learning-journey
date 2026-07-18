package week5.day06;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class A04_IteratorDemo2 {
    public static void main(String[] args) {
        Collection<String> list = new ArrayList<>();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");
        list.add("ddd");

        Iterator<String> it = list.iterator();

        while(it.hasNext()){
            String next = it.next();

            if(next.equals("ccc")){   //当next记录的元素与ccc相同时,调用迭代器中的remove方法进行删除
                it.remove();     //当迭代器运行时,是不能使用集合中的方法进行修改的
            }
        }

        System.out.println(list);
    }
}
