package week6.doudizhu1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class PokerGame2 {
    //牌盒
    //此时我们只需要牌跟序号产生对应关系就行了
    static HashMap<String, Integer> hm = new HashMap<>();
    static ArrayList<String> list = new ArrayList<>();

    static {
        String[] color = {"♦", "♣", "♥", "♠"};
        String[] number = {"3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A", "2"};

        for (String n : number) {
            for (String c : color) {
                list.add(c + n);
            }
        }
        list.add(" 小王");
        list.add(" 大王");

        hm.put("J", 11);
        hm.put("Q", 12);
        hm.put("K", 13);
        hm.put("A", 14);
        hm.put("2", 15);
        hm.put("小王", 50);
        hm.put("大王", 100);
    }

    public PokerGame2() {

        Collections.shuffle(list);

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

        order(lord);
        order(player1);
        order(player2);
        order(player3);

        lookPoker("底牌", lord);
        lookPoker("流萤", player1);
        lookPoker("卡夫卡", player2);
        lookPoker("我", player3);

    }

    public void order(ArrayList<String> list) {
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String color1 = o1.substring(0, 1);
                int value1 = getValue(o1.substring(1));
                String color2 = o2.substring(0, 1);
                int value2 = getValue(o2.substring(1));
                int i = value1 - value2;
                return i == 0 ? color1.compareTo(color2) : i;
            }
        });
    }

    public int getValue(String key) {
        if (hm.containsKey(key)) {
            return hm.get(key);
        } else {
            return Integer.parseInt(key);
        }
    }

    public static void lookPoker(String name, ArrayList<String> list) {
        System.out.print(name + ": ");
        for (String poker : list) {
            System.out.print(poker + " ");
        }
        System.out.println();
    }
}
