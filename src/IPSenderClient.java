import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/*
    This class sends group chat server's IP/PORT to the group chat members. 
 */

public class IPSenderClient {

    public void sendIP(String port, GroupChatUserSelectGUI gui, HashSet<String> IPs) {
        Socket sock = null;
        InputStream inpt = null;
        OutputStream out = null;
        Iterator iterator = IPs.iterator();
        while (iterator.hasNext()) {
            String Ip = (String) iterator.next();
            try {
                sock = new Socket(Ip, Constant.GROUP_CHAT_IP_SENDER_PORT);
                inpt = sock.getInputStream();
                out = sock.getOutputStream();
                out.write(port.getBytes());
                out.write(-2);
                inpt.close();
                out.close();
                sock.close();
            } catch (Exception e) { 
                JOptionPane.showMessageDialog(gui, Ip + " Connection Error !! :(");
                
            }
        }
    }

}
