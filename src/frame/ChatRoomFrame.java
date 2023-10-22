package frame;

import javax.swing.*;

import utils.PrintAndListen;
import utils.Time;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;


public class ChatRoomFrame extends JFrame implements MouseListener{
    private String username;
    private File imgDir = new File("src/img/chatroom/avatar");
    private File[] imgs;
    private File myImage;
    private int ImageIndex;
    private JTextArea text;
    private JButton send = new JButton();
    private Socket s;
    private JTextArea msgText;

    

    public String getUsername() {
        return username;
    }

    public File getImgDir() {
        return imgDir;
    }

    public File[] getImgs() {
        return imgs;
    }

    public File getMyImage() {
        return myImage;
    }

    public int getImageIndex() {
        return ImageIndex;
    }

    public JTextArea getText() {
        return text;
    }

    public JButton getSend() {
        return send;
    }

    public Socket getS() {
        return s;
    }

    public JTextArea getMsgText() {
        return msgText;
    }

    public ChatRoomFrame(String username) throws UnknownHostException, IOException{
        this.username = username;
        //加载图片
        loadImages();
        //初始化界面
        initFrame();
        //设置界面上的组件
        initView();
        //可视化
        this.setVisible(true);
        //连接服务器
        Connect();
        //监听
        PrintAndListen l = new PrintAndListen(this);
        l.start();
    }

    private void Connect() throws UnknownHostException, IOException{
        //连接服务器
        s = new Socket("127.0.0.1", 10000);
        //发送一个消息用以接收用户名
        OutputStream os = s.getOutputStream();
        //传入用户名
        os.write(username.getBytes());
        os.write("\r".getBytes()); //换行符
        
    }

    private void initView() {

        //设置pane背景
        JLabel bg = new JLabel(new ImageIcon("src/img/chatroom/chat_bg.jpg"));
        bg.setBounds(0, 0, 150, 650);

        //设置panel
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, 150, 650);

        //添加自己的信息和图片
        Random r = new Random();
        int index = r.nextInt(imgs.length);
        myImage = imgs[index];
        ImageIndex = index;
        addImage(panel,myImage,0,username);
        


        //设置背景
        panel.add(bg);

        //设置滚条
        JScrollPane spane = new JScrollPane(panel);
        spane.setBounds(0, 0, 150, 650);
        spane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.getContentPane().add(spane);

        //设置聊天室文本框
        text = new JTextArea(10, 15);
        JScrollPane textPane = new JScrollPane(text);
        textPane.setBounds(150, 300, 530, 260);
        textPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.getContentPane().add(textPane);

        //设置消息接收框
        msgText = new JTextArea(10, 15);
        msgText.setBounds(0, 0, 530, 298);
        //JLabel mbg = new JLabel(new ImageIcon("src/img/chatroom/chat_bg.jpg"));
        //mbg.setBounds(0,0,530,298);
        //msgText.add(mbg);
        msgText.setBackground(Color.CYAN);
        JScrollPane mspane = new JScrollPane(msgText);
        mspane.setBounds(150, 0, 530, 298);
        mspane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.getContentPane().add(mspane);

        //设置按钮
        send.setBounds(375, 565, 91, 41);
        send.setIcon(new ImageIcon("src/img/chatroom/发送按钮.png"));
        send.addMouseListener(this);
        this.getContentPane().add(send);

        //设置背景
        JLabel background = new JLabel(new ImageIcon("src/img/chatroom/chat_bg.jpg"));
        background.setBounds(0, 0, 688, 650);
        this.getContentPane().add(background);

    }

    private void initFrame() {
        this.setSize(688, 650);//设置宽高
        this.setTitle("Chatroom");//设置标题
        this.setDefaultCloseOperation(3);//设置关闭模式
        this.setLocationRelativeTo(null);//居中
        this.setAlwaysOnTop(false);//置顶
        this.setLayout(null);//取消内部默认布局
    }

    //加载所有头像图片
    private void loadImages(){
        imgs = imgDir.listFiles();
    }
    //随机获取头像图片
    private void addImage(JPanel pane,File img, int height, String name){
        JLabel head = new JLabel(new ImageIcon(img.getAbsolutePath()));
        head.setBounds(0, height, 42, height+42);
        JLabel username = new JLabel(name);
        username.setBounds(42, height, 108, height+42);
        pane.add(head);
        pane.add(username);
    }
    


    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == send){
            try{
                //获取输出流
                OutputStream os = s.getOutputStream();
                //获取文本
                String content = text.getText();
                if(content.length() == 0) return;
                //System.out.println(content);
                byte[] buffer = content.getBytes();

                //传入用户名
                os.write(username.getBytes());
                os.write("\t".getBytes());
                //传入内容
                os.write(buffer);
                os.write("\r".getBytes());
                //清空
                text.setText("");
                //展示消息
                String time = Time.getDate();
                msgText.append(time+" 我："+content+"\n");
            }catch(IOException e1){
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }


}