package week8.day04;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class A02_Properties2 {
    public static void main(String[] args) throws IOException {
        //测试load方法
        Properties prop = new Properties();

        try(BufferedReader br = new BufferedReader(new InputStreamReader( new FileInputStream("configuration.properties")))){
            prop.load(br);  //加载配置文件
        }
       prop.forEach((k,v) -> System.out.println(k+"="+v));
    }
}
