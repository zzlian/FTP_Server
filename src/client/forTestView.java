package client;

import javax.swing.*;
import java.awt.*;

public class forTestView {

    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame = new JFrame("FTP客户端");     // 建立窗口
        frame.setSize(1000, 700);
        frame.setLocation(400, 200);

        JPanel panel1 = new JPanel();
        panel1.setSize(1000, 200);
        panel1.add(new Button("aa"));
        JPanel panel2 = new JPanel();
        panel2.setSize(600, 500);
        panel2.add(new Button("bb"));
        JPanel panel3 = new JPanel();
        panel3.setSize(400, 500);
        panel3.add(new Button("cc"));

        frame.getContentPane().add(panel1);
        frame.getContentPane().add(panel2);
        frame.getContentPane().add(panel3);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }

}
