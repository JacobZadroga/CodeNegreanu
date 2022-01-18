package game;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;

public class SocketThread extends Thread {
    public void run() {
        Socket socket;
        DataInputStream input;
        System.out.println("Started connection");
        try {
            socket = new Socket("127.0.0.1", 5025);
            System.out.println("Connected!");
            input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    socket.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }));

            String msg = "";
            while(!msg.equals("close")) {
                msg = input.readUTF();
                System.out.println(msg);
            }
            System.out.println("Closed Connection");
            input.close();
            socket.close();

        } catch(Exception e) {
            System.out.println(e);
        }
    }
}
