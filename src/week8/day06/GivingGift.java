package week8.day06;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GivingGift implements Runnable {
    private static int gift = 100;
    private static int give = 0;
    private static final Lock lock = new ReentrantLock();
    @Override
    public void run() {
        while (true) {
            try{
                lock.lock();
                if(gift == 10){
                    break;
                }else {
                    give++;
                    gift--;
                    System.out.println(Thread.currentThread().getName() + "正在送出第" + give
                    + "份礼物,还剩" + gift + "份");
                }
            }finally {
                lock.unlock();
            }
        }
    }
}
