package week3.day06.day06p1;

public class TableTennisAthlete extends SportsWorker implements LearnEnglish{
    public TableTennisAthlete() {
    }

    public TableTennisAthlete(String name, int age) {
        super(name, age);
    }

    @Override
    public void work() {
        System.out.println("学打乒乓球");
    }

    @Override
    public void speak() {
        System.out.println("学英语");
    }
}
