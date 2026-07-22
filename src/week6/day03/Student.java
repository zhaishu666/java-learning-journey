package week6.day03;

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

    public String toString() {
        return "Student{名字 = " + name + ", 年龄 = " + age + "}";

    }

    @Override
    public int compareTo(Student o) {
        //要注意这里有两个变量 this 表示当前要添加的对象,o 表示集合中已经存在的对象
        int i = this.getAge() - o.getAge();
        if(i !=0 ) {
            return i;
        }
        if (!this.getName().equals(o.getName())) {
            return this.getName().compareTo(o.getName());
        }
        return 0;
    }
}
