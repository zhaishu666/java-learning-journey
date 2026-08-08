package week8.day06;

public class OddNumber implements Runnable {
    private static int number = 0;

    @Override
    public void run() {
        while (true) {
            synchronized (OddNumber.class) {
                if (number == 100) {
                    break;
                }else if (number % 2 == 1) {
                    System.out.println(number);
                }
                number++;
            }
        }
    }
}
