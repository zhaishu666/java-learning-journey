package week3.day01;

import java.util.ArrayList;
import java.util.Random;

public class Test {
    static void main() {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        ArrayList<Character> list = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            list.add((char)('a' + i));
            list.add((char)('A' + i));
        }
        for (int i = 0; i < 4; i++) {
            int rani = r.nextInt(list.size());
            sb.append(list.get(rani));
        }
        System.out.println(list);
    }
}
