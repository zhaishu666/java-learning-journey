package week2.day06;

import java.io.FilterOutputStream;

//旋转字符串,将最左边的字符移动到最右边,若干次后两个数组相同,输出true,否则输出false
public class RotateString {
    static void main() {
        String s1 = "abcde";
        String s2 = "cdeab";
        boolean flag = false;
        for (int i = 0; i < s1.length(); i++) {
            s1 = rotate(s1); //将旋转后的值返回给s1
            if(s1.equals(s2)){
                flag = true;
            }
        }
        System.out.println(flag);
    }

    public static String rotate(String s1){
    /*    char c = str.charAt(0);  //方法一
        String moveStr = str.substring(1);
        String newStr = moveStr + c;
        System.out.println(newStr);
        return newStr;*/

        char[] moveStr = s1.toCharArray(); //方法将字符串转化为字符数组
        char first = moveStr[0];
        for (int i = 1; i < moveStr.length; i++) {
            moveStr[i - 1] = moveStr[i];
        }
        moveStr[moveStr.length - 1] = first;
        String newStr = new String(moveStr);  //使用toString不会输出内容而是打印地址
        return newStr;
    }
}
