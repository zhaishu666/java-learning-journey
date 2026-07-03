package week3.day05;

public class Manage extends Worker{
    private String bonus;

    public Manage() {
    }

    public Manage(String id, String name, String salary, String bonus) {
       super(id,name,salary);
        this.bonus = bonus;
    }

    public Manage(String bonus) {
        this.bonus = bonus;
    }

    @Override
    public void work() {
        super.work();
        System.out.println("管理其他人");
    }


    /**
     * 获取
     * @return bonus
     */
    public String getBonus() {
        return bonus;
    }

    /**
     * 设置
     * @param bonus
     */
    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    public String toString() {
        return "Manage{bonus = " + bonus + "}";
    }
}
