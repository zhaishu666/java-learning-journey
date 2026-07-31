package week7.day05;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class A05_IODemo1 {
    static void main() throws UnsupportedEncodingException {
        //测试Java中的编码和解码方式

        String str1 = "zhongren能飞";
        byte[] bytes = str1.getBytes();  //会使用idea中默认的编码方式进行编码,以字节数组的形式返回
        byte[] bytes2 = str1.getBytes("GBK");  //指定编码方式
        System.out.println(Arrays.toString(bytes));
        String str2 = new String(bytes);  //默认解码方式解码
        String str3 = new String(bytes2, "GBK");
        //System.out.println(str2);
        System.out.println(str3);
    }
}
