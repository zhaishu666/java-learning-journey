package week7.day04;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class A03_IODemo3 {
    public static void main(String[] args) throws IOException {

        //简单的文件拷贝,边读边写
        FileInputStream fis = new FileInputStream("C:\\Users\\翟曙\\Desktop\\测试.txt");
        FileOutputStream fos = new FileOutputStream("E:\\develpment\\base-code\\java-learning-journey\\复制测试.txt");
        int read;
        while((read=fis.read())!=-1){
            fos.write(read);
        }
        fis.close();  //先打开的最后关闭
        fos.close();
    }
}
