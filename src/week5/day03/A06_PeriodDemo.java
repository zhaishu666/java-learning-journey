package week5.day03;

import java.time.LocalDate;
import java.time.Period;

public class A06_PeriodDemo {
    public static void main(String[] args) {
        LocalDate d1 = LocalDate.now();

        LocalDate d2 = LocalDate.of(2020, 1, 1);

        Period period = Period.between(d2, d1);  //获得两个日期的时间差,后面减前面
        System.out.println(period.getYears());   //需要使用方法来获取里面的年月日
        System.out.println(period.getMonths());
        System.out.println(period.getDays());
    }
}
