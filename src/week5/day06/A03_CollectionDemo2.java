package week5.day06;

import java.util.ArrayList;
import java.util.Collection;

public class A03_CollectionDemo2 {
    public static void main(String[] args) {
        Collection<Student> students = new ArrayList<>();
        students.add(new Student("zhangsan", 20));
        students.add(new Student("lisi", 22));
        students.add(new Student("wangwu", 21));

        Student s4 = new Student("zhangsan", 20);

        System.out.println(students.contains(s4));  //如果没有重写equals返回值为false,比较的实际是地址值
    }
}
