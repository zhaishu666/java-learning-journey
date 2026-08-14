package week9.day05;

public class Test2 {
    static void main() {

        Season[] values = Season.values();  //返回包含所有枚举常量的数组
        for (Season season : values) {
            System.out.println("季节名称" + season.name() + "季节序号" + season.ordinal());  //分别获得枚举常量名称的字符串形式,以及按照定义顺序的序号

            Season summer = Season.valueOf("SUMMER");  //根据字符串获得对应的枚举实例

        }
    }
}
