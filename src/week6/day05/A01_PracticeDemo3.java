package week6.day05;

import week2.day02.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

public class A01_PracticeDemo3 {
    private static final ArrayList<String> list = new ArrayList<>();
    private static int index = 0;
    public static void main(String[] args) {
        ArrayList<String> shuffleList = new ArrayList<>(list);
        Collections.addAll(shuffleList, "zhangsan", "lisi", "wangwu", "zhaoliu", "jiahao");
        Collections.shuffle(shuffleList);

        while (true) {
            String thisName = shuffleList.get(index);
            System.out.println(thisName);
            index++;

            if (index == shuffleList.size()) {
                break;
            }
        }
    }

}
