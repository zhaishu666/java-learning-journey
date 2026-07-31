package week7.day05;

import java.io.FileReader;
import java.io.IOException;

public class A05_IODemo3 {
    public static void main(String[] args) throws IOException {

        //使用带参的read(char[] buffer) 读取多个数据
        FileReader fr = new FileReader("C:\\Users\\翟曙\\Desktop\\测试.txt");

        char[] chars = new char[2];
        int len;
        while((len = fr.read(chars)) != -1){  //这里的带参构造实际上是吧无参构造中的读取数据,解码,强转三步合并了,把强转后的字符放到数组当中
            //len是每次实际读取的长度
            System.out.println(new String(chars,0,len)); //转化为String类型后再输出
        }
        fr.close();
    }
}
