package week9.day05;

public enum OrderStatus {

    UNPAID(1,"未支付"),
    PAID(2,"已支付"),
    SHIPPED(3,"已发货"),
    COMPLETED(4,"已完成"),
    CANCELLED(5,"已取消");

    private final int code;
    private final String dest;

    private OrderStatus(int code, String dest) {
        this.code = code;
        this.dest = dest;
    }

    public int getCode() {
        return code;
    }
    public String getDest() {
        return dest;
    }

    public static OrderStatus getByCode(int code) {
        for(OrderStatus status : values()){
            if ((status.code == code)){
                return status;
            }
        }
        return null;
    }
}
