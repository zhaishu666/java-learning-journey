package week8.day05;

public class MyThread2 extends Thread{

    public MyThread2() {
    }

    public MyThread2(String name) {
        super(name);
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            Thread t = currentThread(); //获取当前线程的对象
            try {
                t.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(getName() + "@" + i);
        }
    }
}
