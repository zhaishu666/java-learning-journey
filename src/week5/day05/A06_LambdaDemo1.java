package week5.day05;

import java.util.Arrays;

public class A06_LambdaDemo1 {
    public static void main(String[] args) {
        Integer[] arr = {6, 1, 2, 7, 9, 3, 4, 5, 10, 8};

        //Lambda表达式只能用来简化有且只有一个方法的函数式接口
        //Lambda表达式完整写法
        /*Arrays.sort(arr,(Integer o1, Integer o2) ->{
                return o1-o2;   //o1-o2升序排序,o2-o1降序排序
            }
        );*/

        //Lambda表达式省略写法
        Arrays.sort(arr,( o1,  o2) -> o1-o2 );

        System.out.println(Arrays.toString(arr));
    }
}
