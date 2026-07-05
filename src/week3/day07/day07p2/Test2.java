package week3.day07.day07p2;

public class Test2 {
    static void main() {
        new Animal(){
            @Override
            public void eat() {
                System.out.println("重写了eat方法");
            }
        }.eat();
    }
}
