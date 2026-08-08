package week8.day06;

public class OddNumberTest {
    public static void main(String[] args) {

        Thread t1 = new Thread(new OddNumber());
        Thread t2 = new Thread(new OddNumber());

        t1.setName("计算1");
        t2.setName("计算1");
        t1.start();
        t2.start();
    }
}
