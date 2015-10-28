package util;

import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import java.awt.Component;
import javax.swing.UIManager;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * @Author SamZheng 2008-03-31
 * @version 1.0
 */

public class MultiLineHeaderRenderer extends JTextArea implements TableCellRenderer{
  private final DefaultTableCellRenderer adaptee = new DefaultTableCellRenderer();

  public MultiLineHeaderRenderer() {
    super(1, 50);
    setOpaque(true);
    setLineWrap(true);
    setWrapStyleWord(true);
  }

  public Component getTableCellRendererComponent(JTable table, Object obj,
                                                 boolean isSelected, boolean hasFocus,
                                                 int row,int column) {
    int width = 1;
    String value = "";
    if (table != null) {
      JTableHeader header = table.getTableHeader();
      if (header != null) {
        setForeground(header.getForeground());
        setBackground(header.getBackground());
        setFont(header.getFont());
      }
      width = header.getColumnModel().getColumn(column).getWidth();
      if(width==0)
        width = 150;
      value = header.getColumnModel().getColumn(column).getHeaderValue().toString();
    }
    setText( (value == null) ? "Column:" + column : value.toString());
    setBorder(UIManager.getBorder("TableHeader.cellBorder"));
    this.setRows((10*value.length())/width);
    //JScrollPane sp = new JScrollPane(this);
    return this;
  }
}
