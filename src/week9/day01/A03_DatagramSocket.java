package week9.day01;

import java.io.IOException;
import java.net.*;

public class A03_DatagramSocket {
    public static void main(String[] args) throws IOException {

        //创建发送出口
        //无参:系统从所有可用端口种随机选择一个进行使用
        DatagramSocket socket = new DatagramSocket();

        String str = "你好!";
        byte[] buf = str.getBytes();
        InetAddress address = InetAddress.getByName("127.0.0.1");
        int port = 10086;

        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);  //将前面的要发送的内容打包起来

        socket.send(packet);  //通过创建的发送出口将包裹发出

        socket.close(); //释放资源
    }
}
