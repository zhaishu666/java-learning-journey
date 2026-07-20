package week6.day01;

import java.util.HashMap;
import java.util.Map;

public class A05_MapDemo1 {
    static void main() {

        //此Demo用于练习顶层接口Map中的各种API
        Map<String,String> map = new HashMap<>();

        //向对象中添加元素
        //添加时,如果键不存在,那么直接把键值对对象添加到map集合中
        //如果键存在,那么会把原有的键值对对象覆盖,把覆盖的值进行返回
        map.put("流萤","小老婆");
        map.put("卡夫卡","妈妈");
        map.put("诺姆","女儿");
        map.put("key1","value1");

        //String result1 = map.remove("key1");   //根据键的值去删除对应的值
        //System.out.println(result1);

        //map.clear();  //清除所有的键值对对象
        //System.out.println(map);

        boolean result2 = map.containsKey("流萤");  //判断集合是否含有指定的键
        System.out.println(result2);

        boolean result3 = map.containsValue("女儿");  //判断集合是否含有指定的值
        System.out.println(result3);

        boolean result4 = map.isEmpty();  //判断集合是否为空
        System.out.println(result4);

        int size = map.size();
        System.out.println(size);  //集合的长度
    }
}
