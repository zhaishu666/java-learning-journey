package week3.day07.day07p1;

public class Outer {
    int a = 10;

    class Inner {
        int a = 20;

        public void show() {
            int a = 30;
            System.out.println(Outer.this.a);//10
            System.out.println(this.a);//20
            System.out.println(a);//30
        }
    }

    public Inner printInner() {
        return new Inner();
    }
}
