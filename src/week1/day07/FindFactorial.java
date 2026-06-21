package week1.day07;

//找到101~200间的一共有多少质数,并输出
public class FindFactorial {
    static void main() {
        int count = 0;
        for (int num = 101; num <= 200; num++) {
            boolean flag = true;
            for (int i = 2; i <= (int)Math.sqrt(num); i++) { //不能忘记等于的情况,如121 = 11*11
                if( num % i == 0){
                    flag = false;
                    break;
                }
            }if(flag){
                count++;
                System.out.printf("%d ",num);
            }if(num == 200){
                System.out.printf("\n");
            }
        }
        System.out.println("一共有"+ count + "个质数");
    }
}
