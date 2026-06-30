package week3.day02;

public class StaticDemo1 {
    static void main() {
        Student.teacher = "阿伟老师";  //static静态变量,代码执行时会将其放在堆内存的静态区,该变量被该类的所有成员共享
        Student s1 = new Student();
        s1.setAge(18);
        s1.setName("zhangsan");
        s1.setGender("男");

        s1.study();
        s1.show();

        Student s2 = new Student();
        s2.show();
    }
}
