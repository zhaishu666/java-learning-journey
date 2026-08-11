package week9.day01;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class A02_InetAddress {
    static void main() throws UnknownHostException {

        InetAddress name = InetAddress.getByName("DESKTOP-C02F7O4");  //通过InetAddress的静态方法根据IP,域名字符串获得地址对象
        System.out.println(name);

        String hostName = name.getHostName();
        System.out.println(hostName);

        String hostAddress = name.getHostAddress();
        System.out.println(hostAddress);
    }
}
