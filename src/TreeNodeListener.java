import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/*
    Tree Node click listener
*/
class TreeNodeListener extends MouseAdapter {

    private MainWindowGui mainwindowgui;

    TreeNodeListener(MainWindowGui aThis) {
        this.mainwindowgui = aThis;
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            boolean isChatWindowExist = false;
            boolean establishConnection = false;
            JTree jtree = (JTree) event.getComponent();
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) jtree.getLastSelectedPathComponent();
            String ip = null;
            if (treeNode != null && treeNode.isLeaf()) {
                ip = treeNode.getUserObject().toString();
            } else if (treeNode != null){
                DefaultMutableTreeNode child = treeNode.getFirstLeaf();
                ip = child.getUserObject().toString();
            }
            if (InterComOverLan.ipChatWindowGui.containsKey(ip)) {
                isChatWindowExist = true;
            }
            if (isChatWindowExist) {
                int option = JOptionPane.showConfirmDialog(mainwindowgui, "Already Window Exists. Do you want to open another ? ");
                if (option == JOptionPane.YES_OPTION) {
                    establishConnection = true;
                }
                
            } else {
                establishConnection = true;
            }
            
            if (establishConnection) {
                new Thread(new TextChatClient(mainwindowgui, ip)).start();
            }
        }
    }
}
