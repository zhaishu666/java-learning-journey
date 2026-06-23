package week2.day02;


public class TwoDimensionDemo1 {
    static void main() {

        //每一个一维数组其实是二维数组中的元素,每一个一维数组之间使用逗号隔开
        int[][] arr1 = new int[][]{{1, 2, 3}, {1, 2, 3, 4, 5}};
        int[][] arr2 = {{1, 2, 3}, {1, 2, 3, 4, 5}};
        int[][] arr3 = { //标准静态二维数组写法
                {1, 2, 3},
                {4,5,6,7,8,9}
        };//;号易忘
        // System.out.println(arr1[0]); //[I@f6f4d33 ,表示获得二维数组中的第一个一维数组
        // System.out.println(arr1[0][0]); //表示获得第1个一维数组中索引的第0个元素

        //外循环表示得到二维数组里的每一个数组
        for (int i = 0; i < arr3.length; i++) {
            //内循环表示得到数组中的每一个元素
            for (int j = 0; j < arr3[i].length; j++) {
                System.out.print(arr3[i][j] + " ");
            }
            System.out.println();
        }
    }
}
