package game;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.Buffer;

public class DeckTest {
    public static void main(String[] args) throws InterruptedException {
            //PokerGUI gui = new PokerGUI();
            SocketThread st = new SocketThread();
            st.start();
            st.join();

    }
}
