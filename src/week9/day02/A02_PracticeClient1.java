package week9.day02;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class A02_PracticeClient1 {
    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("127.0.0.1", 10086);

        Scanner sc = new Scanner(System.in);


        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),StandardCharsets.UTF_8))) {
            while (true) {
                System.out.println("请输入要发送的内容");
                String line = sc.nextLine();
                bw.write(line);
                bw.newLine();
                bw.flush();

                String resp = br.readLine();
                if(resp == null){
                    System.out.println("连接已断开");
                }
                System.out.println("服务器回执: " + resp);
                if ("exit".equals(line)) {//这里exit放在前面,方式line为null出现空指针
                    break;
                }
            }
        }
        sc.close();
        socket.close();
    }
}
