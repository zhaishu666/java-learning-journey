package week6.day01;

public class A01_hashCodeDemo1 {
    public static void main(String[] args) {

        Student s1 = new Student("Jack", 18);
        Student s2 = new Student("Jack", 18);

        System.out.println(s1.hashCode());   //如果没有重写hashCode,两者的hashCode值将会不同
        System.out.println(s2.hashCode());

        //在小部分情况下,不同属性值或者不同地址值计算出的哈希值也有可能一样
        System.out.println("abc".hashCode());
        System.out.println("acD".hashCode());

    }
}
