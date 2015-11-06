package bas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import DBUtil.DBOperator;
import comUtil.WMSCombobox;
import comUtil.comData;
import dmdata.DataManager;
import main.PBSUIBaseGrid;
import sys.InnerFrame;
import sys.JTableUtil;
import sys.MainFrm;
import sys.Message;
import sys.QueryDialog;
import sys.ToolBarItem;
import util.Math_SAM;

import javax.swing.JCheckBox;

public class BasContainerFrm extends InnerFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5863517916729045124L;
	private JPanel contentPane;
	private static volatile BasContainerFrm instance;
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
	private JScrollPane scrollPane;
	private PBSUIBaseGrid table;
	private JCheckBox chckbx_active;
	int lastStatus;
	BasContainerFrm self;
	public static BasContainerFrm getInstance() {
		if(instance == null) { 
			 synchronized(BasContainerFrm.class){
				 if(instance == null) {
					 instance = new BasContainerFrm();
				 }
			 }
	        }  
	        return instance;
		 
	 }
	
	public static synchronized boolean getOpenStatus() {
		 if(instance == null) {    
	            instance = new BasContainerFrm();  
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
					BasContainerFrm frame = new BasContainerFrm();
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
	public BasContainerFrm() {
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
					System.out.println("BasContainerFrm窗口被关闭");
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
		
		cb_warehouse = new WMSCombobox("SELECT WAREHOUSE_CODE,WAREHOUSE_name FROM bas_warehouse",true);
		editPanel.add(cb_warehouse);
		
		lblNewLabel = new JLabel("*\u7BB1\u53F7\uFF1A");
		editPanel.add(lblNewLabel);
		
		txt_container_code = new JTextField();
		txt_container_code.setEditable(false);
		editPanel.add(txt_container_code);
		txt_container_code.setColumns(10);
		
		label_person = new JLabel("*\u7528\u9014\u7C7B\u578B\uFF1A");
		editPanel.add(label_person);
		
		cb_use_type = new WMSCombobox("select 'normal' type_code,'固定' type_name union all select 'temp' type_code,'临时' type_name ",true);
		editPanel.add(cb_use_type);
		
		lblNewLabel_1 = new JLabel("*\u5BB9\u5668\u7C7B\u578B\uFF1A");
		editPanel.add(lblNewLabel_1);
		
		cb_container_type = new WMSCombobox("select 'inb' type_code,'入库收货' type_name union all select 'pick' type_code,'出库拣货' type_name union all select 'mov' type_code,'库内移库' type_name ",true);
		editPanel.add(cb_container_type);
		
		JLabel label_active = new JLabel("是否有效：");
		editPanel.add(label_active);
		
		chckbx_active = new JCheckBox("");
		editPanel.add(chckbx_active);
		
		scrollPane = new JScrollPane();
		centerPanel.add(scrollPane, BorderLayout.CENTER);
		
		
		table = new PBSUIBaseGrid();
		scrollPane.setViewportView(table);
		init();
	}
	private void init(){
		enableEditComp(false);
		enableSaveCancelButton(false);
		initData("");
//		initWarehouseList();
//		hideColumn(table_user,6);
		table.addMouseListener(mouseAdapter);
	}
	MouseAdapter mouseAdapter = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()>=2){
					table.setColumnSelectionAllowed(true);
				}
				int r = table.getSelectedRow();
				String warehouseName = table.getValueAt(r, table.getColumnModel().getColumnIndex("仓库名称")).toString();
				String containerCode = table.getValueAt(r, table.getColumnModel().getColumnIndex("箱号")).toString();
				String useType = table.getValueAt(r, table.getColumnModel().getColumnIndex("用途类型")).toString();
			    String containerType = table.getValueAt(r, table.getColumnModel().getColumnIndex("容器类型")).toString();
			    String active = table.getValueAt(r, table.getColumnModel().getColumnIndex("是否启用")).toString();
				cb_warehouse.setSelectedItem(warehouseName);
				cb_use_type.setSelectedItem(useType);
				cb_container_type.setSelectedItem(containerType);
				txt_container_code.setText(containerCode);
				if(active.equals("启用")){
					chckbx_active.setSelected(true);
				}else{
					chckbx_active.setSelected(false);
				}
				
			}
		};

	private void initData(String strWhere){
		String sql = "select bc.CONTAINER_CODE 箱号,bc.WAREHOUSE_CODE 仓库编码,bw.WAREHOUSE_NAME 仓库名称, "
				+"case bc.CONTAINER_TYPE_CODE when 'inb' then '入库收货' when 'pick' then '出库拣货' when 'mov' then '库内移库' else bc.CONTAINER_TYPE_CODE end 容器类型,"
				+"case bc.USE_TYPE when 'normal' then '固定' when 'temp' then '临时' else bc.USE_TYPE end 用途类型,"
				+"case bc.`STATUS` when '0' then '空' when '1'then '入库收货中' when '2' then '入库收货完成' else bc.`STATUS` end 状态,"
				+"case bc.IS_ACTIVE when '0'then '不启用' when '1' then '启用' end 是否启用  "
				+"from bas_container bc "
				+"inner join bas_warehouse bw on bc.WAREHOUSE_CODE=bw.WAREHOUSE_CODE "
				+"where 1=1 and bc.warehouse_code='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' ";
		if(!strWhere.equals("")){
			sql = sql + strWhere;
		}
		DataManager dm = DBOperator.DoSelect2DM(sql);
		if (table.getColumnCount() == 0) {
			table.setColumn(dm.getCols());
		}
		table.removeRowAll();
		table.setData(dm.getDataStrings());
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setColumnEditableAll(false);
		JTableUtil.fitTableColumnsDoubleWidth(table);
		table.setSortEnable();
		tableRowColorSetup(table);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void tableRowColorSetup(PBSUIBaseGrid tab){
		Vector cellColor = new Vector();
		for(int i=0;i<tab.getRowCount();i++){
//			Vector rowColor = new Vector();
			String status = (String) tab.getValueAt(i, tab.getColumnModel().getColumnIndex("状态"));
			String atcive = (String) tab.getValueAt(i, tab.getColumnModel().getColumnIndex("是否启用"));
			if(!status.equals("空")){
				Object[] rc1Cell = new Object[3];
		        rc1Cell[0] = new Integer(i);
		        rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("状态"));
		        rc1Cell[2] = status.equals("入库收货中")?Color.ORANGE:Color.green;
		        cellColor.addElement(rc1Cell);
//		        rowColor.addElement(new Integer(i));
//		        detailTable.setRowColor(rowColor, Color.lightGray);
			}
			if(atcive.equals("不启用")){
				Object[] rc1Cell = new Object[3];
		        rc1Cell[0] = new Integer(i);
		        rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("是否启用"));
		        rc1Cell[2] = Color.red;
		        cellColor.addElement(rc1Cell);
			}
		}
		tab.setCellColor(cellColor);
	}
	
