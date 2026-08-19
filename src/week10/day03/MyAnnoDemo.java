package week10.day03;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MyAnnoDemo {
    public static void main(String[] args) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {

        //获取需要解析注解的类的class对象
        Class<?> clazz = Class.forName("week10.day03.MyTestDemo");

        MyTestDemo mtd = new MyTestDemo();

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true);
            if(method.isAnnotationPresent(MyTest.class)){
                method.invoke(mtd);
            }
        }
    }
}
