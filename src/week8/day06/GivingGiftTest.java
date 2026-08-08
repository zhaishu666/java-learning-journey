package week8.day06;

public class GivingGiftTest {
    static void main() {

        Thread t1 = new Thread(new GivingGift());
        Thread t2 = new Thread(new GivingGift());

        t1.setName("同学1");
        t2.setName("同学2");
        t1.start();
        t2.start();
    }
}
