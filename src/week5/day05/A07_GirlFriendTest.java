package week5.day05;

import java.util.Arrays;
import java.util.Comparator;

public class A07_GirlFriendTest {
    public static void main(String[] args) {
        GirlFriend gf1 = new GirlFriend(18, 1.73, "liuying");
        GirlFriend gf2 = new GirlFriend(20, 1.70, "kafuka");
        GirlFriend gf3 = new GirlFriend(19, 1.76, "yixuan");
        GirlFriend gf4 = new GirlFriend(20, 1.70, "weiweian");

        GirlFriend[] arr = {gf1,gf2,gf3,gf4};

        Arrays.sort(arr,( o1,  o2) ->{
                double temp = o1.getAge() - o2.getAge();
                temp = temp == 0 ? o1.getHeight() - o2.getHeight() : temp;
                temp = temp == 0 ? o1.getName().compareTo(o2.getName()) : temp;
                //String中的compareTo方法,会比较两个字符串在ASCII码中的值

                if(temp > 0){  //防止-的结果为小数,而方法的返回值为int而导致排序问题
                    return 1;
                } else if (temp < 0) {
                    return -1;
                }else {
                    return 0;
                }
            }
        );

        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i].toString());
        }
    }
}
