package week9.day05;

public class Test3 {
    static void main() {

        OrderStatus[] values = OrderStatus.values();
        for (OrderStatus status : values) {
            int code = status.getCode();
            System.out.println(code);
        }

        OrderStatus byCode = OrderStatus.getByCode(3);
        System.out.println(byCode.getDest());

    }
}
