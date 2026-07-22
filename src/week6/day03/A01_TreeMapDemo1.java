package week6.day03;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class A01_TreeMapDemo1{
    static void main() {

        TreeMap<Integer,String> map = new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });

        map.put(111,"橘子");
        map.put(113,"苹果");
        map.put(112,"菠萝");

        System.out.println(map);

        Set<Map.Entry<Integer, String>> entries = map.entrySet();


    }

}
