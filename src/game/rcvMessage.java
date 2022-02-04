package game;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.concurrent.Semaphore;

public class rcvMessage extends Thread {
    Socket socket;
    Semaphore flag;
    PokerGUI game;

    public rcvMessage(Socket s, Semaphore f, PokerGUI g) {
        socket = s;
        this.flag = f;
        game = g;
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
                    String[] cards = vals[0].split(" ");
                    System.out.println("Received: " + vals[0] + " from " + vals[1]);
                    flag.acquire();

                    game.rcvHand(cards, vals[1]);

                    flag.release();
                } else {
                    System.out.println("Invalid Card Received By " + vals[1]);
                }
            } catch (Exception e) {
                System.out.println("Invalid Card Connection." + e.toString());
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

        if(isLegal(body)) {
            set[0] = body;
        } else {
            //System.out.println("islegal error");
            set[0] = "ERROR";
        }

        if(from.length() == 10) {
            set[1] = from;
        } else {
            set[1] = "ERROR";
        }

        return set;
    }

    private boolean isLegal(String input)
    {
        input = input.toUpperCase();
        String[] cards = input.split(" ");

        for(String card : cards) {
            if(card.length() != 2) return false;

            char c = card.charAt(0);
            if((c < 50 || c > 57) && c != 74 && c != 81 && c != 75 && c != 65 && c != 84) {
                return false;
            }
            c = card.charAt(1);
            if(c != 67 && c != 68 && c != 72 && c != 83) {
                return false;
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
