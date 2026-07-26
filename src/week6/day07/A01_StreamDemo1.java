package week6.day07;

import java.util.ArrayList;

public class A01_StreamDemo1 {
    static void main() {
        ArrayList<String> list = new ArrayList<>();
        list.add("张三");
        list.add("张雪峰");
        list.add("李四");
        list.add("贾乃亮");

        //将所有开头为"张"的名字传递到新的数组中
        ArrayList<String> list2 = new ArrayList<>();
        list.stream().filter(name-> name.startsWith("张")).forEach(list2::add);
        System.out.println(list2);
    }
}
