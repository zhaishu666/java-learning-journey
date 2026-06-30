package week2.day07;

import java.util.ArrayList;
import java.util.Scanner;

public class StudentSystem {
    static void main() {
        Scanner sc = new Scanner(System.in);
        ArrayList<Student> list = new ArrayList<>();
        while (true) {   //case时可以在while前加上loop: case内部break后也加上loop表示停止while
            int number = getNumber(sc);
            if (number == 1) {
                setStudentMessage(list, sc);
            } else if (number == 2) {
                deleteStudent(list, sc);
            } else if (number == 3) {
                changeStudent(list, sc);
            } else if (number == 4) {
                printStudent(list);
            } else if (number == 5) {
                System.out.println("结束程序");
                break;
            }
        }
        sc.close();
    }

    public static void printStudent(ArrayList<Student> list) {
        if (list.isEmpty()) {   //调用方法对list的长度进行判断
            System.out.println("当前无学生信息,请添加后再查询");
        } else {
            System.out.println("id\t姓名\t年龄\t家庭住址");
            for (int i = 0; i < list.size(); i++) {
                Student stu = list.get(i);
                System.out.println(stu.getId() + "\t" + stu.getName() + "\t" + stu.getAge() + "\t" + stu.getAddress());
            }
        }
    }

    public static void changeStudent(ArrayList<Student> list, Scanner sc) {
        System.out.println("请输入要修改的学生id");
        String inputId = sc.next();
        int count = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(inputId)) {
                count = i;
                break;
            }
        }
        if (count >= 0) {
            Student stu = list.get(count);
            System.out.println("请输入修改后的姓名");
            String name = sc.next();
            int age ;
            while (true) {
                System.out.println("请输入修改后的年龄");
                if (sc.hasNextInt()) {
                    age = sc.nextInt();
                    break;
                } else {
                    System.out.println("非法修改,请重新输入");
                }
            }
            System.out.println("请输入修改后的地址");
            String address = sc.next();
            stu.setName(name);
            stu.setAge(age);
            stu.setAddress(address);
            list.set(count, stu);
        } else {
            System.out.println("未找到该id");
        }
    }

    public static void deleteStudent(ArrayList<Student> list, Scanner sc) {
        System.out.println("请输入要删除的学生的id");
        String inputId = sc.next();
        boolean flag = true;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(inputId)) {
                list.remove(i);
                System.out.println("已删除");
                flag = false;
                break;
            }
        }
        if (flag) {
            System.out.println("id不存在");
        }
    }

    public static void setStudentMessage(ArrayList<Student> list, Scanner sc) { //键盘录入学生信息
        int count = 0;
        while (true) {
            Student stu = new Student();
            System.out.println("请输入第" + (count + 1) + "个学生的id");
            String id = sc.next();
            if (judgeId(list, id)) {
                System.out.println("请输入第" + (count + 1) + "个学生的姓名");
                String name = sc.next();
                System.out.println("请输入第" + (count + 1) + "个学生的年龄");
                int age ;
                if (sc.hasNextInt()) {
                    age = sc.nextInt();
                } else {
                    System.out.println("错误年龄,请重新输入");
                    break;
                }
                System.out.println("请输入第" + (count + 1) + "个学生的家庭住址");
                String address = sc.next();
                stu.setId(id);
                stu.setName(name);
                stu.setAge(age);
                stu.setAddress(address);
                list.add(stu);
                count++;
                System.out.println("学生信息添加成功");
                System.out.println("如要继续请输入1,结束请输入任意建");
                String con = "1";
                String signal = sc.next();
                if (!con.equals(signal)) {
                    break;
                }
            } else {
                System.out.println("id重复,请重新输入");
            }
        }
    }

    public static boolean judgeId(ArrayList<Student> list, String id) {
        boolean flag = true;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(id)) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public static int getNumber(Scanner sc) {
        System.out.println("------------欢迎来到黑马学生管理系统-------------");
        System.out.println("1: 添加学生");
        System.out.println("2: 删除学生");
        System.out.println("3: 修改学生");
        System.out.println("4: 查询学生");
        System.out.println("5: 退出");
        System.out.println("请输入您的选择:");
        int number;
        if (sc.hasNextInt()) {
            number = sc.nextInt();
            if (number < 1 || number > 5) {
                System.out.println("输入超出范围");
                return 5;
            } else {
                return number;
            }
        }
        System.out.println("输入不合法");
        return 5;
    }
}
