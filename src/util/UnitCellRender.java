package util;

import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.Component;
import java.awt.Color;

public class UnitCellRender
    extends javax.swing.table.DefaultTableCellRenderer {
  public UnitCellRender() {
    System.out.println("Create UnitCellRender");
  }

  private JComboBox pcmunits = new JComboBox();
  private JLabel errorLabel = new JLabel();
  public Component getTableCellRendererComponent(JTable table,
                                                 Object value,
                                                 boolean isSelected,
                                                 boolean hasFocus,
                                                 int row,
                                                 int column) {
    errorLabel.setBackground(Color.red);
    try {
      return new JLabel();
    }
    catch (Exception ex) {
      return errorLabel;
    }
  }
}
