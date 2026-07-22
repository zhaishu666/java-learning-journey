package week6.day03;

import java.util.*;

public class A02_TreeMapDemo3 {
    public static void main(String[] args) {

        String str = "aababcabcdabcde";

        ArrayList<Character> list = new ArrayList<>();

        for (int i = 0; i < str.length(); i++) {
            list.add(str.charAt(i));
        }


        Map<Character, Integer> map = new TreeMap<>((Character o1, Character o2) -> o1.compareTo(o2));

        for (Character c : list) {
            if (!map.containsKey(c)) {
                map.put(c, 1);
            } else {
                map.put(c, map.get(c) + 1);
            }
        }

        map.forEach((k, v) -> System.out.print(k + "(" + v + ") "));
    }
}
