package week7.day01;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class A03_MethodRefencePra1 {
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list, "卡夫卡,35","流萤,20","雷米埃尔,100","千夏,16");

        Student[] array = list.stream().map(Student::new).toArray(Student[]::new);
        System.out.println(Arrays.toString(array));
    }
}
