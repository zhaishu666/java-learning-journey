package week3.day06;

public class BAbstractTest {
    static void main() {
        BFrog f = new BFrog();
        BDog d = new BDog();
        BSheep s = new BSheep();
        f.eat();
        f.drink();
        d.eat();
        d.drink();
        d.running();
        s.eat();
        s.drink();
        s.running();
    }
}
