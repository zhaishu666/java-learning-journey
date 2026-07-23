package week6.day04;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class A01_CollectionsDemo {
    static void main(String[] args) {

        List<String> list = new ArrayList<>();

        Collections.addAll(list, "a", "b", "c", "e", "f");  //批量添加元素

        Collections.shuffle(list);  //打乱list集合的顺序
        //System.out.println(list);

        Collections.sort(list);  //按照默认规则进行排序
        //System.out.println(list);

        //Collections.sort(list, (o1, o2) -> o2.compareTo(o1)); //按照指定的规则进行排序
        //System.out.println(list);

        int i = Collections.binarySearch(list, "a");  //以2分查找的方式查找对应Key的索引并返回
        //二分查找只能对升序集合使用
        //System.out.println(i);

        List<String> list2 = new ArrayList<>();
        Collections.addAll(list2, "a", "b", "b", "b");
        Collections.copy(list, list2);   //将list2中的元素拷贝到list中,其中list2.size()必须小于list.size()
        //System.out.println(list);

        //Collections.fill(list, "c");  //用obj元素填充list
        //System.out.println(list);

        String max = Collections.max(list);
        String min = Collections.min(list);
        //System.out.println(max);
        //'System.out.println(min);

        Collections.swap(list, 0, 1);
        System.out.println(list);
    }
}