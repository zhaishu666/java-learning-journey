package week5.day04;

public class A02_BasicSearchDemo2 {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        int[] arr = {1,2,3,4,9,6,7,8,9};
        int i = 9;
        basicSearch(sb,arr,i);
        String string = sb.toString();
        for (int i1 = 0; i1 < string.length(); i1++) {
            if(i1 == string.length()-1){
                System.out.print(string.charAt(i1));
            }else {
                System.out.print(string.charAt(i1) + ", ");
            }
        }
    }

    private static void basicSearch(StringBuilder sb, int[] arr, int i) {
        for (int j = 0; j < arr.length; j++) {
            if (arr[j] == i) {
                sb.append(j);
            }
        }
    }
}
