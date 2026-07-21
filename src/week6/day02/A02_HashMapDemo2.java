package week6.day02;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class A02_HashMapDemo2 {
    static void main() {

        String[] arr = {"A","B","C","D"};

        ArrayList<String> list = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 80; i++) {
            int index = r.nextInt(arr.length);
            list.add(arr[index]);
        }

        HashMap<String, Integer> map = new HashMap<>();

        for (String name : list) {
            if(!map.containsKey(name)){
                map.put(name,1);
            }
            else{
               map.put(name,map.get(name)+1);
            }
        }

        int max = 0;
        String key = null;
        for (String name : map.keySet()) {
            if(map.get(name)>max){
                max = map.get(name);
                key = name;
            }
        }
        System.out.println(key + " " + max);
    }
}
