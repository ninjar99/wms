package util;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import javax.swing.*;
import java.io.IOException;
import java.util.*;
import java.awt.image.*;

public class ReorderableJList extends JList
    implements DragSourceListener, DropTargetListener, DragGestureListener {

    static DataFlavor localObjectFlavor;
    static {
        try {
            localObjectFlavor =
                new DataFlavor (DataFlavor.javaJVMLocalObjectMimeType);
        } catch (ClassNotFoundException cnfe) { cnfe.printStackTrace(); }
    }
    static DataFlavor[] supportedFlavors = { localObjectFlavor };
    DragSource dragSource;
    DropTarget dropTarget;
    Object dropTargetCell;
    int draggedIndex = -1;

    public ReorderableJList () {
        super();
        setCellRenderer (new ReorderableListCellRenderer());
        setModel (new DefaultListModel());
        dragSource = new DragSource();
        DragGestureRecognizer dgr =
            dragSource.createDefaultDragGestureRecognizer (this,
                                                           DnDConstants.ACTION_MOVE,
                                                           this);
        dropTarget = new DropTarget (this, this);
    }
    public ReorderableJList (DefaultListModel defModel) {
      this();
      this.setModel (defModel);
    }
    // DragGestureListener
    public void dragGestureRecognized (DragGestureEvent dge) {
        System.out.println ("dragGestureRecognized");
        // find object at this x,y
        Point clickPoint = dge.getDragOrigin();
        int index = locationToIndex(clickPoint);
        if (index == -1)
            return;
        Object target = getModel().getElementAt(index);
        Transferable trans = new RJLTransferable (target);
        draggedIndex = index;
        dragSource.startDrag (dge,Cursor.getDefaultCursor(),
                              trans, this);
    }
    // DragSourceListener events
    public void dragDropEnd (DragSourceDropEvent dsde) {
        System.out.println ("dragDropEnd()");
        dropTargetCell = null;
        draggedIndex = -1;
        repaint();
    }
    public void dragEnter (DragSourceDragEvent dsde) {}
    public void dragExit (DragSourceEvent dse) {}
    public void dragOver (DragSourceDragEvent dsde) {}
    public void dropActionChanged (DragSourceDragEvent dsde) {}
    // DropTargetListener events
    public void dragEnter (DropTargetDragEvent dtde) {
        System.out.println ("dragEnter");
        if (dtde.getSource() != dropTarget)
            dtde.rejectDrag();
        else {
            dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
            System.out.println ("accepted dragEnter");
        }

    }
    public void dragExit (DropTargetEvent dte) {}
    public void dragOver (DropTargetDragEvent dtde) {
        // figure out which cell it's over, no drag to self
        if (dtde.getSource() != dropTarget)
            dtde.rejectDrag();
        Point dragPoint = dtde.getLocation();
        int index = locationToIndex (dragPoint);
        if (index == -1)
            dropTargetCell = null;
        else
            dropTargetCell = getModel().getElementAt(index);
        repaint();
    }
    public void drop (DropTargetDropEvent dtde) {
        System.out.println ("drop()!");
        if (dtde.getSource() != dropTarget) {
            System.out.println ("rejecting for bad source (" +
                                dtde.getSource().getClass().getName() + ")");
            dtde.rejectDrop();
            return;
        }
        Point dropPoint = dtde.getLocation();
        int index = locationToIndex (dropPoint);
        System.out.println ("drop index is " + index);
        boolean dropped = false;
        try {
            if ((index == -1) || (index == draggedIndex)) {
                System.out.println ("dropped onto self");
                dtde.rejectDrop();
                return;
            }
            dtde.acceptDrop (DnDConstants.ACTION_MOVE);
            System.out.println ("accepted");
            Object dragged =
                dtde.getTransferable().getTransferData(localObjectFlavor);
            // move items - note that indicies for insert will
            // change if [removed] source was before target
            System.out.println ("drop " + draggedIndex + " to " + index);
            boolean sourceBeforeTarget = (draggedIndex < index);
            System.out.println ("source is" +
                                (sourceBeforeTarget ? "" : " not") +
                                " before target");
            System.out.println ("insert at " +
                                (sourceBeforeTarget ? index-1 : index));
            DefaultListModel mod = (DefaultListModel) getModel();
            mod.remove (draggedIndex);
            mod.add ((sourceBeforeTarget ? index-1 : index), dragged);
            dropped = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        dtde.dropComplete (dropped);
    }
    public void dropActionChanged (DropTargetDragEvent dtde) {}

    // test
    public static void main (String[] args) {
        JList list = new ReorderableJList ();
        DefaultListModel defModel = new DefaultListModel();
        list.setModel (defModel);
        String[] listItems = {
            "Chris", "Joshua", "Daniel", "Michael",
            "Don", "Kimi", "Kelly", "Keagan"
        };
        Iterator it = Arrays.asList(listItems).iterator();
        while (it.hasNext())
            defModel.addElement (it.next());
        // show list
        JScrollPane scroller =
            new JScrollPane (list,
                            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        JFrame frame = new JFrame ("Checkbox JList");
        frame.getContentPane().add (scroller);
        frame.pack();
        frame.setVisible(true);
    }

    class RJLTransferable implements Transferable {
        Object object;
        public RJLTransferable (Object o) {
            object = o;
        }
        public Object getTransferData(DataFlavor df)
            throws UnsupportedFlavorException, IOException {
            if (isDataFlavorSupported (df))
                return object;
            else
                throw new UnsupportedFlavorException(df);
        }
        public boolean isDataFlavorSupported (DataFlavor df) {
            return (df.equals (localObjectFlavor));
        }
        public DataFlavor[] getTransferDataFlavors () {
            return supportedFlavors;
        }
    }

    // custom renderer
    class ReorderableListCellRenderer
        extends DefaultListCellRenderer {
        boolean isTargetCell;
        boolean isLastItem;
        Insets normalInsets, lastItemInsets;
        int BOTTOM_PAD = 30;
        public ReorderableListCellRenderer() {
            super();
            normalInsets = super.getInsets();
            lastItemInsets =
                new Insets (normalInsets.top,
                            normalInsets.left,
                            normalInsets.bottom + BOTTOM_PAD,
                            normalInsets.right);
        }
        public Component getListCellRendererComponent (JList list,
                                                       Object value,
                                                       int index,
                                                       boolean isSelected,
                                                       boolean hasFocus) {
            isTargetCell = (value == dropTargetCell);
            isLastItem = (index == list.getModel().getSize()-1);
            boolean showSelected = isSelected &
                                  (dropTargetCell == null);
            return super.getListCellRendererComponent (list, value,
                                                       index, showSelected,
                                                       hasFocus);
        }
        /*
        // this isn't helping, neither does overriding getSize()
        public Insets getInsets(Insets in) {
            Insets useInsets = (isLastItem ? lastItemInsets : normalInsets);
            in.top = useInsets.top;
            in.bottom = useInsets.bottom;
            in.left = useInsets.left;
            in.right = useInsets.right;
            return in;
        }
        */
        public void paintComponent (Graphics g) {
            super.paintComponent(g);
            if (isTargetCell) {
                g.setColor(Color.black);
                g.drawLine (0, 0, getSize().width, 0);
            }
        }
    }
}
