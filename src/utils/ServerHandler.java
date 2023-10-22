package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

public class ServerHandler extends Thread{
    private Socket s;
    private String username;
    private HashMap<String,Socket> clients; //连接到服务器里的所有的用户
    public ServerHandler(Socket s, HashMap<String,Socket> clients) throws IOException{
        this.s = s;
        this.clients = clients;
        //处理用户名
        //获得输入流
        InputStream is = s.getInputStream();
        //转换流 ---输入流 字节形式进 转换一下 就可以以字符的形式读取了
        //utf-8编码 一个英文字母是1个字符 一个中文是3个字符
        InputStreamReader isr = new InputStreamReader(is);
        //在内存里开辟了一块空间缓冲区，每次都会把缓冲区都填满
        //读数据的时候，从缓冲区里边读
        BufferedReader br = new BufferedReader(isr);
        username = br.readLine();
        //将新连接的用户存入到集合中
        clients.put(username, s);
        //将用户名转发给连上服务器的其他用户
        //#表示第一次传入
        String msg = "#"+username;
        sendMessage(msg);
        //将当前在聊天室里的用户传回
        OutputStream os = s.getOutputStream();
        os.write(clients.keySet().toString().getBytes());
        os.write("\r".getBytes());
    }
    @Override
    public void run(){
        
        try {
            //循环读取数据
            InputStream is = s.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while((line = br.readLine()) != null){
                //接收到了一个用户的消息
                //将消息转发给其他的用户
                sendMessage(line);
                if(line.split("\t")[1].equals("886")){
                    break;
                }
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            try {
                //连接断开，从服务器中移除.
                clients.remove(username);
                s.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
    }
    
    
    private void sendMessage(String msg) throws IOException{
        Set<String> keys = clients.keySet();
        for (String key : keys) {
            if(!key.equals(username)){
                Socket ss = clients.get(key);
                OutputStream os = ss.getOutputStream();
                os.write(msg.getBytes());
                os.write("\r".getBytes());
            }
        }
    }
}