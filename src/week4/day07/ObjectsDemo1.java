package week4.day07;

import java.util.Objects;

public class ObjectsDemo1 {
    static void main() {
        String s1 = "abc";
        String s2 = new String("abc");

        //Object里的equals方法会先确定s1和s2是否指向同一个地址
        //然后再判断s1是否为null
        //当s1为null时直接返回false,如果不是null,再与s2逐个比较字符
        //Objects中的equals方法省得我们自己再手动判断s1是否为空
        boolean result = Objects.equals(s1, s2);
        System.out.println(result);

        String s3 = new String();
        String s4 = null;

        System.out.println(Objects.isNull(s3)); // false
        System.out.println(Objects.isNull(s4)); //true
        System.out.println(Objects.nonNull(s3)); //true
        System.out.println(Objects.nonNull(s4)); //false

    }
}
