package week10.day03;

import java.lang.reflect.Method;

public class AnnotationTest {
    public static void main(String[] args) throws NoSuchMethodException {

        Class<User> userClass = User.class;

        //解析类上的注解
        if (userClass.isAnnotationPresent(MyLog.class)) {
            MyLog myLog = userClass.getAnnotation(MyLog.class);
            System.out.println(myLog.value() + " " + myLog.level());
        }

        //解析方法上的注解
        Method login = userClass.getMethod("login");
        if (login.isAnnotationPresent(MyLog.class)) {
            MyLog myLog = login.getAnnotation(MyLog.class);
            System.out.println(myLog.value() + " " + myLog.level());
        }
    }
}
