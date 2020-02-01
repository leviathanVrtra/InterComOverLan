
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
Voice chat server
 */
public class VoipServer implements Runnable {
    private MainWindowGui mainwindowgui;
    public VoipServer(MainWindowGui mainwindowgui) {
        this.mainwindowgui = mainwindowgui;
    }
    @Override
    public void run() {
        try {
            ServerSocket socket = new ServerSocket(Constant.VOICE_CHAT_PORT);
            while (true) {
                Socket s = socket.accept();
                Thread t = new Thread(new VoipServerHandler(s, mainwindowgui));
                t.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(VoipServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
