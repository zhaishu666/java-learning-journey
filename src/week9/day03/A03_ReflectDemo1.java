package week9.day03;

public class A03_ReflectDemo1 {
    public static void main(String[] args) throws ClassNotFoundException {

        //使用三种获取字节码文件的方式

        Class<?> clazz1 = Class.forName("week9.day03.Student");  //括号内填写的是全类名
        //关键: 会触发静态代码块的内容

        Class<Student> clazz2 = Student.class;   //通过类名获得Class对象
        //不会触发静态代码块,仅获取Class对象

        Student student = new Student();
        Class<? extends Student> clazz3 = student.getClass(); //通过对象实例获得Class对象

        System.out.println(clazz1 == clazz2);
        System.out.println(clazz2 == clazz3);
    }
}
