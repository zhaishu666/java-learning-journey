package week2.day07;

import java.util.ArrayList;

//返回所有年龄小于20的学生
public class StudentArrayListTest2 {
    static void main() {
        ArrayList<StudentTest> list = new ArrayList<>();
        StudentTest s1 = new StudentTest("zhangsan",18);
        StudentTest s2 = new StudentTest("lisi",19);
        StudentTest s3 = new StudentTest("wangwu",21);
        list.add(s1);
        list.add(s2);
        list.add(s3);
        ArrayList<StudentTest> studentTestInfo = getStudentInfo(list);
        for (int i = 0; i < studentTestInfo.size(); i++) {
            StudentTest s = studentTestInfo.get(i);
            System.out.println(s.getName() + " " + s.getAge());
        }
    }

    public static ArrayList<StudentTest> getStudentInfo(ArrayList<StudentTest> list){
        ArrayList<StudentTest> infoList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            StudentTest stu = list.get(i);
            int age = stu.getAge();
            if(age < 20){
                infoList.add(stu);
            }
        }
        return infoList;
    }
}
