
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 This class handles multicasting, sends all type of multicast packets 
 */
public class MultiCastSocket {

    public MulticastSocket multicastSoc;
    public InetAddress group;

    public void openSocket() throws IOException {
        multicastSoc = new MulticastSocket(Constant.MULTICAST_PORT);
        group = InetAddress.getByName(Constant.MULTICAST_IP);
        multicastSoc.joinGroup(group);
        multicastSoc.setSoTimeout(20);
    }

    public static String addPadding(String input, int reqLength) {
        int length = input.length();
        StringBuffer finalstr = new StringBuffer();
        if (length < reqLength) {
            finalstr.append(input);
            finalstr.append(";");
            for (int i = length; i < (reqLength - 1); i++) {
                finalstr.append("0");
            }
        }
        return finalstr.toString();
    }

    public void fireFirstPacket() throws IOException {
        String user = System.getProperty("user.name");
        String finalUser = addPadding(user, 20);
        String msg = "hello" + "&" + finalUser;
        DatagramPacket intPacket = new DatagramPacket(msg.getBytes(), msg.length(), group, Constant.MULTICAST_PORT);
        multicastSoc.send(intPacket);
    }

    public void sendHandShakePacket() throws IOException {
        String user = System.getProperty("user.name");
        String finalUser = addPadding(user, 20);
        String msg = "hands" + "&" + finalUser;
        DatagramPacket intPacket = new DatagramPacket(msg.getBytes(), msg.length(), group, Constant.MULTICAST_PORT);
        multicastSoc.send(intPacket);
    }

    public static String removePadding(String token) {
        String tok[] = token.split(";");
        return tok[0];
    }

    public void sendGoodbyePacket() throws IOException {
        String user = System.getProperty("user.name");
        String finalUser = addPadding(user, 20);
        String msg = "goodb" + "&" + finalUser;
        DatagramPacket intPacket = new DatagramPacket(msg.getBytes(), msg.length(), group, Constant.MULTICAST_PORT);
        multicastSoc.send(intPacket);
        multicastSoc.leaveGroup(group);
        multicastSoc.close();
    }
}
