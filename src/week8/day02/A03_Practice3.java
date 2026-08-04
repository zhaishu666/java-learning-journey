package week8.day02;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class A03_Practice3 {
    public static void main(String[] args) throws IOException {
        ArrayList<String> boyList = new ArrayList<>();
        ArrayList<String> girlList = new ArrayList<>();
        HashMap<String, Integer> countMap = new HashMap<>();
        Random r = new Random();

        try(BufferedReader br = new BufferedReader(new FileReader("a.txt"))){
            String line;
            while((line = br.readLine()) != null){
                if((line.split("-")[1]).equals("男")){
                    boyList.add(line);
                }else{
                    girlList.add(line);
                }
            }
        }
        for (int i = 0; i < 1000000; i++) {  //一百万次抽取,查看是否符合3,7分布
            getName(countMap, boyList, girlList,r);
        }
        System.out.println(countMap);
    }

    public static void getName(HashMap<String, Integer> countMap, ArrayList<String> boyList, ArrayList<String> girlList,Random r){

        int i = r.nextInt(0, 10);
        if(i < 7){
            addtoHashMap(r, boyList, countMap, "男");
        }else{
            addtoHashMap(r, girlList, countMap, "女");
        }
    }

    public static void addtoHashMap(Random r, ArrayList<String> list, HashMap<String, Integer> countMap, String gender) {
        int randomStu = r.nextInt(list.size());
        //System.out.println(list.get(randomStu));
        if (!countMap.containsKey(gender)) {
            countMap.put(gender, 1);
        } else {
            countMap.put(gender, countMap.get(gender) + 1);
        }
    }
}
