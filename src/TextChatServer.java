
import java.net.ServerSocket;
import java.net.Socket;

/**
 private text chat server
 */
public class TextChatServer implements Runnable {
    @Override
    public void run() {
        try {
            ServerSocket serversocket = new ServerSocket(Constant.TEXT_CHAT_SERVER_PORT);
            while (true) {
                Socket s = serversocket.accept();
                Thread t = new Thread(new TextChatServerHandler(s));
                t.start();
            }
        } catch (Exception e) {
        }
    }
}
