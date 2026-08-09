package week8.day07;


public class RedEnvelopeTest2 {
    static void main() {
        Thread t1 = new Thread(new RedEnvelope2());
        Thread t2 = new Thread(new RedEnvelope2());
        Thread t3 = new Thread(new RedEnvelope2());
        Thread t4 = new Thread(new RedEnvelope2());
        Thread t5 = new Thread(new RedEnvelope2());

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
