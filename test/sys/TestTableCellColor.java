package sys;

import java.awt.*;

import java.awt.event.*;

import javax.swing.*;

import javax.swing.table.*;

public class TestTableCellColor extends JFrame

{

	private String[] colname = { "第1列", "第2列", "第3列", "第4列", "第5列" }; // 表头信息

	private String[][] data = new String[10][5]; // 表内容

	// 界面组件----------------------//

	private JScrollPane scroPanel = new JScrollPane(); // 中底层滚动面板

	private DefaultTableModel model; // 列表默认TableModel

	private JTable table;

	int r = 1, c = 2;// 用于控制变色区域

	public TestTableCellColor()

	{

		makeFace();

		addListener();

		showFace();

	}

	private void makeFace()

	{

		// 表内容数组 data[][] 赋值------------//

		for (int i = 0; i < 10; i++)

		{

			for (int j = 0; j < 5; j++)

			{

				data[i][j] = "( " + (j + 1) + ", " + (i + 1) + " )";

			}

		}

		table = new JTable(model = new DefaultTableModel(data, colname));

		table.setEnabled(false);

		// 新建列表现器------------------------//

		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column)
			{
				Component cell = super.getTableCellRendererComponent
				(table, value, isSelected, hasFocus, row, column);
				if (row == r && column == c && cell.isBackgroundSet()) // 设置变色的单元格
					cell.setBackground(Color.blue);
				else
					cell.setBackground(Color.WHITE);
				return cell;
			}

		};

		// 设置列表现器------------------------//

		for (int i = 0; i < colname.length; i++)

		{

			table.getColumn(colname[i]).setCellRenderer(tcr);

		}

		scroPanel.getViewport().setBackground(Color.WHITE);

		scroPanel.getViewport().add(table);

		// 总体界面布局------------------------//

		getContentPane().add(scroPanel, BorderLayout.CENTER);

	}

	private void showFace()

	{

		setSize(500, 400);

		Toolkit tmpTK = Toolkit.getDefaultToolkit();

		Dimension dime = tmpTK.getScreenSize();

		setLocation(200, 300);

		setVisible(true);

	}

	private void addListener()

	{

		this.addWindowListener(new WindowAdapter() { // 添加窗口关闭事件

			public void windowClosing(WindowEvent e) {

				setVisible(false);

				dispose();

				System.exit(0);

			}

		});

	}

	public static void main(String args[])

	{

		// 获取设置系统风格-------------------//

		try {

			String laf = UIManager.getSystemLookAndFeelClassName();

			UIManager.setLookAndFeel(laf);

		} catch (Exception e) {
		}

		new TestTableCellColor();

	}

}
