package week2.day07;

import java.util.ArrayList;

//用于熟悉ArrayList集合的各种变更
public class ArrayListDemo1 {
    static void main() {
        ArrayList<String> list = new ArrayList<>();
        boolean result = list.add("aaa"); //增加元素,并返回是否成功
        list.add("bbb");
        list.add("ccc");
        list.add("ddd");

        /*boolean result2 = list.remove("ddd"); //删除元素,并返回是否成功
        System.out.println(result2);*/

       /* String result3 = list.remove(0); //删除对应索引的元素,并返回被删除的元素
        System.out.println(result3);*/

        /*String result4 = list.set(0, "eee"); //改变对应索引的元素,并返回被更改的元素
        System.out.println(result4);*/

        for (int i = 0; i < list.size(); i++) { //获得集合的长度
            System.out.println(list.get(i)); //获得对应索引的元素
        }
    }
}
