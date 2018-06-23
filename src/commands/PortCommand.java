package commands;

import server.FtpServer.ClientHandler;

import java.io.IOException;
import java.io.Writer;


/**
 * 指定数据传输时的端口号
 */
public class PortCommand implements Command{

    public void getResult(String data, Writer writer, ClientHandler t) {
        System.out.println("port : " + data);

        String response = "200 the port and ip have been transfered";
        try {

            String[] iAp =  data.split(",");
            String ip = iAp[0]+"."+iAp[1]+"."+iAp[2]+"."+iAp[3];
            String port = Integer.toString(256*Integer.parseInt(iAp[4])+Integer.parseInt(iAp[5]));
            System.out.println("ip is "+ip);
            System.out.println("port is "+port);
//            t.setDataIp(ip);
//            t.setDataPort(port);
            writer.write(response);
            writer.write("\r\n");
            writer.flush();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

}