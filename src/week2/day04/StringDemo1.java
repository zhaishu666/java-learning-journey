package week2.day04;

import java.util.Scanner;

public class StringDemo1 {
    static void main() {
        String userName = "zhangsan";
        String password = "123zhangsan";

        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < 3; i++) {
            System.out.println("请输入用户");
            String inputName = sc.next();
            System.out.println("请输入密码");
            String inputPass = sc.next();
            if(userName.equals(inputName) && password.equals(inputPass)){
                System.out.println("登录成功");
                break;
            }else{
                System.out.println("用户名或密码错误");
            }
        }
        sc.close();
    }
}
