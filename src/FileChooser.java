
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/*
this file sets download directory via setting menu
 */
public class FileChooser {

    static void createFilechooser(final String path, MainWindowGui mainwindowgui, String title) {
        final JDialog jdialog = new JDialog(mainwindowgui, title);
        Container container = jdialog.getContentPane();
        container.setLayout(null);

        final JLabel jlabel = new JLabel();
        jlabel.setForeground(Color.red);
        jlabel.setBounds(20, 5, 300, 20);

        final JTextField jtextfield = new JTextField(title);
        jtextfield.setEditable(false);
        jtextfield.setText(path);
        jtextfield.setBounds(20, 30, 200, 30);
        JButton browsFile = new JButton("Browse");
        browsFile.setBounds(230, 30, 100, 30);
        JButton okay = new JButton("OK");
        okay.setBounds(110, 70, 100, 30);
        browsFile.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfilechooser = new JFileChooser(path);
                jfilechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int t = jfilechooser.showOpenDialog(jdialog);
                if (t == JFileChooser.APPROVE_OPTION) {
                    File file = jfilechooser.getSelectedFile();
                    String path = file.getAbsolutePath();
                    jtextfield.setText(path);
                    if (!file.canWrite()) {
                        jlabel.setText("This directory is not available for downloading.");
                    } else {
                        jlabel.setText("");
                    }
                }
            }
        });

        okay.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String path = jtextfield.getText();
                System.out.println(path);
                String homedirectory = System.getProperty("user.home");
                String conf_path = homedirectory + "/" + Utility.CONF_DIR + "/" + Utility.CONF_FILE_NAME;
                Properties conf_propert = new Properties();
                File conf_file = new File(conf_path);
                try {
                    FileOutputStream out = new FileOutputStream(conf_file);
                    conf_propert.setProperty(Utility.DOWNLOAD_KEY, path);
                    conf_propert.store(out, conf_path);
                    JOptionPane.showMessageDialog(jdialog, "Successfully done.");
                    jdialog.dispose();
                    out.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(jdialog, "Something Went Wrong!");
                    jdialog.dispose();
                }
            }
        });

        container.add(jlabel);
        container.add(jtextfield);
        container.add(browsFile);
        container.add(okay);

        jdialog.setLocationRelativeTo(mainwindowgui);
        jdialog.setSize(350, 150);
        jdialog.setVisible(true);
        jdialog.setResizable(false);
    }

}
