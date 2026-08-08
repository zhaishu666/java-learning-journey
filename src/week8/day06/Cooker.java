package week8.day06;

public class Cooker extends Thread {
    @Override
    public void run() {

        while (true) {
            synchronized (Desk.lock) {
                if (Desk.count == 0) {
                    break;
                }
               while (Desk.foodFlag == 1) {
                    try {
                        Desk.lock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
               if (Desk.foodFlag == 0) {
                    System.out.println("厨师做了一碗面条");
                    Desk.foodFlag = 1;
                    Desk.lock.notifyAll();
                }
            }
        }
    }
}
