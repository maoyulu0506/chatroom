package frame;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.sql.*;

import javax.swing.*;


public class RegisterFrame extends JFrame implements MouseListener{
    //private ArrayList<User> users;
    //private File file = new File("src/frame/user.txt");
    //注册按钮
    private JButton submit = new JButton();
    //重置按钮
    private JButton reset = new JButton();
    //用户名输入框
    private JTextField username = new JTextField();
    //密码输入框
    private JPasswordField password = new JPasswordField();
    //二次密码输入框
    private JPasswordField rePassword = new JPasswordField();
    //数据库连接
    private Connection conn;

    
    public RegisterFrame(Connection conn) {
        this.conn = conn;
        //初始化界面
        initFrame();
        //在界面上添加内容
        initView();
        //设置可视化
        this.setVisible(true);
    }

    private void initView() {
        //添加注册用户名的文本
        JLabel usernameText = new JLabel(new ImageIcon("src/img/register/注册用户名.png"));
        usernameText.setBounds(85, 135, 80, 20);

        //添加注册用户名的输入框
        username.setBounds(195, 134, 200, 30);

        //添加注册密码的文本
        JLabel passwordText = new JLabel(new ImageIcon("src/img/register/注册密码.png"));
        passwordText.setBounds(97, 193, 70, 20);

        //添加密码输入框
        password.setBounds(195, 195, 200, 30);

        //添加再次输入密码的文本
        JLabel rePasswordText = new JLabel(new ImageIcon("src/img/register/再次输入密码.png"));
        rePasswordText.setBounds(64, 255, 95, 20);

        //添加再次输入密码的输入框
        rePassword.setBounds(195, 255, 200, 30);

        //注册的按钮
        submit.setIcon(new ImageIcon("src/img/register/注册按钮.png"));
        submit.setBounds(123, 310, 128, 47);
        submit.setBorderPainted(false);
        submit.setContentAreaFilled(false);
        submit.addMouseListener(this);

        //重置的按钮
        reset.setIcon(new ImageIcon("src/img/register/重置按钮.png"));
        reset.setBounds(256, 310, 128, 47);
        reset.setBorderPainted(false);
        reset.setContentAreaFilled(false);
        reset.addMouseListener(this);

        //背景图片
        JLabel background = new JLabel(new ImageIcon("src/img/register/register_bg.jpg"));
        background.setBounds(0, 0, 470, 390);

        this.getContentPane().add(usernameText);
        this.getContentPane().add(passwordText);
        this.getContentPane().add(rePasswordText);
        this.getContentPane().add(username);
        this.getContentPane().add(password);
        this.getContentPane().add(rePassword);
        this.getContentPane().add(submit);
        this.getContentPane().add(reset);
        this.getContentPane().add(background);
    }

    private void initFrame() {
        this.setSize(488, 430);//设置宽高
        this.setTitle("Chatroom");//设置标题
        this.setDefaultCloseOperation(3);//设置关闭模式
        this.setLocationRelativeTo(null);//居中
        this.setAlwaysOnTop(false);//置顶
        this.setLayout(null);//取消内部默认布局
    }

    //使用文本存储用户信息
    //private void saveUser(User user) throws IOException{
    //    PrintWriter pw = new PrintWriter(new FileWriter(file,true));
    //    pw.println(user);
    //    pw.close();
    //}

    //使用数据库存储用户信息
    private int saveUser(String username, String passward) throws SQLException{
        String query = "INSERT INTO Users VALUE(null,'"+username+"','"+passward+"')";
        Statement stm = conn.createStatement();
        int flag = stm.executeUpdate(query);
        return flag;
    }

    //private boolean contains(String username){
    //    for (User u : users) {
    //        if(u.getUsername().equals(username)){
    //            return true;
    //        }
    //    }
    //    return false;
    //}

    //通过数据库查询用户名是否存在
    boolean contains(String username) throws SQLException{
        String query = "SELECT id FROM Users WHERE username = '" + username+"'";
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery(query);
        return rs.next();
    }

    public void showDialog(String content) {
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

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        if(e.getSource() == submit){
            //点击了注册按钮
            //1.用户名，密码不能为空
            if(username.getText().length() == 0 || password.getText().length() == 0 || rePassword.getText().length() == 0){
                showDialog("用户名和密码不能为空");
                return;
            }
            //2.判断两次密码输入是否一致
            if(!password.getText().equals(rePassword.getText())){
                showDialog("两次密码输入不一致");
                return;
            }
            //3.判断用户名和密码的格式是否正确
            if(!username.getText().matches("[a-zA-Z0-9]{4,16}")){
                showDialog("用户名不符合规则"); 
                return;
            }
            if(!password.getText().matches("\\S*(?=\\S{6,})(?=\\S*\\d)(?=\\S*[a-z])\\S*")){
                showDialog("密码不符合规则，至少包含1个小写字母，1个数字，长度至少6位");
                return;
            }
            //4.判断用户名是否已经重复
            try {
                if(contains(username.getText())){
                    showDialog("用户名已经存在，请重新输入");
                    return;
                }
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            //5.添加用户
            try {
                //将输入的用户名和密码按格式保存到文件里边。
                //参数：User对象
                //saveUser(new User(username.getText(), password.getText()));
                //保存到数据库中
                int flag = saveUser(username.getText(), password.getText());
                if(flag == 0){
                    showDialog("用户保存失败");
                    return;
                }
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            //7.提示注册成功
            showDialog("注册成功");
            //关闭注册界面，打开登录界面
            this.setVisible(false);
            //释放资源
            try {
                conn.close();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try {
                new LoginFrame();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }else if(e.getSource() == reset){
            username.setText("");
            password.setText("");
            rePassword.setText("");
        }
    }
    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        if(e.getSource() == submit){
            submit.setIcon(new ImageIcon("src/img/register/注册按下.png"));
        }else if(e.getSource() == reset){
            reset.setIcon(new ImageIcon("src/img/register/重置按下.png"));
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        if(e.getSource() == submit){
            submit.setIcon(new ImageIcon("src/img/register/注册按钮.png"));
        }else if(e.getSource() == reset){
            reset.setIcon(new ImageIcon("src/img/register/重置按钮.png"));
        }
    }
    @Override
    public void mouseEntered(MouseEvent e) {
    }
    @Override
    public void mouseExited(MouseEvent e) {
    }
}