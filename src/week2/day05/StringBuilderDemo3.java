package week2.day05;

import java.util.Scanner;

//键盘输入一个字符串,判断其反转后是否与原字符串相同,是则输出是否则为不是
//StringBuilder在方法中应先toString才能返回
public class StringBuilderDemo3 {
    static void main() {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入一个字符串");
        String str = sc.next();

        //链式编辑
        String result = new StringBuilder().append(str).reverse().toString();

        if(str.equals(result)){ //字符串比较
            System.out.println("是");
        }else{
            System.out.println("不是");
        }
    }
}
