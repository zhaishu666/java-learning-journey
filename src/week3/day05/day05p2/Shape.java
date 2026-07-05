package week3.day05.day05p2;

public class Shape {
    public Shape() {
    }

    public void draw() {
        System.out.println("draw");
    }
   public final void getInfo(){
        System.out.println("getInfo");
    }
}

class Circle extends Shape{
    static final int RADIUS =  10;
    @Override
    public void draw(){
        System.out.println("circle");
    }
}

final class Rectangle extends Shape{
    @Override
    public void draw(){
        System.out.println("rectangle");
    }
}