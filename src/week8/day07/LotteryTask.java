package week8.day07;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class LotteryTask implements Runnable {

    private final String boxName;
    private final ArrayList<Integer> prizeList;

    public LotteryTask(String boxName, ArrayList<Integer> prizeList) {
        this.boxName = boxName;
        this.prizeList = prizeList;
    }

    @Override
    public void run() {
        // 模拟抽奖逻辑，过程不打印
        ArrayList<Integer> myPrize = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            synchronized (prizeList) {
                int index = ThreadLocalRandom.current().nextInt(prizeList.size());
                int num = prizeList.remove(index);
                myPrize.add(num);
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 抽完统一打印
        int max = Collections.max(myPrize);
        int sum = myPrize.stream().mapToInt(Integer::intValue).sum();
        System.out.printf("在此次抽奖过程中，%s总共产生了6个奖项。%n", boxName);
        System.out.println("分别为：" + myPrize + "最高奖项为" + max + "元，总计额为" + sum + "元\n");
    }
}

