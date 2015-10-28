package util;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class MyTableCellRenderrer extends DefaultTableCellRenderer {
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		// TODO Auto-generated method stub
		Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		// 隔行换色
		 if(row%2 ==0){
		 comp.setBackground(Color.lightGray);
		 }else if(row%2 ==1){
		 comp.setBackground(Color.lightGray);
		 }
//		if ("2".equals(value + "")) {
//			comp.setBackground(Color.green);
//		} else {
//			// 如果不加这一行，那么全部变红
//			comp.setBackground(Color.WHITE);
//		}
		return comp;
	}

}
