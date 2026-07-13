package week5.day01;

public class RegexDemo1 {
    static void main() {
        System.out.println("a".matches("[abc]"));  //true
        System.out.println("d".matches("[abc]"));  //false
        System.out.println("aa".matches("[abc]"));  //false,此处一个中括号只能匹配一个字符
        System.out.println("aa".matches("[abc][abc]"));  //true

        System.out.println("--------------------------");
        System.out.println("d".matches("[^abc]")); //true,表示除了abc以外的任何字符
        System.out.println("&".matches("[^abc]"));

        System.out.println("--------------------------");
        System.out.println(" ".matches("[a-z A-Z]"));//表示a-z和A-Z内的字符和空格

        System.out.println("a".matches("[a-d[m-p]]")); //表示a-d或者m-p内的字符

        System.out.println("--------------------------");
        System.out.println("d".matches("[a-z&&[def]]"));  //表示a-z与def的交集
        System.out.println("a".matches("[a-z&&[^def]]"));  //表示a-z与非def的交集
        System.out.println("&".matches("[a-z&[^def]]"));  //这里的&表示判断a-z与&和非def的交集
        System.out.println("&".matches("[a-z&&[^m-p]]")); //表示a-z与非m-p的交集

        //需要注意[]中的空格也会参与判断
    }
}
