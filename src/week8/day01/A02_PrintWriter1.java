package week8.day01;

import java.io.PrintStream;

public class A02_PrintWriter1 {
    static void main() {
        PrintStream out = System.out;  //获取打印流的对象,此打印流在虚拟机启动时,由虚拟机创建.默认指向控制台
        out.println("Hello World");
        //out.close();            //注意是不能关闭的,该流在系统中是唯一的,关闭后无法继续打印,只能重启JVM
        out.println("Hello World");
    }
}
