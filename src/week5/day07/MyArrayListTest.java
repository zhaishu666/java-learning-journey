package week5.day07;

public class MyArrayListTest {
    static void main(String[] args) {

        MyArrayList<String> list = new MyArrayList<>();

        list.add("aaa");
        list.add("bbb");
        list.add("ccc");

        String str1 = list.get(0);
        //System.out.println(str1);

        String set = list.set(3, "ddd");
        //System.out.println(set);

        System.out.println(list);

    }
}
