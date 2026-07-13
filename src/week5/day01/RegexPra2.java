package week5.day01;

public class RegexPra2 {
    static void main(String[] args) {
        String regex1 = "\\w{4,16}";
        String regex2 = "[1-9]\\d{16}([\\dXx])";
        String regex3 = "[1-9]\\d{5}(?:18|19|20)\\d{2}(?:0[1-9]|10|11|12)(?:0[1-9]|[1-2]\\d|30|31)\\d{3}[\\dXx]";
        System.out.println("1ab_wee".matches(regex1));
        System.out.println("13195337801123456X".matches(regex2));
    }
}
