package week8.day06;

public class PrizePoolTest {
    public static void main(String[] args) {

        Thread t1 = new Thread(new PrizePool());
        Thread t2 = new Thread(new PrizePool());

        t1.setName("抽奖箱1");
        t2.setName("抽奖箱2");

        t1.start();
        t2.start();
    }
}
