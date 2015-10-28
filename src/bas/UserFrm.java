package bas;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import DBUtil.DBConnectionManager;
import DBUtil.DBOperator;
import comUtil.MD5;
import comUtil.comData;
import dmdata.DataManager;
import main.PBSUIBaseGrid;
import sys.InnerFrame;
import sys.JTableUtil;
import sys.MainFrm;
import sys.Message;
import sys.QueryDialog;
import sys.ToolBarItem;
import sys.selectMenu;

import javax.swing.ListSelectionModel;
import javax.swing.JCheckBox;
import javax.swing.JPasswordField;

public class UserFrm extends InnerFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5863517916729045124L;
	private JPanel contentPane;
	private static volatile UserFrm instance;
	private static boolean isOpen = false;
	private JButton btnAdd;
	private JButton btnModify;
	private JButton btnDelete;
	private JButton btnQuery;
	private JButton btnSave;
	private JButton btnCancel;
	private JButton btnClose;
	private JPanel centerPanel;
	private JPanel editPanel;
	private JLabel warehouseLabel;
	private JLabel label_person;
	private JTextField txt_user_name;
	private JLabel lable_password;
	private JPasswordField txt_user_pwd;
	private JScrollPane scrollPane;
	private PBSUIBaseGrid table_user;
	private JCheckBox chckbx_active;
	int lastStatus;
	private String set_role_name = ""; 
	UserFrm self;
	public static UserFrm getInstance() {
		if(instance == null) { 
			 synchronized(UserFrm.class){
				 if(instance == null) {
					 instance = new UserFrm();
				 }
			 }
	            try {
					instance.setMaximum(true);
				} catch (PropertyVetoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
	        }  
	        return instance;
		 
	 }
	
	public static synchronized boolean getOpenStatus() {
		 if(instance == null) {    
	            instance = new UserFrm();  
	        }  
	        return isOpen;
		 
	 }
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserFrm frame = new UserFrm();
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
	public UserFrm() {
		self = this;
		BasicInternalFrameUI ui = (BasicInternalFrameUI)this.getUI(); //去除标题栏
		ui.getNorthPane().setVisible(false); 
		// 向侦听器列表添加一个 VetoableChangeListener。为所有属性注册该侦听器
//		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.addVetoableChangeListener(new VetoableChangeListener() {

			public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException {
//				JInternalFrame frame = (JInternalFrame) e.getSource();
				String name = e.getPropertyName();
				Object value = e.getNewValue();
				if (name.equals("closed") && value.equals(Boolean.TRUE)) // 窗口被关闭
				{
					System.out.println("UserFrm窗口被关闭");
					instance = null;
					isOpen = false;
				}
			}
		});
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				// write you code here
				isOpen = true;
			}
		});
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 796, 447);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel topPanel = new JPanel();
		contentPane.add(topPanel, BorderLayout.NORTH);
		topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		
		btnAdd = new JButton("\u65B0\u589E");
		btnAdd.addActionListener(addListener);
		topPanel.add(btnAdd);
		
		btnModify = new JButton("\u4FEE\u6539");
		btnModify.addActionListener(modifyListener);
		topPanel.add(btnModify);
		
		btnDelete = new JButton("\u5220\u9664");
		btnDelete.addActionListener(deleteListener);
		topPanel.add(btnDelete);
		
		btnQuery = new JButton("\u67E5\u8BE2");
		btnQuery.addActionListener(queryListener);
		topPanel.add(btnQuery);
		
		btnSave = new JButton("\u4FDD\u5B58");
		btnSave.addActionListener(saveListener);
		topPanel.add(btnSave);
		
		btnCancel = new JButton("\u53D6\u6D88");
		btnCancel.addActionListener(cancelListener);
		topPanel.add(btnCancel);
		
		btnClose = new JButton("\u5173\u95ED");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					instance.setClosed(true);
				} catch (PropertyVetoException e) {
					e.printStackTrace();
				}
			}
		});
		topPanel.add(btnClose);
		
		centerPanel = new JPanel();
		contentPane.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new BorderLayout(0, 0));
		
		editPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) editPanel.getLayout();
		flowLayout.setAlignOnBaseline(true);
		flowLayout.setAlignment(FlowLayout.LEFT);
		flowLayout.setVgap(10);
		centerPanel.add(editPanel, BorderLayout.NORTH);
		
		warehouseLabel = new JLabel("*\u4ED3\u5E93\u53F7:");
		editPanel.add(warehouseLabel);
		
		cb_warehouse = new JComboBox();
		cb_warehouse.setMaximumRowCount(15);
		editPanel.add(cb_warehouse);
		
		lblNewLabel = new JLabel("用户编码：");
		editPanel.add(lblNewLabel);
		
		txt_user_code = new JTextField();
		txt_user_code.setEditable(false);
		editPanel.add(txt_user_code);
		txt_user_code.setColumns(10);
		
		lblNewLabel_1 = new JLabel("*\u767B\u5F55\u8D26\u6237");
		editPanel.add(lblNewLabel_1);
		
		txt_login_code = new JTextField();
		txt_login_code.setEditable(false);
		editPanel.add(txt_login_code);
		txt_login_code.setColumns(10);
		
		label_person = new JLabel("*用户名称：");
		editPanel.add(label_person);
		
		txt_user_name = new JTextField();
		editPanel.add(txt_user_name);
		txt_user_name.setColumns(10);
		
		lable_password = new JLabel("*密码：");
		editPanel.add(lable_password);
		
		txt_user_pwd = new JPasswordField();
		editPanel.add(txt_user_pwd);
		txt_user_pwd.setColumns(10);
		
		JLabel label_active = new JLabel("是否有效：");
		editPanel.add(label_active);
		
		chckbx_active = new JCheckBox("");
		editPanel.add(chckbx_active);
		
		btnRole = new JButton("\u6743\u9650\u8BBE\u7F6E");
		btnRole.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Set<String> roleNameSet = new HashSet<String>();
				if(set_role_name.trim().length()>0){
					String[] temp = set_role_name.trim().split(",");
					for(int i=0;i<temp.length;i++){
						roleNameSet.add(temp[i]);
					}
				}
				UserRoleDialog menu = new UserRoleDialog(self,roleNameSet);
				Toolkit toolkit = Toolkit.getDefaultToolkit();
				int x = (int)(toolkit.getScreenSize().getWidth()-menu.getWidth())/2;
				int y = (int)(toolkit.getScreenSize().getHeight()-menu.getHeight())/2;
				menu.setLocation(x, y);
				menu.setModal(true);
				menu.setVisible(true);
			}
		});
		editPanel.add(btnRole);
		
		scrollPane = new JScrollPane();
		centerPanel.add(scrollPane, BorderLayout.CENTER);
		
		
		table_user = new PBSUIBaseGrid();
		scrollPane.setViewportView(table_user);
		
