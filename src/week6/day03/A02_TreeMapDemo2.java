package week6.day03;

import java.util.TreeMap;

public class A02_TreeMapDemo2 {
    public static void main(String[] args) {

        TreeMap<Student,String> map = new TreeMap<>();

        Student s1 = new Student("zhangsan", 23);
        Student s2 = new Student("lisi", 24);
        Student s3 = new Student("wangwu", 25);
        Student s4 = new Student("zhangsan", 23);

        map.put(s1,"anhui");
        map.put(s2,"sichuan");
        map.put(s3,"beijing");
        map.put(s4,"shanghai");

        System.out.println(map);
    }
}
