package week5.day06;

import java.util.ArrayList;
import java.util.Collection;

public class A03_CollectionDemo1 {
    static void main(String[] args) {
        //Collection接口中的常用方法
        Collection<String> coll = new ArrayList<>();

        boolean result = coll.add("aaa");  //将指定的元素添加到集合中,返回boolean类型结果
        coll.add("bbb");
        coll.add("ccc");

        System.out.println(coll);
        //coll.clear();   //清空集合中所有的元素
        //System.out.println(coll);

        coll.remove("aaa");  //Collection中定义的是共性方法,所以不能通过索引进行删除
        System.out.println(coll);

        boolean result2 = coll.contains("aaa");  //判断集合中是否存在该元素
        //值得注意的是contains方法底层是通过equals方法进行判断的
        // 如果集合中存放的是我们自己书写的对象,使用contains方法就需要我们重写equals方法比较
        System.out.println(result2);

        System.out.println(coll.isEmpty());
        System.out.println(coll.size());
    }
}
