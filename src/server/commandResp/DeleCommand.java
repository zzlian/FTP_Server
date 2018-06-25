package server.commandResp;

import server.FtpServer;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

/**
 * 删除指定的文件
 */
public class DeleCommand implements Command{

    public void getResult(String data, Writer writer, FtpServer.ClientHandler t) {
        System.out.println("data : " + data + "n");

        // 待删除文件
        String dir = t.getNowDir() + File.separator+data;
        File file = new File(dir);
        try {   // 判断待删除文件是否存在
            if((file.exists())&&(file.isFile())) {
                file.delete();    // 删除指定文件
                writer.write("250 dele successfully\r\n");
            } else {    // 文件不存在，删除失败
                writer.write("550 file does not exist\r\n");
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}