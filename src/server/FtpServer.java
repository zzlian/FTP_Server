package server;

import commands.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * FTP服务器
 * 用于处理用户的请求
 */
public class FtpServer {

    private int port = 21;  // 默认端口21
    ServerSocket serverSocket;

    public static void main(String args[]) throws IOException {
        FtpServer ftpServer = new FtpServer();
        ftpServer.go(); // 启动服务器
    }

    // 构造函数初始化FTP服务器
    public FtpServer() throws IOException {
        this.serverSocket = new ServerSocket(this.port);
    }

    // 监听客户端的请求
    public void go() throws IOException {
        Socket socket = null;
        while (true) {
            socket = serverSocket.accept(); // 接收客户端的请求
            Thread t = new Thread(new ClientHandler(socket)); // 建立分线程处理客户端请求
            t.start(); // 启动线程
        }
    }


    // 根据客户端的命令，生成对应的命令操作对象
    public static Command buildCommand(String c){
        c = c.toUpperCase();    // 统一大写
        switch(c)
        {
            case "USER": return new UserCommand();   // 用户名验证
            case "PASS": return new PassCommand();   // 密码验证
            case "LIST": return new ListCommand();    // 显示目录
            case "PORT": return new PortCommand();   //
            case "QUIT": return new QuitCommand();   // 正常退出
            case "RETR": return new RetrCommand();   // 下载文件
            case "CWD": return new CwdCommand();     // 修改文件目录
            case "STOR": return new StorCommand();  // 上传文件
            default : return null;  // 命令不存在
        }
    }



    /**
     * 建立线程管理客户端的请求
     */
    public class ClientHandler implements Runnable {

        private String nowDir = "D:\\Program Files (x86)\\java_work\\FTP_Server\\src\\rootDir";
        private String userInfoFile = "D:\\Program Files (x86)\\java_work\\FTP_Server\\src\\rootDir\\userInfo.txt";
        private BufferedReader reader;
        private BufferedWriter writer;
        private Socket socket;
        private String clientName;
        private String clientPasswd;
        private boolean isLogin = false;
        private String dataIP;
        private String dataPort;

        // 构造方法
        public ClientHandler(Socket socket) throws IOException {
            String[] datas;
            datas = (socket.getRemoteSocketAddress().toString()).split("/");
            datas = datas[1].split(":");
            this.dataIP = datas[0];     // 客户端的IP地址
            this.dataPort = datas[1];   // 客户端的端口

            this.socket = socket;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        }

        public void setClientName(String username){
            this.clientName = username;
        }

        public String getClientName(){
            return this.clientName;
        }

        public void setClientPasswd(String passwd){
            this.clientPasswd = passwd;
        }

        public String getClientPasswd(){
            return this.clientPasswd;
        }

        public void setIsLogin(boolean isLogin){
            this.isLogin = isLogin;
        }

        public Socket getSocket(){
            return this.socket;
        }

        public String getUserInfoFile(){
            return this.userInfoFile;
        }

        public void setUserInfoFile(String userInfoFile){
            this.userInfoFile = userInfoFile;
        }

        public void setNowDir(String nowDir){
            this.nowDir = nowDir;
        }

        public String getNowDir(){
            return this.nowDir;
        }

        public void setDataIP(String dataIP){
            this.dataIP = dataIP;
        }

        public String getDataIP(){
            return this.dataIP;
        }

        public void setDataPort(String dataPort){
            this.dataPort = dataPort;
        }

        public String getDataPort(){
            return this.dataPort;
        }


        // 接收客户端的命令请求
        public void run() {
            String message;
            String[] content = null;
            boolean status = false;

            try {   // 返回连接成功信息
                writer.write("220 hello");
                writer.write("\r\n");
                writer.flush();

                // 处理用户发送的命令
                while (!socket.isClosed()) {
                    message = reader.readLine();  // 获取用户发送的命令
                    content = message.split(" ");

                    System.out.println("client command : " + content[0]);

                    // 构建对应的命令操作对象
                    Command command = buildCommand(content[0]);

                    // 判断是否有执行该命令的权限
                    if(isValidated(command) == false){
                        writer.write("532 执行该命令需要登录，请登录后再执行相应的操作\r\n");
                        writer.flush();
                        continue;
                    }
                    if(command == null){    // 命令不存在
                        writer.write("502  该命令不存在，请重新输入");
                        writer.write("\r\n");
                        writer.flush();
                    }
                    else{   // 执行相应的命令
                        String data = "";
                        if(content.length >=2) {
                            data = content[1];
                        }
                        command.getResult(data, writer,this); // 执行命令
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 判断客户端是否能执行该命令
        public  boolean isValidated(Command command) {
            if(command instanceof UserCommand || command instanceof PassCommand) {
                return true;    // 执行登录验证
            }
            else{
                return this.isLogin;
            }
        }
    }
}