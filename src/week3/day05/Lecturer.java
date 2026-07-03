package week3.day05;

public class Lecturer extends Teacher{
    public Lecturer(){}

    public Lecturer(String id,String name){
        super(id,name);
    }
    public void method(){
        System.out.println("作为讲师进行教学");
    }
}
