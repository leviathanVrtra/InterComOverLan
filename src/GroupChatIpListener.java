
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 Group chat Ip / Port listener class
 */
public class GroupChatIpListener implements Runnable {

    @Override
    public void run() {
        try {
            ServerSocket sock = new ServerSocket(Constant.GROUP_CHAT_IP_SENDER_PORT);
            while (true) {
                Socket s = sock.accept();
                InputStream inpt = s.getInputStream();
                OutputStream output = s.getOutputStream();
                StringBuilder sb = new StringBuilder();
                while (true) {
                    int k = inpt.read();
                    if ((byte) k == -2) {
                        break;
                    }
                    sb.append((char) k);
                }
                String p = sb.toString();
                System.out.println(p);
                String ip;
                int port;
                // check port
                if (p.indexOf(Utility.PORT_PREFIX) == 0) {
                    p = p.substring(Utility.PORT_PREFIX.length());
                    ip = s.getRemoteSocketAddress().toString();
                    sb = new StringBuilder();
                    port = Integer.parseInt(p);
                    for (int i = 1; i < ip.length(); i++) {
                        if (ip.charAt(i) == ':') {
                            break;
                        }
                        sb.append(ip.charAt(i));
                    }
                    ip = sb.toString();

                } else {
                    p = p.substring(Utility.IP_PREFIX.length());
                    StringBuilder IP = new StringBuilder();
                    for (int i = 0; i < p.length(); i++) {
                        if (p.charAt(i) == ':') {
                            break;
                        }
                        IP.append(p.charAt(i));
                    }
                    ip = IP.toString();
                    port = Integer.parseInt(p.substring(p.indexOf(":") + 1));

                }

                Thread t = new Thread(new GroupChatClient(ip, port, true));
                t.start();

            }
        } catch (IOException ex) {
            Logger.getLogger(GroupChatIpListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
