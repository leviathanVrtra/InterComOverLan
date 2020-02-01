import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/*
    this class creates first window GUI.
*/
public class MainWindowGui extends JFrame {

    private DefaultMutableTreeNode topNode;
    private JTree jtree;
    private MultiCastSocket multicast;
    private final MainWindowGui mainWindowGui;
    
    public MainWindowGui(final MultiCastSocket multicast) {
        super("INTERCOM OVER LAN");
        try {

            setIconImage(ImageIO.read(new FileInputStream("logo.png")));
        } catch (Exception e) {
        }
        this.multicast = multicast;
        JDesktopPane jd = new JDesktopPane();
        jd.setLayout(new BorderLayout());
        JMenuBar menuBar = new JMenuBar();
        topNode = new DefaultMutableTreeNode(new String("Online Users"));

        jtree = new JTree(topNode);
        jtree.addMouseListener(new TreeNodeListener(this));
        setSize(400, 600);
        JScrollPane jsp = new JScrollPane(jtree);
        jd.add(jsp, BorderLayout.CENTER);
        setContentPane(jd);
        JMenu settings = new JMenu("Settings");
        JMenuItem Settings = new JMenuItem("Set Download Directory");
        settings.add(Settings);
        
        JMenu groupchat = new JMenu("Group Chat");
        JMenuItem grp = new JMenuItem("Create Group");
        groupchat.add(grp);
        JMenu exitMenu = new JMenu("Exit");
        JMenuItem exit = new JMenuItem("Exit");
        exitMenu.add(exit);
        menuBar.add(groupchat);
        menuBar.add(settings);
        
        menuBar.add(exitMenu);
        setJMenuBar(menuBar);
        setVisible(true);
        mainWindowGui = this;
        // exit button listener
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    multicast.sendGoodbyePacket();
                    System.exit(0);

                } catch (IOException ex) {
                    Logger.getLogger(MainWindowGui.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        // group chat menu item listener
        grp.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    ServerSocket sock = new ServerSocket(0);
                    int port = sock.getLocalPort();
                    Thread t = new Thread(new GroupChatServerHandler(sock));
                    t.start();
                    String str = Integer.toString(port);
                    String myip = InetAddress.getLocalHost().getHostAddress();
                    Thread tp = new Thread(new GroupChatClient(myip, port, false));
                    tp.start();
                    new GroupChatUserSelectGUI(str, null);
                    
                } catch (IOException ex) {
                    Logger.getLogger(MainWindowGui.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
        // Setting menu listener
        Settings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filepath = System.getProperty("user.home");
                FileChooser.createFilechooser(filepath, mainWindowGui, "Select Path");
            }
        });
    }

    public DefaultMutableTreeNode getTopNode() {
        return topNode;
    }

    public JTree getTree() {
        return jtree;
    }
}
