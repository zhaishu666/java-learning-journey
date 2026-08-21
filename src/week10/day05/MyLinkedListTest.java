package week10.day05;

public class MyLinkedListTest {
    static void main() {
        MyLinkedList<String> linkedList = new MyLinkedList<>();
        linkedList.addLast("a");
        linkedList.addLast("b");
        linkedList.addLast("c");

        System.out.println(linkedList.get(0) + linkedList.get(1) + linkedList.get(2));
        linkedList.remove(1);
        System.out.println(linkedList.size());
        linkedList.reverse();
        System.out.println(linkedList.get(0) + linkedList.get(1));
    }
}
