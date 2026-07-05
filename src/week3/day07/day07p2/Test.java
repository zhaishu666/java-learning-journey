package week3.day07.day07p2;

public class Test {
    static void main() {
        method(
                new Animal() {    //把这个匿名内部类的对象当作参数执行方法,典型的多态
                    @Override
                    public void eat() {
                        System.out.println("狗吃骨头");
                    }
                }
        );
    }


    public static void method(Animal a) {
        a.eat();
    }
}
