package week7.day04;

import java.io.FileInputStream;
import java.io.IOException;

public class A03_IODemo1 {
     static void main() throws IOException {
        //测试FileInputStream

        //创建对应的操作本地文件的字节输入流对象
        FileInputStream fis = new FileInputStream("C:\\Users\\翟曙\\Desktop\\测试.txt");

         int r1 = fis.read();
         System.out.println(r1);  //输出的是对应字符的ASCII码值
         int r2 = fis.read();
         System.out.println((char) r2);  //如果想要原来的,直接强转成char类型就行
         int r3 = fis.read();
         System.out.println((char) r3);

         fis.close();
    }
}
