package week8.day02;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class A03_Practice2 {
    public static void main(String[] args) throws IOException {
        ArrayList<String> nameList = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader("a.txt"))){
            String ch;
            while((ch = br.readLine()) != null){
                nameList.add(ch);
            }
        }
        System.out.println(randomName(nameList));
    }
    public static String randomName(ArrayList<String> list){
        Random ran = new Random();
        int i = ran.nextInt(list.size());
        return list.get(i).split("-")[0];
    }
}
