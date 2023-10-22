package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;
import java.util.StringJoiner;

import javax.swing.*;

import frame.ChatRoomFrame;

public class PrintAndListen extends Thread{
    private Socket s;
    private ChatRoomFrame frame;


    public PrintAndListen(ChatRoomFrame frame) throws IOException{
        this.frame = frame;
        s = this.frame.getS();
        //接收当前已经在聊天室里的用户
        InputStream is = s.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line = br.readLine();
        //将接收到的数据进行切分，得到用户的数据
        String[] clients = line.substring(1, line.length()-1).split(", ");
        //将聊天室中的用户显示到界面上
        //界面消息的对象
        JTextArea text = frame.getMsgText();
        //表明当前聊天室只有用户一个人--此时不需要显示任何信息。
        if(clients.length == 1);
        else{
            //对字符串进行拼接的
            StringJoiner sj = new StringJoiner(", ", "已进入当前聊天室的用户有: ", "。\n");
            for (String c : clients) {
                if(!c.equals(frame.getUsername())){
                    sj.add(c);
                }
            }
            text.append(sj.toString());
        }
    }
    

    

    @Override
    public void run(){
        //监听从服务器中传来的数据
        try{
            InputStream is = s.getInputStream();
            //转换流
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            JTextArea text = frame.getMsgText();
            
            String time;
            String line;
            while((line = br.readLine()) != null){
                //获得时间
                time = Time.getDate();
                System.out.println(time+line); 

                //如果以#开头，表明新用户进入聊天室
                //需要在聊天窗口提示
                if(line.startsWith("#")){
                    String msg = time+" "+line.substring(1)+"进入了聊天室\n";
                    text.append(msg);
                    continue;
                }
                String[] str = line.split("\t");
                //如果传入的消息是886
                //则需要显示xxx退出聊天室
                if(str[1].equals("886")){
                    String msg = time+" "+str[0]+"退出了聊天室\n";
                    text.append(msg);
                    continue;
                }
                //其余情况
                //将消息显示到界面上即可
                String msg = time+" "+str[0]+"："+str[1]+"\n";
                text.append(msg);
                    
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}