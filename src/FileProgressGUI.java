
import javax.swing.text.DefaultCaret;

/*
    this class show file status
 */
public class FileProgressGUI extends javax.swing.JFrame{
    String IP;
    public FileProgressGUI(String ip) {
        super(ip + " - File Transfer");
        initComponents();
        setVisible(true);
        setResizable(false);
    }                      
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);
        jTextArea1.setEditable(false);
        DefaultCaret caret = (DefaultCaret) jTextArea1.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
        );

        pack();
    }                              
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    
    public void setTextAreaText(String str) {
        jTextArea1.append(str);
    }
}
