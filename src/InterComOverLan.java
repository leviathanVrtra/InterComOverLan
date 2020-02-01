
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/*
 Main class from where execution starts.
 */
public class InterComOverLan {

    static Map<String, ChatWindowGui> ipChatWindowGui = new HashMap<>();

    /*
     this method does intitial setup like configuring download directory properties file
     */
    static void setInitialSetup() throws Exception {
        String homedirectory = System.getProperty("user.home");
        String conf_path = homedirectory + "/" + Utility.CONF_DIR;
        Properties conf_propert = new Properties();
        File conf_dir = new File(conf_path);

        if (!conf_dir.exists()) {
            System.out.println(homedirectory);
            conf_dir.mkdir();
        }
        File conf_file = new File(conf_path + "/" + Utility.CONF_FILE_NAME);
        if (!conf_file.exists()) {
            try {
                conf_file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(InterComOverLan.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        FileInputStream in = new FileInputStream(conf_file);

        conf_propert.load(in);
        String downloadFiledir = conf_propert.getProperty(Utility.DOWNLOAD_KEY);

        if (downloadFiledir == null || downloadFiledir.isEmpty()) {
            in.close();
            FileOutputStream out = new FileOutputStream(conf_file);
            downloadFiledir = homedirectory + "/" + "INTERCOM_OVER_LAN_DOWNLOADS";
            conf_propert.setProperty(Utility.DOWNLOAD_KEY, downloadFiledir);
            conf_propert.store(out, conf_path);
            out.close();
            System.out.println("input");
            return;
        }
        in.close();
    }

    public static void main(String[] args) throws Exception {
        setInitialSetup();
        Thread textchatserver = new Thread(new TextChatServer());
        textchatserver.start();
        Thread groupchatiplistener = new Thread(new GroupChatIpListener());
        groupchatiplistener.start();

        Map<String, DefaultMutableTreeNode> onlineUser = new HashMap<String, DefaultMutableTreeNode>();
        MultiCastSocket multicast = new MultiCastSocket();
        multicast.openSocket();

        MainWindowGui maingui = new MainWindowGui(multicast);
        registerAppInSystemTray(maingui, multicast);
        Thread filetransferserver = new Thread(new FileTransferServer(maingui));
        filetransferserver.start();
        Thread voipserver = new Thread(new VoipServer(maingui));
        voipserver.start();
        JTree jtree = maingui.getTree();
        DefaultTreeModel treeModel = (DefaultTreeModel) jtree.getModel();
        DefaultMutableTreeNode topNode = maingui.getTopNode();
        // multicast  first hello packet
        multicast.fireFirstPacket();
        while (true) {

            byte[] buf = new byte[26];
            DatagramPacket recv = new DatagramPacket(buf, buf.length);

            try {
                multicast.multicastSoc.receive(recv);
            } catch (Exception e) {
            }
            StringBuffer temp = new StringBuffer();
            for (int i = 0; i < buf.length; i++) {
                char chr = (char) buf[i];
                temp.append(chr);
            }
            String pack = temp.toString();
            String[] tok = pack.split("&");
            InetAddress pacSenderAddr = recv.getAddress();

            if (tok[0].equalsIgnoreCase("hello")) {
                String ip = pacSenderAddr.getHostAddress();
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(ip);
                multicast.sendHandShakePacket();
                if (!onlineUser.containsKey(ip)) {
                    int num = topNode.getChildCount();
                    String user = multicast.removePadding(tok[1]);
                    DefaultMutableTreeNode User = new DefaultMutableTreeNode(user);
                    User.add(node);
                    onlineUser.put(ip, User);
                    topNode.add(User);
                    int index[] = {num};
                    jtree.expandRow(0);
                    treeModel.nodesWereInserted(topNode, index);
                }

            }

            if (tok[0].equalsIgnoreCase("hands")) {
                String ip = pacSenderAddr.getHostAddress();
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(ip);
                if (!onlineUser.containsKey(ip)) {
                    int num = topNode.getChildCount();
                    String user = multicast.removePadding(tok[1]);
                    DefaultMutableTreeNode User = new DefaultMutableTreeNode(user);
                    User.add(node);
                    onlineUser.put(ip, User);
                    topNode.add(User);
                    int index[] = {num};
                    jtree.expandRow(0);
                    treeModel.nodesWereInserted(topNode, index);
                }

            }

            if (tok[0].equalsIgnoreCase("goodb")) {
                String ip = pacSenderAddr.getHostAddress();
                DefaultMutableTreeNode node = onlineUser.get(ip);
                if (node != null) {
                    int num = topNode.getIndex(node);
                    int inde[] = {num};
                    topNode.remove(node);
                    Object rem[] = {node};
                    treeModel.nodesWereRemoved(topNode, inde, rem);
                    onlineUser.remove(ip);
                }
            }
        }
    }

    /*
     this method registers appliction in system tray.
     */
    static void registerAppInSystemTray(final MainWindowGui mainguiwindow, final MultiCastSocket multicast) {
        try {
            ImageIcon icon = new ImageIcon("logo.png");
            if (SystemTray.isSupported()) {
                SystemTray Tray = SystemTray.getSystemTray();
                Image image = icon.getImage();
                PopupMenu popmenu = new PopupMenu();
                MenuItem exit = new MenuItem("Exit");
                popmenu.add(exit);
                TrayIcon trayicon = new TrayIcon(image, "INTERCOM OVER LAN", popmenu);
                exit.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        try {
                            multicast.sendGoodbyePacket();
                            System.exit(0);
                        } catch (IOException ex) {
                            Logger.getLogger(InterComOverLan.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                });

                trayicon.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        mainguiwindow.setVisible(true);
                    }
                });
                Tray.add(trayicon);
            }
        } catch (Exception e) {
        }
    }
}
