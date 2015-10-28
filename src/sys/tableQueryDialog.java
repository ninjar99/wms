package sys;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import DBUtil.DBOperator;
import dmdata.DataManager;
import main.PBSUIBaseGrid;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class tableQueryDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4551066906292028844L;
	private final JPanel contentPanel = new JPanel();
	private tableQueryDialog tableQuery;
	private PBSUIBaseGrid table;
	private JTextField txtFieldValue;
	private JComboBox cbFieldList;
	private DataManager dm;
	public static DataManager resultDM;
	private String querySQL = "";;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			tableQueryDialog dialog = new tableQueryDialog("");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @wbp.parser.constructor
	 */
	public tableQueryDialog(String sql,boolean singleSelect) {
		this(sql);
		if(singleSelect){
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
	}

	/**
	 * Create the dialog.
	 */
	public tableQueryDialog(String sql) {
		tableQuery = this;
		this.querySQL = sql;
		setBounds(100, 100, 800, 600);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, BorderLayout.CENTER);
			{
				table = new PBSUIBaseGrid();
				table.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if(e.getClickCount()>=2){
							Object[] name = dm.getCols();
							Object[] value = table.getRowData(table.getSelectedRow());
							resultDM = new DataManager();
							resultDM.addCols((String[]) name);
							resultDM.AddNewRow(value);
							tableQuery.dispose();
						}
					}
				});
				scrollPane.setViewportView(table);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.LEFT));
			getContentPane().add(buttonPane, BorderLayout.NORTH);
			{
				cbFieldList = new JComboBox();
				buttonPane.add(cbFieldList);
			}
			{
				JLabel label = new JLabel("=");
				buttonPane.add(label);
			}
			{
				txtFieldValue = new JTextField();
				buttonPane.add(txtFieldValue);
				txtFieldValue.setColumns(10);
			}
			{
				JButton btnQuery = new JButton("\u67E5\u8BE2");
				btnQuery.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(txtFieldValue.getText().trim().equals("")){
							Vector vec = DBOperator.DoSelect(sql);
							if(vec==null || vec.size()==0){
								table.removeRowAll();
							}else{
								table.removeRowAll();
								table.setData(vec);
								table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
								table.setColumnEditableAll(false);
								JTableUtil.fitTableColumnsDoubleWidthNew(table);
								table.setSortEnable();
							}
						}else{
							String sqltmp = "select * from ("+sql + ") t where " + cbFieldList.getSelectedItem().toString() + " like '%"
									+ txtFieldValue.getText().trim() + "%' ";
							Vector vec = DBOperator.DoSelect(sqltmp);
							if(vec==null || vec.size()==0){
								table.removeRowAll();
							}else{
								table.removeRowAll();
								table.setData(vec);
								table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
								table.setColumnEditableAll(false);
								JTableUtil.fitTableColumnsDoubleWidthNew(table);
								table.setSortEnable();
							}
						}
					}
				});
				buttonPane.add(btnQuery);
			}
			{
				JButton btnClear = new JButton("\u91CD\u7F6E");
				btnClear.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						txtFieldValue.setText("");
					}
				});
				buttonPane.add(btnClear);
			}
			{
				JButton btnConfirm = new JButton("\u786E\u8BA4");
				btnConfirm.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(table.getRowCount()==0){
							tableQuery.dispose();
							return;
						}
						Object[] name = dm.getCols();
						Object[] value = table.getRowData(table.getSelectedRow());
						if(value==null){
							resultDM = new DataManager();
							resultDM.addCols((String[]) name);
							tableQuery.dispose();
							return;
						}
						resultDM = new DataManager();
						resultDM.addCols((String[]) name);
						resultDM.AddNewRow(value);
						tableQuery.dispose();
					}
				});
				buttonPane.add(btnConfirm);
			}
			{
				JButton btnClose = new JButton("\u5173\u95ED");
				btnClose.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						tableQuery.dispose();
					}
				});
				btnClose.setActionCommand("OK");
				buttonPane.add(btnClose);
				getRootPane().setDefaultButton(btnClose);
			}
		}
		dm = DBOperator.DoSelect2DM(sql);
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		cbFieldList.setModel(model);
		for(int i=0;i<dm.getColCount();i++){
			model.addElement(dm.getCol(i));
		}
		table.setColumn(dm.getCols());
		table.setData(dm.getDataStrings());
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setColumnEditableAll(false);
		JTableUtil.fitTableColumnsDoubleWidthNew(table);
		table.setSortEnable();
	}

}
