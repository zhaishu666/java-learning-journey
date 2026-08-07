package week8.day05;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyRun3 implements Runnable {
    static int ticket = 0;

    static Lock lock = new ReentrantLock();  //通过ReentrantLock实现类创建Lock接口的对象
    @Override
    public void run() {
        while (true) {
            lock.lock();
            try {
                if(ticket == 100) {
                    break;
                }else  {
                    ticket++;
                    System.out.println(Thread.currentThread().getName() + "正在卖第" + ticket + "张票!!!");
                }
            } finally {
                lock.unlock();  //unlock一定要写在finally块中,不然如果业务代码抛出异常,锁永远不会释放,其他线程统统卡死
            }
        }
    }
}

