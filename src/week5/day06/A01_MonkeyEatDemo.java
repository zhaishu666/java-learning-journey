package week5.day06;

public class A01_MonkeyEatDemo {
    static void main() {
        int day = 1;
        System.out.println(getPeach(day));
    }

    public static int getPeach(int day){
        if (day < 1 || day > 10) {
            return -1;
        }
        if(day == 10){
            return 1;
        }

        return (getPeach(day+1) + 1) * 2;
    }
}
