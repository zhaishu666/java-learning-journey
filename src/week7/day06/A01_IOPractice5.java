package week7.day06;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class A01_IOPractice5 {
    public static void main(String[] args) throws IOException {
        //将原本文件中的2-7-1-4-9-8 中的数字排序后输出,如1-2-4-7-8-9
        try (FileInputStream fis = new FileInputStream("C:/Users/翟曙/Desktop/测试.txt");
             FileOutputStream fos = new FileOutputStream("C:/Users/翟曙/Desktop/输出测试.txt")){
            StringBuilder sb = new StringBuilder();
            int ch;
            while ((ch = fis.read()) != -1) {
                sb.append((char) ch);
            }

            Integer[] arr = Arrays.stream(sb.toString().split("-"))
                    .map(Integer::parseInt)
                    .sorted()
                    .toArray(Integer[]::new);

            String s = Arrays.toString(arr);
            String result = s.substring(1, s.length() - 1).replace(", ", "-");
            fos.write(result.getBytes(StandardCharsets.UTF_8));
        }
    }
}
