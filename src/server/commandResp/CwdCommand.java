package server.commandResp;

import server.FtpServer.ClientHandler;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

/**
 * 改变工作目录命令
 * */
public class CwdCommand implements Command{

    public void getResult(String data, Writer writer, ClientHandler t) {
        System.out.println("data : " + data + "\n");

        // 待切换的目标目录
        String dir = t.getNowDir() +File.separator+data;
        File file = new File(dir);
        try {   // 判断待切换的目录是否存在
            if((file.exists())&&(file.isDirectory())) {
                String nowDir =t.getNowDir() +File.separator+data;
                t.setNowDir(nowDir);    // 切换工作目录
                writer.write("250 cwd successfully\r\n");
            } else {    // 目录不存在，切换失败
                writer.write("550 dir does not exist\r\n");
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
