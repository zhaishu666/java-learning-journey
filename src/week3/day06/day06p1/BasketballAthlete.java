package week3.day06.day06p1;

public class BasketballAthlete extends SportsWorker implements LearnEnglish{
    public BasketballAthlete() {
    }

    public BasketballAthlete(String name, int age) {
        super(name, age);
    }

    @Override
    public void work() {
        System.out.println("学打篮球");
    }

    @Override
    public void speak() {
        System.out.println("说英语");
    }
}
