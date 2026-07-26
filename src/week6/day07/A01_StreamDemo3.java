package week6.day07;

import java.util.Arrays;
import java.util.stream.Stream;

public class A01_StreamDemo3 {
    public static void main(String[] args) {
        //对于数组,想要获取它的Stream流,可以使用Arrays工具类的stream方法创建
        int[] arr = {1, 2, 3, 4, 5};
        String[] arr2 = {"a", "b", "c", "d"};

        //Arrays.stream(arr).forEach(System.out::println);
        //Arrays.stream(arr2).forEach(System.out::println);

        //值得注意的是
        //Stream接口中的of方法的形参是可变参数,可以传递零散的数据,也可以传递数组
        //但数组不能是基本数据类型的,是不会自动装箱,而是作为一个整体传递给of,只能打印出地址值
        //Stream.of(arr2).forEach(System.out::println);
        Stream.of(arr).forEach(System.out::println);
    }
}
