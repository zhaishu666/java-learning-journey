package week2.day06;

//给定两个字符串形式的非负整数num1和num2,计算它们的乘积,结果也以字符串形式输出
public class StringNumber {
    static void main() {
        String num1 = "123456789";
        String num2 = "12345";
        int number1 = 0;
        int number2 = 0;
        for (int i = 0; i < num1.length(); i++) {
            int num = num1.charAt(i) - '0';
            number1 = number1 * 10 + num;
        }
        for (int i = 0; i < num2.length(); i++) {
            int num = num2.charAt(i) - '0';
            number2 = number2 * 10 + num;
        }
        System.out.println(number1);
        System.out.println(number2);

        int result = number1 * number2;
        StringBuilder sb = new StringBuilder();
        String strResult = sb.append(result).toString();
        System.out.println(strResult);
    }
}
