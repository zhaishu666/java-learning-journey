package week4.day06;

public class UserTest {
    static void main(String[] args) throws CloneNotSupportedException {
        int[] data = {1,2,3,4,5,6,7,8,9,10};
        User user1 = new User(11, "zhangsan", "123", "动物11", data);

        User user2 = (User) user1.clone();
        int[] data1 = user1.getData();
        data1[0] = 100;
        System.out.println(user1);  //为什么这里不用手动调用toString? 因为只要对象不为 null，println自动执行 obj.toString()
        System.out.println(user2);
    }
}
