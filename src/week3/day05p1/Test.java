package week3.day05p1;

public class Test {
    static void main() {
        Person p1 = new Person();
        p1.setAge(30);
        p1.setName("老王");

        Person p2 = new Person();
        p2.setAge(25);
        p2.setName("老李");

        Dog dog = new Dog(2,"黑颜色","狗");
        Cat cat = new Cat(3,"灰颜色","猫");

        p1.keepPet(dog,"骨头");
        p2.keepPet(cat,"鱼");
    }
}
