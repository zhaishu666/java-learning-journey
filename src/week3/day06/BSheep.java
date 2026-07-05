package week3.day06;

public class BSheep extends BAnimal implements Run{
    public BSheep() {
    }

    public BSheep(String name, int age) {
        super(name, age);
    }

    @Override
    public void eat() {
        System.out.println("吃草");
    }

    @Override
    public void running() {
        System.out.println("羊正在狂奔");
    }
}
