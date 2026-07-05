package week3.day06.day06p1;

public abstract class SportsWorker {
    private String name;
    private int age;

    public SportsWorker() {
    }

    public SportsWorker(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public abstract void work();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
