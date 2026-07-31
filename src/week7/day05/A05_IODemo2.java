package week7.day05;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class A05_IODemo2 {
    static void main() throws IOException {
        //尝试使用字符流的read()读取文本文件中的中文
        FileReader fr = new FileReader("C:\\Users\\翟曙\\Desktop\\测试.txt");
        int len;
        while((len = fr.read()) != -1){
            char[] chars = Character.toChars(len); //将对应字符集上的数字转化为中文的形式
            System.out.print(chars);
        }
        fr.close();
    }
}
