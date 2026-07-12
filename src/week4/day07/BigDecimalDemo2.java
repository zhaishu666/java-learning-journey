package week4.day07;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalDemo2 {
    static void main(String[] args) {
        BigDecimal bd1 = new BigDecimal("10.9");
        BigDecimal bd2 = new BigDecimal("0.226");

        BigDecimal add = bd1.add(bd2);
        System.out.println(add);
        System.out.println(bd1);
        System.out.println(bd2);

        System.out.println(bd1.subtract(bd2));
        System.out.println(bd1.multiply(bd2));

        System.out.println(bd1.divide(bd2, 2, RoundingMode.HALF_UP));
    }
}
