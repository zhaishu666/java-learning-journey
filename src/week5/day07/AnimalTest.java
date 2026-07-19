package week5.day07;

import java.util.ArrayList;

public class AnimalTest {
    public static void main(String[] args) {

        ArrayList<Cat> list = new ArrayList<>();
        list.add(new bosimao("zhangsan"));
        list.add(new lihuamao("hajimi"));
        keepPet(list);
    }

    public static void keepPet(ArrayList<? extends Cat> list) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).eat();
        }
    }
}
