package week7.day07;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class A02_IOPractice2 {
    public static void main(String[] args) throws IOException {
        //字节缓冲流一次读取一个字节或整个字节数组
        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream("C:/Users/翟曙/Desktop/测试.txt"))){
            byte[] b = new byte[1024];
            int len;
            long start = System.currentTimeMillis();
            while((len=bis.read(b))!=-1){
                System.out.print(new String(b,0,len));
            }
            System.out.println();
            long end = System.currentTimeMillis();
            System.out.println(end - start);
        }
    }
}
