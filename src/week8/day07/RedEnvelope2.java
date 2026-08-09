package week8.day07;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class RedEnvelope2 implements Runnable {

    //设置总红包钱数
    private static BigDecimal remainingMoney = new BigDecimal("100.00"); //传入金额是必须使用字符串
    //剩余红包数目
    private static int remainingAmount = 3;
    //设置最小红包金额
    private static BigDecimal MIN = BigDecimal.valueOf(0.01);

    @Override
    public void run() {
        synchronized (RedEnvelope2.class) {
            if (remainingAmount == 0) {
                System.out.println(Thread.currentThread().getName() + "没有抢到红包");
            } else {
                BigDecimal redEnvelope;
                if (remainingAmount == 1) {
                    redEnvelope = remainingMoney;
                } else {
                    //设置抽奖范围
                    double bounds = remainingMoney.subtract(BigDecimal.valueOf(remainingAmount - 1).multiply(MIN)).doubleValue();
                    Random r = new Random();
                    //抽奖金额
                    redEnvelope = BigDecimal.valueOf(r.nextDouble(bounds));
                }
                //设置抽到红包保留两位小数
                redEnvelope = redEnvelope.setScale(2, RoundingMode.HALF_UP);

                remainingMoney = remainingMoney.subtract(redEnvelope);

                remainingAmount--;

                System.out.println(Thread.currentThread().getName() + "抢到了" + redEnvelope +"元");
            }
        }
    }
}
