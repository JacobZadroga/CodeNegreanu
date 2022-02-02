package setup;

import game.PokerGUI;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Semaphore;

public class SocketThread extends Thread {
    Socket socket;
    Semaphore flag;

    public SocketThread(Socket s, Semaphore f) {
        socket = s;
        this.flag = f;
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
            if(i != -1) {
                line = line + new String(buf).substring(0, i);
            }
            //System.out.println(line);
            try {
                String[] vals = stringSplitter(line);
                if(!vals[0].equals("ERROR") && !vals[1].equals("ERROR")) {
                    System.out.println("do something");
                } else {
                    new Thread(new PostThread("Invalid Card.", vals[1])).start();
                    System.out.println("Invalid Card Received By " + vals[1]);
                }
            } catch (Exception e) {
                System.out.println("Invalid Card Conneciton.");
            }
            //System.out.println(line.substring(0, line.indexOf("HTTP/")));
            String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + "<html><body>Working.</body></html>";
            output.write(httpResponse.getBytes(StandardCharsets.UTF_8));
            socket.close();

        } catch(Exception e) {
            JOptionPane.showConfirmDialog(null, e.toString(), "Error!", JOptionPane.PLAIN_MESSAGE);
        }
        try {
            socket.close();
        } catch(Exception e) {

        }

    }

    private String[] stringSplitter(String input)
    {
        String[] splitter = input.split("&");
        String body = plustospace(splitter[1].substring(5,splitter[1].length())).strip();
        String from = plustospace(splitter[2].substring(9,splitter[2].length())).strip();
        //System.out.println("body: " + body + " | from: " + from);
        String[] set = new String[2];

        if(body.length() == 5 && isLegal(body))
        {
            set[0] = body;
        }
        else
        {
            set[0] = "ERROR";
        }

        if(from.length() == 10)
        {
            set[1] = from;
        }
        else
        {
            set[1] = "ERROR";
        }

        return set;
    }

    private boolean isLegal(String input)
    {
        input = input.toUpperCase();

        if(input.charAt(0) == input.charAt(3) && input.charAt(1) == input.charAt(4))
        {
            return false;
        }

        for(int i = 0; i < input.length(); i++)
        {

            if(i == 0 || i == 3)
            {
                char c = input.charAt(i);
                if((c < 50 || c > 57) && c != 74 && c != 81 && c != 75 && c != 65) {
                    return false;
                }
            }
            else if(i == 1 || i == 4)
            {
                char c = input.charAt(i);
                if(c != 67 && c != 68 && c != 72 && c != 83) {
                    return false;
                }
            }
            else
            {
                if(input.charAt(i) != 32)
                {
                    return false;
                }
            }
        }

        return true;
    }

    private String plustospace(String str) {
        char[] chars = str.toCharArray();
        for(int i = 0; i < chars.length; i++) {
            if(chars[i] == 43) {
                chars[i] = 32;
            }
        }
        return new String(chars);
    }
}
