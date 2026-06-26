package week2.day05;

public class BlockPhoneNumber {
    static void main() {
        String number = "13112345678";
        String block = "****";
        String sub1 = number.substring(0, 3);
        String sub2 = number.substring(7, 11);
        String newNumber = sub1 + block + sub2;
        System.out.println(newNumber);
    }
}
