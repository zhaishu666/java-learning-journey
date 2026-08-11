package week9.day01;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class A01_ThreadPoolExecutorDemo {
    static void main() {
        //练习创建线程
        ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 4, 60L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

        //pool.submit()  支持Runnable,Callable 返回Future对象,可以获取返回值,捕获异常
        //pool.execute()  仅支持没有返回值的Runnable,异常直接抛出

        //System.out.println(Runtime.getRuntime().availableProcessors());  获得Java可以获得的线程数
    }
}
