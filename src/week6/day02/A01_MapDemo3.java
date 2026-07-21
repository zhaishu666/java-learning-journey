package week6.day02;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class A01_MapDemo3 {
    public static void main(String[] args) {

        Map<String,String> map = new HashMap<>();

        map.put("流萤","小老婆");
        map.put("卡夫卡","妈妈");
        map.put("诺姆","女儿");
        map.put("key1","value1");

        //Map的键值对遍历
        Set<Map.Entry<String, String>> set = map.entrySet();  //获取Map中的所有键值对,并返回一个集合

        Iterator<Map.Entry<String, String>> it = set.iterator();
        while(it.hasNext()){
            Map.Entry<String, String> entry = it.next();
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }

        for (Map.Entry<String, String> entry2 : set) {
            System.out.println(entry2.getKey() + ":" + entry2.getValue());
        }

        set.forEach( entry3 ->System.out.println(entry3.getKey() + ":" + entry3.getValue()));
    }
}
