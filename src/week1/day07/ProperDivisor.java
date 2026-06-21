package week1.day07;

/*完数定义：一个数等于它所有真因数之和，例如 6=1+2+3
方法 1：int getFactorSum(int n) 求所有真因数之和
方法 2：boolean isPerfectNum(int n) 内部调用 getFactorSum 判断是否完数
main 遍历 1~1000，打印所有完数

行20 例：n=28（标准完数）
因数：1、2、4、7、14，总和 = 28
√28≈5，循环 i=1~5
i=1：sum+1
i=2：sum+2
i=4：sum+4
sum=7，明显不等于 28，判定不是完数，结果直接错误
*/
public class ProperDivisor {

    public static int getFactorSum(int n) {
        int sum = 0;
        for (int i = 1; i <= (int)Math.sqrt(n); i++){ //真因数：能整除 n、小于 n 的数，不含自身
            // i表示因数
            if (n % i == 0) {
                sum += i;
                int other = n / i; //加上另一半因数
                if (other != i) {
                    sum += other;
                }
            }
        }
        sum -= n;
        return sum;
    }

    public static boolean isPerfectNum(int n) {
        int sum = getFactorSum(n);
        return sum == n; //直接输出判断,无需赋给另一个值
    }

    public static void main(String[] args) {
        for (int n = 1; n <= 1000; n++) {
          if(isPerfectNum(n)){
              System.out.print(n + " ");
          }
        }
    }
}
