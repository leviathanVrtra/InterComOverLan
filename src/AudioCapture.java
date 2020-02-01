
import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JOptionPane;

/*
 This class reads audio input from socket, and plays the sound. 
 */
public class AudioCapture implements Runnable {

    private DataInputStream inpt;
    private String pcname;

    AudioCapture(DataInputStream in, String p) throws IOException {
        inpt = in;
        pcname = p;
    }

    @Override
    public void run() {
        // audio format
        AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, false);
        DataLine.Info sourceInfo = new DataLine.Info(SourceDataLine.class, format);
        SourceDataLine sourceLine = null;

        try {
            // getting mixures
            Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
            Mixer mix = AudioSystem.getMixer(mixerInfo[1]);
            sourceLine = (SourceDataLine) mix.getLine(sourceInfo);
        } catch (Exception e) {
            try {
                sourceLine = (SourceDataLine) AudioSystem.getLine(sourceInfo);
            } catch (LineUnavailableException ex) {
                Logger.getLogger(VoipClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            sourceLine = (SourceDataLine) AudioSystem.getLine(sourceInfo);
            sourceLine.open(format);
            sourceLine.start();
            int numBytesRead;
            byte[] targetData = new byte[Constant.VOICE_CHAT_BUFFER_SIZE];
            System.out.println("Audio Capture started");
            // reading from socket
            while (true) {
                numBytesRead = inpt.read(targetData);
                if (numBytesRead == -1) {
                    break;
                }
                sourceLine.write(targetData, 0, numBytesRead);
            }
            System.out.println("After Audio Stopped");
            inpt.close();
        } catch (LineUnavailableException ex) {
            //  JOptionPane.showMessageDialog(null, " Something went wrong. Please restart application. :(" );
        } catch (IOException ex) {
             JOptionPane.showMessageDialog(null, pcname + " disconnected VOIP :(");
        } finally {
            try {
                sourceLine.close();
                inpt.close();
            } catch (IOException ex) {

            }
        }

    }
}
