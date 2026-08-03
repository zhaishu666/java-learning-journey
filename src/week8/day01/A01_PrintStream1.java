package week8.day01;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class A01_PrintStream1 {
    static void main() throws FileNotFoundException {

        PrintStream ps = new PrintStream(new FileOutputStream("a.txt"));

        ps.print(97);
        ps.println("Hello World");
        ps.printf("它有%d个魔丸",10);
        ps.close();
    }
}
