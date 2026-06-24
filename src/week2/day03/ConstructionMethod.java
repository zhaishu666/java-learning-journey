package week2.day03;

import week2.day02.student;

public class ConstructionMethod {
    static void main() {
       // GirlFriend s = new GirlFriend();  //空参构造
        GirlFriend ss = new GirlFriend(18,"流萤",'女');
        int age = ss.getAge();
        String name = ss.getName();
        char gender = ss.getGender();
        System.out.println(age);
        System.out.println(name);
        System.out.println(gender);
    }
}
