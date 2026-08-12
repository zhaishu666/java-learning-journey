package week9.day03;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;

public class A03_ReflectDemo2 {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        //测试反射的Constructor API
        Class<?> clazz = Class.forName("week9.day03.Student");

       /* Constructor<?>[] cons1 = clazz.getConstructors();  //获得所有public构造方法
        for (int i = 0; i < cons1.length; i++) {
            System.out.println(cons1[i]);
        }*/

       /* Constructor<?>[] cons2 = clazz.getDeclaredConstructors();  //获得所有构造方法(含private)
        for (int i = 0; i < cons2.length; i++) {
            System.out.println(cons2[i]);
        }*/

        /*Constructor<?> con1 = clazz.getConstructor(String.class);  //获取指定参数的public构造器
        System.out.println(con1);*/

        Constructor<?> con2 = clazz.getDeclaredConstructor(String.class, int.class); //获取指定任意参数的构造器,但依旧不能创建该对象
        int m = con2.getModifiers();  //获取权限修饰符所对应的常量字段值
        Parameter[] parameters = con2.getParameters(); //获取该构造方法所有的参数
        //临时取消权限的校验
        con2.setAccessible(true);  //暴力反射,允许创建该私有构造器的对象
        Object obj = con2.newInstance("zhangsan", 23); //暴力反射后才可以创建
        System.out.println(obj);


    }
}
