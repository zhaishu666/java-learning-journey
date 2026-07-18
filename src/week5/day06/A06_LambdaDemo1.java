package week5.day06;

import java.util.ArrayList;
import java.util.function.Consumer;

public class A06_LambdaDemo1 {
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");

        list.forEach(s-> System.out.println(s));  //Lambda表达式遍历
    }
}
