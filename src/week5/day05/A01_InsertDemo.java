package week5.day05;

public class A01_InsertDemo {
    static void main(String[] args) {

        //使用插入排序的方法获得数组
        int[] arr = {3, 44, 38, 5, 47, 15, 36, 26, 27, 2, 46, 4, 19, 50, 48};

        int stratIndex = -1;
        //遍历数组获得其中有序的部分
        for (int i = 0; i < arr.length -1; i++) {
            if(arr[i] > arr[i+1]){
                stratIndex = i + 1;
                break;
            }
        }

        for (int i = stratIndex; i < arr.length; i++) {   //遍历数组中后面的无序部分
            for (int j = i; j > 0; j--) {     //每次获得的无序部分的索引时,将这个索引的值逐个与前面比较,次数逐渐减少
                if(arr[j] < arr[j-1]){
                    int temp = arr[j];
                    arr[j] = arr[j-1];
                    arr[j-1] = temp;
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
