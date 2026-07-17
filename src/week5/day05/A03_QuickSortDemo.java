package week5.day05;

import java.util.Random;

public class A03_QuickSortDemo {
    public static void main(String[] args) {
        //int[] arr = {6, 1, 2, 7, 9, 3, 4, 5, 10, 8};

        int[] arr = new int[100000];
        Random random = new Random();
        for (int i = 0; i < 100000; i++) {
            arr[i] = random.nextInt();
        }

        long time1 = System.currentTimeMillis();
        quickSort(arr, 0, arr.length - 1);
        long time2 = System.currentTimeMillis();
        System.out.println(time2 - time1);

        /*for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }*/
    }

    public static void quickSort(int[] arr, int i, int j) {

        int start = i;
        int end = j;

        if (start > end) {
            return;
        }

        int base = arr[i];  //定义基准数

        //如果将0索引的数当作基准数的时候,一定要先移动end再移动start
        //因为先移动start会让两个指针指向一个大于基准数的数字,这样在基准数归位时会将大数交换到前面

        while (start != end) {
            while (true) {
                if (end <= start || arr[end] < base) {
                    break;
                }
                end--;
            }

            while (true) {
                if (end <= start || arr[start] > base) {
                    break;
                }
                start++;
            }
            //把start和end所指向的元素进行交换
            int temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
        }
        //基准数归位
        int temp = arr[start];
        arr[start] = arr[i];
        arr[i] = temp;

        //确定刚刚左边的范围,采用递归的方法再次进行
        quickSort(arr, i, start - 1);
        //右边也是如此
        quickSort(arr, start + 1, j);
    }

}
