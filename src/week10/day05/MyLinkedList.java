package week10.day05;

public class MyLinkedList<T> {

    class Node{
        T data;
        Node next;
    }

    private Node head;

    public void addFirst(T data){
        Node newNode = new Node();
        newNode.data = data;
        newNode.next = head;
        head = newNode;
    }

    public void addLast(T data){
        Node newNode = new Node();
        newNode.data = data;
        if (head == null){
            head = newNode;
            return;
        }
        Node cur = head;
        while(cur.next != null){  //单独写的话如果是空链表,head==null,cur.next直接NullPointerException
            cur = cur.next;
        }
        cur.next = newNode;
    }

    public T get(int index){
        Node cur = head;
        for (int i = 0; i < index; i++) {
            cur = cur.next;
        }
        return cur.data;
    }

    public int size(){
        Node cur = head;
        int count = 0;
        while (cur != null){
            cur = cur.next;
            count++;
        }
        return count;
    }

    public void remove(int index){
        if (index < 0 || index >= size()){
            return;
        }
        if (index == 0){
            head = head.next;
            return;
        }
        Node pre = head;
        for (int i = 0; i < index - 1; i++) {
            pre = pre.next;
        }
        pre.next = pre.next.next;
    }

    public void reverse(){
        Node prev = null;
        Node curr = head;
        while (curr != null) {
            Node next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        head = prev;
    }
}
