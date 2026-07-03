package week3.day05;

public class Worker {
    private String id;
    private String name;
    private String salary;


    public Worker() {
        this(null,null,"3000");
    }

    public Worker(String id, String name, String salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    public void work(){

        System.out.print("工作");
    }
    public void eats(){
        System.out.println("吃饭(吃米饭)");
    }

    /**
     * 获取
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置
     * @param id
     */
    public void setId(String id) {
        this.id = id;
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

    /**
     * 获取
     * @return salary
     */
    public String getSalary() {
        return salary;
    }

    /**
     * 设置
     * @param salary
     */
    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String toString() {
        return "Worker{id = " + id + ", name = " + name + ", salary = " + salary + "}";
    }
}
