package week7.day03;

import java.io.File;

public class A01_FileDemo2 {
    public static void main(String[] args) {

        File f1 = new File("C:\\Users\\翟曙\\Desktop\\新建文件夹");
        File f2 = new File("C:\\Users\\翟曙\\Desktop\\测试.txt");

        System.out.println(f1.isDirectory());  //判断该路径名表示的File是否为文件夹
        System.out.println(f1.isFile());       //判断该路径名表示的File是否为文件
        System.out.println(f1.exists());       //判断该路径名表示的File是否存在
        System.out.println("=========================");

        System.out.println(f2.isDirectory());
        System.out.println(f2.isFile());
        System.out.println(f2.exists());
    }
}
