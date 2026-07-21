package week6.day02;

import java.util.HashMap;

public class A02_HashMapDemo1 {
    public static void main(String[] args) {

        HashMap<Student, String> stu = new HashMap<>();

        Student s1 = new Student("zhangsan", 23);
        Student s2 = new Student("lisi", 24);
        Student s3 = new Student("wangwu", 25);
        Student s4 = new Student("zhangsan", 23);
        stu.put(s1,"安徽");
        stu.put(s2,"湖北");
        stu.put(s3,"北京");
        stu.put(s4,"南京");

        stu.entrySet().forEach(System.out::println);

    }
}
