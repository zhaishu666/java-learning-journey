package week8.day07;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

public class PrizePool2 implements Callable<Integer> {
    private static final ArrayList<Integer> prizePool = new ArrayList<>();  //可以在()内填List.of(1,2,3...)
    //原理: List.of创建固定长度只读集合,传入ArrayList构造器完成拷贝

    static {
        Collections.addAll(prizePool, 10, 5, 20, 50, 100, 200, 500, 800, 2, 80, 300, 700);
    }

    @Override
    public Integer call() throws Exception {
        ArrayList<Integer> prizeBox = new ArrayList<>();
        while (true) {
            synchronized (PrizePool2.class) {
                if (prizePool.isEmpty()) {
                    System.out.println(Thread.currentThread().getName() + "总共抽出" + prizeBox);
                    break;
                }
                int randomIndex = ThreadLocalRandom.current().nextInt(prizePool.size());
                Integer prize = prizePool.get(randomIndex);
                prizeBox.add(prize);
                prizePool.remove(randomIndex);
            }
            Thread.sleep(10);
        }
        if(prizeBox == null){
            return null;
        }else {
            return Collections.max(prizeBox);
        }
    }
}
