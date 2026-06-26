package week2.day05;

public class StringDemo4 {
    static void main() {
        int[] arr = new int[]{1,2,3};
        String str = stringToArray(arr);
        System.out.println(str);
    }

    public static String stringToArray(int[] arr) {
        if (arr == null) {
            return "";
        }
        if (arr.length == 0) {
            return "[]";
        } else {
            String result = "[";
            for (int j = 0; j < arr.length; j++) {
                if (j < arr.length - 1) {
                    result = result + arr[j] + ',';
                } else if (j == arr.length - 1) {
                    result = result + arr[j] + ']';
                }
            }
            return result;
        }
    }
}
