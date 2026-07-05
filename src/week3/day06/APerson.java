package week3.day06;

//抽象类可以有构造方法
//抽象类中可能有抽象方法
public abstract class APerson {
    private String name;
    private int age;

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

    public APerson(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public APerson(String name) {
        this.name = name;
    }

    public abstract void work();

    public void print(){
        System.out.println("执行print方法");
    }
}
