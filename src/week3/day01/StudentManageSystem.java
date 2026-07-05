package week3.day01;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class StudentManageSystem {
    private static final String  LOGIN = "1";
    private static final String  REGISTER = "2";
    private static final String  FORGOT_PASSWORD = "3";
    private static final String  EXIT = "4";

    static ArrayList<User> list = new ArrayList<>();
    static {
        list.add(new User("zhangsan","1234qwer","341412345678901112","13112345678"));
    }
    static void main() {
        Scanner sc = new Scanner(System.in);

       mainLoop: while (true) {
            System.out.println("欢迎来到学生管理系统");
            System.out.println("请选择操作: 1.登录 2.注册 3.忘记密码 4.退出");
            Random r = new Random();
            String num = sc.next();
            switch (num) {
                case LOGIN -> login(list, sc, r);
                case REGISTER -> register(list, sc);
                case FORGOT_PASSWORD -> forgotPassword(list,sc);
                case EXIT -> {
                    break mainLoop;
                }
                default -> System.out.println("没有这个选项");
            }
        }
        sc.close();
    }

    public static void forgotPassword(ArrayList<User> list, Scanner sc){ //用于找回忘记的密码
        User userMessage;
        String forgotId;
        String forgotNumber;
        System.out.println("请输入要找回的用户名");
        String forgotName = sc.next();
        if(judgeUsername(list,forgotName)){
            userMessage = getUserMessage(list, forgotName);
            System.out.println("请输入身份证号码");
            forgotId = sc.next();
            System.out.println("请输入电话号码");
            forgotNumber = sc.next();
        }else{
            System.out.println("用户未注册");
            return;
        }
        if(userMessage.getId().equalsIgnoreCase(forgotId) && userMessage.getPhoneNumber().equals(forgotNumber)){ //身份证号码和电话号码判断
            String newPassword = setUserPassword(sc);
            userMessage.setPassword(newPassword);
        }else{
            System.out.println("账号信息不匹配,修改失败");
        }
    }
    public static void login(ArrayList<User> list, Scanner sc,Random r) { //登录账号
        int count = 0;  //记录密码输错的次数
        User u2;
        loop :while (true) {
            System.out.println("请输入用户名");
            String inputName = sc.next();
            if (!judgeUsername(list, inputName)) {
                System.out.println("用户未注册,请先注册");
                break;
            } else {
                u2 = getUserMessage(list, inputName);
            }
            boolean flag;
            while (true) {  //检查验证码是否正确,如果不正确,就重新输入
                String RanVerCode = verificationCode(r);  //将随机生成的验证码提取出来
                System.out.println(RanVerCode);
                flag = checkVerCode(RanVerCode, sc);
                if (flag) {
                    break;
                }
            }
            if (flag) {  //对输入的密码进行判断,错误达到3次就直接停止
                while (count < 3) {
                    System.out.println("请输入密码");
                    String inputPassword = sc.next();
                    if (u2.getPassword().equals(inputPassword)) {
                        System.out.println("登录成功");
                        break loop;
                    } else {
                        count++;
                        System.out.println("密码错误");
                        if(count == 3){
                            System.out.println("错误次数过多,请重新登录");
                            break loop;
                        }
                    }
                }
            }
        }
    }


    public static boolean checkVerCode(String RanVerCode, Scanner sc) {  //检测输入的验证码是否正确
        System.out.println("请输入验证码");
        String inputVerCode = sc.next();
        if (RanVerCode.equalsIgnoreCase(inputVerCode)) { //对比验证码并忽略大小写
            return true;
        }
        return false;
    }

    public static User getUserMessage(ArrayList<User> list, String inputName) { //获得某个用户的信息
        User u2 = new User();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUsername().equals(inputName)) {
                u2 = list.get(i);
                break;
            }
        }
        return u2;
    }

    public static String verificationCode(Random r) {//产生随机验证码
        StringBuilder sb = new StringBuilder();
        /*char[] arr = new char[52];  //存放所有大小写字母
        for (int i = 0; i < arr.length; i++) {
            if (i <= 25) {
                arr[i] = (char) (65 + i);  //前26存大写字母
            } else {
                arr[i] = (char) (97 + i - 26); //后26位存小写字母
            }
        }
        for (int i = 0; i < 4; i++) {  //生成4个随机字母
            int ri = r.nextInt(arr.length);
            sb.append(arr[ri]);
        }
        int rNum = r.nextInt(10);  //生成一位随机数字
        sb.append(rNum);
        String verCode = sb.toString();
        char[] newArr = shuffleString(verCode, r);
        return arrayToString(newArr);*/

        ArrayList<Character> list = new ArrayList<>();  //将所有大小写字母存入集合中,更加方便
        for (int i = 0; i < 26; i++) {
            list.add((char)('a' + i));
            list.add((char)('A' + i));
        }
        for (int i = 0; i < 4; i++) {
            int rani = r.nextInt(list.size());
            sb.append(list.get(rani));
        }
        int rNum = r.nextInt(10);  //生成一位随机数字
        sb.append(rNum);
        String verCode = sb.toString();
        char[] newArr = shuffleString(verCode, r);
        return arrayToString(newArr);
    }

    public static String arrayToString(char[] newArr) {  //将验证码从数组转化为字符串,方便比较
        String arrStr = "";
        for (int i = 0; i < newArr.length; i++) {
            arrStr = arrStr + newArr[i];
        }
        return arrStr;
    }

    public static char[] shuffleString(String verCode, Random r) { //打乱验证码的所有元素的位置
        char[] newArr = verCode.toCharArray();
        for (int i = 0; i < newArr.length; i++) {
            int ran = r.nextInt(newArr.length);
            char temp = newArr[i];
            newArr[i] = newArr[ran];
            newArr[ran] = temp;
        }
        return newArr;
    }

    public static boolean judgeUsername(ArrayList<User> list, String inputName) { //判断输入的用户在集合中是否存在
        if (list == null || list.isEmpty()) {
            return false;
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getUsername().equals(inputName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void register(ArrayList<User> list, Scanner sc) { //实现注册账号
        String username;
        String password;
        String id;
        String phoneNumber;
        loop:
        while (true) {  //判断输入的用户名是否合法
            System.out.println("请输入用户名");
            username = sc.next();
            int len = username.length();  //将方法调用结果赋值给一个变量,减少对方法的调用次数
            if (len < 3) {
                System.out.println("用户名过短");
                continue;
            } else if (len > 15) {
                System.out.println("用户名过长");
                continue;
            }
            if (!judgeUsername(username)) {
                break;
            } else {
                System.out.println("用户名不能是纯数字");
            }
            for (int i = 0; i < list.size(); i++) {    //先判断格式是否正确,再判断用户名是否重复,节省内存
                if (list.get(i).getUsername().equals(username)) {
                    System.out.println("用户名重复,请重新输入");
                    continue loop;
                }
            }
        }
        password = setUserPassword(sc);
        while (true) {
            System.out.println("请输入身份证号码");
            id = sc.next();
            if (id.length() != 18) {
                System.out.println("长度错误");
            } else if (id.charAt(0) == '0') {
                System.out.println("不能以0为开头");
            } else if (!judgeId(id)) {
                System.out.println("存在不合理字符");
            } else if (judgeLast(id)) {
                break;
            } else {
                System.out.println("最后一位不合理");
            }
        }
        while (true) {
            System.out.println("请输入手机号验证");
            phoneNumber = sc.next();
            if (!checkLength(phoneNumber)) {
                System.out.println("错误号码");
            } else if (phoneNumber.charAt(0) == '0') {
                System.out.println("错误号码");
            } else if (checkNumber(phoneNumber)) {
                User u = new User(username,password,id,phoneNumber);  //在校验全部通过时再创建user
                list.add(u);
                System.out.println("注册成功");
                break;
            }
        }
    }

    public static String setUserPassword(Scanner sc) {
        String password;
        while (true) {  //用于设置密码
            System.out.println("请输入密码");
            password = sc.next();
            System.out.println("请再次输入密码");
            String password2 = sc.next();
            if (password.equals(password2)) {
                System.out.println("密码设置成功");
                break;
            } else {
                System.out.println("两次输入的密码不同,请重新输入");
            }
        }
        return password;
    }

    public static boolean checkNumber(String phoneNumber) {  //检查输入的号码是否合法
        for (int i = 0; i < phoneNumber.length(); i++) {
            char c = phoneNumber.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    public static boolean checkLength(String phoneNumber) {  //检查号码长度
        if (phoneNumber.length() == 11) {
            return true;
        }
        return false;
    }

    public static boolean judgeLast(String id) { //判断身份证的最后一位
        char last = id.charAt(id.length() - 1);
        if ((last > '0' && last < '9') || last == 'x' || last == 'X') {
            return true;
        }
        return false;
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
            char c2 = username.charAt(i);
            if (c2 >= '0' && c2 <= '9') {
                flag = false;
            } else {
                flag = true;
            }
            if (flag) { //如果有一个字母则立即返回false表示不是纯数字
                return false;
            }
        }
        return true;
    }
}
