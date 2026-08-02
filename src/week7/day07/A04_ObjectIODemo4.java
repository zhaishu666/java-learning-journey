package week7.day07;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class A04_ObjectIODemo4 {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //通过反序列化集合获取里面的元素

        ObjectInputStream bis = new ObjectInputStream(new FileInputStream("a.txt"));

        ArrayList<Student> list = (ArrayList<Student>) bis.readObject();
        for (Student student : list) {
            System.out.println(student);
        }
        bis.close();
    }
}
