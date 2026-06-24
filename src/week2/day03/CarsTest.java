package week2.day03;

import java.util.Scanner;

public class CarsTest {
    static void main() {
        Scanner sc = new Scanner(System.in);
        Cars[] cars = new Cars[3];

       /* Cars car1 = new Cars(); //错误写法,过多重复
        Cars car2 = new Cars();
        Cars car3 = new Cars();
        cars[0] = car1;
        cars[1] = car2;
        cars[2] = car3;*/

        for (int i = 0; i < cars.length; i++) {
            Cars c = new Cars(); //在堆中new出汽车对象
            System.out.println("请输入第" + (i+1) + "辆车的品牌");
            String brand = sc.next();
            c.setBrand(brand); //此处应为set
            System.out.println("请输入第" + (i+1) + "辆车的价格");
            int price = sc.nextInt();
            c.setPrice(price);
            System.out.println("请输入第" + (i+1) + "辆车的颜色");
            String color = sc.next();
            c.setColor(color);
            cars[i] = c; //将c的属性赋值给数组
        }

        for (int i = 0; i < cars.length; i++) {
            System.out.println(cars[i].getBrand()+","+cars[i].getPrice()+","+cars[i].getColor());
        }
        sc.close();
    }
}
