package week1.day06;

import java.util.Scanner;

/*飞机票的价格会随着是否为淡季和是否为头等舱变化,输入原票价,月份,头等舱或经济舱
5-10月旺季头等舱9折,经济舱8.5折,11月到来年4月为淡季,头等舱7折经济舱6.5折
CTRL + ALT + M 自动抽取方法  CTRL + P 显示方法内顺序
 */
public class AirTicket {

    public static double change(double price,int month,boolean flag){
        double newPrice = 0;
        if(flag){
            if(month >= 5 && month <= 10){
                 newPrice = price * 0.9;
            } else if (month >= 11 || month <= 4) {
                newPrice = price * 0.7;
            }
        }else{
            if(month >= 5 && month <= 10){
                newPrice = price * 0.85;
            } else if (month >= 11 || month <= 4) {
                newPrice = price * 0.65;
            }
        }
        return newPrice;
    }

    static void main() {
        Scanner sc = new Scanner(System.in);
        System.out.println("是否为头等舱,是请输入true,经济舱输入false");
        boolean flag = sc.nextBoolean();
        System.out.println("请输入原票价");
        double price = sc.nextDouble();
        if( price <= 0){
            System.out.println("不合理价格");
            return;
        }
        System.out.println("请输入月份");
        int month = sc.nextInt();
        if (month < 0 || month > 12){
            System.out.println("不存在这个月份");
            sc.close();
            return;
        }
        double newPrice = change(price,month,flag);
        System.out.println("打完折后的价格为" + newPrice);
    }
}
