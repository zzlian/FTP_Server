package server.commandResp;

import server.FtpServer.ClientHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * 获取ftp目录里面的文件列表
 */
public class ListCommand implements Command{

    public void getResult(String data, Writer writer, ClientHandler t) {
        System.out.println("dir : " + data);

        String desDir = t.getNowDir(); // 获取文件路径
        File dir = new File(desDir);

        try {
            if(!dir.exists()) { // 文件目录路径不存在
                writer.write("210 dir does not exist\r\n");
                writer.flush();
            }
            else {  // 显示文件目录信息
                StringBuilder dirs = new StringBuilder();
                String[] lists= dir.list();
                String flag = null;

                for(String name : lists) {  // 目录文件信息
                    System.out.println(name);
                    File temp = new File(desDir+File.separator+name);

                    // 获取文件的最后修改时间
                    String lt;
                    Calendar cal = Calendar.getInstance();
                    long time = temp.lastModified();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    cal.setTimeInMillis(time);
                    lt = formatter.format(cal.getTime());

                    // 获取文件大小
                    String size = String.valueOf(temp.length()) + " 字节";

                    // 获取文件的属性，文件/文件夹
                    if(temp.isDirectory()) {
                        flag = "d"; // 文件夹
                    }
                    else {
                        flag = "f"; // 文件
                    }
                    // 保存文件信息
                    dirs.append(name);
                    dirs.append("\t");
                    for(int i=name.length(); i <= 20; i++) dirs.append(" ");
                    dirs.append(size);
                    dirs.append("    \t");
                    dirs.append(flag);
                    dirs.append("\t    ");
                    dirs.append(lt);
                    dirs.append("\n");
                }
                // 建立数据连接，将数据发送给客户端
                Socket s;
                writer.write("150 Opening data connection for directory list...\r\n");
                writer.flush();
                s = new Socket(t.getDataIP(), 20);

                // 发送目录里的文件信息
                BufferedWriter dataWriter = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                dataWriter.write(dirs.toString());
                System.out.println(dirs.toString());
                dataWriter.flush();
                s.close();
                writer.write("220 transfer complete...\r\n");
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
