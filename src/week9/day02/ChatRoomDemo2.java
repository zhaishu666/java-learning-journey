package week9.day02;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class ChatRoomDemo2 {
    public static void main(String[] args) throws IOException {
        //接收另一个demo发送的消息

        DatagramSocket ds = new DatagramSocket(10086);

        byte[] bytes = new byte[8192];
        DatagramPacket dp = new DatagramPacket(bytes, bytes.length);

        while (true) {
            ds.receive(dp);

            //解析包里的内容
            byte[] data = dp.getData();
            int len = dp.getLength();
            InetAddress address = dp.getAddress();
            int port = dp.getPort();

            System.out.println(new String(data, 0, len));
            System.out.println("从" + address + "的" + port + "发出");

        }

    }
}
