package week3.day06.day06p1;

public class TableTennisCoach extends SportsWorker{
    @Override
    public void work() {
        System.out.println("教打乒乓球");
    }

    public TableTennisCoach() {
    }

    public TableTennisCoach(String name, int age) {
        super(name, age);
    }
}
