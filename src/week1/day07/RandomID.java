package week1.day07;

import java.util.Random;

//随机产生一个5位数的验证码,要求前4位数是大写字母或小写字母,最后一位是数字
//总结:a在ASCII表中对应97,A对应65,随机字母可以先将字母存入数组中
public class RandomID {
    static void main() {
        char[] chs = new char[52];
        for (int i = 0; i < chs.length; i++) {
            //ASCII表
            if (i <= 25){
                chs[i] = (char)(97 + i); //小写字母a在ASCII表中的位置
            }else{
                chs[i] = (char)(65 + i - 26); //-26使数组从A开始录入
            }
        }
        String result = ""; //用一个字符串来接受结果,而不是单纯打出一段字符
        Random r = new Random();
        for (int i = 0; i < 4; i++) {
            int randomIndex = r.nextInt(chs.length);//不能忘记加chs.length
            result =result + chs[randomIndex];
        }
        int randomNum = r.nextInt(10);
        result = result + randomNum;
        System.out.println(result);
    }
}
