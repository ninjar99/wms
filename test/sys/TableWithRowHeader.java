package sys;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;


public class TableWithRowHeader {

	public static void main(String[] args) {
		final JFrame f = new JFrame("有行头的表格");
		String[][] tableData = {
				{"张三", "90" , "89" , "67" , "88"},
				{"李四", "80" , "99" , "77" , "58"},
				{"王二", "80" , "99" , "77" , "58"}
		};
		String[] columnNames = {"姓名\\课程", "数学", "语文", "英语", "化学"};
		
		JTable table = new JTable(tableData, columnNames);
		// 将表格的第一列作为表头，使用渲染器改变其外观，看起来像表头
		table.getColumnModel().getColumn(0).setCellRenderer(new RowHeaderRenderer());
		
		f.add(new JScrollPane(table));
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
		SwingUtilities.invokeLater(new Runnable()
		{

			@Override
			public void run() {
				f.setVisible(true);
			}
			
		});
	}
}

class RowHeaderRenderer implements TableCellRenderer
{
	private JLabel label = new JLabel();
	// 获取表头的字体、前景色和背景色，用来将Label伪装成表头的样子
	private static Font font = (Font) UIManager.get("TableHeader.font");
	private static Color fgc = (Color) UIManager.get("TableHeader.foreground");
	private static Color bgc = (Color) UIManager.get("TableHeader.background");
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		// TODO Auto-generated method stub
		label.setFont(font);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setText(value.toString());
		label.setOpaque(true);
		label.setForeground(fgc);
		label.setBackground(bgc);
		return label;
	}
	
}


