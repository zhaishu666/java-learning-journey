package week8.day05;

public class MyRun2 implements Runnable {
    int ticket;
    @Override
    public void run() {
        while (true) {
            if (sellTicket()) break;
        }
    }

    public synchronized boolean sellTicket() {  //同步方法,直接给整个方法加锁.缺点: 锁粒度较大,并发性能低于同步代码块
        if(ticket==100){
            return true;
        }else {
            ticket++;
            System.out.println(Thread.currentThread().getName() + "正在卖第" + ticket + "张票!!!");
        }
        return false;
    }
}
