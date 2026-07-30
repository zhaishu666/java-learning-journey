package week7.day04;

import java.io.File;

public class A01_FilePractice5 {
    static void main(String[] args) {

        File f = new File("C:\\Users\\翟曙\\Desktop\\新建文件夹");
        System.out.println(getByte(f));
    }

    public static long getByte(File f) {
        File[] files = f.listFiles();
        long len = 0;
        for (File file : files) {
            if (file.isFile()) {
                len += file.length();
            }
            else{
                len += getByte(file);
            }
        }
        return len;
    }
}
