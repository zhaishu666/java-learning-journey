package week1.day04;

//定义一个数组,存储1,2,3,4,5,6,7,8,9,10 统计数组里有多少个能被3整除的数
//可以使用 数组名.fori一建生成数组长度遍历
public class ArrayTest {
    static void main() {
        int[] arr = {1,2,3,4,5,6,7,8,9,10};
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            if( arr[i] % 3 == 0){
                sum++;
            }
        }
        System.out.println(sum);
    }
}

