package week5.day03;

import java.time.Duration;
import java.time.LocalTime;

public class A07_DurationDemo {
    public static void main(String[] args) {
        LocalTime t1 = LocalTime.now();

        LocalTime t2 = LocalTime.of(1, 1, 1, 0);

        Duration duration = Duration.between(t2, t1);
        System.out.println(duration.getSeconds());
        System.out.println(duration.getNano());
    }
}
