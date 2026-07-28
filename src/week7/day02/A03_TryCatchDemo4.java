package week7.day02;

public class A03_TryCatchDemo4 {
    static void main() {
        //测试异常顶级父类的getMessage(), toString(), printStackTrace() 方法

        int[] arr = {1,2,3,4,5};

        try{
            System.out.println(arr[5]);
        }catch(ArrayIndexOutOfBoundsException e){
            //System.out.println(e.getMessage());  //返回此throwable的详细消息的字符串
            //System.out.println(e.toString());  //返回此可抛出的简短描述,事实上toString可以省略,与直接打印e效果相同
            e.printStackTrace();  //最常用,把异常的错误信息输出在控制台
        }
        System.out.println("看看我执行了吗?");
    }
}
