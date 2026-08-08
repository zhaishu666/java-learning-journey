package week8.day06;

public class Desk {

    // 0: 没有面条. 1: 有面条吃
    public static int foodFlag = 0;

    //面条的碗数
    public static int count = 10;

    //锁对象
    public static Object lock = new Object();
}
