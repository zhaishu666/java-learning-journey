package week8.day05;

public class A01_ThreadDemo2 {
    public static void main(String[] args) {

        MyThread2 mt1 = new MyThread2("线程1");
        MyThread2 mt2 = new MyThread2("线程2");

        mt1.setPriority(10);  //给线程设置优先级,优先级越高,被CPU执行的概率越大
        mt2.setPriority(1);

        mt1.start();
        mt2.start();
    }
}
