package week3.day06;

public class AStudent extends APerson {
    public AStudent(String name, int age) {
        super(name, age);
    }

    public AStudent(String name) {
        super(name);
    }

    //子类要么重写父类中所有的抽象方法,要么是抽象类
    @Override
    public void work() {
        System.out.println("学生正在写作业");
    }
}
