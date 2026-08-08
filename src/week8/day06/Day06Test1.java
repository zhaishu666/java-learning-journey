package week8.day06;

public class Day06Test1 {
    public static final Cooker COOKER = new Cooker();

    public static void main(String[] args) {

        Cooker cooker = new Cooker();
        Foodie foodie = new Foodie();

        cooker.setName("厨师");
        foodie.setName("吃货");

        cooker.start();
        foodie.start();
    }
}
