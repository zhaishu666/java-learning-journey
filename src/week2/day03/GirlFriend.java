package week2.day03;

public class GirlFriend {
    private int age;
    private String name;
    private char gender;

    public GirlFriend(){

    }

    public GirlFriend(int age,String name,char gender){
        if (age >= 18 && age <= 25) {
            this.age = age;
        }else{
            System.out.println("错误年龄");
        }
        this.name = name;
        this.gender = gender;
    }

    public int getAge(){
        return age;
    }

    public String getName() {
        return name;
    }

    public char getGender() {
        return gender;
    }
}
