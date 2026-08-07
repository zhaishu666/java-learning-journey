package week8.day05;

public class A02_RunnableDemo2 {
    public static void main(String[] args) {

        MyRun2 mr = new MyRun2();

        Thread t1 = new Thread(mr);
        Thread t2 = new Thread(mr);
        Thread t3 = new Thread(mr);

        t1.start();
        t2.start();
        t3.start();
    }
}
