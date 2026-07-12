package week4.day07;

import java.math.BigInteger;

public class BigIntegerDemo2 {
    static void main(String[] args) {
        BigInteger bd1 = BigInteger.valueOf(10);
        BigInteger bd2 = BigInteger.valueOf(4);

        System.out.println(bd1.add(bd2)); //加法
        System.out.println("----------------------------------------");
        // System.out.println(bd1);
        // System.out.println(bd2);

        System.out.println(bd1.subtract(bd2));  //减法
        System.out.println("----------------------------------------");
        //System.out.println(bd1);
        // System.out.println(bd2);

        System.out.println(bd1.multiply(bd2));   //乘法
        System.out.println("----------------------------------------");

        System.out.println(bd1.divide(bd2));  //只获取商的除法
        System.out.println("----------------------------------------");

        BigInteger[] arr = bd1.divideAndRemainder(bd2);
        System.out.println(arr[0] + " " + arr[1]);
        System.out.println("----------------------------------------");

        System.out.println(bd1.equals(bd2));  //比较是否相同

        System.out.println(bd1.pow(2));  //2次幂
        System.out.println(bd2.pow(2));

        System.out.println(bd1.max(bd2));
        System.out.println(bd1.min(bd2));

        System.out.println(bd1.intValue());  //转化为int类型
    }
}
