package week3.day06;

import java.util.Scanner;

public class InterTest {
    static void main() {
        // Inter a = 20;  //报错因为接口中的成员变量为常量
        InterImpl ii = new InterImpl();
        ii.method();
        Scanner sc = new Scanner(System.in);
        sc.next();
    }
}

