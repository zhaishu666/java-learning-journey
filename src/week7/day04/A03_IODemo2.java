package week7.day04;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class A03_IODemo2 {
    static void main() throws IOException {

        FileInputStream fis = new FileInputStream("C:\\Users\\翟曙\\Desktop\\测试.txt");

        //循环读取对应文件的内容
        int read;
        while((read = fis.read()) != -1){  //这里用read记录所读取到的元素,防止内部输出时再fis.read()移动指针导致数据漏读
            System.out.print((char)read);
        }
        fis.close();
    }
}
