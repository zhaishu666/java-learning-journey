package week2.day03;

//定义一个商品商品属性包括商品id,名字,价格,库存
public class Goods {
    private int id;
    private String goodName;
    private double price;
    private int supply;

    public Goods() {
    }

    public Goods(int id, String goodName, double price, int supply) { //全参构造不要忘了给成员变量赋值
        this.id = id;
        this.goodName = goodName;
        this.price = price;
        this.supply = supply;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public int getSupply() {
        return supply;
    }

    public void setSupply(int supply) {
        this.supply = supply;
    }
}
