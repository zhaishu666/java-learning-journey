package week7.day07;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class A04_ObjectIODemo2 {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //通过反序列化把刚刚序列化的文件读取出来
        ObjectInputStream bis = new ObjectInputStream(new FileInputStream("a.txt"));
        Object o = bis.readObject();
        System.out.println(o);
    }
}
