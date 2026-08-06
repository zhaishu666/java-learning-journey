package week8.day04;

import java.util.concurrent.FutureTask;

public class A03_Callable1 {
    public static void main(String[] args){
        //CallTask包装成FutureTask,再交给Thread
        FutureTask<Integer> ift = new FutureTask<>(new CallTask());
        Thread t = new Thread(ift);
        t.start();
    }
}
