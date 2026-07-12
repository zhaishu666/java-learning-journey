package week4.day07;

import java.math.BigDecimal;

public class BigDecimalDemo1 {
    static void main(String[] args) {
        //在使用BigDecimal构造方法时传入小数可能导致结果不精确,建议使用String或者静态方法
        BigDecimal bd1 = new BigDecimal("10.9");
        BigDecimal bd2 = new BigDecimal(10.9);
        System.out.println(bd2);
        System.out.println(bd1);

        BigDecimal bd3 = BigDecimal.valueOf(10);
        BigDecimal bd4 = BigDecimal.valueOf(10);
        System.out.println((bd3 == bd4));
    }
}
