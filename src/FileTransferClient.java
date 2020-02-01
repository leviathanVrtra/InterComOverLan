
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * file sending client
 */
public class FileTransferClient implements Runnable {

    private List<File> file_list = new ArrayList<File>();
    private String IP;

    FileTransferClient(String ip, List<File> l) {
        this.IP = ip;
        this.file_list = l;
    }

    @Override
    public void run() {
        Iterator<File> itr = file_list.iterator();
        FileProgressGUI filegui = new FileProgressGUI(IP);
        while (itr.hasNext()) {
            File file = itr.next();
            String file_name = file.getName();
            Socket socket;
            try {
                socket = new Socket(IP, Constant.FILE_TRANSFER_PORT);
                OutputStream os;
                os = socket.getOutputStream();
                InputStream inpt = socket.getInputStream();
                String temp = file_name;
                filegui.setTextAreaText("Sending file : " + file_name + "\n");
                file_name = Constant.FILE_ACCEPT_PREFIX + file_name;
                os.write(file_name.getBytes());
                os.write(-2);
                int t = inpt.read();
                os.close();
                inpt.close();
                socket.close();
                if ((byte) t != -1 && t != -1) {
                    sendFile(file, "");
                    filegui.setTextAreaText(" Succesfully Transfered  : " + temp + "\n");
                    socket = new Socket(IP, Constant.FILE_TRANSFER_PORT);
                    os = socket.getOutputStream();
                    temp = Constant.FILE_SUCCESS_PREFIX + temp;
                    os.write(temp.getBytes());
                    os.write(-2);
                    os.close();
                    socket.close();

                } else {
                    filegui.setTextAreaText(" User refused this file/folder  : " + temp + "\n");
                }

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "User disconnected");
                filegui.dispose();
            }

        }

    }

    private void sendFile(File file, String path) throws IOException {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            for (File child : children) {
                sendFile(child, path + file.getName() + "/");
            }
        } else {
            Socket socket = new Socket(IP, Constant.FILE_TRANSFER_PORT);
            OutputStream os;
            os = socket.getOutputStream();
            InputStream inpt = socket.getInputStream();
            String file_name;
            file_name = Constant.FILE_NAME_PREFIX + path + file.getName();
            os.write(file_name.getBytes());
            os.write(-2);

            FileInputStream fis = new FileInputStream(file.getAbsolutePath());
            byte[] buffer = new byte[16 * 1024];
            int count;
            while ((count = fis.read(buffer)) > 0) {
                os.write(buffer, 0, count);
            }

            fis.close();
            os.close();
            inpt.close();
            socket.close();
        }
    }
}
