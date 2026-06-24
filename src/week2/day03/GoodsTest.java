package week2.day03;

public class GoodsTest {
    public static void main(String[] args) {
        //创立一个数组
        Goods[] arr = new Goods[3];

        Goods g1 = new Goods(111, "方便面", 4.5, 200);
        Goods g2 = new Goods(112, "火腿肠", 2, 400);
        Goods g3 = new Goods(113, "辣条", 0.5, 500);

       /* g1.setId(111);
       g1.setPrice(25.5);
       g1.setGoodName("方便面");
       g1.setSupply(20);*/

        arr[0] = g1;
        arr[1] = g2;
        arr[2] = g3;

        for (int i = 0; i < arr.length; i++) {
            Goods goods = arr[i];
            System.out.println(goods.getId() + "," + goods.getGoodName() + "," + goods.getPrice()+ "," + goods.getSupply());
        }
    }
}
