package week5.day07;

public abstract class Animal {
     public String name;

    public Animal() {
    }

    public Animal(String name) {
        this.name = name;
    }

    public abstract void eat();
}

abstract class Cat extends Animal {

    public Cat() {
    }

    public Cat(String name) {
        super(name);
    }

}

class bosimao extends Cat {
    public bosimao() {}
    public bosimao(String name) {
        super(name);
    }

    @Override
    public void eat() {
        System.out.println("一只叫做"+ name +"的波斯猫,正在吃小饼干");
    }
}

class lihuamao extends Cat {
    public lihuamao() {}
    public lihuamao(String name) {
        super(name);
    }

    @Override
    public void eat() {
        System.out.println("一只叫做"+ name +"的狸花猫,正在吃鱼");
    }
}

abstract class Dog extends Animal {
    public Dog() {
    }
    public Dog(String name) {
        super(name);
    }
}

class taidi extends Dog {
    public taidi() {}
    public taidi(String name) {
        super(name);
    }

    @Override
    public void eat() {
        System.out.println("一只叫做"+ name +"的泰迪,正在吃骨头,边吃边蹭");
    }
}

class hashiqi extends Dog {
    public hashiqi() {}
    public hashiqi(String name) {
        super(name);
    }

    @Override
    public void eat() {
        System.out.println("一只叫做"+ name +"的哈士奇,正在吃骨头,边吃边拆家");
    }
}
