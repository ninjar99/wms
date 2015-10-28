package bas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import DBUtil.DBConnectionManager;
import sys.JTableUtil;

public class UserRoleDialog  extends JDialog{
	
	
	private final JPanel contentPanel = new JPanel();
	UserRoleDialog self;
	Vector columns_table;
	Vector data_table;
	JTable table_user_role;
	UserRoleTableModel UR_TableModel;
	UserFrm userFrm;
	Set<String> roleName = new HashSet<String>();
	public UserRoleDialog(UserFrm userFrm,Set<String> roleName){
		this.userFrm = userFrm;
		self = this;
		this.roleName = roleName;
		self.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
//		self.setLocationRelativeTo(null);
//		self.setModal(true);
//		self.setVisible(true);
		
		setBounds(120, 120, 655, 413);
		
		columns_table = new Vector();
		columns_table.add("是否有权限");
		columns_table.add("权限菜单ID");
		columns_table.add("权限菜单");
		data_table = new Vector();
		UR_TableModel = new UserRoleTableModel(data_table,columns_table);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		panel.add(contentPanel, BorderLayout.CENTER);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(new BorderLayout(0, 0));
		table_user_role = new JTable(UR_TableModel);
		table_user_role.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JTableUtil.fitTableColumns(table_user_role);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(table_user_role);
		contentPanel.add(scrollPane);
		
		JPanel buttonPane = new JPanel();
		panel.add(buttonPane, BorderLayout.SOUTH);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JButton okButton = new JButton("确定");
		okButton.setActionCommand("OK");
		okButton.addActionListener(OKListener);
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		getRootPane().setDefaultButton(okButton);
		JButton cancelButton = new JButton("取消");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				self.dispose();
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		
		init();
	}
	ActionListener OKListener = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			String roleName = "";
			StringBuffer sBuffer = new StringBuffer();
			for(int i=0;i<table_user_role.getRowCount();i++){
				if(table_user_role.getValueAt(i, 0).toString().equals("true")){
					sBuffer = sBuffer.append(table_user_role.getValueAt(i, 1));
					sBuffer = sBuffer.append(",");
				}
			}
			if (sBuffer.length()>0) {
				roleName = sBuffer.substring(0, sBuffer.length()-1);
			}
			userFrm.setRoleName(roleName);
			self.dispose();
		}
	};
	
	private void init(){
		
		initTableData();
	}
	private void initTableData(){
		String sql = "select id,menu_item from sys_user_role_menu_util where 1=1 order by id";
		try {
			java.sql.Connection con = DBConnectionManager.getInstance().getConnection("wms");
			java.sql.Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Vector rowdata = new Vector();
				rowdata.add(false);
				rowdata.add(rs.getString("id"));
				rowdata.add(rs.getString("menu_item"));
				data_table.add(rowdata);
				
			}
			UR_TableModel.setDataVector(data_table, columns_table);
			if(roleName.size()>0){
				for(int i=0;i<table_user_role.getRowCount();i++){
					if(roleName.contains(table_user_role.getValueAt(i, 1))){
						table_user_role.setValueAt(true, i, 0);
					}
				}
			}
			DBConnectionManager.getInstance().freeConnection("wms", con);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	class UserRoleTableModel extends DefaultTableModel{
		public UserRoleTableModel(java.lang.Object[][] d,java.lang.String[] e){
			super(d,e);
		}
		public UserRoleTableModel(Vector data,Vector columns){
			super(data,columns);
		}
		public boolean isCellEditable(int row,int column){
			if (column == 0) {
				return true;
			}else{
				return false;	
			}
		}
		public Class<?> getColumnClass(int columnIndex) {
            if(columnIndex==0){
                return Boolean.class;
            }
            else{
            	return String.class;
            }
        }
	}
	public static void makeFace(JTable table) {
		try {
			DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() {
				public Component getTableCellRendererComponent(JTable table,
						Object value, boolean isSelected, boolean hasFocus,int row, int column) {
					if (row % 2 == 0)
						setBackground(Color.white);
					else if (row % 2 == 1)
						setBackground(new Color(206, 231, 255));
					return super.getTableCellRendererComponent(table, value,isSelected, hasFocus, row, column);
				}
			};
			for (int i = 0; i < table.getColumnCount(); i++) 
				table.getColumn(table.getColumnName(i)).setCellRenderer(tcr);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	
}
