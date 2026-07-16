package week5.day04;

public class A01_BasicSearchDemo1{
    public static void main(String[] args) {
        int[] arr = {1,2,3,4,45,6,7,8,9};
        int num = 99;
        int i = basicSearch(arr, num);
        System.out.println(i);
    }

    private static int basicSearch(int[] arr, int num) {
        for (int i = 0; i < arr.length; i++) {
            if(arr[i] == num){
                return i;
            }
        }
        return -1;
    }
}
