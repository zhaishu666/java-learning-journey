package week6.day01;

import java.util.Objects;

public class Student implements Comparable<Student>{
    private String name;
    private int age;


    public Student() {
    }

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    /**
     * 获取
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取
     * @return age
     */
    public int getAge() {
        return age;
    }

    /**
     * 设置
     * @param age
     */
    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return age == student.age && Objects.equals(name, student.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

    public String toString() {
        return "Student{name = " + name + ", age = " + age + "}";
    }

    @Override
    public int compareTo(Student o) {
        //此处this表示要添加的元素, o 表示当前已存在与红黑树要与this比较的元素
        System.out.println("this: " + this);
        System.out.println("o: " + o);
        System.out.println("------------------");
        //return的值大于0,则表示当前要添加的元素是大的,放右边
        //小于0,表示要添加的元素是小的,放左边,等于0则不添加
        return this.getAge()-o.getAge();
    }
}
