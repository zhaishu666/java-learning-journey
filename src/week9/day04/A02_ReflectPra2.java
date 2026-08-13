package week9.day04;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

public class A02_ReflectPra2 {
    static void main() throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Properties prop = new Properties();
        try(FileInputStream fis = new FileInputStream("configuration.properties")){
            prop.load(fis);
        }

        String classname = (String) prop.get("classname");
        String method = (String) prop.get("method");

        //利用反射创建对象并运行方法
        Class<?> clazz = Class.forName(classname);

        Constructor<?> con = clazz.getDeclaredConstructor();
        con.setAccessible(true);

        Object stu = con.newInstance();  //创建对象
        System.out.println(stu);

        Method m = clazz.getDeclaredMethod(method);
        m.setAccessible(true);
        m.invoke(stu);
    }
}
