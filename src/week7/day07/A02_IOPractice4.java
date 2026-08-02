package week7.day07;

import java.io.*;

public class A02_IOPractice4 {
    public static void main(String[] args) throws IOException {
        //将软件使用次数放文件中,次数大于3后不可使用
        BufferedReader br = new BufferedReader(new FileReader("count.txt"));
        String s = br.readLine();
        int count = Integer.parseInt(s);
        count++;
        if (count <= 3) {
            System.out.println("欢迎使用本软件,当前为第" + count + "次,免费使用");
        } else {
            System.out.println("免费使用次数已用完");
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter("count.txt"));
        bw.write(count + "");  //为什么要加""? 因为你写数据实际上写的是这个数据在字符集中所对应的字符

        bw.close();
        br.close();
    }
}
