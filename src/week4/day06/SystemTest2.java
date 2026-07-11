package week4.day06;

public class SystemTest2 {
    static void main(String[] args) {
        int[] arr1 = {0,1,2,3,4,5,6,7,8,9};
        int[] arr2 = new int[10];
        System.arraycopy(arr1,0,arr2,0,10);
        for (int i = 0; i < arr2.length; i++) {
            System.out.print(arr2[i] + " ");
        }
    }
}
