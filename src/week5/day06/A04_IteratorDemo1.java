package week5.day06;

import java.util.ArrayList;
import java.util.Iterator;

public class A04_IteratorDemo1 {
    public static void main(String[] args) {

        ArrayList<String> list = new ArrayList<>();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");

        Iterator<String> it = list.iterator();  //获取迭代器对象

        while(it.hasNext()){
            String next = it.next();   //获取当前位置的元素,并将指针后移一位
            System.out.println(next);
        }
    }
}
