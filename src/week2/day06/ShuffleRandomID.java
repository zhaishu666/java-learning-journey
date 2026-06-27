package week2.day06;

import java.util.Random;

//随机生成一个五位数的字符串验证码,英文大小写都可,要求有一位数字,并随机出现在任何位置
public class ShuffleRandomID {
    static void main() {
        Random r = new Random();
        String id = "";
        char[] chs = getChars();

        id = getString(r, chs, id);

        String result = ShuffleString(id, r);
        System.out.println(result);
    }

    public static String ShuffleString(String id, Random r) { //将字符串转化为数组后打乱
        char[] idCharArray = id.toCharArray();
        char temp;
        for (int i = 0; i < idCharArray.length; i++) {
            int ran = r.nextInt(idCharArray.length);
            temp = idCharArray[i];
            idCharArray[i] = idCharArray[ran];
            idCharArray[ran] = temp;
        }
        String result = new String(idCharArray);
        return result;
    }

    public static char[] getChars() { //让数组存放英文字母的所有大小写的字符形式
        char[] chs = new char[52];
        for (int i = 0; i < chs.length; i++) {
            if (i <= 25) {
                chs[i] = (char) (97 + i);
            } else {
                chs[i] = (char) (65 + i - 26);
            }
        }
        return chs;
    }

    public static String getString(Random r, char[] chs, String id) {
        for (int i = 0; i < 4; i++) {
            int randomIndex = r.nextInt(chs.length);
            id = id + chs[randomIndex];
        }
        int num = r.nextInt(10);
        id = id + num; //只要表达式里出现字符串 + 就会变成拼接
        return id;
    }

}
