package week9.day04;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class A01_ReflectDemo4 {
    static void main() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Class<?> clazz = Class.forName("week9.day04.Student");

        Method eat = clazz.getDeclaredMethod("eat", String.class);

        eat.setAccessible(true);

        Parameter[] parameters = eat.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            System.out.println(parameters[i]);
        }

        Student stu = new Student();
        Object o = eat.invoke(stu, "hanbao");
        System.out.println(o);

    }
}
