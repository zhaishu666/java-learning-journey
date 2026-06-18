package week1.day04;

//在控制台输出所有的水仙花数(指一个三位数个位,十位,百位的立方的和等于原数

public class Narcissus{
    public static void main(String[] args) {
        for( int i = 100 ; i < 1000 ; i++ ){
            int ge = i % 10;
            int shi = (i/10) % 10;
            int bai = i/100;
            int count = ge*ge*ge + shi*shi*shi + bai*bai*bai;
            if( count == i ){
                System.out.println( i + "是水仙花数");
            }
        }
    }
}
