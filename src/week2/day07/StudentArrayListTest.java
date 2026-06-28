package week2.day07;

import java.util.ArrayList;
import java.util.Scanner;

public class StudentArrayListTest {
    static void main() {
        ArrayList<StudentTest> list = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < 3; i++) {
            StudentTest stu = new StudentTest();  //每次循环都新创建一个stu
            System.out.println("请添加第" + (i + 1) + "个学生的姓名");
            String name = sc.next();
            stu.setName(name);
            System.out.println("请添加第" + (i + 1) + "个学生的姓名");
            int age = sc.nextInt();
            stu.setAge(age);
            list.add(stu);
        }
        for (int i = 0; i < list.size(); i++) {
            StudentTest stu2 = list.get(i);
            System.out.println(stu2.getName() + stu2.getAge());
        }
        sc.close();
    }
}
