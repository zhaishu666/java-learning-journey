package week1.day05;

//交换一个数组的值交换前为1,2,3,4,5 交换后为5,4,3,2,1
public class Exchange {
    static void main() {
        int[] arr ={1,2,3,4,5};
        int temp;
        for (int i = 0, j = arr.length - 1; i < j; i++,j--) {//不能忘记j--
            temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
    }
}
