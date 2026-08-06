package week8.day04;

import java.io.*;
import java.util.Properties;

public class A02_Properties1 {
    //测试properties的store()方法
    static void main() throws IOException {

        //Properties类继承于Hashtable,拥有Map集合的所有特点
        Properties prop = new Properties();
        prop.setProperty("key1", "value1");  //等价于put
        prop.setProperty("key2", "value2");

        try (BufferedWriter bw = new BufferedWriter( new OutputStreamWriter( new FileOutputStream("configuration.properties")))) {
            prop.store(bw, "配置注释");  //把内存中的键值对持久化写入配置文件
        }
    }
}
