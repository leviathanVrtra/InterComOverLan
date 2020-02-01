
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/*
 private text chat server handler
 */
public class TextChatServerHandler implements Runnable {

    private Socket socket;
    private String IP = null;
    private String username = null;
    private String messsage = null;

    TextChatServerHandler(Socket s) {
        socket = s;
    }

    @Override
    public void run() {
        BufferedReader input = null;
        PrintWriter output = null;
        try {
            output = new PrintWriter(socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            InetAddress useraddr = socket.getInetAddress();
            IP = useraddr.getHostAddress();
            ChatWindowGui chatWindowgui = new ChatWindowGui(socket);
            while (true) {
                String str = input.readLine();

                if (str == null) {
                    //chatWindowgui.dispose();
                    chatWindowgui.setTextChatText(username + " disconnected.");
                    break;
                }
                if (str.indexOf(Constant.EXIT_MESSAGE) == 0) {
                    chatWindowgui.setTextChatText(username + " disconnected.");
                    //chatWindowgui.dispose();
                    break;
                }
                if (str.indexOf(Constant.USERNAME_PREFIX) == 0) {
                    username = str.substring(10);
                    chatWindowgui.SetTitle(IP, username);

                    String str2 = System.getProperty("user.name");
                    output.println(Constant.USERNAME_PREFIX + str2);

                }
                if (str.indexOf(Constant.MESSAGE_PREFIX) == 0) {
                    if (!InterComOverLan.ipChatWindowGui.containsKey(IP)) {
                        InterComOverLan.ipChatWindowGui.put(IP, chatWindowgui);
                        if (!chatWindowgui.checkVisible()) {
                            chatWindowgui.makeVisible(true);
                        }
                    } else {
                        chatWindowgui = InterComOverLan.ipChatWindowGui.get(IP);
                    }
                    messsage = str.substring(9);
                    chatWindowgui.setTextChatText(username + " -> " + messsage);

                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, username + " disconnected.");
        } finally {
            InterComOverLan.ipChatWindowGui.remove(IP);
            try {
                if (socket != null) {
                    input.close();
                    output.close();
                    socket.close();
                }
            } catch (IOException ex) {

            }
        }

    }

}
