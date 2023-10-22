package frame;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import javax.swing.*;


import utils.CodeUtil;

public class LoginFrame extends JFrame implements MouseListener{
    //private ArrayList<User> users = new ArrayList<>();
    //File f = new File("./src/frame/user.txt");
    JButton login = new JButton();
    JButton register = new JButton();
    JTextField username = new JTextField();
    JPasswordField passward = new JPasswordField();
    JTextField code = new JTextField();

    //数据库
    static final String DB_URL = "jdbc:mysql://localhost:3306/CHATROOM";
    static final String USER = "root";
    static final String PASS = "chatroom";
    private Connection conn;

    //正确的校验码
    JLabel rightCode = new JLabel();

    public LoginFrame() throws IOException, SQLException{
        //加载用户，用txt保存用户数据
        //loadUsers(f);


        //初始化界面
        initJFrame();
        //在这个界面中添加内容
        initView();
        //连接数据库
        ConnectDB();
        //显示界面
        this.setVisible(true);
    }

    //连接数据库
    private void ConnectDB() throws SQLException{
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
    }

    //在界面中添加内容
    private void initView() {
        //添加用户名
        JLabel usernameText = new JLabel(new ImageIcon("./src/img/login/用户名.png"));
        usernameText.setBounds(116, 135, 47, 17);
        this.getContentPane().add(usernameText);

        //添加用户名输入框
        username.setBounds(195, 134, 200, 30);
        this.getContentPane().add(username);

        //添加密码文字
        JLabel passwordText = new JLabel(new ImageIcon("./src/img/login/密码.png"));
        passwordText.setBounds(130, 195, 32, 16);
        this.getContentPane().add(passwordText);

        //密码输入框
        passward.setBounds(195, 195, 200, 30);
        this.getContentPane().add(passward);

        //验证码
        JLabel codeText = new JLabel(new ImageIcon("./src/img/login/验证码.png"));
        codeText.setBounds(133, 256, 50, 30);
        this.getContentPane().add(codeText);

        //验证码输入框
        code.setBounds(195, 256, 100, 30);
        code.addMouseListener(this);
        this.getContentPane().add(code);

        String codeStr = CodeUtil.getCode();
        //设置内容
        rightCode.setText(codeStr);
        //绑定鼠标事件
        rightCode.addMouseListener(this);
        //位置和宽高
        rightCode.setBounds(300, 256, 50, 30);
        //添加到界面
        this.getContentPane().add(rightCode);

        //添加登陆按钮
        login.setBounds(123, 310, 128, 47);
        login.setIcon(new ImageIcon("./src/img/login/登录按钮.png"));
        //去除按钮的边框
        login.setBorderPainted(false);
        //去除按钮的背景
        login.setContentAreaFilled(false);
        //给登录按钮绑定鼠标事件
        login.addMouseListener(this);
        this.getContentPane().add(login);

        //添加注册按钮
        register.setBounds(256, 310, 128, 47);
        register.setIcon(new ImageIcon("./src/img/login/注册按钮.png"));
        //去除按钮的边框
        register.setBorderPainted(false);
        //去除按钮的背景
        register.setContentAreaFilled(false);
        //给注册按钮绑定鼠标事件
        register.addMouseListener(this);
        this.getContentPane().add(register);

        //添加背景图片
        JLabel background = new JLabel(new ImageIcon("./src/img/login/login_bg.jpg"));
        background.setBounds(0, 0, 470, 390);
        this.getContentPane().add(background);

    }

    //初始化JFrame
    private void initJFrame() {
        this.setSize(488, 430);//设置宽高
        this.setTitle("Chatroom");//设置标题
        this.setDefaultCloseOperation(3);//设置关闭模式
        this.setLocationRelativeTo(null);//居中
        this.setAlwaysOnTop(false);//置顶
        this.setLayout(null);//取消内部默认布局
    }

    //加载用户数据
    //从txt文本中
    //private void loadUsers(File userFile) throws IOException{
    //    BufferedReader br = new BufferedReader(new FileReader(userFile));
    //    String line;
    //    while((line = br.readLine()) != null){
    //        String[] userID = line.split("&");
    //        users.add(new User(userID[0].split("=")[1], userID[1].split("=")[1]));
    //    }

    //    br.close();
    //}

    //展示弹框
    public void showJDialog(String content) {
        //创建一个弹框对象
        JDialog jDialog = new JDialog();
        //给弹框设置大小
        jDialog.setSize(200, 150);
        //让弹框置顶
        jDialog.setAlwaysOnTop(true);
        //让弹框居中
        jDialog.setLocationRelativeTo(null);
        //弹框不关闭永远无法操作下面的界面
        jDialog.setModal(true);

        //创建Jlabel对象管理文字并添加到弹框当中
        JLabel warning = new JLabel(content);
        warning.setBounds(0, 0, 200, 150);
        jDialog.getContentPane().add(warning);

        //让弹框展示出来
        jDialog.setVisible(true);
    }
    

    //private boolean contains(User u){
    //    return users.contains(u);
    //}


    //判断输入的用户名和密码是否在数据库中
    private boolean contains(String username, String passward) throws SQLException{
        String query = "SELECT id FROM Users WHERE username = '"
                        + username + "' and passward = '"+passward+"'";
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery(query);
        return rs.next();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        if(e.getSource() == login){
            //获取输入的用户名和密码
            String userName = username.getText();
            String passWard = passward.getText();
            //获取用户输入的验证码
            String checkCode = code.getText();
            
            //User userinfo = new User(userName, passWard);
            if(checkCode.length() == 0)
                showJDialog("验证码不能为空");
            else if(userName.length() == 0 || passWard.length() == 0){
                showJDialog("用户名或密码不能为空");
            }else if(!CodeUtil.CodeEquals(checkCode, rightCode.getText())){
                showJDialog("验证码输入错误");
            } else
                try {
                    if(!contains(userName,passWard)){
                        showJDialog("用户名或密码输入错误");
                    }else{
                        //将界面设置为隐藏
                        //并释放数据库资源
                        this.setVisible(false);
                        conn.close();
                        try {
                            new ChatRoomFrame(userName);
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
        }
        else if(e.getSource() == register){
            this.setVisible(false);
            new RegisterFrame(conn);

        }
        else if(e.getSource() == rightCode){
            //更改验证码
            String rightcode = CodeUtil.getCode();
            rightCode.setText(rightcode);
        }
    }


    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        if(e.getSource() == login){
            login.setIcon(new ImageIcon("./src/img/login/登录按下.png"));
        }
        else if(e.getSource() == register){
            register.setIcon(new ImageIcon("./src/img/login/注册按下.png"));
        }
    }


    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        if(e.getSource() == login){
            login.setIcon(new ImageIcon("./src/img/login/登录按钮.png"));
        }
        else if(e.getSource() == register){
            register.setIcon(new ImageIcon("./src/img/login/注册按钮.png"));
        }
    }


    @Override
    public void mouseEntered(MouseEvent e) {
    }


    @Override
    public void mouseExited(MouseEvent e) {
    }
}