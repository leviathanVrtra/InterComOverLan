import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
    Server handler class , handles all the group users
 */
public class GroupChatServerHandler implements Runnable {

    HashSet<PrintWriter> writers = new HashSet<PrintWriter>();
    ServerSocket socket;
    boolean flag;

    GroupChatServerHandler(ServerSocket socket) throws IOException {
        this.socket = socket;
        socket.setSoTimeout(20);
        flag = true;
    }

    @Override
    public void run() {
        try {
            while (true) {
                try {
                new handler(socket.accept()).start();
                } catch(Exception e) { }
                if (!flag) {
                    break;
                }
            }
            System.out.println("out");
        } catch (Exception ex) {
            Logger.getLogger(GroupChatServerHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(GroupChatServerHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    class handler extends Thread {

        Socket sock;
        BufferedReader in;
        PrintWriter out;

        private handler(Socket accept) {
            this.sock = accept;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(
                        sock.getInputStream()));
                out = new PrintWriter(sock.getOutputStream(), true);
                writers.add(out);
                while (true) {
                    String inpt = in.readLine();
                    if (inpt == null) {
                        writers.remove(out);
                        out.close();
                        in.close();
                        return;
                    }

                    if (inpt.indexOf(Constant.EXIT_PREFIX) == 0) {
                        InetAddress inet = InetAddress.getLocalHost();
                        String str = inet.getHostAddress();
                        String temp = sock.getRemoteSocketAddress().toString();
                        StringBuilder sb = new StringBuilder();
                        for (int i = 1; i < temp.length(); i++) {
                            if (temp.charAt(i) == ':') {
                                break;
                            }
                            sb.append(temp.charAt(i));
                        }
                        temp = sb.toString();

                        writers.remove(out);
                        if (str.equals(temp)) {
                            for (PrintWriter writer : writers) {
                                writer.println("MESSAGE :" + inpt.substring(Constant.EXIT_PREFIX.length()) + " closed the group");
                                
                            }
                            writers.clear();
                            flag = false;
                        } else {
                            for (PrintWriter writer : writers) {
                                writer.println("MESSAGE :" + inpt.substring(Constant.EXIT_PREFIX.length()) + " left the group");
                            }
                        }
                        out.close();
                        in.close();
                        return;
                    }
                    for (PrintWriter writer : writers) {
                        writer.println("MESSAGE :" + inpt);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(GroupChatServerHandler.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (out != null) {
                    out.close();
                }
                try {
                    sock.close();
                } catch (IOException e) {

                }
            }
        }
    }
}
