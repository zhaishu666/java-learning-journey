package week7.day02;

import java.util.ArrayList;
import java.util.Arrays;

public class A01_MethodRefencePra1 {
    static void main() {
        //"卡夫卡,35","流萤,20","雷米埃尔,100","千夏,16"
        ArrayList<Student> students = new ArrayList<>();
        students.add(new Student("卡夫卡", 35));
        students.add(new Student("流萤", 20));
        students.add(new Student("雷米埃尔", 100));

        String[] stuArr = students.stream()
                .map(s -> s.getName() + "-" + s.getAge())
                .toArray(String[]::new);
        System.out.println(Arrays.toString(stuArr));
    }
}
