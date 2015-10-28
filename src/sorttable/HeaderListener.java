package sorttable;

public class HeaderListener extends java.awt.event.MouseAdapter {
  javax.swing.table.JTableHeader header;
  sorttable.SortButtonRenderer renderer;

  public HeaderListener(javax.swing.table.JTableHeader header,
                        SortButtonRenderer renderer) {
    this.header = header;
    this.renderer = renderer;
  }

  public void mousePressed(java.awt.event.MouseEvent e) {
	  if(header==null){
		  return;
	  }
	  if(header.getTable()==null){
		  return;
	  }
    SortTableModel model = (sorttable.SortTableModel) header.getTable().
        getModel();
    int oldselectedrow = header.getTable().getSelectedRow();
    int[] theindex = model.getIndexes();
    int oldselectedIndex;
    if (oldselectedrow != -1)
      oldselectedIndex = theindex[oldselectedrow];
    else
      oldselectedIndex = 0 ;
//    System.out.println("You selected row : " + header.getTable().getSelectedRow());
//    System.out.println("You selected row's Index is : ");
    int col = header.columnAtPoint(e.getPoint());
    int sortCol = header.getTable().convertColumnIndexToModel(col);
    header.getTable().clearSelection();
    renderer.setPressedColumn(col);
    renderer.setSelectedColumn(col);
    header.repaint();

    if (header.getTable().isEditing()) {
      header.getTable().getCellEditor().stopCellEditing();
    }

    boolean isAscent;
    if (sorttable.SortButtonRenderer.DOWN == renderer.getState(col)) {
      isAscent = true;
    }
    else {
      isAscent = false;
    }
    ( (sorttable.SortTableModel) header.getTable().getModel()).sortByColumn(
        sortCol, isAscent);
    theindex = model.getIndexes();
    int newselectrow = model.getSelectRowByIndex(oldselectedIndex);
    if (model instanceof main.thisModel && ( (main.thisModel) model).keyCol != null) {
//      System.out.println("it's common setup frame sort");
      header.getTable().setRowHeight(header.getTable().getRowCount() - 1, 1);
//      main.PBSUIBaseSetupFrame.isNeedmodi = false;
      if (header.getTable().getRowCount() > 1)
        header.getTable().changeSelection(newselectrow,col,false,false);
//      main.PBSUIBaseSetupFrame.isNeedmodi = true;
      return;
    }
//    main.PBSUIBaseSetupFrame.isNeedmodi = false;
    if (header.getTable().getRowCount() > 0)
      header.getTable().changeSelection(newselectrow,col,false,false);
//    main.PBSUIBaseSetupFrame.isNeedmodi = true;
  }

  public void mouseReleased(java.awt.event.MouseEvent e) {
    int col = header.columnAtPoint(e.getPoint());
    renderer.setPressedColumn( -1); // clear
    header.repaint();
  }
}
