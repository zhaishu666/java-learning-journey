package week8.day06;

public class SellTicketTest {
    public static void main(String[] args) {

        Thread t1 = new Thread(new SellTicket());
        Thread t2 = new Thread(new SellTicket());

        t1.setName("窗口1");
        t2.setName("窗口2");
        t1.start();
        t2.start();
    }
}
