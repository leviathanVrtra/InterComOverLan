import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/*
    private chat client 
 */

public class TextChatClient implements Runnable {

    private MainWindowGui mainwindowgui;
    private String IP;
    private Socket socket = null;

    public TextChatClient(MainWindowGui mainwindowgui, String ip) {
        this.mainwindowgui = mainwindowgui;
        IP = ip;
    }

    @Override
    public void run() {
        String userName = null;
        String myPcname = null;
        BufferedReader input = null;
        PrintWriter output = null;
        try {
            socket = new Socket(IP, Constant.TEXT_CHAT_CLIENT_PORT);
            output = new PrintWriter(socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            myPcname = System.getProperty("user.name");
            
            output.println(Constant.USERNAME_PREFIX + myPcname);
            ChatWindowGui chatwindowgui = new ChatWindowGui(socket);
            
            InterComOverLan.ipChatWindowGui.put(IP, chatwindowgui);
            
            while (true) {
                String str = input.readLine();
                if (str == null) {
                    //JOptionPane.showMessageDialog(chatwindowgui, "User Disconnected");
                    //chatwindowgui.dispose();
                    chatwindowgui.setTextChatText(userName + " disconnected.");
                    break;
                }

                if (str.indexOf(Constant.EXIT_MESSAGE) == 0) {
                    //JOptionPane.showMessageDialog(chatwindowgui, "User Disconnected");
                    ///chatwindowgui.dispose();
                    chatwindowgui.setTextChatText(userName + " disconnected.");
                    break;
                }

                if (str.indexOf(Constant.USERNAME_PREFIX) == 0) {
                    userName = str.substring(10);
                    chatwindowgui.SetTitle(IP, userName);
                    chatwindowgui.makeVisible(true);
                }

                if (str.indexOf(Constant.MESSAGE_PREFIX) == 0) {
                    String message = str.substring(9);
                    chatwindowgui.setTextChatText(userName + " -> " + message);

                }
            }

        } catch (IOException ex) {
           JOptionPane.showMessageDialog(null, userName + " disconnected.");
        } finally {
            InterComOverLan.ipChatWindowGui.remove(IP);
            try {
                if (socket != null) {
                    input.close();
                    output.close();
                    socket.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(TextChatClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
