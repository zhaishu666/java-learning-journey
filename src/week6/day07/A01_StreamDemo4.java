package week6.day07;

import java.util.stream.Stream;

public class A01_StreamDemo4 {
    public static void main(String[] args) {
        //对于零散的数据,可以使用Stream接口中的静态方法,这里的数据类型必须相同
        Stream.of(1,2,3,4,5).forEach(System.out::println);
    }
}
