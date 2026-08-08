package week8.day06;

import java.util.concurrent.ArrayBlockingQueue;

public class Day06Test2 {
    static void main() {
        /*
        * 利用阻塞队列实现等待唤醒机制
        * 细节:
        *     消费者和生产者必须公用一个阻塞队列
        * */
      ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(1);  //创建数组有界阻塞队列

        Cooker2 cooker2 = new Cooker2(queue);
        Foodie2 foodie2 = new Foodie2(queue);

        cooker2.start();
        foodie2.start();
    }
}
