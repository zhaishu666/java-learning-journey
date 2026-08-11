package week9.day02;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class ChatRoomDemo {
    static void main() throws IOException {
        //通过UDP协议循环录入键盘输入的内容并发送,直到输入886就停止

        DatagramSocket ds = new DatagramSocket();

        Scanner sc = new Scanner(System.in);

        while (true) {
            String str = sc.nextLine();
            byte[] buf = str.getBytes();
            InetAddress address = InetAddress.getByName("127.0.0.1");
            int port = 10086;

            DatagramPacket dp  = new DatagramPacket(buf, buf.length, address, port);

            ds.send(dp);

            if(str.equals("886")){
                break;
            }
        }
        ds.close();
    }
}
