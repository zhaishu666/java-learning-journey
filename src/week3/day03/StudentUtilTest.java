package week3.day03;

import java.util.ArrayList;

public class StudentUtilTest {
    static void main() {
        ArrayList<Student> list = new ArrayList<>();
        Student stu1 = new Student("zhangsan",18,'男');
        Student stu2 = new Student("lisi",19,'女');
        Student stu3 = new Student("王五",20,'男');

        list.add(stu1);
        list.add(stu2);
        list.add(stu3);

        int theAge = StudentUtil.biggestAge(list);
        System.out.println(theAge);
    }
}
