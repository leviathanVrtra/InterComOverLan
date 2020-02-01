import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Properties;
import javax.swing.JOptionPane;

/*
    file reciever handler
 */
public class FileTransferServerHandler implements Runnable {

    private MainWindowGui mainwindowgui;
    private Socket socket;
    String downloadDir = null;
    FileTransferServerHandler(Socket s, MainWindowGui mainwindowgui) {
        this.socket = s;
        this.mainwindowgui = mainwindowgui;
        String homedirectory = System.getProperty("user.home");
        String conf_path = homedirectory + "/" + Utility.CONF_DIR + "/" + Utility.CONF_FILE_NAME;
        try {
             FileInputStream in = new FileInputStream(new File(conf_path));
             Properties conf_propert = new Properties();
             conf_propert.load(in);
             downloadDir = conf_propert.getProperty(Utility.DOWNLOAD_KEY);
             if (downloadDir == null) {
                 downloadDir = homedirectory + "/" + "INTERCOM_OVER_LAN_DOWNLOADS/";
             }
             in.close();
        } catch (Exception e) {
            downloadDir = homedirectory + "/" + "INTERCOM_OVER_LAN_DOWNLOADS/";
        }
        System.out.println(downloadDir);
    }

    @Override
    public void run() {
        InputStream inpt;
        OutputStream out;
        try {
            
            inpt = socket.getInputStream();
            out = socket.getOutputStream();
            StringBuilder sb = new StringBuilder();
            while (true) {
                int k = inpt.read();
                if ((byte) k == -2) {
                    break;
                }
                sb.append((char) k);
            }
            String file_name = sb.toString();
            System.out.println(file_name);
            if (file_name.indexOf(Constant.FILE_ACCEPT_PREFIX) == 0) {
                file_name = file_name.substring(Constant.FILE_ACCEPT_PREFIX.length());
                int option = JOptionPane.showConfirmDialog(mainwindowgui, "Do you want to recieve this file : " + file_name);
                if (option == JOptionPane.YES_OPTION) {
                    out.write(-2);
                } else {
                    out.write(-1);
                }

                inpt.close();
                out.close();
                socket.close();
                return;

            } else if (file_name.indexOf(Constant.FILE_SUCCESS_PREFIX) == 0) {
                JOptionPane.showMessageDialog(mainwindowgui, "Successfully  Recieved : " + file_name.substring(Constant.FILE_SUCCESS_PREFIX.length()));
                inpt.close();
                out.close();
                socket.close();
                return;
            } else {
                file_name = downloadDir + "/" + file_name.substring(Constant.FILE_NAME_PREFIX.length());
            }
            int i = file_name.lastIndexOf('/');
            if (i != -1) {
                File dir = new File(file_name.substring(0, i));
                if (!dir.exists()) {
                    dir.mkdirs();
                }
            }

            OutputStream output = new FileOutputStream(file_name);
            byte[] bytes = new byte[16 * 1024];

            int count;
            while ((count = inpt.read(bytes)) > 0) {
                output.write(bytes, 0, count);
            }
            out.close();
            inpt.close();
            socket.close();
            output.close();
        } catch (Exception e) {
        }
    }

}
