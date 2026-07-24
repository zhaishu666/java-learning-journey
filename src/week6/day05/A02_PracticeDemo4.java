package week6.day05;

import java.util.*;

public class A02_PracticeDemo4 {
    public static void main(String[] args) {

        //练习集合的嵌套
        Map<String, ArrayList<String>> map = new HashMap<>();
        ArrayList<String> list1 = new ArrayList<>();
        ArrayList<String> list2 = new ArrayList<>();
        Collections.addAll(list1,"无为市","合肥市","芜湖市","六安市");
        Collections.addAll(list2,"南京市","苏州市","常州市","无锡市");
        map.put("安徽省",list1);
        map.put("江苏省",list2);

        Set<Map.Entry<String, ArrayList<String>>> entries = map.entrySet();
        for(Map.Entry<String, ArrayList<String>> entry : entries){
            StringJoiner sj = new StringJoiner(", ","","");
            String key = entry.getKey();
            for (String city : entry.getValue()) {
                sj.add(city);
            }
            System.out.println(key + " = " + sj);

        }
    }
}
