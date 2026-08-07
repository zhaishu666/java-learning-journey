package week8.day05;

public class MyDaemonThread1 extends Thread {

    public MyDaemonThread1() {
    }

    public MyDaemonThread1(String name) {
        super(name);
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println(getName() + "@" + i);
        }
    }
}
