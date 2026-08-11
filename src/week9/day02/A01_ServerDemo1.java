package week9.day02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class A01_ServerDemo1 {
    public static void main(String[] args) throws IOException {

        //创建SeverSocket接收数据
        ServerSocket serverSocket = new ServerSocket(10086);

        //监听客户端
        Socket acc = serverSocket.accept();

        BufferedReader br = new BufferedReader(new InputStreamReader(acc.getInputStream()));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        acc.close();
    }
}
