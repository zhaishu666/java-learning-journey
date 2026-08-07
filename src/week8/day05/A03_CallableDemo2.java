package week8.day05;

import java.util.concurrent.FutureTask;

public class A03_CallableDemo2 {
    static void main(){

        FutureTask<String> sft = new FutureTask<>(new MyCallable2());

        Thread t1 = new Thread(sft);
        Thread t2 = new Thread(sft);
        Thread t3 = new Thread(sft);

        t1.setName("售票处1");
        t2.setName("售票处2");
        t3.setName("售票处3");

        t1.start();
        t2.start();
        t3.start();
    }
}
