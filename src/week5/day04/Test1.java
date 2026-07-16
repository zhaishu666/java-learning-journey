package week5.day04;

import java.util.ArrayList;
import java.util.Scanner;

public class Test1 {
    static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();
        loop:
        while (true) {
            System.out.println("请输入一个1~100的整数");
            Scanner sc = new Scanner(System.in);
            int num = 0;
            if (sc.hasNextInt() &&(sc.nextInt()<=100 && sc.nextInt()>=0)) {
                int i = sc.nextInt();
                list.add(i);
            } else {
                System.out.println("输入的数不合法");
                continue;
            }

            for (int i = 0; i < list.size(); i++) {
                num = num + list.get(i);
                if (i == list.size() - 1) {
                    System.out.println("目前数据总和为" + num);
                }
                if (num > 200) {
                    break loop;
                }
            }
        }

    }
}
