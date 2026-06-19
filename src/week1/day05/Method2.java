package week1.day05;

//定义一个方法,求一家商场每个季度的营业额,根据方法结果求出年营业额
public class Method2 {
    public static int getMoney( int num1, int num2, int num3){
        int result = num1 + num2 + num3;
        return result;
    }

    static void main(String[] args) {
        System.out.println("年营业额为" + getMoney(10,12,16));
    }
}
