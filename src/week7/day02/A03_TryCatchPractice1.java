package week7.day02;

import java.util.Scanner;

public class A03_TryCatchPractice1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        GirlFriend gf = new GirlFriend();
        while (true) {
            try {
                System.out.println("请输入老婆的姓名");
                gf.setName(sc.nextLine());
                System.out.println("请输入老婆的年龄");
                int age = Integer.parseInt(sc.nextLine());
                gf.setAge(age);
                break;
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println(gf);
    }
}
