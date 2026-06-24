package week2.day03;

import java.util.Random;

public class Character {
    private String name;
    private int blood;
    //private char gender;
    private String crit;

    String[] boyRate = {"20%", "30%", "40%", "50%"};
    String[] girlRate = {"60%", "70%", "80%", "90%"};

    public Character() {
    }

    public Character(String name, int blood) {
        this.name = name;
        this.blood = blood;
    }

    /**
     * 获取
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取
     *
     * @return blood
     */
    public int getBlood() {
        return blood;
    }

    /**
     * 设置
     *
     * @param blood
     */
    public void setBlood(int blood) {
        this.blood = blood;
    }

    //实现两个角色相互攻击
    public void attack(Character c) { //表示调用character中的c1 or c2;
        Random r = new Random();
        int hurt = r.nextInt(1, 21); //表示伤害范围
        int remainBlood = c.getBlood() - hurt;
        remainBlood = remainBlood < 0 ? 0 : remainBlood; //对血量进行判断
        c.setBlood(remainBlood);

        System.out.println(this.getName() + "攻击了" + c.getName() + "造成了" + hurt + "点伤害" +
                c.getName() + "还剩下" + remainBlood + "点血量");
    }

    public String CritRate() {
        Random r = new Random();
        if (getName() == "镜流") {
            int ran = r.nextInt(girlRate.length);
            crit = girlRate[ran];
            // System.out.println("镜流的暴击率为" + crit);
        } else if (getName() == "刃") {
            int ran = r.nextInt(boyRate.length);
            crit = boyRate[ran];
            // System.out.println("刃的暴击率为" + crit);
        }
        return crit;
    }
}
