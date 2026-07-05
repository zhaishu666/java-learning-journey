package week3.day06;

public class CPerson {
    static {   //静态代码块属于类先于main方法执行,且只执行一次
        System.out.println("Person类加载");
    }
    {   //构造代码块每次调用方法时都会执行
        System.out.println("创建人对象");
    }
    public CPerson(){
        System.out.println();
    }
}
