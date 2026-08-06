package week7.day06;

import java.io.*;

public class A02_BufferIODemo1 {
    public static void main(String[] args) throws IOException {
        //利用缓冲流来读取数据
        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream("C:/Users/翟曙/Desktop/测试.txt"));
            BufferedOutputStream bos = new BufferedOutputStream( new FileOutputStream("E:/develpment/base-code/java-learning-journey/configuration.properties"))
        ){
            byte[] bytes = new byte[1024];
            int len;
            while((len=bis.read(bytes))!=-1){
                bos.write(bytes,0,len);
            }
        }
    }
}
