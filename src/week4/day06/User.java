package week4.day06;

import java.util.StringJoiner;

public class User implements Cloneable {
    private int id;
    private String username;
    private String password;
    private String path;
    private int[] data;

    public User() {
    }

    public User(int id, String username, String password, String path, int[] data) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.path = path;
        this.data = data;
    }

    /**
     * 获取
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * 设置
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 获取
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取
     *
     * @return path
     */
    public String getPath() {
        return path;
    }

    /**
     * 设置
     *
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取
     *
     * @return data
     */
    public int[] getData() {
        return data;
    }

    /**
     * 设置
     *
     * @param data
     */
    public void setData(int[] data) {
        this.data = data;
    }

    public String toString() {
        return "User{id = " + id + ", username = " + username + ", password = " + password + ", path = " + path + ", data = " + arrayToString() + "}";
    }

    public String arrayToString() {
        StringJoiner sj = new StringJoiner(",", "[", "]");

        for (int i = 0; i < data.length; i++) {
            String str = "";
            str = str + data[i];
            sj.add(str);
        }
        return sj.toString();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        //重写了Object类中的clone方法,让这个protect方法可以在本类中被调用
        //实现深拷贝
        //把要拷贝的数组获取出来
        int[] data = this.data;  //这里不写临时变量丝毫没有影响,会直接调用成员变量中的data
        //创建一个新的数组
        int[] newData = new int[data.length];
        //将数组中的内容逐个拷贝
        //也可以使用数组工具类int[] newData = Arrays.copyOf(this.data, this.data.length);
        for (int i = 0; i < data.length; i++) {
            newData[i] = data[i];
        }
        //调用父类中的克隆方法
        User u = (User) super.clone();
        //因为父类中的克隆为浅克隆,替换克隆出来对象数组的地址值
        u.data = newData;
        return u;
    }
}
