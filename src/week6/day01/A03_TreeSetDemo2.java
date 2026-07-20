package week6.day01;

import java.util.TreeSet;

public class A03_TreeSetDemo2 {
    public static void main(String[] args) {

        Student s1 = new Student("zhangsan",23);
        Student s2 = new Student("lisi",24);
        Student s3 = new Student("wangwu" ,25);
        Student s4 = new Student("zhaoliu",26);

        TreeSet<Student> treeSet = new TreeSet<>();

        treeSet.add(s1);
        treeSet.add(s2);
        treeSet.add(s3);
        treeSet.add(s4);
        System.out.println(treeSet);
    }
}
