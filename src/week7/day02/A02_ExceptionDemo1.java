package week7.day02;

public class A02_ExceptionDemo1 {
    public static void main(String[] args) {

        //异常的作用
        //作用一: 异常是用来查询bug的关键参考信息
        //作用二: 异常可以作为方法内部的一种特殊返回值,以便调用者知晓底层的执行情况

        Student s1 = new Student();
        s1.setName("千夏");
        s1.setAge(16);  //如果大于18则会抛出RuntimeException
        System.out.println(s1);
    }
}
