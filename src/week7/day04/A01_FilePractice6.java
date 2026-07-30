package week7.day04;

import java.io.File;
import java.util.HashMap;

public class A01_FilePractice6 {

    public static void main(String[] args) {

        HashMap<String, Integer> map = new HashMap<>();
        File f = new File("C:\\Users\\翟曙\\Desktop\\新建文件夹");
        allKindFilesCount(f,map);
        map.forEach((s, count) -> System.out.println(s + ": " + count));
    }

    public static void allKindFilesCount(File f, HashMap<String, Integer> map) {
        File[] files = f.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String name = file.getName().toLowerCase();
                    if (name.endsWith(".txt")) {
                        map.merge("txt", 1, Integer::sum);
                    }if  (name.endsWith(".docx")) {
                        map.merge("docx", 1, Integer::sum);
                    }if (name.endsWith(".jpg")) {
                        map.merge("jpg", 1, Integer::sum);
                    }
                }
                else if(file.isDirectory()) {
                    allKindFilesCount(file,map);
                }
            }
        }
    }
}
