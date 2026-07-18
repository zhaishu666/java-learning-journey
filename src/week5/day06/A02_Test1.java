package week5.day06;

public class A02_Test1 {
    public static void main(String[] args) {
        //小明一次有可能爬1,2,3个台阶,计算爬20个台阶有多少种爬法
        int count = 20;
        System.out.println(getCount(count));

    }

    public static int getCount(int count){
        if(count < 1){
            return -1;
        }if (count == 1){
            return 1;
        }if (count == 2){
            return 2;
        }if (count == 3){
            return 4;
        }
        return getCount(count - 1)  + getCount(count - 2) + getCount(count - 3);
    }
}
