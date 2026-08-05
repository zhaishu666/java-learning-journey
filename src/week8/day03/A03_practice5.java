package week8.day03;

import java.io.*;
import java.util.Scanner;

public class A03_practice5 {
    public static void main(String[] args) throws IOException {
        //简单登录小案例
        Scanner input = new Scanner(System.in);
        System.out.println("请输入你的用户名");
        String inputtedUsername = input.nextLine();
        System.out.println("请输入密码");
        String inputtedPassword = input.nextLine();

        try (BufferedReader br = new BufferedReader(new FileReader("用户信息.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] messages = line.split("&");
                String Username = messages[0].substring(9);
                String password = messages[1].substring(9);
                int count = Integer.parseInt(messages[2].substring(6));
                System.out.println(Username);
                System.out.println(password);
                if (Username.equals(inputtedUsername) && password.equals(inputtedPassword) && count < 3) {
                    System.out.println("登录成功");
                    writeInfo("username=" + inputtedUsername + "&password=" + inputtedPassword + "&count=0");
                    break;
                } else if (Username.equals(inputtedUsername) & !password.equals(inputtedPassword)) {
                    count++;
                    if (count < 3) {
                        System.out.println("用户名或密码错误,还剩 " + (3 - count) + "次");
                    } else {
                        System.out.println("输入次数过多,账户已锁定");
                    }
                    writeInfo("username=" + inputtedUsername + "&password=" + inputtedPassword + "&count=" + count);
                    break;
                }
            }
        }
    }

    private static void writeInfo(String content) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("用户信息.txt"))) {
            bw.write(content);
        }
    }
}
