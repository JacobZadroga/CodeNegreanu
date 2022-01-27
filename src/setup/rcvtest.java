package setup;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class rcvtest {
    public static void main(String[] args) throws IOException {
        ServerSocket server;
        Socket socket;
        server = new ServerSocket(5025, 10);
        while(true) {
            try {
                socket = server.accept();
                new Thread(new SocketThread(socket)).start();

            } catch(Exception e) {

            }
        }


    }
}
