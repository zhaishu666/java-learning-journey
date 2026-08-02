package week7.day07;

import java.io.FileInputStream;
import java.io.IOException;

public class A02_IOPractice1 {
    public static void main(String[] args) throws IOException {
        //使用字节基本流一次读写一个字节数组
        try(FileInputStream fis = new FileInputStream("C:/Users/翟曙/Desktop/测试.txt")){
            byte[] b = new byte[1024*1024];
            int len;
            long start = System.currentTimeMillis();
            while((len=fis.read(b))!=-1){
                System.out.println(new String(b,0,len));
            }
            System.out.println();
            long end = System.currentTimeMillis();
            System.out.println(end-start);
        }

    }
}
