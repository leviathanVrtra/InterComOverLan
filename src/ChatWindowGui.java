
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.text.DefaultCaret;
/*
 this class create private chat window GUI
 */

public class ChatWindowGui extends javax.swing.JFrame {

    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField1;
    private Socket socket;
    private boolean visible;
    private String username;
    private String Ip;
    private PrintWriter out;
    private String mypcname;
    private ChatWindowGui chatguiwindow;

    public ChatWindowGui(Socket s) throws IOException {
        initComponents();
        setResizable(false);
        socket = s;
        visible = false;
        out = new PrintWriter(socket.getOutputStream(), true);
        mypcname = System.getProperty("user.name");
        chatguiwindow = this;
    }

    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        DefaultCaret caret = (DefaultCaret) jTextArea1.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        jScrollPane1.setViewportView(jTextArea1);
        jTextArea1.setEditable(false);
        jTextField1.setText("Type Your Text here");

        jButton1.setText("Send");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String str = jTextField1.getText().trim();
                if (str != null && str.length() != 0) {
                    jTextArea1.append(mypcname + " -> " + str + "\n");
                    String msg = Constant.MESSAGE_PREFIX + str;
                    try {
                        out.println(msg);
                    } catch(Exception e) {
                        //JOptionPane.showMessageDialog(null, username + " disconnected.");
                    }
                }
                jTextField1.setText("");
            }
        });

        jTextField1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                String str = jTextField1.getText().trim();
                if (str != null && str.length() != 0) {
                    jTextArea1.append(mypcname + " -> " + str + "\n");
                    String msg = Constant.MESSAGE_PREFIX + str;
                    try {
                        out.println(msg);
                    } catch(Exception e) {
                        //JOptionPane.showMessageDialog(null, username + " disconnected.");
                    }
                }
                jTextField1.setText("");
            }
        });
        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);
        jTextArea2.setEditable(false);
        jTextArea2.setText("Drop your file here. Then click on Send File Button\n");
        DefaultCaret caret1 = (DefaultCaret) jTextArea2.getCaret();
        caret1.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        jButton2.setText("Send File");
        final DragAndDropListener dnd = new DragAndDropListener(Ip, this);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                List<File> temp = new ArrayList<File>();
                Iterator<File> iterator = dnd.fileList.iterator();
                while (iterator.hasNext()) {
                    File file = iterator.next();
                    temp.add(file);
                }
                dnd.fileList.clear();
                Thread t = new Thread(new FileTransferClient(Ip, temp));
                t.start();

            }
        });

        jButton3.setText("Voice Chat");

        jButton4.setText("Exit");
        jButton4.setVisible(false);
        jButton4.disable();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                InterComOverLan.ipChatWindowGui.remove(Ip);
                
                try {
                    out.println(Constant.EXIT_MESSAGE);
                } catch (Exception ex) {
                    
                }
                dispose();
            }
        });

        new DropTarget(jTextArea2, dnd);

        jButton3.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                Thread t = new Thread(new VoipClient(Ip, chatguiwindow));
                t.start();
            }

        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addContainerGap()
                                                        .addComponent(jButton3)
                                                        .addGap(261, 261, 261))
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(jScrollPane2)
                                                        .addGap(18, 18, 18)))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(jTextField1)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButton1)))
                        .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                                .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
        );

        pack();
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println();

    }

    void makeVisible(boolean t) {
        visible = t;
        setVisible(t);
    }

    void SetTitle(String ip, String u) {
        this.Ip = ip;
        this.username = u;
        setTitle(Ip + " " + username);
    }

    boolean checkVisible() {
        return visible;
    }

    void setTextChatText(String string) {
        jTextArea1.append(string + "\n");
    }

    void setFileText(String string) {
        jTextArea2.append(string + "\n");
    }

    void clearfileText() {
        jTextArea2.setText("");
    }

    String getPcname() {
        return username;
    }
}
