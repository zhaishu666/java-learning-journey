package week2.day05;

import java.util.Scanner;

public class StringChangeMoney {
    static void main() {
        Scanner sc = new Scanner(System.in);
        int money;
        while (true) {
            System.out.println("请输入金额(小于7位数)");
            money = sc.nextInt();
            if (money >= 0 && money <= 9999999) {
                break;
            } else {
                System.out.println("超出范围");
            }
        }
        String fanYuan = changeMoney(money);
        String newYuan = reverseMoney(fanYuan);
        newYuan = getling(newYuan) + newYuan;
        String finalYuan = getFinalYuan(newYuan);
        System.out.println(finalYuan);
    }

    public static String getFinalYuan(String newYuan) {
        String[] arr = {"佰","拾","万","仟","佰","拾","元"};
        String finalYuan = "";
        for (int i = 0; i < newYuan.length(); i++) {
            char c = newYuan.charAt(i);
            finalYuan = finalYuan + c + arr[i];
        }
        return finalYuan;
    }

    public static String getling(String newYuan) { //获得需要补的零
        int count = 7;
        count = count - newYuan.length();
        String ling = "";
        for (int i = 0; i < count; i++) {
            ling = ling + "零";
        }
        return ling;
    }

    public static String changeMoney(int money) { //将数字的价钱转化为繁体字中文
        String[] arr = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
        String fanYuan = "";
        while (true) {
            if (money != 0) {
                int num = money % 10;
                money = money / 10;
                String change = arr[num];
                fanYuan += change;
            } else {
                break;
            }
        }
        return fanYuan;
    }

    public static String reverseMoney(String fanYuan) { //将繁体字字符串反转回来
        String newStr = "";
        for (int i = fanYuan.length() - 1; i >= 0; i--) { //str.length.forr倒着遍历
            newStr = newStr + fanYuan.charAt(i);
        }
        return newStr;
    }
}
