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
		final JFrame f = new JFrame("����ͷ�ı��");
		String[][] tableData = {
				{"����", "90" , "89" , "67" , "88"},
				{"����", "80" , "99" , "77" , "58"},
				{"����", "80" , "99" , "77" , "58"}
		};
		String[] columnNames = {"����\\�γ�", "��ѧ", "����", "Ӣ��", "��ѧ"};
		
		JTable table = new JTable(tableData, columnNames);
		// �����ĵ�һ����Ϊ��ͷ��ʹ����Ⱦ���ı�����ۣ����������ͷ
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
	// ��ȡ��ͷ�����塢ǰ��ɫ�ͱ���ɫ��������Labelαװ�ɱ�ͷ������
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


