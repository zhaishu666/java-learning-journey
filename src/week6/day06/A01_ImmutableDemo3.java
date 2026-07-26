package week6.day06;

import java.util.Map;
import java.util.Set;

public class A01_ImmutableDemo3 {
    public static void main(String[] args) {
        //Map接口中的静态of方法创建不可变集合
        //注意:Map接口直接调用of方法,最多只能向()形参中传递10个键值对对象


        Map<String, String> map1 = Map.of("1", "q", "2", "w", "3", "e", "4",
                "r", "5", "t", "6", "y", "7", "u", "8",
                "i", "9", "o", "10", "p");

        //如果想要创建的map的键值对对象数量需要大于10,可以使用Map接口中的ofEntries方法,将另外一个集合中的键值对逐个传递过来
        Set<Map.Entry<String, String>> entries = map1.entrySet();
        Map.Entry[] arr1 = new Map.Entry[0];
        //此处不用担心arr1的长度是否小于entries的实际长度,toArray底层会判断两者的长度,
        // 如果arr1的长度小于entries的长度,则会重新创建数组
        Map.Entry[] arr2 = entries.toArray(arr1);

        Map map = Map.ofEntries(arr2);
        //简化写法
        Map<Object, Object> smallMap = Map.ofEntries(map1.entrySet().toArray(new Map.Entry[0]));

        //或者直接调用Map中的copyOf方法(JDK10)以上,该方法底层其实就是使用了ofEntries方法
        Map<String, String> map3 = Map.copyOf(map1);
        System.out.println(map3);
    }
}
