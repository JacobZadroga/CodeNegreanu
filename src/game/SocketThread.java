package game;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class SocketThread extends Thread {
    PokerGUI game;
    private final HashMap<String, Integer> dealmap = new HashMap<String, Integer>();

    public SocketThread(PokerGUI g) {
        game = g;
        dealmap.put("as", 0);
        dealmap.put("ks", 1);
        dealmap.put("qs", 5);
        dealmap.put("js", 22);
        dealmap.put("ts", 98);
        dealmap.put("9s", 453);
        dealmap.put("8s", 2031);
        dealmap.put("7s", 8698);
        dealmap.put("6s", 22854);
        dealmap.put("5s", 83661);
        dealmap.put("4s", 262349);
        dealmap.put("3s", 636345);
        dealmap.put("2s", 1479181);

        dealmap.put("ah", 2097152);
        dealmap.put("kh", 2097153);
        dealmap.put("qh", 2097157);
        dealmap.put("jh", 2097174);
        dealmap.put("th", 2097250);
        dealmap.put("9h", 2097605);
        dealmap.put("8h", 2099183);
        dealmap.put("7h", 2105850);
        dealmap.put("6h", 2120006);
        dealmap.put("5h", 2180813);
        dealmap.put("4h", 2359501);
        dealmap.put("3h", 2733497);
        dealmap.put("2h", 3576333);

        dealmap.put("ac", 16777216);
        dealmap.put("kc", 16777217);
        dealmap.put("qc", 16777221);
        dealmap.put("jc", 16777238);
        dealmap.put("tc", 16777314);
        dealmap.put("9c", 16777669);
        dealmap.put("8c", 16779247);
        dealmap.put("7c", 16785914);
        dealmap.put("6c", 16800070);
        dealmap.put("5c", 16860877);
        dealmap.put("4c", 17039565);
        dealmap.put("3c", 17413561);
        dealmap.put("2c", 18256397);

        dealmap.put("ad", 119537664);
        dealmap.put("kd", 119537665);
        dealmap.put("qd", 119537669);
        dealmap.put("jd", 119537686);
        dealmap.put("td", 119537762);
        dealmap.put("9d", 119538117);
        dealmap.put("8d", 119539695);
        dealmap.put("7d", 119546362);
        dealmap.put("6d", 119560518);
        dealmap.put("5d", 119621325);
        dealmap.put("4d", 119800013);
        dealmap.put("3d", 120174009);
        dealmap.put("2d", 121016845);
    }
    public void run() {
        Socket socket;
        InputStreamReader in;
        BufferedReader input;
        System.out.println("Started connection");
        try {
            socket = new Socket("127.0.0.1", 5025);
            System.out.println("Connected!");

            while(true) {
                if(socket.getInputStream().available() > 0) {
                    byte[] buffer;
                    buffer = new byte[socket.getInputStream().available()];
                    socket.getInputStream().read(buffer);
                    String msg = new String(buffer, StandardCharsets.UTF_8);
                    System.out.println(msg);
                    if(msg.equals("close")) {
                        break;
                    }
                    for(int i = 0; i < msg.length()/2; i++) {
                        String card = msg.substring(i*2, (i*2)+2);
                        if(dealmap.containsKey(card)) {
                            game.rcvDealCard(dealmap.get(card));
                        }
                    }


                }
            }
//            while(!msg.equals("close")) {
//
//            }
            System.out.println("Closed Connection");
            socket.close();

        } catch(Exception e) {
            System.out.println(e);
        }
    }
}
