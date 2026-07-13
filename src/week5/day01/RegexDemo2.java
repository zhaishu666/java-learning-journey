package week5.day01;

public class RegexDemo2 {
    static void main(String[] args) {
        System.out.println("a".matches("."));  //.表示任意字符
        System.out.println("a".matches("\\d"));  //数字0~9
        System.out.println("a".matches("\\D"));  //非数字
        System.out.println("a".matches("\\s"));  //空白字符
        System.out.println("a".matches("\\S"));  //非空白字符
        System.out.println("a".matches("\\w"));  //a-zA-Z_0-9
        System.out.println("_".matches("\\w"));  //a-zA-Z_0-9
        System.out.println("你".matches("\\W")); //非上面的字符

        System.out.println("a123".matches("[a-c]\\d{3,}"));
        //这里输出的结果是false,因为[a-c]表示的是第一个字符的范围而\\D则是从第二个开始{3}表示\\D的结果出现3次
        //这样的话字符串的长度就应该为四
        //值得注意的是去掉{3}后\\D只默认比较一个字符,而原字符串长度为3,输出任然为false
    }
}
