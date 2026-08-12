package week9.day03;

import java.lang.reflect.Field;

public class A03_ReflectDemo3 {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {

        //测试反射获取成员变量Field API

        Class<?> clazz = Class.forName("week9.day03.Student");

        /*Field field = clazz.getField("age");  //根据名字获取public属性
        System.out.println(field);*/

        Field name = clazz.getDeclaredField("name");

        name.setAccessible(true);

        int modifiers = name.getModifiers(); //获取权限修饰符对应的常量字段值

        Student stu = new Student("zhangsan", 23);

        Object obj = name.get(stu);  //获取指定对象的该属性值
        name.set(stu, "lisi"); //给对象的该属性值赋值
        //System.out.println(stu.getName());


    }
}
