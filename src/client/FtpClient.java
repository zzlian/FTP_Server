package client;


import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class FtpClient {

    private String userDir = "D:\\Program Files (x86)\\java_work\\FTP_Server\\src\\userDir"; // 默认下载文件路径
    private PrintWriter writer;   // 打印输出流
    private Socket socket;
    private BufferedReader reader;


    public BufferedReader getReader() {
        return reader;
    }

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public void setWriter(PrintWriter writer) {
        this.writer = writer;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getUserDir() {
        return userDir;
    }

    public void setUserDir(String userDir) {
        this.userDir = userDir;
    }


    // 建立连接
    public boolean link(String IP, int PORT, JTextArea respInfo) throws IOException {
        String message;
        try {
            this.socket = new Socket(IP, PORT);// 建立连接
            System.out.println("连接成功");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());

            message = reader.readLine();
            respInfo.append(message + "\n");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}