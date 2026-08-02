package week7.day07;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

public class A02_IOPractice3 {
    public static void main(String[] args) throws IOException {
        //将一个文件中的乱序段落重新排序后写到a.txt
        try (BufferedReader br = new BufferedReader(new FileReader("C:/Users/翟曙/Desktop/测试.txt"));
             BufferedWriter bw = new BufferedWriter(new FileWriter("a.txt"))) {
            TreeMap<Integer, String> treemap = new TreeMap<>();
            String line;
            while ((line = br.readLine()) != null) {
                treemap.put((int) line.charAt(0), line);
            }
            for (Map.Entry<Integer, String> entry : treemap.entrySet()) {
                String value = entry.getValue();
                bw.write(value);
                bw.newLine();
            }
        }
    }
}
