package week2.day02;

public class student {
    private String name;
 //   private String grade;
    private char gender;
    private int age;

    //对于每个私有化的成员变量,都要提供set和get方法
    public void setAge(int age) { //set赋值
        if(age >= 18 && age <= 25){
            this.age = age;
        }else{
            System.out.println("错误年龄");
        }
    }
    public int getAge() { //get获取
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public char getGender() {
        return gender;
    }

    public void learn() {
        System.out.println("学生正在学习");
    }

    public void sleep() {
        System.out.println("学生正在睡觉");
    }
}
