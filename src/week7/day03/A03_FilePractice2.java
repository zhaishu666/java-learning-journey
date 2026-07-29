package week7.day03;

import java.io.File;

public class A03_FilePractice2 {
    public static void main(String[] args) {
        File f = new File("src/week7/day03");
        File[] files = f.listFiles();
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".avi")) {
                System.out.println(file.getName());
            }
        }
    }
}
