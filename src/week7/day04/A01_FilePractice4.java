package week7.day04;

import java.io.File;

public class A01_FilePractice4 {
    static void main() {

        File f = new File("C:\\Users\\翟曙\\Desktop\\新建文件夹");
        delete(f);
    }

    public static void delete(File f) {
        File[] files = f.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    file.delete();
                }
                else {
                    delete(file);
                }
            }
        }
        f.delete();
    }
}
