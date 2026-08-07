package week8.day05;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyCallable2 implements Callable<String> {
    static int ticket = 0;

    private static final Lock lock = new ReentrantLock();
    @Override
    public String call() throws Exception {
        while(true){
            try{
                lock.lock();
                if(ticket == 100){
                    break;
                }else {
                    Thread.sleep(100);
                    ticket++;
                    System.out.println(Thread.currentThread().getName() + "正在卖第" + ticket + "张票!!!");
                }
            }finally {
                lock.unlock();
            }
        }
        return null;
    }
}
