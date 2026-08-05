package week8.day03;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class A03_Practice4 {
    /*
    * 随机点名器,文件每行存储一个学生对象
    * 第三次必定点到张三
    * */
    static void main() throws IOException {
        ArrayList<String> nameList = new ArrayList<>();
        Random r = new Random();
        try (BufferedReader br = new BufferedReader(new FileReader("a.txt"))) {
            String ch;
            while ((ch = br.readLine()) != null) {
                nameList.add(ch);
            }
        }
        System.out.println(randomName(nameList, r));
    }

    public static String randomName(ArrayList<String> list, Random r) throws IOException {
        try (FileInputStream fis = new FileInputStream("count.txt")) {
            int read = fis.read();
            System.out.println((char) read);
            if( read <= '3'){
                read++;
                try(FileOutputStream fos = new FileOutputStream("count.txt")){
                    fos.write((char)read);
                }
            }
            if((char)read == '3'){
                return "张三";
            }
        }
        int i = r.nextInt(list.size());
        return list.get(i).split("-")[0];
    }
}
