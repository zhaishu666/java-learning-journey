package week1.day06;

import java.util.Scanner;

//在main方法中输入一个1~20之间的数字,求其阶乘(递归 + 普通方法两种实现)
public class Method4 {

   /* public static long factorial(int num){ //普通方法实现
        long result = 1;
        for (int i = 1; i <= num; i++) {
             result *= i;
        }
        return result;
    }

    */
       public static long factorialRec(int num){ //递归实现
           if(num == 1){
               return 1;
           }
           return num * factorialRec(num - 1);//递归就是自己调用自己,将大问题转化为多个小问题
       }


      static void main(String[] args) {
          Scanner sc = new Scanner(System.in);
          System.out.println("请输入一个1~20的数");
          int num = sc.nextInt();
          if(num < 1 || num > 20){
              System.out.println("不在数字范围");
              sc.close();
              return;//防止执行接下来的代码
          }
          System.out.println(factorialRec(num));//结果需要sout输出
    }
}
