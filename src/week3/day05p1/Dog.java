package week3.day05p1;

public class Dog extends Animal{
    private String name;
    public Dog(){
    }

    public Dog(int age,String color,String name){
        super(age,color,name);
    }

    @Override
    public void eat(String something){
        System.out.println("狗在吃"+ something);
    }

    public void lookHouse(){
        System.out.println("狗正在看家");
    }

}
