package week8.day04;

import java.util.concurrent.Callable;

public class CallTask implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        return 100;   //可以返回结果,抛出异常
    }

}
