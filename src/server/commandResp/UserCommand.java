package server.commandResp;

import server.FtpServer.ClientHandler;

import java.io.*;


/**
 * 检验是否有这个用户名存在
 */
public class UserCommand implements Command{

    public void getResult(String data, Writer writer, ClientHandler t) {
        System.out.println("username : " + data + "\n");

        String response = "";
        boolean result;

        // 判断用户名是否存在
        result = isUserNameExit(data, t);


        if(result == false) {  // 用户名不存在
            response = "501 user is not validate\r\n";
        }
        else {  // 用户名存在
            response = "331 please input your password\r\n";
        }
        try {   // 响应客户端
            writer.write(response);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断用户是否存在
     * 若存在，保存用户名和对应的密码
     * 保存的密码用于响应PASS命令
     */
    public boolean isUserNameExit(String username, ClientHandler t){
        File file = new File(t.getUserInfoFile()); // 用户信息文件
        String line = null;
        String[] content = null;

        // 判断用户名是否存在
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while((line = reader.readLine()) != null){
                content = line.split(" ");
                if(content[0].equals(username)){    // 用户名存在
                    t.setClientName(content[0]);    // 保存用户名
                    t.setClientPasswd(content[1]);  // 保存密码
                    reader.close();         // 关闭连接
                    return true;          // 返回用户名对应的密码
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;  // 用户不存在
    }

}