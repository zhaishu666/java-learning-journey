package week9.day03;

import java.util.UUID;

public class A01_UUID {
    static void main() {

        System.out.println(UUID.randomUUID().toString().replace("-", ""));
    }
}
