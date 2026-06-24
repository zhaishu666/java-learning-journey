package week2.day03;

public class PlayCharacter {
    static void main(String[] args) {
        Character c1 = new Character("镜流", 100);
        Character c2 = new Character("刃", 100);
        /*while (true) {
            c1.attack(c2);
            if(c2.getBlood() == 0){
                System.out.println(c1.getName() + "赢了");
                break;
            }
            c2.attack(c1);
            if(c1.getBlood() == 0){
                System.out.println(c2.getName() + "赢了");
                break;
            }*/
        System.out.println("暴击率为" + c2.CritRate());
    }
}

