package week7.day06;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class A01_IOPractice3 {
    public static void main(String[] args) throws IOException {
        //解密对加密文件进行解密
        FileInputStream fis = new FileInputStream("C:/Users/翟曙/Desktop/加密测试.txt");
        FileOutputStream fos = new FileOutputStream("C:/Users/翟曙/Desktop/解密测试.txt");
        int len;
        while ((len = fis.read()) != -1) {
            len = len ^ 10;
            fos.write(len);
        }
        fos.close();
        fis.close();
    }
}
