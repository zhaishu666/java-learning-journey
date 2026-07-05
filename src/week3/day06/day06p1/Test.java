package week3.day06.day06p1;

public class Test {
    static void main() {
        TableTennisAthlete tta = new TableTennisAthlete("张三",18);
        BasketballAthlete ba = new BasketballAthlete("李四",19);
        TableTennisCoach ttc = new TableTennisCoach("王五",30);
        BasketballCoach bc = new BasketballCoach("老刘",36);
        tta.work();
        tta.speak();
        ba.work();
        ba.speak();
        ttc.work();
        bc.work();
    }
}
