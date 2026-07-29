package week7.day03;

import java.io.File;
import java.io.IOException;

public class A01_FileDemo4 {
    public static void main(String[] args) throws IOException {

        File f1 = new File("C:\\Users\\翟曙\\Desktop\\新建文件夹\\aaa.txt");
        File f2 = new File("C:\\Users\\翟曙\\Desktop\\测试.txt");

        System.out.println(f1.createNewFile());  //在指定路径中创建一个空的文件,只会创建文件,如果该路径表示的文件是存在的,则返回false
        File f3 = new File("C:\\Users\\翟曙\\Desktop\\新建文件夹\\bbb");

        System.out.println(f3.mkdir());  //创建单级文件夹

        File f4 = new File("C:\\Users\\翟曙\\Desktop\\新建文件夹\\aaa\\ccc");
        System.out.println(f4.mkdirs()); //既可以创建单极文件夹,也可以创建多级文件夹

        File f5 = new File("C:\\Users\\翟曙\\Desktop\\新建文件夹\\aaa");
        System.out.println(f3.delete());  //删除对应文件及空文件夹
        System.out.println(f5.delete());  //不是空文件夹,删除失败,返回false
    }
}
