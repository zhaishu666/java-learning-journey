package week7.day02;

public class A03_TryCatchDemo2 {
    public static void main(String[] args) {
        //如果一个try中可能出现多种异常,代码怎么执行
        int[] arr = {1,2,3,4,5};

        try{
            System.out.println(2/0);
            System.out.println(arr[10]);
        }/*catch (ArithmeticException e){
            System.out.println("算数异常");  //对于try可能出现多种异常,可以使用多个catch来与之对应
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("数组越界异常");
        }*/
        catch(IndexOutOfBoundsException | ArithmeticException e){  //JDK7之后支持一个catch捕获多种异常,但|只能写一个
            System.out.println("数组越界或计算异常");
        }


        System.out.println("执行这段代码");
    }
}
