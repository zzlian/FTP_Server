package client;


import com.sun.security.ntlm.Server;
import server.FtpServer.ClientHandler;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 客户端向FTP服务器发送的请求
 */
public class CommandReq {

    private static String data;   // 保存服务器传送过来的数据
    private static File dataFile = null; // 用于保存需要用于传输的文件
    private static String userDir; // 文件下载路径
    private static String filename; // 下载的文件名

    // 用户名验证请求
    public static boolean userReq(String username, BufferedReader reader, PrintWriter writer, JTextArea respInfo){
        System.out.print("用户名验证：");
        String message;
        String[] datas;
        try {
            // 向服务器发送用户名验证命令
            writer.write("user " + username + "\r\n");
            writer.flush();

            // 接收服务器的响应信息
            message = reader.readLine();
            datas = message.split(" ");
            
            // 显示信息
            respInfo.append(message + "\n");
            
            if(datas[0].equals("331")) {
                System.out.println("验证成功。\n");
                return true;  // 用户名存在
            } else{   // 用户名不存在
                System.out.println("验证失败！\n");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 密码验证请求
    public static boolean passReq(String passwd, BufferedReader reader, PrintWriter writer, JTextArea respInfo){
        System.out.print("密码验证：");
        String message;
        String[] datas;
        try {
            // 向服务器发送密码验证请求
            writer.write("pass " + passwd + "\r\n");
            writer.flush();

            // 接收服务器的响应信息
            message = reader.readLine();
            datas = message.split(" ");

            // 显示信息
            respInfo.append(message + "\n");

            if(datas[0].equals("230")) {
                System.out.println("验证成功。\n");
                return true; // 验证密码成功
            } else{   // 密码不正确
                System.out.println("验证失败！\n");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 目录文件信息显示请求
    public static String listReq(String dir, BufferedReader reader, PrintWriter writer, JTextArea respInfo){
        System.out.print("显示目录文件信息：");
        String message;
        String[] datas;
        try {
            // 启动20号端口的服务器，等待服务器端的连接，用于传输数据
            ServerSocket dataSocket = new ServerSocket(20);
            Thread t = new Thread(new DataSocket(dataSocket, "dir"));
            t.start();

            // 向FTP服务器发送显示文件目录请求
            writer.write("list " + dir + "\r\n");
            writer.flush();

            // 接收响应信息
            message = reader.readLine();
            datas = message.split(" ");

            // 显示信息
            respInfo.append(message + "\n");

            if(datas[0].equals("150")){ // 建立数据连接，接收文件目录信息
                message = reader.readLine();

                // 显示信息
                respInfo.append(message + "\n");

                System.out.println("信息加载完成  断开数据连接。\n");
                dataSocket.close(); // 断开连接
                return data;
            }else{
                System.out.println("目录不存在   数据连接断开！\n");
                dataSocket.close(); // 断开数据连接
                return "error";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "error";
    }

    // 正常退出请求
    public static boolean quitReq(BufferedReader reader, PrintWriter writer, JTextArea respInfo){
        System.out.println("已正常退出\n");
        String message;
        try {
            // 向FTP服务器发送结束请求
            writer.write("quit goodbye\r\n");
            writer.flush();
            // 获取响应信息
            message = reader.readLine();
            // 显示信息
            respInfo.append(message + "\n");
            // 结束数据传输
            writer.close();
            reader.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 从FTP服务器下载文件请求
    public static boolean retrReq(String fname, BufferedReader reader, PrintWriter writer, String dir, JTextArea respInfo){
        System.out.print("正在从FTP服务器下载文件：");
        userDir = dir;
        filename = fname;
        String message;
        String[] datas;
        try {
            // 启动20号端口的服务器，等待服务器端的连接，用于传输数据
            ServerSocket dataSocket = new ServerSocket(20);
            Thread t = new Thread(new DataSocket(dataSocket, "retr"));
            t.start();

            // 向FTP服务器发送显示文件目录请求
            writer.write("retr " + filename + "\r\n");
            writer.flush();

            // 接收响应信息
            message = reader.readLine();
            datas = message.split(" ");

            // 显示信息
            respInfo.append(message + "\n");

            if(datas[0].equals("150")){
                message = reader.readLine();

                // 显示信息
                respInfo.append(message + "\n");

                System.out.println("下载文件已完成   断开数据连接。\n");
                dataSocket.close();
                return true;
            }else{
                dataSocket.close();
                System.out.println("下载文件失败   断开数据连接！\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 修改目录信息请求
    public static boolean cwdReq(String dirName, BufferedReader reader, PrintWriter writer, JTextArea respInfo){
        System.out.print("切换目录中：");
        String message;
        String[] datas;
        try {
            // 向FTP服务器发送显示文件目录请求
            writer.write("cwd " + dirName + "\r\n");
            writer.flush();

            // 获取响应信息
            message = reader.readLine();
            datas = message.split(" ");

            // 显示信息
            respInfo.append(message + "\n");

            if(datas[0].equals("250")){ // 切换成功
                System.out.println("切换成功。\n");
                return true;
            }else{  // 目录不存在，切换失败
                System.out.println("目录不存在，切换失败！\n");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 存储文件请求
    public static boolean storeReq(File file, BufferedReader reader, PrintWriter writer, JTextArea respInfo){
        System.out.print("正在上传文件：");
        dataFile = file;
        String message;
        String[] datas;
        try {
            // 启动20号端口的服务器，等待服务器端的连接，用于传输数据
            ServerSocket dataSocket = new ServerSocket(20);
            Thread t = new Thread(new DataSocket(dataSocket, "store"));
            t.start();

            // 向FTP服务器发送显示文件目录请求
            writer.write("stor " + file.getName() + "\r\n");
            writer.flush();

            // 接收响应信息
            message = reader.readLine();
            datas = message.split(" ");

            // 显示信息
            respInfo.append(message + "\n");

            // 进入数据传输状态
            if(datas[0].equals("150")){
                message = reader.readLine();

                // 显示信息
                respInfo.append(message + "\n");

                System.out.println("文件上传成功。\n");
                dataSocket.close(); // 关闭数据连接
                return true;
            }else{  // 数据传输错误
                System.out.println("文件上传失败！\n");
                dataSocket.close(); // 断开数据连接
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 删除指定文件
    public static boolean deleReq(String fname, BufferedReader reader, PrintWriter writer, JTextArea respInfo){
        System.out.print("删除指定文件：");
        String message;
        String[] datas;
        try {
            // 向FTP服务器发送显示文件目录请求
            writer.write("dele " + fname + "\r\n");
            writer.flush();

            // 接收响应信息
            message = reader.readLine();
            datas = message.split(" ");

            // 显示信息
            respInfo.append(message + "\n");

            if(datas[0].equals("250")){
                System.out.println("文件删除完成。\n");
                return true;
            }else{
                System.out.println("文件删除失败！\n");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 建立线程用于与FTP服务器进行数据的传输
     */
    public static class DataSocket implements Runnable{
        private ServerSocket dataSocket;
        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;
        private String commandType;
        private boolean isCompleted;


        public boolean isCompleted() {
            return this.isCompleted;
        }

        public void setCompleted(boolean completed) {
            this.isCompleted = completed;
        }

        public ServerSocket getDataSocket() {
            return this.dataSocket;
        }

        public void setDataSocket(ServerSocket dataSocket) {
            this.dataSocket = dataSocket;
        }

        // 构造函数
        public DataSocket(ServerSocket dataSocket, String commandType){
            this.dataSocket = dataSocket;
            this.commandType = commandType;
            this.isCompleted = false;
        }

        public void run(){
            try {
                socket = dataSocket.accept();   // 获取数据连接
                // 传输文件目录信息
                if(commandType.equals("dir")){
                    String line;
                    StringBuilder sb = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    // 接收数据
                    while((line = reader.readLine()) != null){
                        sb.append(line);
                        sb.append("\n");
                    }
                    data = sb.toString();
                }
                // 上传文件
                else if(commandType.equals("store")){
                    String line;

                    // 进行数据传输
                    BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
                    byte[] buf = new byte[1024];
                    InputStream is = new FileInputStream(dataFile);
                    while(-1 != is.read(buf)) {
                        bos.write(buf);
                    }
                    bos.flush();    // 传输结束
                    bos.close();
                    is.close();
                }
                // 下载文件
                else if(commandType.equals("retr")){
                    // 在默认文件夹中新建文件
                    File file = new File(userDir + "/" + filename);
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                    // 从FTP服务器获取数据
                    InputStream is = socket.getInputStream();
                    byte[] buf = new byte[1024];
                    int len;
                    while((len = is.read(buf)) != -1) {
                        bos.write(buf);
                    }
                    bos.flush();   // 文件下载完成
                    bos.close();
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
