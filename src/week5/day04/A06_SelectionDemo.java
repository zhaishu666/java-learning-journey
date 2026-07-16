package week5.day04;

public class A06_SelectionDemo {
    public static void main(String[] args) {
        int[] arr = {2, 4, 5, 3, 1};

        //使用选择排序的方法进行排序,从0索引开始一次跟后面每一个数据比较,小的放前面
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > arr[j]) {
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
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


