package week8.day07;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class PrizePoolTest2 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        FutureTask ft1 = new FutureTask<>(new PrizePool2());
        FutureTask ft2 = new FutureTask<>(new PrizePool2());

        Thread t1 = new Thread(ft1);
        Thread t2 = new Thread(ft2);

        t1.setName("抽奖箱1");
        t2.setName("抽奖箱2");

        t1.start();
        t2.start();

        Integer result1 = (Integer) ft1.get();
        Integer result2 = (Integer) ft2.get();

        if(result1 > result2){
            System.out.println("抽奖箱1抽出了最大奖" + result1);
        }else{
            System.out.println("抽奖箱2抽出了最大奖" + result2);
        }
    }
}
