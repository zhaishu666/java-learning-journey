package week7.day03;

import javax.xml.crypto.Data;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class A01_FileDemo3 {
    public static void main(String[] args) {

        File f1 = new File("C:\\Users\\翟曙\\Desktop\\新建文件夹");
        File f2 = new File("C:\\Users\\翟曙\\Desktop\\测试.txt");

        System.out.println(f2.length());  //返回文件的大小(字节数量)
        System.out.println(f1.length());  //对于文件夹是无法返回其文件大小的,这里返回的大小并不准确.没有参考价值

        System.out.println(f1.getAbsolutePath());  //返回文件的绝对路径
        System.out.println(f2.getAbsolutePath());
        File f3 = new File("aaa.txt");
        System.out.println(f3.getAbsolutePath());  //输出为 E:\develpment\base-code\java-learning-journey\aaa.txt

        System.out.println("=======================");
        System.out.println(f1.getPath()); //返回定义文件时使用的路径
        System.out.println(f2.getPath());
        System.out.println(f3.getPath());

        System.out.println("=======================");
        System.out.println(f1.getName());  //返回文件的名字,带后缀,如果是文件夹就没有后缀
        System.out.println(f2.getName());

        System.out.println("=======================");
        long l = f1.lastModified();  //返回文件最后修改的时间
        long l2 = f2.lastModified();

        Date date = new Date(l);
        Date date2 = new Date(l2);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(date));
        System.out.println(sdf.format(date2));
    }
}
