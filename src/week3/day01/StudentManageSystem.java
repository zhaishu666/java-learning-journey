package week3.day01;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class StudentManageSystem {
    static void main() {
        while (true) {
            System.out.println("欢迎来到学生管理系统");
            System.out.println("请选择操作: 1.登录 2.注册 3.忘记密码");
            Scanner sc = new Scanner(System.in);
            ArrayList<User> list = new ArrayList<>();
            Random r = new Random();
            String num = sc.next();
            switch (num) {
                case "1" -> login(list, sc);
                case "2" -> register(list, sc);
                case "3" -> System.out.println("忘记密码");
                default -> System.out.println("没有这个选项");
            }
        }
    }

    public static void login(ArrayList<User> list, Scanner sc) {
        int count = 0;
        User u2 ;
        while (true) {
            System.out.println("请输入用户名");
            String inputName = sc.next();
            if (!judgeUsername(list, inputName)) {
                System.out.println("用户未注册,请先注册");
                break;
            } else {
                u2 = getUserMessage(list, inputName);
            }
            String RanVerCode = verificationCode(new Random());
            System.out.println(RanVerCode);
            boolean flag;
            while(true){
                flag = checkVerCode(RanVerCode, sc);
                if(flag){
                    break;
                }
            }
            if (flag) {
                System.out.println("请输入密码");
                String inputPassword = sc.next();
                if (u2.getPassword().equals(inputPassword)) {
                    System.out.println("登录成功");
                } else if (count == 3) {
                    System.out.println("错误过多");
                } else {
                    count++;
                    System.out.println("密码错误");
                }
            }
        }
        //目前问题,登录账号时无论输入什么都是没有注册
    }

    public static boolean checkVerCode(String RanVerCode,Scanner sc){  //检测输入的验证码是否正确
        System.out.println("请输入验证码");
        String inputVerCode = sc.next();
        if(RanVerCode.equals(inputVerCode)){
            return true;
        }
        return false;
    }
    public static User getUserMessage(ArrayList<User> list, String inputName) {
        User u2 = new User();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUsername().equals(inputName)) {
                u2 = list.get(i);
                break;
            }
        }
        return u2;
    }

    public static String verificationCode(Random r) {
        char[] arr = new char[52];
        String verCode = "";
        for (int i = 0; i < arr.length; i++) {
            if (i <= 25) {
                arr[i] = (char) (65 + i);
            } else {
                arr[i] = (char) (97 + i - 26);
            }
        }
        for (int i = 0; i < 4; i++) {
            int ri = r.nextInt(arr.length);
            verCode = verCode + arr[ri];
        }
        int rNum = r.nextInt(10);
        verCode += rNum;
        char[] newArr = shuffleString(verCode, r);
        return arrayToString(newArr);
    }

    public static String arrayToString(char[] newArr) {
        String arrStr = "";
        for (int i = 0; i < newArr.length; i++) {
            arrStr = arrStr + newArr[i];
        }
        return arrStr;
    }

    public static char[] shuffleString(String verCode, Random r) {
        char[] newArr = verCode.toCharArray();
        for (int i = 0; i < newArr.length; i++) {
            int ran = r.nextInt(newArr.length);
            char temp = newArr[i];
            newArr[i] = newArr[ran];
            newArr[ran] = temp;
        }
        return newArr;
    }

    public static boolean judgeUsername(ArrayList<User> list, String inputName) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUsername().equals(inputName)) {
                return true;
            }
        }
        return false;
    }

    public static void register(ArrayList<User> list, Scanner sc) { //实现注册账号
        User u = new User();
        loop:
        while (true) {  //判断输入的用户名是否合法
            System.out.println("请输入用户名");
            String username = sc.next();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getUsername().equals(username)) {
                    System.out.println("用户名重复,请重新输入");
                    continue loop;
                }
            }
            if (username.length() < 3) {
                System.out.println("用户名过短");
                continue;
            } else if (username.length() > 15) {
                System.out.println("用户名过长");
                continue;
            }
            if (!judgeUsername(username)) {
                u.setUsername(username);
                break;
            } else {
                System.out.println("用户名不能是纯数字");
            }
        }
        while (true) {  //用于设置密码
            System.out.println("请输入密码");
            String password = sc.next();
            System.out.println("请再次输入密码");
            String password2 = sc.next();
            if (password.equals(password2)) {
                System.out.println("密码设置成功");
                u.setPassword(password);
                break;
            } else {
                System.out.println("两次输入的密码不同,请重新输入");
            }
        }
        while (true) {
            System.out.println("请输入身份证号码");
            String id = sc.next();
            if (id.length() != 18) {
                System.out.println("长度错误");
            } else if (id.charAt(0) == '0') {
                System.out.println("不能以0为开头");
            } else if (!judgeId(id)) {
                System.out.println("存在不合理字符");
            } else if (!judgeLast(id)) {
                u.setId(id);
                break;
            } else {
                System.out.println("最后一位不合理");
            }
        }
        while (true) {
            System.out.println("请输入手机号验证");
            String phoneNumber = sc.next();
            if (!checkLength(phoneNumber)) {
                System.out.println("错误号码");
            } else if (phoneNumber.charAt(0) == '0') {
                System.out.println("错误号码");
            } else if (checkNumber(phoneNumber)) {
                u.setPhoneNumber(phoneNumber);
                list.add(u);
                break;
            }
        }
    }

    public static boolean checkNumber(String phoneNumber) {
        for (int i = 0; i < phoneNumber.length(); i++) {
            if (phoneNumber.charAt(i) < '0' || phoneNumber.charAt(i) > '9') {
                return false;
            }
        }
        return true;
    }

    public static boolean checkLength(String phoneNumber) {
        if (phoneNumber.length() == 11) {
            return true;
        }
        return false;
    }

    public static boolean judgeLast(String id) { //判断身份证的最后一位
        char last = id.charAt(id.length() - 1);
        if ((last > '0' && last < '9') || last == 'x' || last == 'X') {
            return false;
        }
        return true;
    }

    public static boolean judgeId(String id) {  //判断身份证的前17位是否为数字
        boolean flag = true;
        for (int i = 0; i < id.length() - 1; i++) {
            if (id.charAt(i) < '0' || id.charAt(i) > '9') {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public static boolean judgeUsername(String username) { //判断用户名是否是纯数字
        boolean flag;
        for (int i = 0; i < username.length(); i++) {
            if (username.charAt(i) >= '0' && username.charAt(i) <= '9') {
                flag = true;
            } else {
                flag = false;
            }
            if (!flag) {
                return false;
            }
        }
        return true;
    }
}
