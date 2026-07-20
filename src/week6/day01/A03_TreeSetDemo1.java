package week6.day01;

import java.util.Iterator;
import java.util.TreeSet;
import java.util.function.Consumer;

public class A03_TreeSetDemo1 {
    public static void main(String[] args) {

        TreeSet<Integer> ts = new TreeSet<>();

        ts.add(3);
        ts.add(2);
        ts.add(1);
        ts.add(4);
        ts.add(5);

        //TreeSet会对添加的元素进行排序.默认从小到大
        //System.out.println(ts);

        //迭代器遍历
        Iterator<Integer> it = ts.iterator();
        while(it.hasNext()){
            System.out.println(it.next());
        }
        System.out.println("---------------");

        //增强for循环遍历
        for (Integer t : ts) {
            System.out.println(t);
        }

        System.out.println("---------------");
        //Lambda表达式遍历
        ts.forEach( integer -> System.out.println(integer));
    }
}
