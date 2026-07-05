package week3.day05.day05p2;

public class ShapeTest {
    public static void main(String[] args) {
        Shape[] arr = new Shape[2];
        Circle c = new Circle();
        Rectangle r = new Rectangle();
        arr[0] = c;
        arr[1] = r;

        for (int i = 0; i < arr.length; i++) {
            arr[i].draw();
            arr[i].getInfo();
        }
    }
}
