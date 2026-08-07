package week8.day05;

public class A01_ThreadDemo3 {
    public static void main(String[] args) {

        MyThread3 mt = new MyThread3("女神");
        MyDaemonThread1 mdt = new MyDaemonThread1("备胎");

        mdt.setDaemon(true); //设置为守护线程
        //当其他非守护线程执行完毕后,守护线程也会陆陆续续结束
        mt.start();
        mdt.start();
    }
}
