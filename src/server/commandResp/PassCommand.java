package server.commandResp;

import server.FtpServer.ClientHandler;
import java.io.*;


/**
 * 判断用户输入的密码是否正确
 */
public class PassCommand implements Command{

    public void getResult(String data, Writer writer, ClientHandler t) {
        System.out.println("passwd : " + data + "\n");

        String response = null;
        boolean result = false;
        String userDir;

        // 判断用户输入的密码是否正确
        result = data.equals(t.getClientPasswd());

        if(result == true) {    // 密码正确
            if(!t.getClientName().equals("root")){
                userDir = t.getNowDir() + "\\" + t.getClientName();
                t.setNowDir(userDir);   // 设置当前用户的工作目录
            }
            t.setIsLogin(true);
            response = "230 User "+t.getClientName()+" logged in\r\n";
        }
        else {
            response = "530 passwd error\r\n";
        }
        try {
            writer.write(response);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
