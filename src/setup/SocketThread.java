package setup;

import game.PokerGUI;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class SocketThread extends Thread {
    Socket socket;

    public SocketThread(Socket s) {
        socket = s;
    }

    public void run() {
        try {
            OutputStream output = socket.getOutputStream();
            InputStreamReader input = new InputStreamReader(socket.getInputStream());
            //output.write("<html><body>test</body></html>".getBytes(StandardCharsets.UTF_8));
            String line = "";
            int i;
            char[] buf = new char[256];
            while(true) {
                i = input.read(buf);
                if(i == -1 || i != 256) {
                    break;
                }
                //System.out.println(i);
                line = line + new String(buf);
            }
            line = line + new String(buf).substring(0, i);
            System.out.println(line.substring(0, line.indexOf("HTTP/")));
            String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + "<html><body>Working.</body></html>";
            output.write(httpResponse.getBytes(StandardCharsets.UTF_8));
            socket.close();

        } catch(Exception e) {
            JOptionPane.showConfirmDialog(null, e.toString(), "Error!", JOptionPane.PLAIN_MESSAGE);
        }
    }
}
