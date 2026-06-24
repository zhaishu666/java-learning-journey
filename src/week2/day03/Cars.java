package week2.day03;

//创建3个汽车对象,汽车的属性:品牌,价格,颜色 数据通过键盘录入
public class Cars {
    private String brand;
    private int price;
    private String color;

    public Cars(){}

    public Cars(String brand,int price,String color){
        this.brand = brand;
        this.price = price;
        this.color = color;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
    public String getBrand(){
        return brand;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}

