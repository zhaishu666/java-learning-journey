package week8.day06;

public class SellTicket implements Runnable {
    private static int ticket = 0;

    @Override
    public void run() {
        while (true) {
            synchronized (SellTicket.class) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (ticket == 1000) {
                    break;
                }
                ticket++;
                System.out.println(Thread.currentThread().getName() + "正在卖第" + ticket + "张票"
                        + "还剩下" + (1000 - ticket) + "张票");
            }
        }
    }
}
