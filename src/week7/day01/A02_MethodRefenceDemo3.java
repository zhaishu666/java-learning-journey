package week7.day01;

import java.util.ArrayList;
import java.util.Collections;

public class A02_MethodRefenceDemo3 {
    static void main(String[] args) {
        //通过类名引用成员方法
        ArrayList<String> list = new ArrayList<>();

        Collections.addAll(list, "a", "b", "c");
        //将所有的String中的字母转化为大写

        list.stream().map(String::toUpperCase).forEach(System.out::println);
        //此时map中抽象方法的第一个参数充当方法的调用者
    }
}
