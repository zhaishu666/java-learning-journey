package week3.day06.day06p1;

public class BasketballCoach extends SportsWorker{
    @Override
    public void work() {
        System.out.println("教打篮球");
    }

    public BasketballCoach() {
    }

    public BasketballCoach(String name, int age) {
        super(name, age);
    }
}
