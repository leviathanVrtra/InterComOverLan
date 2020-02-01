import java.net.ServerSocket;
import java.net.Socket;

/*
File reciever server
 */
public class FileTransferServer implements Runnable {
    private MainWindowGui mainwindowgui;
    public FileTransferServer(MainWindowGui mainwindowgui) {
        this.mainwindowgui = mainwindowgui;
        
        
    }
    

    @Override
    public void run() {
        try {
            ServerSocket serversocket = new ServerSocket(Constant.FILE_TRANSFER_PORT);
            while (true) {
                Socket s = serversocket.accept();
                Thread t = new Thread(new FileTransferServerHandler(s, mainwindowgui));
                t.start();
            }
        } catch (Exception e) {
        }
    }

}
