package server.commandResp;

import server.FtpServer.ClientHandler;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.Socket;

/**
 * 从FTP服务器下载文件命令
 * 向客户端发送文件信息
 */
public class RetrCommand implements Command{

    public void getResult(String data, Writer writer, ClientHandler t) {
        System.out.println("filename : " + data);

        Socket s;
        String desDir = t.getNowDir()+File.separator+data;  // 目标文件地址
        File file = new File(desDir);
        System.out.println(desDir);

        // 文件存在时，建立数据连接，进行数据传输
        if(file.exists())
        {
            try {
                // 向客户端发出建立数据连接的指令
                writer.write("150 open ascii mode...\r\n");
                writer.flush();

                // 建立数据连接
                s = new Socket(t.getDataIP(), 20);

                // 进行数据传输
                BufferedOutputStream bos = new BufferedOutputStream(s.getOutputStream());
                byte[] buf = new byte[1024];
                InputStream is = new FileInputStream(file);
                while(-1 != is.read(buf)) {
                    bos.write(buf);
                }
                bos.flush();    // 传输结束
                bos.close();
                is.close();
                s.close();  // 关闭连接

                // 发送数据连接结束信息
                writer.write("220 transfer complete...\r\n");
                writer.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {  // 文件不存在
            try {
                writer.write("220  file does not exist...\r\n");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
