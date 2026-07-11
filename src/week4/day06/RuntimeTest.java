package week4.day06;

import java.io.IOException;

public class RuntimeTest {
    static void main() throws IOException {
        Runtime runtime = Runtime.getRuntime();

        System.out.println(runtime.availableProcessors()); //获取到cpu的线程数

        System.out.println(runtime.maxMemory() / 1024 /1024); //jvm能从系统中获取的最大内存大小/1024/1024转化为mb

        System.out.println(runtime.totalMemory() / 1024 /1024); //已经获取的内存大小

        System.out.println(runtime.freeMemory() / 1024 / 1024);  //jvm中剩余内存大小

        runtime.exec("shutdown -a");  //该写法已过时

      /*  runtime.exit(0);
        System.out.println("执行了这段代码");*/
    }
}
