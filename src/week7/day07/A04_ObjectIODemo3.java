package week7.day07;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class A04_ObjectIODemo3 {
    public static void main(String[] args) throws IOException {
        //对于要序列化多个对象的时候,我们会先把创建的对象全放入集合当中再序列化,然后反序列化的时候只需要反序列化集合就行
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("a.txt"));
        Student stu1 = new Student("zhangsan",23);
        Student stu2 = new Student("liuying",20);
        Student stu3 = new Student("kafuka",30);

        ArrayList<Student> list = new ArrayList<>();  //ArrayList也实现了Serializable接口,可以直接被序列化
        Collections.addAll(list,stu1,stu2,stu3);
        oos.writeObject(list);
        oos.close();
    }
}
