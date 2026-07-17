package week5.day05;

public class A08_RabbitDemo {
    public static void main(String[] args) {
        //一对兔子繁殖符合斐波那契数列,求出兔子12个月后的数量
        int month = 12;

        System.out.println(getNum(month));
    }

    public static int getNum(int month) {

        if(month == 1 || month == 2){  //当getNum(month - 1) + getNum(month - 2) 为2和1时递归就会结束
            return 1;
        }
        return getNum(month - 1) + getNum(month - 2);
    }
}
