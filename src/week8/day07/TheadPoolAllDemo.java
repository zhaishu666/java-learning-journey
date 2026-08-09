package week8.day07;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TheadPoolAllDemo {
    public static void main(String[] args) {

        //标准创建线程池
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                2, //核心线程数量
                4, //最大总线程4个  = 核心线程 + 临时线程
                2L, //临时线程空闲2秒后销毁,改类型为long类型
                TimeUnit.SECONDS,  //设置时间单位
                new ArrayBlockingQueue<>(5),  //阻塞队列
                Executors.defaultThreadFactory(),   //线程工厂,创建线程
                new ThreadPoolExecutor.CallerRunsPolicy()   //拒绝策略
        );

        // 初始化两个抽奖箱奖品
        ArrayList<Integer> box1 = new ArrayList<>();
        box1.add(10);box1.add(20);box1.add(100);box1.add(500);box1.add(2);box1.add(300);
        ArrayList<Integer> box2 = new ArrayList<>();
        box2.add(5);box2.add(50);box2.add(200);box2.add(800);box2.add(80);box2.add(700);

        // 2.提交任务到线程池，复用线程执行抽奖
        pool.execute(new LotteryTask("抽奖盒1", box1));
        pool.execute(new LotteryTask("抽奖盒2", box2));

        //温和关闭线程池,等待所有任务执行完成
        pool.shutdown();

        try {
            //等待线程池全部执行完毕,最多等待一分钟
            pool.awaitTermination(1,TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
