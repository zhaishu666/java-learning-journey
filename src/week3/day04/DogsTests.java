package week3.day04;

public class DogsTests {
    static void main(String[] args) {
        Husky husky = new Husky();
        husky.eats();
        husky.drink();
        husky.lookHouse();
        husky.breakHouse();

        SharPei sharPei = new SharPei();
        sharPei.eats();
        sharPei.drink();
        sharPei.lookHouse();

        chineseDogs dog1 = new chineseDogs();
        dog1.eats();
        dog1.drink();
        dog1.lookHouse();
    }
}
