package week3.day03;

public class ArrayUtilTest {
    static void main() {
        int[] arr = {1, 2, 3, 4, 5};
        ArrayUtil.work(arr);
        double[] scores = {1.2,3.4,1.4,5.4,5.5};
        double avg = ArrayUtil.average(scores);
        System.out.println(avg);
    }
}
