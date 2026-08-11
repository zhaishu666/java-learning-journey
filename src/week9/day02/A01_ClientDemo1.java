package week9.day02;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class A01_ClientDemo1 {
    static void main() throws IOException {
        //通过TCP创建客户端

        Socket socket = new Socket("127.0.0.1",10086);

        //获得输出流
        OutputStream ops = socket.getOutputStream();
        //向流中写入数据
        ops.write("Hello World, 你好世界".getBytes());

        ops.close();
        socket.close();
    }
}