//		scrollPane.setViewportView(table_user);
//		DefaultTableModel model_tableUser = (DefaultTableModel) table_user.getModel();
//		model_tableUser.addColumn("序号");
//		model_tableUser.addColumn("仓库号");
//		model_tableUser.addColumn("用户编码");
//		model_tableUser.addColumn("登录账号");
//		model_tableUser.addColumn("用户名称");
//		model_tableUser.addColumn("密码");
//		model_tableUser.addColumn("用户角色");
//		model_tableUser.addColumn("是否启用");
//		table_user.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//		JTableUtil.fitTableColumns(table_user);
		init();
	}
	private void init(){
		enableEditComp(false);
		enableSaveCancelButton(false);
		initData("");
		initWarehouseList();
//		hideColumn(table_user,6);
		table_user.addMouseListener(mouseAdapter);
	}
	MouseAdapter mouseAdapter = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()>=2){
					table_user.setColumnSelectionAllowed(true);
				}
				int r = table_user.getSelectedRow();
				String WAREHOUSE_CODE = table_user.getValueAt(r, table_user.getColumnModel().getColumnIndex("仓库号")).toString();
				String USER_CODE = table_user.getValueAt(r, table_user.getColumnModel().getColumnIndex("用户编码")).toString();
				String LOGIN_CODE = table_user.getValueAt(r, table_user.getColumnModel().getColumnIndex("登录账号")).toString();
				String USER_NAME = table_user.getValueAt(r, table_user.getColumnModel().getColumnIndex("用户名称")).toString();
				String PASSWORD = table_user.getValueAt(r, table_user.getColumnModel().getColumnIndex("密码")).toString();
				String ROLE_NAME = NulltoSpace(table_user.getValueAt(r, table_user.getColumnModel().getColumnIndex("用户权限")));
				String active = table_user.getValueAt(r, table_user.getColumnModel().getColumnIndex("是否启用")).toString();
				setSelectedWarehouse(WAREHOUSE_CODE);
				txt_user_code.setText(USER_CODE);
				txt_login_code.setText(LOGIN_CODE);
				txt_user_name.setText(USER_NAME);
				txt_user_pwd.setText(PASSWORD);
				if(active.equals("是")){
					chckbx_active.setSelected(true);
				}else{
					chckbx_active.setSelected(false);
				}
				set_role_name = ROLE_NAME;
				
			}
		};

	private void initData(String strWhere){
        String sql = "select su.warehouse_code 仓库号,bw.warehouse_name 仓库名称,su.USER_CODE 用户编码,su.LOGIN_CODE 登录账号,su.USER_NAME 用户名称,su.PASSWORD 密码,su.ROLE_NAME 用户权限,case su.active when '1' then '是' else '否' end 是否启用 from sys_user su "
        			+"inner join bas_warehouse bw on su.warehouse_code=bw.warehouse_code "
        			+" where 1=1 ";
        if(!strWhere.equals("")){
			sql = sql + strWhere;
			System.out.println("strWhere = "+strWhere);
		}
		DataManager dm = DBOperator.DoSelect2DM(sql);
		if (table_user.getColumnCount() == 0) {
			table_user.setColumn(dm.getCols());
		}
		table_user.removeRowAll();
		table_user.setData(dm.getDataStrings());
		table_user.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table_user.setColumnEditableAll(false);
		JTableUtil.fitTableColumnsDoubleWidth(table_user);
		table_user.setSortEnable();
		table_user.updateUI();
		if(table_user.getRowCount()>0){
			table_user.setRowSelectionInterval(0, 0);//默认选中第一行
		}
        
	}
	
	public void initWarehouseList(){
		String sql = "select warehouse_code,warehouse_name,port_no from bas_warehouse where 1=1 ";
		try{
			java.sql.Connection con = DBConnectionManager.getInstance().getConnection("wms");
			java.sql.Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			DefaultComboBoxModel model = new DefaultComboBoxModel();
			cb_warehouse.setModel(model);
			while (rs.next()) {
				model.addElement(rs.getString("warehouse_code")+"-"+rs.getString("warehouse_name"));
			}
			DBConnectionManager.getInstance().freeConnection("wms", con);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setSelectedWarehouse(String warehousecode){
		for(int i=0;i<cb_warehouse.getItemCount();i++){
			String value = cb_warehouse.getItemAt(i).toString();
			if(value.split("-")[0].toString().equals(warehousecode)){
				cb_warehouse.setSelectedIndex(i);
				return;
			}
		}
	}
	
	ActionListener saveListener = new ActionListener() {
		
		@SuppressWarnings("deprecation")
		@Override
		public void actionPerformed(ActionEvent e) {
			java.sql.Statement stmt = null;
			java.sql.Connection con = null;
			if(cb_warehouse.getSelectedItem().toString().equals("")||txt_user_name.getText().trim().equals("")
					||txt_user_pwd.getText().trim().equals("") ||txt_login_code.getText().trim().equals("")
					){
				Message.showInfomationMessage("* 为必选项!");
			}else if(lastStatus==ToolBarItem.Create){
//				System.out.println("set_role_name  =  "+set_role_name);
				try {
					con = DBConnectionManager.getInstance().getConnection("wms");
					String UserCode = comData.getValueFromBasNumRule("sys_user", "user_code");
					String is_active = chckbx_active.isSelected()?"1":"0";
					String pwd = txt_user_pwd.getText().trim();
					String password = pwd.length()==32?pwd:MD5.GetMD5Code(pwd);
					
					String sql_insert = "insert into sys_user(SYS_USER_ID,WAREHOUSE_CODE,USER_CODE,LOGIN_CODE,USER_NAME,PASSWORD,ACTIVE,CREATED_DTM_LOC,CREATED_BY_USER,ROLE_NAME) "
							+ " value("+UserCode+",'"+cb_warehouse.getSelectedItem().toString().split("-")[0]+"',"+UserCode+",'"+txt_login_code.getText().trim()+"','"+txt_user_name.getText().trim()+"','"+
							password+"'," +is_active+ ", now(),'sys' ,'"+ set_role_name +"' ) ";
					System.out.println(sql_insert);
					stmt = con.createStatement();
					int rst = stmt.executeUpdate(sql_insert);
					if(rst == 1){
						Message.showInfomationMessage("新增成功!");
						enableAll(true);
						initData("");
					}
					DBConnectionManager.getInstance().freeConnection("wms", con);
				} catch (SQLException e1) {
					e1.printStackTrace();
				} 
			}else if(lastStatus==ToolBarItem.Modify){
				try {
					String is_active = chckbx_active.isSelected()?"1":"0";
					String pwd = txt_user_pwd.getText().trim();
					String password = pwd.length()==32?pwd:MD5.GetMD5Code(pwd);
					
					String sql_update = " update sys_user set WAREHOUSE_CODE = '"+cb_warehouse.getSelectedItem().toString().split("-")[0]+
										"', USER_NAME = '"+txt_user_name.getText().trim() + 
										"', PASSWORD = '"+password+
										"', ACTIVE = "+is_active +
										", ROLE_NAME = '"+set_role_name+
										"', UPDATED_BY_USER ='sys' ,UPDATED_DTM_LOC=now() where USER_CODE = '"+ txt_user_code.getText().trim()+"'";
					int rst = DBOperator.DoUpdate(sql_update);
					if(rst==1){
						Message.showInfomationMessage("更新成功!");
						enableAll(true);
						initData("");
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			//用户权限更新
			comData.updateUserInfo();
		}
	};
	ActionListener deleteListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			lastStatus = ToolBarItem.Delete;
			if(table_user.getModel().getRowCount()==0){
				Message.showInfomationMessage("不能进行删除操作还没有数据,请先新增");
				return;
			}
			if(txt_user_code.getText().length()<=0){
				Message.showInfomationMessage("请先点击下面表格选择一行要删除的数据");
				return;
			}
			boolean b_confirm = Message.showOKorCancelMessage("是否删除所选行?");
			if(b_confirm){
				if (txt_user_code.getText().trim().equals("")) {
					Message.showErrorMessage("很抱歉,现在不能删除,出现错误,联系管理员解决");
					return;
				}if (txt_user_code.getText().trim().equals(MainFrm.getUserInfo().getString("USER_CODE", 0))) {
					Message.showErrorMessage("不能删除当前登录账号");
					return;
				}
				try {
					int selectedRow = table_user.getSelectedRow();
					((DefaultTableModel) table_user.getModel()).removeRow(selectedRow);
					java.sql.Connection con = DBConnectionManager.getInstance().getConnection("wms");
					java.sql.Statement stmt = con.createStatement();
					String sql_delete = "delete from sys_user where USER_CODE = '"+ txt_user_code.getText().trim()+"'";
					int rst = stmt.executeUpdate(sql_delete);
					if(rst==1){
						Message.showInfomationMessage("删除成功!");
						enableAll(true);
						initData("");
					}
					stmt.close();
					con.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	};
	ActionListener cancelListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			lastStatus = ToolBarItem.Cancel;
			enableAll(true);
		}
	};
	ActionListener queryListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			ArrayList fieldList = new ArrayList();
			fieldList.add("su.WAREHOUSE_CODE:仓库号");
			fieldList.add("bw.warehouse_name:仓库名称");
			fieldList.add("su.LOGIN_CODE:登录账户");
			fieldList.add("su.USER_NAME:用户名称");
			fieldList.add("su.ACTIVE:是否启用");
			QueryDialog query = QueryDialog.getInstance(fieldList);
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			int x = (int)(toolkit.getScreenSize().getWidth()-query.getWidth())/2;
			int y = (int)(toolkit.getScreenSize().getHeight()-query.getHeight())/2;
			query.setLocation(x, y);
			query.setVisible(true);
			String retWhere = QueryDialog.queryValueResult;
			if(retWhere.length()>0){
				retWhere = " and "+retWhere;
			}
			initData(retWhere);
			
			
		}
	};
	ActionListener modifyListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(table_user.getModel().getRowCount()==0){
				Message.showInfomationMessage("不能进行修改操作还没有数据,请先新增");
				return;
			}
			if(txt_user_code.getText().length()<=0){
				Message.showInfomationMessage("请先点击下面表格选择一行要修改的数据");
				return;
			}
			lastStatus = ToolBarItem.Modify;
			enableCRUDbutton(false);
			enableEditComp(true);
			enableSaveCancelButton(true);
			table_user.setEnabled(false);
			txt_login_code.setEditable(false);
		}
	};
	ActionListener addListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			lastStatus = ToolBarItem.Create;
		    
			enableCRUDbutton(false);
			enableEditComp(true);
			enableSaveCancelButton(true);
			clearEditUI();
			table_user.setEnabled(false);
			table_user.removeMouseListener(mouseAdapter);
			
		}
	};
	public void setRoleName(String roleName){
		this.set_role_name = roleName;
	}
	private JLabel lblNewLabel;
	private JTextField txt_user_code;
	private JComboBox cb_warehouse;
	private JButton btnRole;
	private JLabel lblNewLabel_1;
	private JTextField txt_login_code;
	private void enableAll(boolean b){
		enableCRUDbutton(b);
		enableEditComp(!b);
		enableSaveCancelButton(!b);
		clearEditUI();
		table_user.setEnabled(b);
		if(b==true){
			table_user.addMouseListener(mouseAdapter);
		}
	}
	
	private void enableCRUDbutton(boolean b){
		btnAdd.setEnabled(b);
		btnModify.setEnabled(b);
		btnDelete.setEnabled(b);
		btnQuery.setEnabled(b);
		
	}
	private void enableSaveCancelButton(boolean b){
		btnSave.setEnabled(b);
		btnCancel.setEnabled(b);
	}
	private void enableEditComp(boolean b){
		btnRole.setEnabled(b);
		cb_warehouse.setEnabled(b);
		txt_login_code.setEditable(b);
		txt_user_name.setEditable(b);
		txt_user_pwd.setEditable(b);
		chckbx_active.setEnabled(b);
	}
	private void clearEditUI(){
		txt_login_code.setText("");
		txt_user_code.setText("");
		cb_warehouse.setSelectedIndex(0);
		txt_user_name.setText("");
		txt_user_pwd.setText("");
		chckbx_active.setSelected(true);
		set_role_name = "";
		table_user.clearSelection();
	}
	/*
	 * 返回当前日期  YYMMDD
	 */
	private String getSysDate(){
		SimpleDateFormat df = new SimpleDateFormat("yyMMdd");
		return df.format(new Date());
	}
	public static String NulltoSpace(Object o){
		if(o==null)
			return "";
		else if(o.equals("null")){
			return "";
		}
		else 
			return o.toString().trim();
	}
	/** 
	 * 隐藏表格中的某一列 
	 * @param table  表格 
	 * @param index  要隐藏的列 的索引
	 */ 
	protected void hideColumn(JTable table,int index){ 
	    TableColumn tc= table.getColumnModel().getColumn(index); 
	    tc.setMaxWidth(0); 
	    tc.setPreferredWidth(0); 
	    tc.setMinWidth(0); 
	    tc.setWidth(0); 
	    table.getTableHeader().getColumnModel().getColumn(index).setMaxWidth(0); 
	    table.getTableHeader().getColumnModel().getColumn(index).setMinWidth(0); 
	}

}
