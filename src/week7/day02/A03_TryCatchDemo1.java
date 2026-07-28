package week7.day02;

public class A03_TryCatchDemo1 {
    public static void main(String[] args) {

        //此Demo用于演示捕获异常
        //捕获异常与默认处理异常对比的好处就是出现了异常依旧可以让程序继续往下执行

        int[] arr = {1,2,3,4,5};

        try {
            System.out.println(arr[10]);  //在这一部分底层会创建一个ArrayIndexOutOfBoundsException对象
                                          //然后会拿创建的对象与catch中的变量进行对比,看括号中的变量能否接收这个对象
                                          //如果能接收,就代表这个异常被捕获了,执行catch中的代码,执行完毕后接着执行下面的代码
        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println("数组越界了");
        }
        System.out.println("看看我执行了吗?");
    }
}
