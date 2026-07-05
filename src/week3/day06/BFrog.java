package week3.day06;

public  class BFrog extends BAnimal{
    public BFrog() {
    }

    public BFrog(String name, int age) {
        super(name, age);
    }

    @Override
    public void eat() {
        System.out.println("吃虫子");
    }


}
