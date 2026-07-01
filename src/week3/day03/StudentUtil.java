package week3.day03;

import java.util.ArrayList;

public class StudentUtil {
    public StudentUtil(){}

    private static int getBiggestAge(ArrayList<Student> list){
        int biggest = 0;
        for (int i = 0; i < list.size(); i++) {
            int tempAge = list.get(i).getAge();
            if(tempAge > biggest){
                biggest = tempAge;
            }
        }
        return biggest;
    }
    public static int biggestAge(ArrayList<Student> list){
        return getBiggestAge(list);
    }
}
