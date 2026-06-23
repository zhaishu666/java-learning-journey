package week2.day02;

public class studentTest {
    static void main() {
        student Stu = new student(); //对于私有成员变量,只能用set输入,get获取
        Stu.setName("张三");
        Stu.setGender('男');
        Stu.setAge(18);
        System.out.println(Stu.getName());
        System.out.println(Stu.getAge());
        System.out.println(Stu.getGender());

        Stu.learn(); //调用成员方法不要忘记括号
        Stu.sleep();
    }
}
