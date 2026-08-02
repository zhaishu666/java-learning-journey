package week7.day07;

import java.io.*;

public class A01_BufferedIO1 {
    static void main() throws IOException {
        // 测试字符缓冲流的两个独有方法
        try(BufferedReader br = new BufferedReader(new FileReader("C:/Users/翟曙/Desktop/测试.txt"));
            BufferedWriter bw = new BufferedWriter(new FileWriter("a.txt"))){
            String line;
            while ((line = br.readLine()) != null){  //readLine会读取整行的字符,没有数据可读时,会返回null
                //System.out.println(line);  //虽然能识别到换行符,但不会读取,所以需要加ln
                bw.write(line);
                bw.newLine();    //跨平台的换行符,所有平台都可用
            }
        }
    }
}
