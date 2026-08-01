package week7.day06;

import java.io.*;

public class A01_IOPractice2 {
    public static void main(String[] args) throws IOException {
        //对文件进行加密后存储  "C:/Users/翟曙/Desktop/测试.txt"  "C:/Users/翟曙/Desktop/加密测试.txt"
        FileInputStream fis = new FileInputStream("C:/Users/翟曙/Desktop/测试.txt");
        FileOutputStream fos = new FileOutputStream("C:/Users/翟曙/Desktop/加密测试.txt");
        int len;
        while((len = fis.read()) != -1){
            len = len ^ 10;

            fos.write(len);
        }
        fos.close();
        fis.close();
    }
}
