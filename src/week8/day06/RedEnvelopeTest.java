package week8.day06;

public class RedEnvelopeTest {
    public static void main(String[] args) {

        Thread t1 = new Thread(new RedEnvelope());
        Thread t2 = new Thread(new RedEnvelope());
        Thread t3 = new Thread(new RedEnvelope());
        Thread t4 = new Thread(new RedEnvelope());
        Thread t5 = new Thread(new RedEnvelope());

        t1.setName("用户1");
        t2.setName("用户2");
        t3.setName("用户3");
        t4.setName("用户4");
        t5.setName("用户5");

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
    }
}
