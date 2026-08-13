package week9.day04;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

public class A02_ReflectPra1 {
    public static void main(String[] args) throws IOException, IllegalAccessException {

        Student stu = new Student("zhangsan", 23, "anhui");
        writeMessageToFile(stu);
    }

    public static void writeMessageToFile(Student stu) throws IllegalAccessException, IOException {

        Class<? extends Student> clazz = stu.getClass();

        try(BufferedWriter bw = new BufferedWriter(new FileWriter("student.txt", true))) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                Object value = field.get(stu);
                bw.write(name+"="+value);
                bw.newLine();
            }
        }
    }
}
