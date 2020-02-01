
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
Voip server handler
 */
public class VoipServerHandler implements Runnable {

    private MainWindowGui mainwindowgui;
    private Socket socket;
    private VoipStopWindow voips;
    

    VoipServerHandler(Socket s, MainWindowGui mainwindowgui) {
        this.socket = s;
        this.mainwindowgui = mainwindowgui;
    }

    @Override
    public void run() {
        String pc = null;
        DataInputStream inpt = null;
        DataOutputStream out = null;
        TargetDataLine targetLine = null;
        try {
            inpt = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            StringBuilder sb = new StringBuilder();
            while (true) {
                int k = inpt.read();
                if ((byte) k == -2) {
                    break;
                }
                sb.append((char) k);
            }
            int option = JOptionPane.showConfirmDialog(mainwindowgui, " Do you want to receive voice call from " + sb.toString() + "?");
            if (option == JOptionPane.YES_OPTION) {
                pc = sb.toString();
                voips = new VoipStopWindow(sb.toString());
                out.write(-2);
                AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, false);
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
                targetLine.open(format);
                targetLine.start();
                int numBytesRead;
                byte[] targetData = new byte[Constant.VOICE_CHAT_BUFFER_SIZE];
                AudioCapture audiocapture = new AudioCapture(inpt, sb.toString());
                Thread tp = new Thread(audiocapture);
                tp.start();
                while (!voips.stopaudio()) {
                    numBytesRead = targetLine.read(targetData, 0, targetData.length);
                    if (numBytesRead == -1) {
                        break;
                    }
                    out.write(targetData, 0, numBytesRead);
                    //sourceLine.write(targetData, 0, numBytesRead);
                }
            }
        } catch (IOException ex) {
         //   JOptionPane.showMessageDialog(mainwindowgui, pc + " disconnected VOIP :(");
            //Logger.getLogger(VoipServerHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex) {
          //  JOptionPane.showMessageDialog(mainwindowgui," Something went wrong. Please restart application. :(" );
        } finally {
            try {
                if (targetLine != null)
                    targetLine.close();
               // System.out.println("EXITING Serverhandler");
                inpt.close();
                out.close();
            } catch (IOException ex) {
                
            }

        }
    }

}
