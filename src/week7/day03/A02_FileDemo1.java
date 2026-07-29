package week7.day03;

import java.io.File;

public class A02_FileDemo1 {
    public static void main(String[] args) {

        File f1 = new File("C:\\Users\\翟曙\\Desktop\\新建文件夹");

        File[] files = f1.listFiles(); //获取新建文件夹里的所有内容,放到数组中进行返回
        for (File f : files) {
            System.out.println(f);
        }
    }
}
