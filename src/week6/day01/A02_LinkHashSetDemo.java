package week6.day01;

import java.util.LinkedHashSet;

public class A02_LinkHashSetDemo {
    public static void main(String[] args) {
        Student s1 = new Student("zhangsan",23);
        Student s2 = new Student("lisi",24);
        Student s3 = new Student("wangwu" ,25);
        Student s4 = new Student("zhangsan",23);

        LinkedHashSet<Student> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.add(s1);
        linkedHashSet.add(s2);
        linkedHashSet.add(s3);
        linkedHashSet.add(s4);

        //LinkedHashSet可以保证存取顺序相同,不过效率没有那么高
        //只有在要既要保证存取顺序,又要去重时才使用
        System.out.println(linkedHashSet);
    }
}
