package week9.day03;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class A02_Server {
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(10086);
        ThreadPoolExecutor pool = new ThreadPoolExecutor(4, 10,
                60, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

        while (true) {
            Socket acc = serverSocket.accept();

            try {
                pool.submit(new A02_UpLoadTask(acc));
            } catch (RejectedExecutionException e) {
                System.out.println("服务器过载,拒绝连接" + acc.getRemoteSocketAddress());
                acc.close();
            }
        }
    }
}
