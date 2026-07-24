package week6.day05;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class A01_PracticeDemo2 {
    public static void main(String[] args) {

        ArrayList<String> boyList = new ArrayList<>();
        ArrayList<String> girlList = new ArrayList<>();
        Random r = new Random();

        Collections.addAll(boyList,"0","1","2","3","4","5","6","7","8","9");
        Collections.addAll(girlList,"10","11","12","13","14","15","16","17","18","19");


        int i = r.nextInt(10);

        if(i <= 6){
            System.out.println(boyList.get(r.nextInt(boyList.size())));
        }
        else{
            System.out.println(girlList.get(r.nextInt(girlList.size())));
        }
    }
}
