package week8.day04;

public class MyRun implements Runnable {
    @Override
    public void run() {
        for (int i = 1; i <= 100; i++) {
            Thread t = Thread.currentThread();  //获得当前线程的对象
            System.out.println(t.getName() + " is running");
        }
    }
}
