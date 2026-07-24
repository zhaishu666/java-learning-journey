package week6.day05;

import java.util.ArrayList;
import java.util.Collections;

public class A03_PokerGameDemo {
    //创建一副扑克牌
    static ArrayList<String> list = new ArrayList<>();

    static {
        String[] color = {"♦", "♣", "♥", "♠"};
        String[] number = {"3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A", "2"};
        for (String c : color) {
            for (String n : number) {
                list.add(c + n);
            }
        }
        list.add("大王");
        list.add("小王");
    }

    A03_PokerGameDemo() {
        //洗牌
        Collections.shuffle(list);

        //发牌
        ArrayList<String> lord = new ArrayList<>();
        ArrayList<String> player1 = new ArrayList<>();
        ArrayList<String> player2 = new ArrayList<>();
        ArrayList<String> player3 = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (i <= 2) {
                lord.add(list.get(i));
                continue;
            }
            if (i % 3 == 0) {
                player1.add(list.get(i));
            } else if (i % 3 == 1) {
                player2.add(list.get(i));
            } else if (i % 3 == 2) {
                player3.add(list.get(i));
            }
        }

        lookPoker("底牌",lord);
        lookPoker("流萤",player1);
        lookPoker("卡夫卡",player2);
        lookPoker("我",player3);
    }

    public static void lookPoker(String name, ArrayList<String> list){
        System.out.print(name + ": ");
        for (String poker : list) {
            System.out.print(poker + " ");
        }
        System.out.println();
    }
}
