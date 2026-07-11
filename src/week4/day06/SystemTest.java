package week4.day06;

public class SystemTest {
    static void main(String[] args) {

        long begin = System.currentTimeMillis();

        for (int i = 2; i < 100000; i++) {
            boolean prime1 = isPrime2(i);
            if(prime1){
                System.out.println(i);
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(end - begin);
    }

    public static boolean isPrime1(int number){
        for (int i = 2; i < number; i++) {
            if(number % i ==0){
                return true;
            }

        }
        return false;
    }
    public static boolean isPrime2(int number){
        for (int i = 2; i < Math.sqrt(number); i++) {
            if(number% i ==0){
                return true;

            }
        }
        return false;
    }

}
