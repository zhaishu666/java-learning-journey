package week5.day03;

import java.time.ZoneId;
import java.util.Set;

public class ZoneIdDemo1 {
    static void main(String[] args) {
        Set<String> allId = ZoneId.getAvailableZoneIds(); //调用ZoneId中的静态方法获得所有java支持的时区
        //返回类型为Set集合
        System.out.println(allId);

        ZoneId zoneId = ZoneId.systemDefault();  //获取当前系统默认时区
        System.out.println(zoneId);

        ZoneId zoneId1 = ZoneId.of("Asia/Taipei");   //获取指定时区
        System.out.println(zoneId1);
    }
}
