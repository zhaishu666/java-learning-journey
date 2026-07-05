package week3.day07.day07p1;

import java.util.Scanner;

public class OuterTest {
    static void main() {
        Outer.Inner inner = new Outer().new Inner();//这里是private就无法调用
        Outer o = new Outer();
        System.out.println(o.a);

        //System.out.println(inner.a);
        inner.show();
        Scanner sc = new Scanner(System.in);
        sc.next();
    }
}
