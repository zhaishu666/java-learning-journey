package week5.day04;


public class A04_InterpolationSearchDemo {
    public static void main(String[] args) {
        int[] arr = {1,2,3,4,5,6,7,8,9};
        int num = 7;
        int index = myInterpolationSearch(arr, num);
        System.out.println(index);
    }

    private static int myInterpolationSearch(int[] arr, int num) {
        int min = 0, max = arr.length-1;
        while (min <= max) {
            int mid = min + (num-arr[min])/(arr[max]-arr[min]) * (max-min);
            if (arr[mid] == num) {
                return mid;
            }else if (arr[mid] < num) {
                min = mid+1;
            } else if (arr[mid] > num) {
                max = mid-1;
            }
        }
        return -1;
    }
}
