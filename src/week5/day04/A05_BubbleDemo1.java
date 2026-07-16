package week5.day04;

public class A05_BubbleDemo1 {
    static void main(String[] args) {
        int[] arr = {2,3,4,5,1};

        //执行冒泡排序法进行排序
        for (int i = 0; i < arr.length -1; i++) {   //对于有n个元素的数组我们只需要排序n-1次就行了
            for (int j = 0; j < arr.length -1 -i; j++) {
                if(arr[j] > arr[j+1]){
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
        printArr(arr);
    }

    public static void printArr(int[] arr) {
        for (int k = 0; k < arr.length; k++) {
            System.out.print(arr[k] + " ");
        }
        System.out.println();
    }
}
