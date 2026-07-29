package week7.day03;

import java.io.File;

public class A03_FilePractice3 {
    public static void main(String[] args) {
        //找到week7中所有以avi结尾的文件
        File dir = new File("src/week7");
        getAvi(dir);

    }

    public static void getAvi(File f) {
        File[] files = f.listFiles();
        if (files == null) {
            System.out.println("目录无法访问" + f.getAbsolutePath());
            return;
        }
        for (File f1 : files) {
            if (f1.isDirectory()) {
                getAvi(f1);
            } else if (f1.isFile() && f1.getName().toLowerCase().endsWith(".avi")) {
                System.out.println(f1.getAbsoluteFile());
            }
        }
    }
}
