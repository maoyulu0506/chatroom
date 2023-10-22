import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import utils.ServerHandler;

public class Server{
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(10000);
        //用来保存连到这个服务器上面的所有的用户的socket
        HashMap<String,Socket> clients = new HashMap<>();
        while(true){
            //监听用户连接
            //监听到了，返回一个socket，并用它来与用户端进行传输数据
            Socket s = ss.accept();
            //继承Thread父类，重写了run方法
            //多线程--每一个连接用户给他开一个线程
            ServerHandler sh = new ServerHandler(s, clients);
            //启动线程
            sh.start();
        }


        //ss.close();
    }
}