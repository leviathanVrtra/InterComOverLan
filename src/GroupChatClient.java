import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

/*
    Group chat client window 
*/
class GroupChatClient implements Runnable {

    BufferedReader in;
    PrintWriter output;
    JFrame jf = new JFrame();
    JTextField jt = new JTextField("Enter your text here ", 44);
    JButton jb = new JButton("Send");
    JTextArea ja = new JTextArea(8, 50);
    JScrollPane sp = new JScrollPane(ja);
    JButton addbutton = new JButton("Add More Members");
    boolean flag;
    String IP;
    String name;
    int port;
    Socket clientSocket;
    
    
    public GroupChatClient(String p, int port, boolean flg) {
        jf.setTitle(p);
        flag = flg;
        name = System.getProperty("user.name");
        this.IP = p;
        this.port = port;
        ja.setEditable(false);
        jf.getContentPane().add(sp, "Center");
        jf.pack();
        jf.setSize(580, 230);
        jf.setLayout(new FlowLayout());
        jf.getContentPane().add(jt);
        jf.add(jb);
        jf.add(addbutton);
        jf.setVisible(true);
        jf.setResizable(false);
        // code to auto srcoll
        DefaultCaret caret = (DefaultCaret) ja.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        // send button listener
        jb.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                String str = jt.getText();
                output.println(name + "-> " + str);
                jt.setText("");
            }
        });
        // text field listener
        jt.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                String str = jt.getText();
                output.println(name + "-> " + str);
                jt.setText("");
            }
        });
        
        // window exit listener
        jf.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                output.println(Constant.EXIT_PREFIX + name);
                jf.dispose();
            }
        });
        
        // add more member button listener
        addbutton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
               if (flag == false) {
                   new GroupChatUserSelectGUI(Integer.toString(port), null);
               } else {
                   new GroupChatUserSelectGUI(Integer.toString(port), IP);
               }
            }
        });
    }

    @Override
    public void run() {
        try {
            clientSocket = new Socket(IP, port);
            in = new BufferedReader(new InputStreamReader(
                    clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(), true);
            output.println(name + " is connected. :)");
            // reading data from server
            while (true) {
                String lne = in.readLine();
                if (lne == null)
                    break;
                ja.append(lne.substring(9) + "\n");  
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(jf.getContentPane(), "Admin closed :(");
        } finally {
            try {
                in.close();
                output.close();
                clientSocket.close();
                
            } catch (Exception ex) {
            }
        }
    }
}
