
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JOptionPane;

/*
Voice chat client
 */
public class VoipClient implements Runnable {

    private String IP;
    private ChatWindowGui chatwindowgui;
    private String pcname;
    private VoipStopWindow voips;
    public VoipClient(String ip, ChatWindowGui chatwindowgui) {
        this.IP = ip;
        this.chatwindowgui = chatwindowgui;
    }

    @Override
    public void run() {
        Socket socket = null;
        DataOutputStream out = null;
        DataInputStream inpt = null;
        pcname = chatwindowgui.getPcname();
        TargetDataLine targetLine = null;
        try {
            socket = new Socket(IP, Constant.VOICE_CHAT_PORT);
            out = new DataOutputStream(socket.getOutputStream());
            inpt = new DataInputStream(socket.getInputStream());
            String myname = System.getProperty("user.name");
            out.write(myname.getBytes());
            out.write(-2);
            int t = inpt.read();
            voips = new VoipStopWindow(pcname);
            if (t != -1 || (byte) t != -1) {
                AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, false);
                System.out.println("Sachin : " + format);
                DataLine.Info targetInfo = new DataLine.Info(TargetDataLine.class, format);
                
                try {
                    Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
                    Mixer mixer = AudioSystem.getMixer(mixerInfo[3]);
                    targetLine = (TargetDataLine) mixer.getLine(targetInfo);
                } catch (Exception e) {
                    try {
                        targetLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
                    } catch (LineUnavailableException ex) {
                        Logger.getLogger(VoipClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                targetLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
                targetLine.open(format);
                targetLine.start();
                int numBytesRead;
                byte[] targetData = new byte[Constant.VOICE_CHAT_BUFFER_SIZE];
                AudioCapture audiocapture = new AudioCapture(inpt, pcname);
                Thread tp = new Thread(audiocapture);
                tp.start();
                while (!voips.stopaudio()) {
                    numBytesRead = targetLine.read(targetData, 0, targetData.length);
                    if (numBytesRead == -1) {
                        break;
                    }
                    out.write(targetData, 0, numBytesRead);
                }
            }
            out.close();
            inpt.close();
            System.out.println("exiting voip clinet");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(chatwindowgui, pcname + " disconnected VOIP :(");
        } catch (LineUnavailableException ex) {
            JOptionPane.showMessageDialog(chatwindowgui," Something went wrong. Please restart application. :(" );
        } finally {

            try {
                if (targetLine != null)
                    targetLine.close();
                out.close();
                inpt.close();
                socket.close();
                
            } catch (IOException ex) {
               // JOptionPane.showMessageDialog(chatwindowgui, IP + " VoIp Disconnected:(");
                //Logger.getLogger(VoipClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
