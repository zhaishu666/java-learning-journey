package week7.day07;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class A04_ObjectIODemo1 {
    public static void main(String[] args) throws IOException {

        Student student = new Student("zhangsan",23);
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("a.txt"));
        oos.writeObject(student);
        oos.close();
    }
}
