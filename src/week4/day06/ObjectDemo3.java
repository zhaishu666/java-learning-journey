package week4.day06;

public class ObjectDemo3 {
    static void main(String[] args) {
        String str = "abc";
        StringBuilder sb = new StringBuilder("abc");

        System.out.println(str.equals(sb));  //false
        //这里是字符串str调用了equals方法来跟sb这个对象比较
        //因为字符串中的equals方法被重写了,所以会逐个比较字符串中的字符
        //但sb并不是字符串形式,所以直接返回false
        System.out.println(sb.equals(str)); //false
        //StringBuilder的对象调用了Object父类中的equals方法
        // Object中,默认用==比较地址值
        //两者的地址值不同,所以输出false
    }
}
