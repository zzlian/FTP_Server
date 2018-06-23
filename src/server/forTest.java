package server;

import java.io.*;

public class forTest {
    public static void main(String[] args){
        String filename = "D:\\Program Files (x86)\\java_work\\FTP_Server\\src\\rootDir\\userInfo.txt";
        File file = new File(filename);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while((line = reader.readLine()) != null){
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
