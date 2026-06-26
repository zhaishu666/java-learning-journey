package week2.day05;

public class StringBuilderDemo1 {
    static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        String s1 = "aaa";
        String s2 = "bbb";
        String s3 = "ccc";
        sb.append(s1);  //将括号里的内容添加到StringBuilder中
        sb.append(s2);
        sb.append(s3);
       // System.out.println(sb);

        String str = sb.toString(); //转化为字符串
        System.out.println(str);
    }
}
