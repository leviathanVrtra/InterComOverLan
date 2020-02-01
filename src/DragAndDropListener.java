
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 File drag and drop listener class
 */
class DragAndDropListener extends DropTargetAdapter {

    public List<File> fileList = new ArrayList<File>();
    private String IP;
    private ChatWindowGui chatwindowgui;

    public DragAndDropListener(String ip, ChatWindowGui c) {
        IP = ip;
        chatwindowgui = c;
    }

    @Override
    public void drop(DropTargetDropEvent event) {
        Transferable transferable = event.getTransferable();
        if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            event.acceptDrop(DnDConstants.ACTION_COPY);
            try {
                List<File> temp = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                Iterator<File> iterator = temp.iterator();
                while (iterator.hasNext()) {
                    File file = iterator.next();
                    fileList.add(file);
                    chatwindowgui.setFileText(file.getName());
                }

            } catch (UnsupportedFlavorException ex) {
                Logger.getLogger(DragAndDropListener.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(DragAndDropListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            event.acceptDrop(DnDConstants.ACTION_COPY);
            try {
                String str = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                event.getDropTargetContext().dropComplete(true);
                System.out.println(str);
            } catch (UnsupportedFlavorException ex) {
                Logger.getLogger(DragAndDropListener.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(DragAndDropListener.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            event.rejectDrop();
        }
    }

}
