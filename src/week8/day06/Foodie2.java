package week8.day06;

import java.util.concurrent.ArrayBlockingQueue;

public class Foodie2 extends Thread{

    ArrayBlockingQueue<String> queue;

    public Foodie2(ArrayBlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                queue.take();
                System.out.println("顾客正在吃面");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
