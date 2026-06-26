package week2.day05;

import java.util.Scanner;

//用户输入一段字符串,统计其中数字,小写字母,大写字母的数目并打印
//charAt()比较的是ASCII码的值
public class StringDemo3 {
    static void main() {
        Scanner sc =new Scanner(System.in);
        System.out.println("请输入一个字符串");
        String str = sc.next();
        int num = 0;
        int letters = 0;
        int upLetters = 0;
        for (int i = 0; i < str.length(); i++) {
            if(str.charAt(i) >= '0' && str.charAt(i) <= '9' ){ //与字符0~9进行比较
                num++;
            } else if (str.charAt(i) >= 'A' && str.charAt(i) <= 'Z') { //或者charAt(i) >= 65 && charAt(i) <= 90
                upLetters++;
            } else if (str.charAt(i) >= 'a' && str.charAt(i) <= 'z') {
                letters++;
            }
        }
        System.out.println("数字数为"+ num);
        System.out.println("小写字母数为"+ letters);
        System.out.println("大写字母数为"+ upLetters);
    }
}
