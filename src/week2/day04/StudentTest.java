package week2.day04;

import java.util.Scanner;

public class StudentTest {
    static void main() {
        //创建数组用于存放学生
        Student[] arr = new Student[3];
        Student stu1 = new Student(1, "昔涟", 18);
        Student stu2 = new Student(2, "鲨鱼妹", 19);
        Student stu3 = new Student(3, "卡夫卡", 20);

        arr[0] = stu1;
        arr[1] = stu2;
        arr[2] = stu3;

        //再次添加学生判断

        Student stu4 = new Student(4, "雷米埃尔", 21);

        if (judgeStu(arr, stu4)) {
            int count = getCount(arr); //获得arr数组中非空索引的长度,不能在循环中使用
            Student[] finalArr = getFinalArr(count, arr, stu4);
            boolean flag = deleteStudent(finalArr); //true表示id不存在
            if (!flag) {
                printStu(finalArr);
                Scanner sc = new Scanner(System.in);
                System.out.println("请输入要查询的学生id");
                int findId = sc.nextInt();
                for (Student student : finalArr) {  //这是增强型的for
                    if (student != null) {
                        if (student.getId() == findId) {
                            int NewAge = student.getAge() + 1;
                            student.setAge(NewAge);
                            System.out.println("更改后的年龄为" + student.getAge());
                        }
                    }
                }
            }
        }
    }

    public static boolean deleteStudent(Student[] finalArr) {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入要删除的学生的id");
        int changId = sc.nextInt();
        boolean flag = true;
        for (int i = 0; i < finalArr.length; i++) {
            if (changId == finalArr[i].getId()) {
                finalArr[i] = null;
                flag = false;
                break;
            }
        }
        if (flag) {
            System.out.println("删除失败,不存在这个id");
        }
        return flag;
    }

    public static Student[] getFinalArr(int count, Student[] arr, Student stu4) { //获得最终的数组
        Student[] finalArr;
        if (count < arr.length) {
            arr[count] = stu4;
            printStu(arr);
            finalArr = arr;
        } else {
            Student[] NewArr = getNewArr(arr);
            NewArr[count] = stu4;
            printStu(NewArr);
            finalArr = NewArr;
        }
        return finalArr;
    }

    public static int getCount(Student[] arr) { //获得arr数组所有不为空的索引
        int count = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != null) {
                count++;
            }
        }
        return count;
    }

    public static void printStu(Student[] arr) { //遍历数组中的每个元素
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != null) {
                System.out.println("id" + arr[i].getId() + "的学生,姓名为" + arr[i].getName() + "年龄为" + arr[i].getAge());
            }
        }
        System.out.println();
    }

    public static Student[] getNewArr(Student[] arr) { //创建一个新数组,长度为原数组加一
        Student[] NewArr = new Student[arr.length + 1];
        for (int i = 0; i < arr.length; i++) {
            NewArr[i] = arr[i];
        }
        return NewArr;
    }

    public static boolean judgeStu(Student[] arr, Student stu4) { //判断输入学生的学号是否唯一
        boolean flag = true;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != null) {
                if (stu4.getId() == arr[i].getId()) {
                    System.out.println("输入的id重复,请重新输入"); //要注意该方法既会返回boolean,又会打印字符
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }
}
