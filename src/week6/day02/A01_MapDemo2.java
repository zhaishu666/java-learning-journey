package week6.day02;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class A01_MapDemo2 {
    static void main(String[] args) {

        Map<String, String> map = new HashMap<>();

        map.put("流萤","小老婆");
        map.put("卡夫卡","妈妈");
        map.put("诺姆","女儿");
        map.put("key1","value1");

        Set<String> str = map.keySet();   //获取所有的键,把这些键放到一个单列集合中

        //增强for循环遍历单列集合
        for (String s : str) {
            String value = map.get(s);  //利用集合中的键获取对应的值
            System.out.println(value);
        }
        System.out.println("-------------------------");

        //迭代器遍历单列集合
        Iterator<String> it = str.iterator();  //此处注意是单列集合才能调用迭代器方法
        while(it.hasNext()){
            String key = it.next();
            String value = map.get(key);
            System.out.println(value);
        }
        System.out.println("-------------------------");

        //
        map.forEach((k,v)->{
            System.out.println(v);
        });
    }
}
