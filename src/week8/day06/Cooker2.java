package week8.day06;

import java.util.concurrent.ArrayBlockingQueue;

public class Cooker2 extends Thread {

    ArrayBlockingQueue<String> queue;

    public Cooker2(ArrayBlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                queue.put("面条");
                System.out.println("厨师做了一碗面条");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
