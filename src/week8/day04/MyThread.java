package week8.day04;

public class MyThread extends Thread{
    @Override
    public void run() {
        //书写线程所要执行的代码
        for (int i = 0; i < 100; i++) {
            System.out.println(getName() + ": Hello World!");
        }
    }
}
