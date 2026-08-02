package week7.day07;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;

public class A03_ChangerIODemo1 {
    public static void main(String[] args) throws IOException {
        //创建字符流,指定字符集读取文件
        FileReader fr = new FileReader("C:\\Users\\翟曙\\Desktop\\测试.txt", Charset.forName("GBK"));
        FileWriter fw = new FileWriter("a.txt");
        int len;
        while ((len = fr.read()) != -1) {
            char ch = (char) len;
            fw.write(ch);
        }
        fw.close();
        fr.close();
    }
}
