package week8.day05;

public class A01_ThreadDemo4 {
    public static void main(String[] args) {

        MyThread4 mt1 = new MyThread4("售票口1");
        MyThread4 mt2 = new MyThread4("售票口2");
        MyThread4 mt3 = new MyThread4("售票口3");

        mt1.start();
        mt2.start();
        mt3.start();
    }
}
