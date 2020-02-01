import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import javax.swing.JOptionPane;

/*
    this class create a gui to input group chat member's IPs.
*/
public class GroupChatUserSelectGUI extends javax.swing.JFrame {

    GroupChatClient groupchatclient;
    String port;
    String ip;
    public GroupChatUserSelectGUI(String port, String ip) {
        super("Group Chat Users");
        this.ip = ip;
        initComponents();
        setVisible(true);
        setResizable(false);
        this.port = port;
    }
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField(3);
        jTextField2 = new javax.swing.JTextField(3);
        jTextField3 = new javax.swing.JTextField(3);
        jTextField4 = new javax.swing.JTextField(3);
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1.setText("Enter your Group IP's here : ");
        jButton1.setText("Add");
        jButton2.setText("Finish");
        final GroupChatUserSelectGUI gwindow = this;
        HashSet<String> IPs = new HashSet<String>();
        
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String IP = "";
                String temp = jTextField1.getText().trim();
                if (temp.length() == 0) {
                    JOptionPane.showMessageDialog(gwindow, "Text Field is Empty.");
                    return;
                } else {
                    IP += temp + ".";
                }
                temp = jTextField2.getText().trim();
                if (temp.length() == 0) {
                    JOptionPane.showMessageDialog(gwindow, "Text Field is Empty.");
                    return;
                } else {
                    IP += temp + ".";
                }
                temp = jTextField3.getText().trim();
                if (temp.length() == 0) {
                    JOptionPane.showMessageDialog(gwindow, "Text Field is Empty.");
                    return;
                } else {
                    IP += temp + ".";
                }
                temp = jTextField4.getText().trim();
                if (temp.length() == 0) {
                    JOptionPane.showMessageDialog(gwindow, "Text Field is Empty.");
                    return;
                } else {
                    IP += temp;
                }
                IPs.add(IP);
                jTextField1.setText("");
                jTextField2.setText("");
                jTextField3.setText("");
                jTextField4.setText("");
            }
        });
        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (ip == null) {
                    port = Utility.PORT_PREFIX + port;
                } else {
                    port = Utility.IP_PREFIX + ip + ":" + port;
                }
                if (IPs.size() != 0) 
                    new IPSenderClient().sendIP(port, gwindow, IPs);
                else 
                    JOptionPane.showMessageDialog(gwindow, "No ip entered. Make sure click add button after each ip entered");
                dispose();
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButton1)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2)
                        .addGap(18, 18, 18))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton1))
                        .addGap(34, 34, 34)
                        .addComponent(jButton2)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }

    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
}
