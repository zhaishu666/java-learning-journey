package week9.day02;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class A03_PracticeServer1 {
    public static void main(String[] args) throws IOException {

        ServerSocket ss = new ServerSocket(10086);

        Socket acc = ss.accept();
        InputStream is = acc.getInputStream();
        OutputStream os = acc.getOutputStream();
        int len;
        byte[] buffer = new byte[8192];
        while ((len = is.read(buffer)) != -1) {
            System.out.println("用户上传" + new String(buffer, 0, len));
        }
        byte[] resp = {'o','k'};
        os.write(resp);
        os.flush();

        os.close();
        is.close();
        ss.close();
    }
}
