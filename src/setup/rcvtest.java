package setup;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class rcvtest {
    public static void main(String[] args) throws IOException {
        Semaphore flag = new Semaphore(1, true);

        ServerSocket server;
        Socket socket;
        server = new ServerSocket(5025, 10);


        System.out.println("Starting Listening Server...");


        while(true) {
            try {
                socket = server.accept();
                new Thread(new SocketThread(socket, flag)).start();
                System.out.println("Opened New Connection");

            } catch(Exception e) {

            }
        }
    }
}
