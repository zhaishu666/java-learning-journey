package week5.day07;

import java.util.ArrayList;

public class ListUtilTest {
    static void main() {

        ArrayList<Integer> list = new ArrayList<Integer>();

        ListUtil.addAll(list,1,2,3,4,5);
        System.out.println(list);
    }
}
