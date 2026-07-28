package week7.day02;

public class A03_TryCatchDemo3 {
    static void main(String[] args) {
        //如果try中出现的异常没有被捕获,怎么执行?

        try{
            System.out.println(2/0);
        }catch(NullPointerException e){  //如果try中出现的异常没有被捕获,就执行JVM默认处理异常的方法,相当于try-catch白写
            System.out.println(e);
        }
        System.out.println("看看我执行了吗");
    }
}
