package week7.day01;

import java.util.ArrayList;
import java.util.Arrays;

public class A03_MethodRefencePra2 {
    public static void main(String[] args) {
        ArrayList<Student> students = new ArrayList<>();
        students.add(new Student("卡夫卡", 35));
        students.add(new Student("流萤", 20));
        students.add(new Student("雷米埃尔", 100));

        String[] array = students.stream().map(Student::getName).toArray(String[]::new);
        System.out.println(Arrays.toString(array));
    }
}
