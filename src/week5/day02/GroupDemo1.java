package week5.day02;

public class GroupDemo1 {
    static void main() {
        //判断一组字符串开头和结尾的字符是否一致
        //a123a  1qwrr1 bweqeqb  @qeer@
        String regex1 = "(.).+\\1";  //这里表示任意字符.被复用
        System.out.println("a123a".matches(regex1));
        System.out.println("1qwrr1".matches(regex1));
        System.out.println("bweqeqb".matches(regex1));
        System.out.println("@qeer@".matches(regex1));
        System.out.println("@qeerr".matches(regex1));

        System.out.println("--------------------");
        //判断开头的一段字符与结尾的一段字符是否一致
        //abc123abc  123qwer123  @12312@  123321
        String regex2 = "(.+).+\\1";    //这里表示出现一次或多次的任意字符被复用
        System.out.println("abc123abc".matches(regex2));
        System.out.println("123qwer123".matches(regex2));
        System.out.println("@12312@".matches(regex2));
        System.out.println("123321".matches(regex2));

        System.out.println("--------------------");
        //判断开头的一段字符与结尾的一段字符是否一致,且字符必须是同一个字符
        //aaaabbbccbaaa  111qwer111   oopoo  010qwe101
        String regex3 = "((.)\\2*).+\\1";    //(.)把首字母看成一组 \\2*表示这组内容的复用可以出现0次或多次
        System.out.println("aaaabbbccbaaa".matches(regex3));
        System.out.println("111qwer111".matches(regex3));
        System.out.println("oopoo".matches(regex3));
        System.out.println("010qwe101".matches(regex3));
    }
}
