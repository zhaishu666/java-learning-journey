package week9.day05;

public class Test {
    public static void main(String[] args) {

        Season s = Season.SPRING;
        System.out.println(s);

        switch (s) {
            //枚举天生适合做分支判断
            case SPRING -> System.out.println("SPRING");
            case SUMMER -> System.out.println("SUMMER");
            case WINTER -> System.out.println("WINTER");
            case AUTUMN -> System.out.println("AUTUMN");
        }
    }
}
