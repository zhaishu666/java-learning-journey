package week7.day03;

import java.io.File;
import java.io.IOException;

public class A03_FilePractice1 {
    public static void main(String[] args) throws IOException {

        File f = new File("src/week7/day03/bbb.txt");
        System.out.println(f.createNewFile());
    }
}
