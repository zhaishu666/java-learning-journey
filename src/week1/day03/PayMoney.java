package week1.day03;

import java.util.Scanner;

//根据不同的会员等级进行打折1级打9折,2级8折,3级7折,算出最后所需要付的钱
public class PayMoney {
    static void main() {
        Scanner sc = new Scanner(System.in);
        System.out.println("输入会员等级");
        int level = sc.nextInt();
        System.out.println("输入商品原价");
        double price = sc.nextDouble();
        if( level > 0 && level <= 3){
            if( level == 1 ){
                price = price * 0.9;
            }else if( level == 2 ){
                price = price * 0.8;
            }else{
                price = price * 0.7;
            }
            System.out.println("应付价格为" + price);
        }else{
            System.out.println("无对应会员等级");
        }
    }
}
