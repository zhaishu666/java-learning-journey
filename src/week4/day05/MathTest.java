package week4.day05;

public class MathTest {
    static void main(String[] args) {
        System.out.println(Math.abs(10));  //获得这个数的绝对值
        System.out.println(Math.abs(-12.3));//对于小数依旧可以

        //对于int类型,表示范围为-2147483648~2147483647 如果超过这个范围就无法输出
        System.out.println(Math.abs(-2147483648));
        System.out.println(Math.absExact(-2147483647));  //可以使用absExact来找出这个异常

        //进一法,向上进一
        System.out.println(Math.ceil(13.5));
        System.out.println(Math.ceil(-12.3));

        //去尾法,去掉小数点后的数并向下退一
        System.out.println(Math.floor(13.5));
        System.out.println(Math.floor(-12.3));

        //四舍五入
        System.out.println(Math.round(4.4));
        System.out.println(Math.round(4.5));

        //返回2的3次幂的值,返回值类型为double
        System.out.println(Math.pow(2, 3));

        //返回double类的随机数,范围为[0.0 , 1.0) 包左不包右
        System.out.println(Math.random());

        //获取两个整数间的最大值
        System.out.println(Math.max(5, 10));

        //开平方根
        System.out.println(Math.sqrt(4));
        //开立方根
        System.out.println(Math.cbrt(8));
    }
}
