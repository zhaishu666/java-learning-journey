package week9.day01;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ReceiveMessageDemo {
    public static void main(String[] args) throws IOException {

        //创建接收端
        DatagramSocket socket = new DatagramSocket(10086);

        //接收数据包
        byte[] bytes = new byte[1024];
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length);

        socket.receive(packet);  //阻塞等待接收包

        //解析数据包
        byte[] data = packet.getData();
        int len = packet.getLength();
        InetAddress address = packet.getAddress();
        int port = packet.getPort();

        System.out.println("接收到数据" + new String(data, 0, len));
        System.out.println("该数据是从" + address + "的" + port + "端口发出的");
    }
}
