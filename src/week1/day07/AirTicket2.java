package week1.day07;

import java.util.Scanner;

/*飞机票的价格会随着是否为淡季和是否为头等舱变化,输入原票价,月份,头等舱或经济舱
5-10月旺季头等舱9折,经济舱8.5折,11月到来年4月为淡季,头等舱7折经济舱6.5折
CTRL + ALT + M 自动抽取方法  CTRL + P 显示方法内顺序 CTRL + shift + / 一键注释多行
总结:书写代码时需要注意代码的可扩展性
 */
public class AirTicket2 {
    /*public static double change(double price, int seat, double v0, double v1) {
        if (seat == 0) {
            price = price * v0;
        } else if (seat == 1) {
            price = price * v1;
        }
        return price;
    }*/

    static void main() {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入月份");
        int month = sc.nextInt();
        if (month < 0 || month > 12) {
            System.out.println("不存在这个月份");
            sc.close();
            return;
        }
        System.out.println("头等舱请输入0,经济仓请输入1");
        int seat = sc.nextInt();
        System.out.println("请输入原票价");
        double price = sc.nextDouble();
        if (price <= 0) {
            System.out.println("不合理价格");
            return;
        }

        if (month >= 5 && month <= 10){
            price = getPrice(seat, price, 0.9, 0.85);
        } else if (month <= 4 || month >= 11) {
            price = getPrice(seat, price, 0.7, 0.65);
        }

        System.out.println("打完折后的价格为" + price);
    }

    public static double getPrice(int seat, double price, double x, double x1) {
        if (seat == 0) {
            price = price * x;
        } else if (seat == 1) {
            price = price * x1;
        }
        return price;
    }
}
