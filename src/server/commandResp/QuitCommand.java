package server.commandResp;

import server.FtpServer.ClientHandler;

import java.io.IOException;
import java.io.Writer;


/**
 * 正常结束命令
 * 关闭与客户端的连接
 */
public class QuitCommand implements Command{

    public void getResult(String data, Writer writer, ClientHandler t) {
        System.out.println("quit data : " + data);
        try {
            writer.write("221 goodbye...\r\n");  // 发送结束信息
            writer.flush();
            writer.close();
            t.getSocket().close();  // 关闭连接
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
