package week9.day04;

public class Test {
    public static void main(String[] args) {


        BigStar star = new BigStar("鸡哥");

        Star proxy = ProxyUnit.createProxy(star);

        String result = proxy.sing("只因你太美");
        System.out.println(result);
    }
}