//	public void initWarehouseList(){
//		String sql = "select warehouse_code,warehouse_name,port_no from bas_warehouse where 1=1 ";
//		try{
//			java.sql.Connection con = DBConnectionManager.getInstance().getConnection("wms");
//			java.sql.Statement stmt = con.createStatement();
//			ResultSet rs = stmt.executeQuery(sql);
//			DefaultComboBoxModel model = new DefaultComboBoxModel();
//			cb_warehouse.setModel(model);
//			while (rs.next()) {
//				model.addElement(rs.getString("warehouse_code")+"-"+rs.getString("warehouse_name"));
//			}
//			DBConnectionManager.getInstance().freeConnection("wms", con);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	private void setSelectedWarehouse(String warehousecode){
//		for(int i=0;i<cb_warehouse.getItemCount();i++){
//			String value = cb_warehouse.getItemAt(i).toString();
//			if(value.split("-")[0].toString().equals(warehousecode)){
//				cb_warehouse.setSelectedIndex(i);
//				return;
//			}
//		}
//	}
	
	ActionListener saveListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			String containerCodeTmp = "";
			if (cb_warehouse.getSelectedItem().toString().equals("") || cb_use_type.getSelectedItem().toString().equals("") 
					|| cb_container_type.getSelectedItem().toString().equals("")) {
				Message.showInfomationMessage("* 为必选项!");
				return;
			} else if (lastStatus == ToolBarItem.Create) {
				chckbx_active.isSelected();
				String warehouseCode = cb_warehouse.getSelectedOID();
				String useType = cb_use_type.getSelectedOID();
				String containerType = cb_container_type.getSelectedOID();
				String containerCode = comData.getValueFromBasNumRule("bas_container", "container_code");
				containerCodeTmp = containerCode;
				txt_container_code.setText(containerCode);
				String sql = "insert into bas_container(CONTAINER_CODE,WAREHOUSE_CODE,CONTAINER_TYPE_CODE,USE_TYPE,STATUS) "
						+ "select '" + containerCode + "','"+warehouseCode+"','"+containerType+"','"+useType+"','0' ";
				int rst = DBOperator.DoUpdate(sql);
				if (rst == 1) {
					txt_container_code.setText(containerCode);
					Message.showInfomationMessage("新增成功!");
					enableAll(true);
					initData("");
				}else{
					Message.showErrorMessage("新增失败!");
					return;
				}
			} else if (lastStatus == ToolBarItem.Modify) {
				try {
					containerCodeTmp = txt_container_code.getText().trim();
					String useType = cb_use_type.getSelectedOID();
					String containerType = cb_container_type.getSelectedOID();
					String isActive = chckbx_active.isSelected()==true?"1":"0";
					String sql_update = "update bas_container set CONTAINER_TYPE_CODE='"+containerType+"',USE_TYPE='"+useType+"',IS_ACTIVE="+isActive+" "
							+"where container_code='"+txt_container_code.getText().trim()+"'";
					int rst = DBOperator.DoUpdate(sql_update);
					if (rst == 1) {
						Message.showInfomationMessage("更新成功!");
						enableAll(true);
						initData("");
					}else{
						Message.showErrorMessage("更新失败!");
						return;
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			//滚动 定位Table保存数据行
			Rectangle rect = new Rectangle(0, table.getHeight(), 20, 20);
			table.scrollRectToVisible(rect);
			table.setRowSelectionInterval(table.getRowCount() - 1, table.getRowCount() - 1);
			table.grabFocus();
			table.changeSelection(table.getRowIndexByColIndexAndColValue(table.getColumnModel().getColumnIndex("箱号"),containerCodeTmp), 0, false, true);
		}
		
	};
	ActionListener deleteListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			lastStatus = ToolBarItem.Delete;
			if(table.getModel().getRowCount()==0){
				Message.showInfomationMessage("不能进行删除操作还没有数据,请先新增");
				return;
			}
			if(txt_container_code.getText().length()<=0){
				Message.showInfomationMessage("请先点击下面表格选择一行要删除的数据");
				return;
			}
			boolean b_confirm = Message.showOKorCancelMessage("是否删除所选行?");
			if(b_confirm){
				try {
					String sql_delete = "select bs.container_code from bas_container bs "
							+"where (exists(select ii.CONTAINER_CODE from inv_inventory ii where ii.container_code=bs.container_code and ii.WAREHOUSE_CODE=bs.WAREHOUSE_CODE) "
							+"or exists(select ird.CONTAINER_CODE from inb_receipt_header irh inner join inb_receipt_detail ird on irh.INB_RECEIPT_HEADER_ID=ird.INB_RECEIPT_HEADER_ID  "
							+"where irh.`STATUS`<'900' and ird.CONTAINER_CODE=bs.CONTAINER_CODE and bs.WAREHOUSE_CODE = ird.WAREHOUSE_CODE) "
							+") and bs.container_code='"+txt_container_code.getText().trim()+"' ";
					DataManager dm = DBOperator.DoSelect2DM(sql_delete);
					if(dm==null || dm.getCurrentCount()==0){
						
					}else{
						Message.showWarningMessage("此箱号已经在系统使用，不能删除");
						return;
					}
					
					sql_delete = "delete from bas_container where container_code = '"+ txt_container_code.getText().trim()+"'";
					int rst = DBOperator.DoUpdate(sql_delete);
					if(rst==1){
						Message.showInfomationMessage("删除成功!");
						enableAll(true);
						initData("");
					}
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
			fieldList.add("bc.WAREHOUSE_CODE:仓库号");
			fieldList.add("bw.WAREHOUSE_NAME:仓库名称");
			fieldList.add("bc.CONTAINER_CODE:箱号");
			fieldList.add("bc.IS_ACTIVE:是否启用");
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
			if(table.getModel().getRowCount()==0){
				Message.showInfomationMessage("不能进行修改操作还没有数据,请先新增");
				return;
			}
			if(txt_container_code.getText().length()<=0){
				Message.showInfomationMessage("请先点击下面表格选择一行要修改的数据");
				return;
			}
			lastStatus = ToolBarItem.Modify;
			enableCRUDbutton(false);
			enableEditComp(true);
			enableSaveCancelButton(true);
			table.setEnabled(false);
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
			cb_warehouse.setSelectedDisplayName(MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0));
			table.setEnabled(false);
			table.removeMouseListener(mouseAdapter);
			
		}
	};
	public void setRoleName(String roleName){
	}
	private JLabel lblNewLabel;
	private JTextField txt_container_code;
	private WMSCombobox cb_warehouse;
	private WMSCombobox cb_use_type;
	private JLabel lblNewLabel_1;
	private WMSCombobox cb_container_type;
	private void enableAll(boolean b){
		enableCRUDbutton(b);
		enableEditComp(!b);
		enableSaveCancelButton(!b);
		clearEditUI();
		table.setEnabled(b);
		if(b==true){
			table.addMouseListener(mouseAdapter);
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
		cb_warehouse.setEnabled(b);
		chckbx_active.setEnabled(b);
		cb_use_type.setEnabled(b);
		cb_container_type.setEnabled(b);
	}
	private void clearEditUI(){
		txt_container_code.setText("");
		cb_warehouse.setSelectedIndex(0);
		cb_use_type.setSelectedIndex(0);
		cb_container_type.setSelectedIndex(0);
		txt_container_code.setText("");
		chckbx_active.setSelected(true);
		table.clearSelection();
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

}
