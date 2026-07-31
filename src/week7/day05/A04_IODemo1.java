package week7.day05;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;

public class A04_IODemo1 {
    static void main() throws IOException {
        //IO流一次读取多个数据
        FileInputStream fis = new FileInputStream("C:\\Users\\翟曙\\Desktop\\测试.txt");
        byte[] bytes = new byte[5];
        int len1 = fis.read(bytes);
        System.out.println(new String(bytes,0,len1));
        /*int len2 = fis.read(bytes);
        System.out.println(new String(bytes,0,len2));
        int len3 = fis.read(bytes);
        System.out.println(new String(bytes,0,len3));*/
        fis.close();
    }
}
