package week8.day06;

public class Foodie extends Thread {

    @Override
    public void run() {
        while (true) {
            synchronized (Desk.lock) {
                if (Desk.count == 0) {
                    break;
                } else {
                    while (Desk.foodFlag == 0) {
                        try {
                            Desk.lock.wait();  //让当前线程于锁绑定
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if(Desk.foodFlag == 1){
                        Desk.count--;
                        System.out.println("吃货正在吃面条,还能吃" + Desk.count + "碗!!!");
                        //吃完后唤醒厨师继续做
                        Desk.lock.notifyAll();
                        //修改桌子的状态
                        Desk.foodFlag = 0;
                    }
                }
            }
        }
    }
}