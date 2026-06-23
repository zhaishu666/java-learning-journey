package week2.day02;

public class TwoDimensionDemo2 {
    static void main() {
      /*  int[][] doubleArr = new int[2][5]; //2和5分别表示二维数组内的一维数组数和长度
        //方法弊端为每个数组的长度都是相同的
        //系统会自动在堆中开辟两块区域来存放数组
        int[] arr1 = {1,2,3,4};
        int[] arr2 = {5,6,7,8,9};*/
        int[][] doubleArr = new int[2][]; //不初始化数组的长度,更加灵活
        int[] arr1 = {1,2,3,4};
        int[] arr2 = {5,6,7,8,9};
        doubleArr[0] = arr1;
        doubleArr[1] = arr2;
        for (int i = 0; i < doubleArr.length; i++) {
            for (int j = 0; j < doubleArr[i].length; j++) {
                System.out.print(doubleArr[i][j] + " ");
            }
            System.out.println();
        }
    }

}
