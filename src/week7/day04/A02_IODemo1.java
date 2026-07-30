package week7.day04;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class A02_IODemo1 {
    static void main() throws IOException {

        //创建一个操作本地文件的字节输出流
        FileOutputStream f1 = new FileOutputStream("C:\\Users\\翟曙\\Desktop\\测试.txt");
        //写数据
        f1.write(97);
        //释放资源
        f1.close();
    }
}
