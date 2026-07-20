package week6.day01;

import java.util.HashSet;

public class A01_hashCodeDemo2 {
    public static void main(String[] args) {

        //利用Hashcode集合去除重复的元素

        Student s1 = new Student("zhangsan",23);
        Student s2 = new Student("lisi",24);
        Student s3 = new Student("wangwu",25);
        Student s4 = new Student("zhangsan",23);

        HashSet<Student> set = new HashSet<>();
        System.out.println(set.add(s1));
        System.out.println(set.add(s2));
        System.out.println(set.add(s3));
        System.out.println(set.add(s4));

        //对于我们自己创建的对象一定要去重写它的hashCode和equals方法
        //对于String Integer这些类,JAVA已经在底层帮我们重写好了这两个方法,无需手动重写
        System.out.println(set);
    }
}
