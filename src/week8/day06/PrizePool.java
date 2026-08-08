package week8.day06;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class PrizePool implements Runnable {
    private static ArrayList<Integer> redEnvelope = new ArrayList<>();  //可以在()内填List.of(1,2,3...)
    //原理: List.of创建固定长度只读集合,传入ArrayList构造器完成拷贝
    static {
        Collections.addAll(redEnvelope,10,5,20,50,100,200,500,800,2,80,300,700);
    }
    @Override
    public void run() {
        while (true) {
            synchronized (PrizePool.class) {
                if (redEnvelope.isEmpty()) break;

                int randomIndex = ThreadLocalRandom.current().nextInt(redEnvelope.size());
                Integer prize = redEnvelope.get(randomIndex);
                System.out.println(Thread.currentThread().getName()+ "又产生了一个" +
                        prize + "元大奖");
               redEnvelope.remove(randomIndex);
            }
        }
    }
}
