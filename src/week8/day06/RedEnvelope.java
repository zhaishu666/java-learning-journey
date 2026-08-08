package week8.day06;

import java.util.Random;

public class RedEnvelope implements Runnable {
    private static int redEnvelope = 100;
    private static Random r = new Random();
    static int count = 3;
    @Override
    public void run() {
        synchronized (RedEnvelope.class) {
            if(redEnvelope == 0){
                System.out.println(Thread.currentThread().getName() + "没抢到");
            }else {
                count--;
                int gain = 0;
                if (count > 0) {
                    gain = r.nextInt(redEnvelope);
                    redEnvelope -= gain;
                }else if (count == 0) {
                    gain = redEnvelope;
                    redEnvelope = 0;
                }
                System.out.println(Thread.currentThread().getName() + "抢到了" + gain + "元");
            }
        }
    }
}
