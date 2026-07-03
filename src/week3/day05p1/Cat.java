package week3.day05p1;

public class Cat extends Animal{
    private String name;
    public Cat(){
    }

    public Cat(int age,String color,String name){
        super(age,color,name);
    }

    @Override
    public void eat(String something){
        System.out.println("猫在吃" + something);
    }
    public void catchMouse(){
        System.out.println("猫正在抓老鼠");
    }
}
