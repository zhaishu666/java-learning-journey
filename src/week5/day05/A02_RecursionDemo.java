package week5.day05;

public class A02_RecursionDemo {
    public static void main(String[] args) {
        //定义一个递归计算1~100的和
        System.out.println(getSum(100));

    }

    public static int getSum(int number){
        if(number == 1){
            return 1;
        }
        return number + getSum(number-1);
    }
}
