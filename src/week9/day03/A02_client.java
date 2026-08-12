package week9.day03;

import java.io.*;
import java.net.Socket;

public class A02_client {
    public static void main(String[] args) throws IOException {

        //注意点: 关闭这里的包装流并不会关闭socket本身
        Socket socket = new Socket("127.0.0.1", 10086);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream("student.txt"));
        BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
        BufferedReader resp = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        try {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.flush();
            socket.shutdownOutput();

           String line;
           while ((line = resp.readLine()) != null) {
               System.out.println(line);
           }

        }finally {
            bis.close();
            socket.close();
        }
    }
}
