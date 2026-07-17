package week5.day05;

public class GirlFriend {
    private int age;
    private double height;
    private String name;


    public GirlFriend() {
    }

    public GirlFriend(int age, double height, String name) {
        this.age = age;
        this.height = height;
        this.name = name;
    }

    /**
     * 获取
     * @return age
     */
    public int getAge() {
        return age;
    }

    /**
     * 设置
     * @param age
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * 获取
     * @return height
     */
    public double getHeight() {
        return height;
    }

    /**
     * 设置
     * @param height
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * 获取
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "GirlFriend{age = " + age + ", height = " + height + ", name = " + name + "}";
    }
}
