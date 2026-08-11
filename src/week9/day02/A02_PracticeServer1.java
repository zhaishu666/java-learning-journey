package week9.day02;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class A02_PracticeServer1 {
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(10086);

        try (Socket acc = serverSocket.accept();
             BufferedReader in = new BufferedReader(new InputStreamReader(acc.getInputStream(), StandardCharsets.UTF_8));
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(acc.getOutputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("收到: " + line);
                bw.write("已收到数据");
                bw.newLine();
                bw.flush();
                if ("exit".equals(line)) {
                    System.out.println("收到exit,关闭连接");
                    break;
                }
            }
        }
        serverSocket.close();
    }
}
