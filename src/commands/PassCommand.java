package commands;

import server.FtpServer.ClientHandler;
import java.io.*;


/**
 * 判断用户输入的密码是否正确
 */
public class PassCommand implements Command{

    public void getResult(String data, Writer writer, ClientHandler t) {
        System.out.println("passwd : " + data);

        String response = null;
        boolean result = false;

        // 判断用户输入的密码是否正确
        result = data.equals(t.getClientPasswd());

        if(result == true) {    // 密码正确
            System.out.println("登录成功");
            t.setIsLogin(true);
            response = "230 User "+t.getClientName()+" logged in";
        }
        else {
            System.out.println("登录失败，密码错误");
            response = "530   密码错误";
        }
        try {
            writer.write(response);
            writer.write("\r\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
