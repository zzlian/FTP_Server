package server.commandResp;

import server.FtpServer.ClientHandler;

import java.io.*;
import java.net.Socket;


/**
 * 向服务器上传文件命令
 * 保存客户端发送的文件
 */
public class StorCommand implements Command{

    public void getResult(String data, Writer writer, ClientHandler t) {
        System.out.println("filename : " + data + "\n");

        try{
            // 向客户端发出建立数据连接的指令
            writer.write("150 Opening data connection for stor\r\n");
            writer.flush();

            // 建立数据连接
            Socket s = new Socket(t.getDataIP(), 20);

            // 接收客户端的文件数据
            File file = new File(t.getNowDir()+"/"+data);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            // 进行数据传输
            InputStream is = s.getInputStream();
            byte[] buf = new byte[1024];
            while(is.read(buf) != -1) {
                bos.write(buf);
            }
            bos.flush();   // 文件传输结束
            bos.close();
            is.close();
            s.close();

            //断开数据连接
            writer.write("226 transfer complete\r\n");
            writer.flush();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}