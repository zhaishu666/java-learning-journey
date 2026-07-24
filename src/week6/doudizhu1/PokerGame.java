package week6.doudizhu1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeSet;

public class PokerGame {
    //牌盒
    //此时我们只需要牌跟序号产生对应关系就行了
    static HashMap<Integer, String> hm = new HashMap<>();
    static ArrayList<Integer> list = new ArrayList<>();

    static {
        String[] color = {"♦", "♣", "♥", "♠"};
        String[] number = {"3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A", "2"};

        //序号
        int servalNumber = 1;
        for (String n : number) {
            for (String c : color) {
                hm.put(servalNumber, c + n);
                list.add(servalNumber);
                servalNumber++;
            }
        }
        hm.put(servalNumber, "小王");
        list.add(servalNumber);
        servalNumber++;
        hm.put(servalNumber, "大王");
        list.add(servalNumber);
    }

    PokerGame() {
        //洗牌
        Collections.shuffle(list);
        //发牌.使用TreeSet是因为它的底层是红黑树,会自动排序
        TreeSet<Integer> lord = new TreeSet<>();
        TreeSet<Integer> player1 = new TreeSet<>();
        TreeSet<Integer> player2 = new TreeSet<>();
        TreeSet<Integer> player3 = new TreeSet<>();

        for (int i = 0; i < list.size(); i++) {
            if (i <= 2) {
                lord.add(list.get(i));
                continue;
            }
            if(i % 3 == 0) {
                player1.add(list.get(i));
            } else if (i % 3 == 1) {
                player2.add(list.get(i));
            }else if (i % 3 == 2) {
                player3.add(list.get(i));
            }
        }


        lookPoker("底牌", lord);
        lookPoker("流萤", player1);
        lookPoker("卡夫卡",player2);
        lookPoker("我", player3);

    }

    //获得序号所对应的牌
    public static void lookPoker(String name,TreeSet<Integer> ts){
        System.out.print(name + ": ");
        for (Integer servalNumber : ts) {
            System.out.print(hm.get(servalNumber) + " ");
        }
        System.out.println();
    }
}
