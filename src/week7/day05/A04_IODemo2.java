package week7.day05;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class A04_IODemo2 {
    public static void main(String[] args) {
        //拷贝文件的方式
        long strat = System.currentTimeMillis();
        try(FileInputStream fis = new FileInputStream("C:/Users/翟曙/Desktop/新建文件夹/学习计划.docx");
            FileOutputStream fos = new FileOutputStream("E:/develpment/base-code/java-learning-journey/复制测试.docx")){
            byte[] bytes = new byte[1024 * 1024];
            int len;
            while((len = fis.read(bytes)) != -1){
                fos.write(bytes,0,len);  //为什么不直接write(bytes)?
                //read返回len代表这次读到的有效字符量,最后一次很可能bytes没有读满,直接write(bytes)可能会将上次read的脏数据一齐写出
            }
            fos.flush();  //刷新内存,虽然流关闭是close()内部会执行,但建议手动加上
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long end = System.currentTimeMillis();
        System.out.println(end-strat);
    }
}
