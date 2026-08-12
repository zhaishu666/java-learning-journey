package week9.day03;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

public class A02_UpLoadTask implements Runnable {
    Socket socket;

    public A02_UpLoadTask(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try(BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("src/Serverdir/" + UUID.randomUUID().toString().replace("-", "") + ".txt"));
        BufferedOutputStream resp = new BufferedOutputStream(socket.getOutputStream()))
         {
            int len;
            byte[] buffer = new byte[1024];
            while ((len = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            String str = "收到数据";
            resp.write(str.getBytes());
            resp.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
