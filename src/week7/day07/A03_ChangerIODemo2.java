package week7.day07;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class A03_ChangerIODemo2 {
    public static void main(String[] args) throws IOException {
        //字符流想要使用缓冲流中的readLine方法,该怎么实现?
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("a.txt")));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }
}
