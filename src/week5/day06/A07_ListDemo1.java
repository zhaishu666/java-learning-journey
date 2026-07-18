package week5.day06;

import java.util.ArrayList;
import java.util.List;

public class A07_ListDemo1 {
    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");

        list.add(1,"QQQ");  //当添加的索引处有元素时,元素会依次向后移动
        System.out.println(list);

        String remove = list.remove(1);  //删除指定索引的元素,并返回该元素
        System.out.println(remove);
        System.out.println(list);

        String set = list.set(0, "kfc");  //修改指定处的元素,并返回被修改的元素
        System.out.println(set);

        String s = list.get(0);  //获得指定索引的元素
        System.out.println(s);
    }
}
