package week8.day04;

public class A03_Thread1 {
    public static void main(String[] args) {
        MyThread mt1 = new MyThread();
        MyThread mt2 = new MyThread();

        mt1.setName("线程1");
        mt2.setName("线程2");

        mt1.start();  //一个CPU在两个线程之间交替执行,这就是并发
        mt2.start();
    }
}
