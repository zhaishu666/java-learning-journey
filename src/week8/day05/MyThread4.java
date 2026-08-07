package week8.day05;

public class MyThread4 extends Thread {

    static int ticket = 0;
    public MyThread4() {
    }

    public MyThread4(String name) {
        super(name);
    }

    @Override
    public void run() {
        while (true) {
            //同步代码块,限定同一时刻只能有一个线程能执行这个代码块里的内容
            synchronized (MyThread4.class) {  //任意非null对象都可以充当锁对象,不过这个对象一定要是唯一的
                //静态情况下常用当前类名.class当作锁对象
                if(ticket < 100){
                    try {
                        sleep(10);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    ticket++;
                    System.out.println(getName() + "正在卖第" + ticket + "张票!!!");
                }else {
                    break;
                }
            }
        }
    }
}
