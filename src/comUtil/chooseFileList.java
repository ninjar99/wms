package comUtil;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Date;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableModel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class chooseFileList extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6263975104443132420L;
	private static chooseFileList instance;
	public static String ResultValue="";
	
	public static synchronized chooseFileList getInstance() {
		 if(instance == null) {    
	            instance = new chooseFileList();
	            instance.setModal(true);
	        }  
	        return instance;
		 
	 }

	/**
	 * 自定义扩展名过滤器
	 * 
	 * @author 李钟尉
	 */
	private final class CustomFilter implements java.io.FileFilter {
		@Override
		public boolean accept(File pathname) {
			// 获取用户设置的指定扩展名
			String extName = extNameField.getText();
			if (extName == null || extName.isEmpty())
				return false;
			if (!extName.startsWith(".")) // 判断扩展名前缀
				extName = "." + extName;// 完事扩展名前缀
			extName = extName.toLowerCase();
			String extNames[] = extName.split(";");
			for (int i = 0; i < extNames.length; i++) {
				// 判断扩展名与过滤文件名是否符合要求
				if (pathname.getName().toLowerCase().endsWith(extNames[i])) {
					return true;
				} 
			}
			return false;
		}
	}

	private JPanel contentPane;
	private JTextField extNameField;
	private JTable table;
	private File dir;
	private JLabel fileFolderLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					chooseFileList frame = chooseFileList.getInstance();
					
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public chooseFileList() {
		setTitle("显示指定类型的文件");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 93, 54, 0 };
		gbl_panel.rowHeights = new int[] { 23, 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		JButton button = new JButton("选择文件夹");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_button_actionPerformed(e);
			}
		});
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.anchor = GridBagConstraints.NORTH;
		gbc_button.gridx = 0;
		gbc_button.gridy = 0;
		panel.add(button, gbc_button);

		fileFolderLabel = new JLabel("文件夹");
		GridBagConstraints gbc_fileFolderLabel = new GridBagConstraints();
		gbc_fileFolderLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_fileFolderLabel.gridx = 1;
		gbc_fileFolderLabel.gridy = 0;
		panel.add(fileFolderLabel, gbc_fileFolderLabel);

		JLabel label_1 = new JLabel("输入指定文件扩展名称：");
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.anchor = GridBagConstraints.EAST;
		gbc_label_1.insets = new Insets(0, 0, 0, 5);
		gbc_label_1.gridx = 0;
		gbc_label_1.gridy = 1;
		panel.add(label_1, gbc_label_1);

		extNameField = new JTextField();
		extNameField.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				do_extNameField_caretUpdate(e);
			}
		});
		extNameField.setText(".xlsx;.xls");
		GridBagConstraints gbc_extNameField = new GridBagConstraints();
		gbc_extNameField.insets = new Insets(0, 0, 5, 0);
		gbc_extNameField.fill = GridBagConstraints.HORIZONTAL;
		gbc_extNameField.gridx = 1;
		gbc_extNameField.gridy = 1;
		panel.add(extNameField, gbc_extNameField);
		extNameField.setColumns(10);

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getClickCount() == 2) {
					int r = table.getSelectedRow();
					int c = table.getSelectedColumn();
					String fileDir = table.getValueAt(r, 0).toString();
					ResultValue = fileFolderLabel.getText()+"\\"+fileDir;
					instance.dispose();
				}
			}
		});
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "文件名称", "文件大小", "修改日期" }) {
			/**
			         * 
			         */
			private static final long serialVersionUID = 5274214559103654856L;
			boolean[] columnEditables = new boolean[] { false, false, false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(220);
		table.getColumnModel().getColumn(1).setPreferredWidth(85);
		table.getColumnModel().getColumn(2).setPreferredWidth(110);
		scrollPane.setViewportView(table);
	}

	/**
	 * 选择文件夹按钮的事件处理方法
	 * 
	 * @param e
	 */
	protected void do_button_actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();// 创建文件选择器
		// 设置选择器的过滤器
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.showDialog(this, null);
		dir = chooser.getSelectedFile();
		if(dir==null){
			return;
		}
		getLabel().setText(dir.toString());
		// 获取过滤后的符合条件的文件数组
		listFiles();
	}

	/**
	 * 显示文件夹中的文件
	 */
	private void listFiles() {
		if (dir == null)
			return;
		// 获取符合条件的文件数组
		File[] files = dir.listFiles(new CustomFilter());
		// 获取表格的数据模型
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0);
		for (File file : files) {// 遍历文件数组
			// 创建表格行数据
			Object[] row = { file.getName(), file.length(), new Date(file.lastModified()) };
			model.addRow(row);// 添加行数据到表格模型
		}
	}

	protected void do_extNameField_caretUpdate(CaretEvent e) {
		listFiles();
	}

	protected JLabel getLabel() {
		return fileFolderLabel;
	}
}
