package week7.day05;

import java.io.FileWriter;
import java.io.IOException;

public class A05_IODemo4 {
    public static void main(String[] args) throws IOException {

        //通过FileWriter的对象写出内容

        FileWriter fw = new FileWriter("E:/develpment/base-code/java-learning-journey/configuration.properties");

        char[] chars = {'我','是','帅','b'};
        //fw.write(25105); //写出一个字符,()内为这个字符的Unicode编码值
        //fw.write("中国人能飞"); //写出一个字符串
        //fw.write("中国人能飞",0,3); //写出字符串的部分,包左不包右
        //fw.write(chars);  //写出字符数组
        fw.write(chars,0,3); //写出字符数组的部分,包左不包右
        fw.close();
    }
}
