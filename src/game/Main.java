package game;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
        PokerGUI gui = new PokerGUI();
        Semaphore flag = new Semaphore(1, true);

        ServerSocket server = null;
        Socket socket;
        try {
            System.out.println("Starting Listening Server...");
            server = new ServerSocket(5025, 10);
            server.setSoTimeout(3000);
            System.out.println("Server Started");
        } catch (Exception e) { }

        while(!gui.getKillServer()) {
            try {
                socket = server.accept();
                new Thread(new rcvMessage(socket, flag, gui)).start();
                //System.out.println("Opened New Connection");

            } catch(Exception e) {
            }
        }
        try {
            System.out.println("Server Closing...");
            server.close();
        } catch (IOException e) {e.printStackTrace(); }
    }
}
