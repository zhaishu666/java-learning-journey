package week5.day02;

public class GroupDemo2 {
    static void main(String[] args) {
        String str = "我要学学学,,,,,编编编编编程程程程程程";

        //将字符串中重复的内容去掉多余的
        String string = str.replaceAll("(.)\\1+", "$1"); //这里外部调用组号需要使用$
        System.out.println(string);

        //?: ?= ?!的括号内容是不占用组号的
    }
}
