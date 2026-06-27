package week2.day06;

import java.util.Scanner;

//将输入的字符串数字转变为罗马数字,长度不超过9且只能有数字
//总结:对于循环后要使用的数据要定义在循环外面,可以定义一个canConvert来防止循环结束后继续执行接下来的文件
public class RomaNumber {
    static void main() {
        Scanner sc = new Scanner(System.in);
        String strNum; //在循环外声明
        int errCount = 0;
        boolean canConvert = false;
        while (true){
            System.out.println("请输入一个字符串");
            strNum = sc.next();
            if(strNum.length() > 9){
                errCount++;
                System.out.println("超出范围");
            }else if (!jungleStr(strNum)){
                errCount++;
                System.out.println("含有非数字字符");
            }else if (errCount == 3){
                System.out.println("错误次数过多");
                break;
            }else{
                canConvert = true;
                break;
            }
        }
        if (canConvert) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < strNum.length(); i++) {
                int num = strNum.charAt(i) - '0';
                String s = mathToRoma(num);
                sb.append(s);
            }
            System.out.println(sb);
            sc.close();
        }
    }

    public static boolean jungleStr(String strNum) {
        boolean flag = true;
        for (int i = 0; i < strNum.length(); i++) {
            if (strNum.charAt(i) <= '0' || strNum.charAt(i) >= '9') {
                flag = false;
            }
        }
        return flag;
    }

    public static String mathToRoma(int num) {
        String[] romaNumber = {" ", "Ⅰ", "Ⅱ", "Ⅲ", "Ⅳ", "Ⅴ", "Ⅵ", "Ⅶ", "Ⅷ", "Ⅸ"};
        return romaNumber[num];
    }
}
