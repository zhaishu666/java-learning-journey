package week3.day06;

public class BDog extends BAnimal implements Run{
    public BDog() {
    }

    public BDog(String name, int age) {
        super(name, age);
    }

    @Override
    public void eat() {
        System.out.println("吃骨头");
    }

    @Override
    public void running() {
        System.out.println("狗正在奔跑");
    }
}
