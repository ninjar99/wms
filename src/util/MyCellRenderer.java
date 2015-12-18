package util;

import javax.swing.*;
import java.awt.Color;
import java.awt.Component;

public class MyCellRenderer extends javax.swing.plaf.basic.BasicComboBoxRenderer {
    PMMdmComboBox tmpCmb ;
    public MyCellRenderer(PMMdmComboBox cmb) {
        this.tmpCmb = cmb;
        setOpaque(true);
//        this.setBorder(BorderFactory.createLineBorder(Color.gray));
    }
    public Component getListCellRendererComponent(
        JList list,
        Object value,
        int index,
        boolean isSelected,
        boolean cellHasFocus)
    {
        if (value == null)
          return new DefaultListCellRenderer();
        if (value != null){
          if (tmpCmb.getDescByValue(value) != null)
            if (tmpCmb.isNeedName){
              setText(tmpCmb.getDescByValue(value));
            }
            else
              setText(value.toString());
          else
            setText(value.toString());
        }
        setBackground(isSelected ? Color.red : Color.white);
        setForeground(isSelected ? Color.white : Color.black);
        return this;
    }
}
