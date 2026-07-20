package week6.day01;

import java.util.TreeSet;

public class A04_TreeSetDemo4 {
    public static void main(String[] args) {

        Student2 s1 = new Student2("zhangsan",23,90,100,50);
        Student2 s2 = new Student2("lisi",24,95,98,50);
        Student2 s3 = new Student2("wangwu",25,90,90,60);
        Student2 s4 = new Student2("zhaoliu",26,90,90,60);
        Student2 s5 = new Student2("zhaoliu",26,90,90,60);

        TreeSet<Student2> ts = new TreeSet<>();

        ts.add(s1);
        ts.add(s2);
        ts.add(s3);
        ts.add(s4);
        ts.add(s5);

        ts.forEach(System.out::println);
    }
}
