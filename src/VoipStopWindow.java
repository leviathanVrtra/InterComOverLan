import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
this class creates GUI to stop voice chat.
 */
public class VoipStopWindow {

    private boolean stopAudio;
    private JButton stopButton;
    private JFrame frame;
    VoipStopWindow(String name) {
        frame = new JFrame(name);
        frame.resize(300, 100);
        stopButton = new JButton("Stop Voice Chat");
        frame.add(stopButton);
        frame.setVisible(true);
        frame.setResizable(false);
        stopButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                stopAudio = true;
                frame.dispose();
            }
        });
        
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                stopAudio = true;
                frame.dispose();
            }
        });
    }
    boolean stopaudio() {
        return stopAudio;
    }
}
