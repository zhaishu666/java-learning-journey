package week4.day07;

import java.math.BigInteger;
import java.util.Random;

public class BigIntegerDemo1 {
    static void main(String[] args) {
        //BigInteger可以创建数量级非常大的整数,几乎无上限
        BigInteger bd1 = new BigInteger("99999999999999999999999999999");
        System.out.println(bd1);

       /* for (int i = 0; i < 100; i++) {
            BigInteger bd2 = new BigInteger(5, new Random());  //生成0到2的(5-1)次方的随机数
            System.out.println(bd2);
        }*/

        BigInteger bd3 = new BigInteger("10010", 2);  //获取二进制的10010转化的十进制数
        System.out.println(bd3);

        //静态方法获取BigInteger对象,对-16~16进行了优化,不会反复创建,此方法只能获取到long范围内的变量
        BigInteger bd4 = BigInteger.valueOf(16);
        BigInteger bd5 = BigInteger.valueOf(16);
        BigInteger bd6 = BigInteger.valueOf(17);
        BigInteger bd7 = BigInteger.valueOf(17);
        System.out.println((bd4 == bd5));
        System.out.println((bd6 == bd7));

        System.out.println(bd4.add(bd5)); //此方法虽然会输出两个BigInteger类变量相加的值,但不会改变两个变量原本的值
    }
}
