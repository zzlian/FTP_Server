package server.commandResp;

import server.FtpServer.ClientHandler;

import java.io.Writer;


/**
 * 处理客户端请求命令的接口
 */
public interface Command {

    /**
     * @param data    从ftp客户端接收的命令参数
     * @param writer  网络输出流
     * @param t       处理客户端请求的分线程
     */
    public void getResult(String data, Writer writer, ClientHandler t);
}