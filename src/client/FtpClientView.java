package client;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;


/**
 * FTP客户端操作界面
 * 向FTP服务器发送请求
 * 接收FTP服务器响应的信息
 */
public class FtpClientView {
    private JPanel panel1;
    private JLabel linkLabel;
    private JLabel usernameLabel;
    private JTextField IP;
    private JTextField username;
    private JTextField Port;
    private JPasswordField passwd;
    private JButton link;
    private JButton login;
    private JTextArea respInfo;
    private JList<String> fileInfo;
    private JButton button4;
    private JButton store;
    private JButton retr;
    private JButton cwd;
    private JButton quit;
    private JButton list;
    private JButton port;
    private JLabel IPLabel;
    private JLabel PortLabel;
    private JLabel passwdLabel;
    private JLabel isLinked;
    private JLabel isLogin;
    private JLabel fileInfoLabel;
    private JLabel label;
    private JLabel label2;
    private JButton refresh;
    private JTextField dirName;
    private JButton dele;
    private FtpClient client = null;
    private String selectedFile = null;
    private String fLable;

    // 构造函数，添加按钮的监听事件
    public FtpClientView() {
        // 上传文件
        store.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean ok;
                if(isLinked.getText().equals("未连接")){
                    JOptionPane.showMessageDialog(null, "未连接FTP服务器！");
                    return;
                }
                if(isLogin.getText().equals("未登录")){
                    JOptionPane.showMessageDialog(null, "未登录！");
                    return;
                }
                // 选择要上传的文件
                JFileChooser fileChooser = new JFileChooser();  // 文件选择窗口
                fileChooser.setCurrentDirectory(FileSystemView.getFileSystemView().getHomeDirectory()); // 显示当前文件目录为桌面
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);  // 只选取文件
                fileChooser.showDialog(null, null); // 显示窗口
                File file = fileChooser.getSelectedFile();  // 获取选取的文件
                if(file == null) return;
                // 开始上传文件
                ok = CommandReq.storeReq(file, client.getReader(), client.getWriter(), respInfo);
                if(ok == true){
                    JOptionPane.showMessageDialog(null, "文件上传成功！");
                }else{
                    JOptionPane.showMessageDialog(null, "文件上传失败！");
                }
            }
        });
        // 下载文件，下载从文件信息列表框中显示的文件
        retr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isLinked.getText().equals("未连接")){
                    JOptionPane.showMessageDialog(null, "未连接FTP服务器！");
                    return;
                }
                if(isLogin.getText().equals("未登录")){
                    JOptionPane.showMessageDialog(null, "未登录！");
                    return;
                }
                if(selectedFile == null){
                    JOptionPane.showMessageDialog(null, "请选择要下载的文件！");
                    return;
                }else if(fLable.equals("d")){
                    JOptionPane.showMessageDialog(null, "不能选择文件夹，请选择合适的文件！");
                    return;
                }
                // 向服务器发送下载指定文件的请求
                boolean ok;
                ok = CommandReq.retrReq(selectedFile, client.getReader(), client.getWriter(), client.getUserDir(), respInfo);
                if(ok == true){
                    JOptionPane.showMessageDialog(null, "文件下载完成！");
                }else{
                    JOptionPane.showMessageDialog(null, "文件下载失败！");
                }
            }
        });
        // 修改文件目录
        cwd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isLinked.getText().equals("未连接")){
                    JOptionPane.showMessageDialog(null, "未连接FTP服务器！");
                    return;
                }
                if(isLogin.getText().equals("未登录")){
                    JOptionPane.showMessageDialog(null, "未登录！");
                    return;
                }
                if(dirName.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "目录名不能为空！");
                    return;
                }
                // 向服务器发送切换工作目录请求
                boolean ok;
                ok = CommandReq.cwdReq(dirName.getText(), client.getReader(), client.getWriter(), respInfo);
                if(ok == true){
                    JOptionPane.showMessageDialog(null, "成功切换到工作目录: " + dirName.getText());
                }else{
                    JOptionPane.showMessageDialog(null, "指定的目录不存在，切换失败！");
                }
            }
        });
        // 正常退出
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isLinked.getText().equals("未连接")){
                    JOptionPane.showMessageDialog(null, "未连接FTP服务器！");
                    return;
                }
                if(isLogin.getText().equals("未登录")){
                    JOptionPane.showMessageDialog(null, "未登录！");
                    return;
                }
                boolean ok;
                ok = CommandReq.quitReq(client.getReader(), client.getWriter(), respInfo);
                if(ok == true){
                    try {
                        client.getSocket().close(); // 关闭连接

                        // 清空信息
                        fileInfo.setListData(new String[0]);
                        respInfo.setText("");
                        username.setText("");
                        passwd.setText("");
                        IP.setText("");
                        Port.setText("");
                        selectedFile = null;
                        // 重新设置参数
                        isLinked.setText("未连接");
                        link.setEnabled(true);
                        isLogin.setText("未登录");
                        login.setEnabled(true);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        // 显示文件目录信息
        list.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message;
                String[] datas;
                if(isLinked.getText().equals("未连接")){
                    JOptionPane.showMessageDialog(null, "未连接FTP服务器！");
                    return;
                }
                if(isLogin.getText().equals("未登录")){
                    JOptionPane.showMessageDialog(null, "未登录！");
                    return;
                }
                // 向服务器发送命令
                message = CommandReq.listReq(list.getText(), client.getReader(), client.getWriter(), respInfo);
                if(message.equals("error")){   // 文件夹不存在
                    JOptionPane.showMessageDialog(null, "指定文件夹不存在！");
                    return;
                }else{  // 显示文件目录信息
                    datas = message.split("\n");
                    fileInfo.setListData(datas);
                }
            }
        });
        // 刷新界面
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileInfo.setListData(new String[0]);  // 清空信息
                respInfo.setText("");
                selectedFile = null;
            }
        });
        // 连接FTP服务器
        link.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean ok;
                try {
                    if(IP.getText().equals("")){
                        JOptionPane.showMessageDialog(null, "IP地址不能为空！");
                        return;
                    }
                    if(Port.getText().equals("")) Port.setText("21"); // 默认21号端口
                    if(client == null) client = new FtpClient();   // 建立客户端
                    ok = client.link(IP.getText(), Integer.parseInt(Port.getText()), respInfo); // 建立连接
                    // 连接失败
                    if(ok == false){
                        JOptionPane.showMessageDialog(null, "refused 地址错误！");
                        return;
                    }
                    // 连接成功，参数设置
                    isLinked.setText("已连接");
                    link.setEnabled(false);
                } catch (IOException e1) { // 连接失败
                    e1.printStackTrace();
                }
            }
        });
        // 登录FTP服务器
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean ok;
                if(isLinked.getText().equals("已连接")){
                    if(username.getText().equals("")){
                        JOptionPane.showMessageDialog(null, "用户名不能为空！");
                        return;
                    }
                    // 进行用户名验证
                    ok = CommandReq.userReq(username.getText(), client.getReader(), client.getWriter(), respInfo);
                    if(ok == true){ // 用户名验证成功
                        if(passwd.getText().equals("")){
                            JOptionPane.showMessageDialog(null, "密码不能为空！");
                            return;
                        }
                        // 进行密码验证
                        ok = CommandReq.passReq(passwd.getText(), client.getReader(), client.getWriter(), respInfo);
                        if(ok == true){ // 验证密码成功，登录成功
                            isLogin.setText("已登录");
                            login.setEnabled(false);
                        }else{  // 密码错误
                            JOptionPane.showMessageDialog(null, "密码错误！");
                            return;
                        }

                    }else{  // 用户名验证失败
                        JOptionPane.showMessageDialog(null, "用户名不存在！");
                        return;
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "未连接FTP服务器！");
                    return;
                }
            }
        });
        // 文件信息显示框，获取被选取的文件信息，用于下载文件
        fileInfo.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String message;
                String[] datas;
                message = fileInfo.getSelectedValue();
                if(message != null){
                    datas = message.split("\t"); // 获取被选取的文件名
                    selectedFile = datas[0];    // 获取文件名
                    fLable = datas[2];      // 获取文件类别
                }
            }
        });
        // 删除指定的文件
        dele.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isLinked.getText().equals("未连接")){
                    JOptionPane.showMessageDialog(null, "未连接FTP服务器！");
                    return;
                }
                if(isLogin.getText().equals("未登录")){
                    JOptionPane.showMessageDialog(null, "未登录！");
                    return;
                }
                if(selectedFile == null){
                    JOptionPane.showMessageDialog(null, "请选择要删除的文件！");
                    return;
                }else if(fLable.equals("d")){
                    JOptionPane.showMessageDialog(null, "不能选择文件夹，请选择合适的文件！");
                    return;
                }
                // 向服务器发送删除指定文件的请求
                boolean ok;
                ok = CommandReq.deleReq(selectedFile, client.getReader(), client.getWriter(), respInfo);
                if(ok == true){
                    JOptionPane.showMessageDialog(null, "文件删除完成！");
                }else{
                    JOptionPane.showMessageDialog(null, "文件删除失败！");
                }
            }
        });
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("FTP客户端");
        frame.setContentPane(new FtpClientView().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        frame.setSize(1000, 600);   // 指定界面的大小
        frame.setLocation(400, 200);       // 指定界面的位置
        frame.setResizable(false);              // 界面大小不可修改
        frame.setVisible(true);                 // 显示界面
    }
}
