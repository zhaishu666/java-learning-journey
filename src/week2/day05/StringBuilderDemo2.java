package week2.day05;

public class StringBuilderDemo2 {
    static void main() {
        StringBuilder sb = new StringBuilder();
        String s1 = "aaa";
        String s2 = "12.3";
        String s3 = "0.3";

        sb.append(s1);
        sb.append(s2);
        sb.append(s3);

        sb.reverse(); //反转其中的元素
        System.out.println(sb);

        int length = sb.length();  //获得sb中字符出现的个数
        System.out.println(length);
    }

}
