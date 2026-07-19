package week5.day07;

public class GenericsDemo1 {
    static void main() {
        MyList list = new MyList();
        list.add("a");

        MyList2<String> list2 = new MyList2<>();
        list2.add("b");
    }

}
