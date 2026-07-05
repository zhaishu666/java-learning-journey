package week3.day07.day07p3;

public class School {
    String name = "一中";
    static String address = "合肥";
    static public class Student{
        public void printInfo(){
            System.out.println(address);
            School s = new School();
            System.out.println(s.name);
        }
    }
}
