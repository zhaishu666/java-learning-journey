package week7.day04;

import java.io.FileOutputStream;
import java.io.IOException;

public class A02_IODemo2 {
    public static void main(String[] args) throws IOException {

        FileOutputStream f1 = new FileOutputStream("C:\\Users\\翟曙\\Desktop\\测试.txt");

        byte[] bytes = {97, 98, 99, 100, 101, 102};
        //每次写入一个字符
        f1.write(97);
        f1.write(98);
        //一次写一个字节数组数据
        f1.write(bytes);
        //一次写一个字节数组的部分数据
        f1.write(bytes,1,5);

        f1.close();
    }
}
