package week9.day02;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class A03_PracticeClient1 {
    static void main() throws IOException {


        Socket socket = new Socket("127.0.0.1", 10086);
        OutputStream netOut = socket.getOutputStream();

        try (FileInputStream fis = new FileInputStream("C:/Users/翟曙/Desktop/测试.txt")) {

            byte[] buffer = new byte[8192];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                netOut.write(buffer, 0, len);
            }
            netOut.flush();
        }
        socket.shutdownOutput();
        // try‑with‑resources自动关闭 fis、socket；socket关闭时内部输出流自动关闭

        InputStream resp = socket.getInputStream();  //不要把getInputStream()放进 try ()，否则读完之前 socket 就被流 close 关掉
        int len;
        byte[] buffer = new byte[8192];
        while ((len = resp.read(buffer)) != -1) {
            System.out.println("服务器回执: " + new String(buffer, 0, len));
        }
        resp.close();
        socket.close();
    }
}
