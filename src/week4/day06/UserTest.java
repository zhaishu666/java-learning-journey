package week4.day06;

import com.google.gson.Gson;

public class UserTest {
    static void main(String[] args) throws CloneNotSupportedException {
        int[] data = {1,2,3,4,5,6,7,8,9,10};
        User user1 = new User(11, "zhangsan", "123", "动物11", data);

        //User user2 = (User) user1.clone();

       // System.out.println(user1);  //为什么这里不用手动调用toString? 因为只要对象不为 null，println自动执行 obj.toString()
       // System.out.println(user2);

        //使用第三方工具进行克隆
        Gson gson = new Gson();
        //把user对象转化为字符串
        String s = gson.toJson(user1);
       //再把s转化为user对象
        User user2 = gson.fromJson(s, User.class);
        int[] data1 = user1.getData();
        data1[0] = 100;
        System.out.println(user1);
        System.out.println(user2);
    }
}
