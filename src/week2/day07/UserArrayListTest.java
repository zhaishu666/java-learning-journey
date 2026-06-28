package week2.day07;

import java.util.ArrayList;
import java.util.Scanner;

//存入三个对象,定义一个方法根据id查找对应的用户信息
public class UserArrayListTest {
    static void main() {
        ArrayList<User> list = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        User user1 = new User(123,"zhangsan","123456");
        User user2 = new User(111,"lisi","123456");
        User user3 = new User(222,"wangwu","8888888");
        list.add(user1);
        list.add(user2);
        list.add(user3);

        System.out.println("请输入要查询的id");
        int inputId = sc.nextInt();
        /*boolean flag = getId(list, inputId);
        if(flag){
            System.out.println("存在");
        }else{
            System.out.println("不存在");
        }*/
        int result = getIndex(list, inputId);
        System.out.println(result);
    }

    public static int getIndex(ArrayList<User> list, int inputId) {
        for (int i = 0; i < list.size(); i++) {
            int id = list.get(i).getId();
            if(id == inputId){
                return i;
            }
        }
        return -1;
    }

    public static boolean getId(ArrayList<User> list, int inputId) {

        return getIndex(list, inputId) >= 0;
    }
}
