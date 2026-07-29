package week7.day03;

import java.io.File;

public class A01_FileDemo1 {
    public static void main(String[] args) {
        //测试File类中的三个方法
        File f1 = new File("C:\\Users\\翟曙\\Desktop\\测试.txt");
        //System.out.println(f1);

        //父类名字符串和子类名字符串拼接
        String fu = "C:\\Users\\翟曙\\Desktop";
        String zi = "测试.txt";
        File f2 = new File(fu, zi);
        System.out.println(f2);
        //父类File路径与子类字符串拼接

        File fu2 = new File("C:\\Users\\翟曙\\Desktop");
        String zi2 = "测试.txt";
        File fu3 = new File(fu2, zi2);
        System.out.println(fu3);
    }
}
