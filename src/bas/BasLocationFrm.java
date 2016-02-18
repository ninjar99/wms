package bas;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;

import DBUtil.DBOperator;
import comUtil.WMSCombobox;
import dmdata.DataManager;
import main.PBSUIBaseGrid;
import sys.InnerFrame;
import sys.JTableUtil;
import sys.MainFrm;
import sys.Message;
import sys.QueryDialog;
import sys.ToolBarItem;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.DefaultComboBoxModel;

public class BasLocationFrm extends InnerFrame{
	private static final long serialVersionUID = 1L;
	private static volatile BasLocationFrm instance;
	private static boolean isOpen = false;
	
	private JPanel contentPane;
	private JButton btnAdd;
	private JButton btnModify;
	private JButton btnDelete;
	private JButton btnQuery;
	private JButton btnSave;
	private JButton btnCancel;
	private JButton btnClose;
	private JPanel centerPanel;
	private JPanel editPanel;
	private PBSUIBaseGrid table_location;
	int lastStatus;
	private JPanel panel;
	private WMSCombobox comboBox_warehouseCode;
	private JTextField textField_LOCATION_CODE;
	private JTextField textField_LOGICAL_LOCATION_CODE;
	private JTextField textField_TEMPLATE1;
	private JTextField textField_TEMPLATE2;
	private JTextField textField_TEMPLATE3;
	private JTextField textField_TEMPLATE4;
	private JTextField textField_TEMPLATE5;

	
	public BasLocationFrm(){
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
		centerPanel.add(editPanel, BorderLayout.NORTH);
		editPanel.setLayout(new BorderLayout(0, 0));
		
		panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setVgap(8);
		flowLayout.setAlignment(FlowLayout.LEFT);
		editPanel.add(panel, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("*\u4ED3\u5E93\u53F7:");
		panel.add(lblNewLabel);
		
		comboBox_warehouseCode = new WMSCombobox("SELECT WAREHOUSE_CODE,WAREHOUSE_name FROM bas_warehouse",true);
		panel.add(comboBox_warehouseCode);
		
		JLabel lblNewLabel_1 = new JLabel("*\u5E93\u4F4D\u6761\u7801:");
		panel.add(lblNewLabel_1);
		
		textField_LOCATION_CODE = new JTextField();
		panel.add(textField_LOCATION_CODE);
		textField_LOCATION_CODE.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("*\u903B\u8F91\u5E93\u4F4D\u6761\u7801:");
		panel.add(lblNewLabel_2);
		
		textField_LOGICAL_LOCATION_CODE = new JTextField();
		panel.add(textField_LOGICAL_LOCATION_CODE);
		textField_LOGICAL_LOCATION_CODE.setColumns(10);
		
		lblNewLabel_8 = new JLabel("\u5E93\u4F4D\u7C7B\u578B:");
		panel.add(lblNewLabel_8);
		
		cb_location_type = new JComboBox();
		cb_location_type.setEnabled(false);
		cb_location_type.setModel(new DefaultComboBoxModel(new String[] {"Normal", "Dock", "Damage"}));
		cb_location_type.setSelectedIndex(0);
		panel.add(cb_location_type);
		
		JPanel panel_1 = new JPanel();
		FlowLayout fl_panel_1 = (FlowLayout) panel_1.getLayout();
		fl_panel_1.setAlignment(FlowLayout.LEFT);
		editPanel.add(panel_1, BorderLayout.CENTER);
		
		JLabel lblNewLabel_3 = new JLabel("\u5C5E\u60271:");
		panel_1.add(lblNewLabel_3);
		
		textField_TEMPLATE1 = new JTextField();
		panel_1.add(textField_TEMPLATE1);
		textField_TEMPLATE1.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel("\u5C5E\u60272:");
		panel_1.add(lblNewLabel_4);
		
		textField_TEMPLATE2 = new JTextField();
		panel_1.add(textField_TEMPLATE2);
		textField_TEMPLATE2.setColumns(10);
		
		JLabel lblNewLabel_5 = new JLabel("\u5C5E\u60273:");
		panel_1.add(lblNewLabel_5);
		
		textField_TEMPLATE3 = new JTextField();
		panel_1.add(textField_TEMPLATE3);
		textField_TEMPLATE3.setColumns(10);
		
		JLabel lblNewLabel_6 = new JLabel("\u5C5E\u60274:");
		panel_1.add(lblNewLabel_6);
		
		textField_TEMPLATE4 = new JTextField();
		panel_1.add(textField_TEMPLATE4);
		textField_TEMPLATE4.setColumns(10);
		
		JLabel lblNewLabel_7 = new JLabel("\u5C5E\u60275:");
		panel_1.add(lblNewLabel_7);
		
		textField_TEMPLATE5 = new JTextField();
		panel_1.add(textField_TEMPLATE5);
		textField_TEMPLATE5.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		centerPanel.add(scrollPane, BorderLayout.CENTER);
		
		table_location = new PBSUIBaseGrid();
		table_location.setSortEnable();
		table_location.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_location.setColumnEditableAll(false);
		scrollPane.setViewportView(table_location);

		String[] RHColumnNames ={"序号","仓库号","库位条码","逻辑库位条码","库位类型","属性1","属性2","属性3","属性4","属性5","库存数量"};
		table_location.setColumn(RHColumnNames);
		table_location.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JTableUtil.fitTableColumns(table_location);
		init();
		
		
		BasicInternalFrameUI ui = (BasicInternalFrameUI)this.getUI(); //去除标题栏
		ui.getNorthPane().setVisible(false); 
		this.addVetoableChangeListener(new VetoableChangeListener() {

			public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException {
				
				String name = e.getPropertyName();
				Object value = e.getNewValue();
				if (name.equals("closed") && value.equals(Boolean.TRUE)) // 窗口被关闭
				{
					System.out.println("BasLocationFrm窗口被关闭");
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
	}
	private void init(){
		enableEditComp(false);
		enableSaveCancelButton(false);
		initTableData("");
		
		table_location.addMouseListener(mouseAdapter);
	}
	MouseAdapter mouseAdapter = new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount()>=2){
				table_location.setColumnSelectionAllowed(true);
			}
			int r = table_location.getSelectedRow();
			comboBox_warehouseCode.setSelectedItem(table_location.getValueAt(r, table_location.getColumnModel().getColumnIndex("仓库号")));
			cb_location_type.setSelectedItem(table_location.getValueAt(r, table_location.getColumnModel().getColumnIndex("库位类型")));
			textField_LOCATION_CODE.setText(NulltoSpace(table_location.getValueAt(r, table_location.getColumnModel().getColumnIndex("库位条码"))));
			textField_LOGICAL_LOCATION_CODE.setText(NulltoSpace(table_location.getValueAt(r, table_location.getColumnModel().getColumnIndex("逻辑库位条码"))));
			textField_TEMPLATE1.setText(NulltoSpace(table_location.getValueAt(r, table_location.getColumnModel().getColumnIndex("属性1"))));
			textField_TEMPLATE2.setText(NulltoSpace(table_location.getValueAt(r, table_location.getColumnModel().getColumnIndex("属性2"))));
			textField_TEMPLATE3.setText(NulltoSpace(table_location.getValueAt(r, table_location.getColumnModel().getColumnIndex("属性3"))));
			textField_TEMPLATE4.setText(NulltoSpace(table_location.getValueAt(r, table_location.getColumnModel().getColumnIndex("属性4"))));
			textField_TEMPLATE5.setText(NulltoSpace(table_location.getValueAt(r, table_location.getColumnModel().getColumnIndex("属性5"))));
		}
	};
	ActionListener addListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			lastStatus = ToolBarItem.Create;
		    	
			enableCRUDbutton(false);
			enableEditComp(true);
			enableSaveCancelButton(true);
			clearEditUI();
			comboBox_warehouseCode.setSelectedDisplayName(MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0));
			table_location.setEnabled(false);
			table_location.removeMouseListener(mouseAdapter);
		}
	};
	ActionListener saveListener = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			String warehouseCode = comboBox_warehouseCode.getSelectedOID();
			String LOCATION_CODE = textField_LOCATION_CODE.getText().trim();
			String LOGICAL_LOCATION_CODE = textField_LOGICAL_LOCATION_CODE.getText().trim();
			String location_type = cb_location_type.getSelectedItem().toString();
			String TEMPLATE1 = textField_TEMPLATE1.getText().trim();
			String TEMPLATE2 = textField_TEMPLATE2.getText().trim();
			String TEMPLATE3 = textField_TEMPLATE3.getText().trim();
			String TEMPLATE4 = textField_TEMPLATE4.getText().trim();
			String TEMPLATE5 = textField_TEMPLATE5.getText().trim();
			if(warehouseCode.equals("")||LOCATION_CODE.equals("")||LOGICAL_LOCATION_CODE.equals("")){
				Message.showInfomationMessage("* 为必选项!");
			}else if(lastStatus==ToolBarItem.Create){
				String validateSql = "SELECT BAS_LOCATION_ID FROM bas_location WHERE WAREHOUSE_CODE = '"
						+warehouseCode+"' AND LOCATION_CODE = '"+LOCATION_CODE+"'";
				DataManager dmtmp2 = DBOperator.DoSelect2DM(validateSql);
				if(dmtmp2.getCurrentCount()>0){
					Message.showInfomationMessage("仓库号为 "+comboBox_warehouseCode.getSelectedItem().toString()+
							" 库位条码为"+LOCATION_CODE+" 的信息已存在,不能添加");
				}else{
					String sql = "insert into bas_location(WAREHOUSE_CODE,LOCATION_CODE,LOGICAL_LOCATION_CODE,LOCATION_TYPE_CODE,TEMPLATE_FIELD1,"
							+"TEMPLATE_FIELD2,TEMPLATE_FIELD3,TEMPLATE_FIELD4,TEMPLATE_FIELD5,CREATED_BY_USER,CREATED_DTM_LOC)"
							+" value('"+warehouseCode+"','"+LOCATION_CODE+"','"+LOGICAL_LOCATION_CODE+"','"+location_type+"','"+TEMPLATE1
							+"','"+TEMPLATE2+"','"+TEMPLATE3+"','"+TEMPLATE4+"','"+TEMPLATE5
							+"','"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"',now())";
					System.out.println("sql = "+sql);
					int t = DBOperator.DoUpdate(sql);
					if(t>0){
						Message.showInfomationMessage("保存成功!");
						enableAll(true);
						initTableData("");
					}else{
						Message.showInfomationMessage("出错了,保存失败!");
					}
				}
			}else if (lastStatus == ToolBarItem.Modify) {
				String sql_update = " update bas_location set LOGICAL_LOCATION_CODE = '"+LOGICAL_LOCATION_CODE
						+"',LOCATION_TYPE_CODE='"+location_type
						+"',TEMPLATE_FIELD1='"+TEMPLATE1
						+"',TEMPLATE_FIELD2='"+TEMPLATE2
						+"',TEMPLATE_FIELD3='"+TEMPLATE3
						+"',TEMPLATE_FIELD4='"+TEMPLATE4
						+"',TEMPLATE_FIELD5='"+TEMPLATE5
						+"',UPDATED_BY_USER ='"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"',UPDATED_DTM_LOC = now() "
						+" where WAREHOUSE_CODE = '"+warehouseCode+"' and LOCATION_CODE ='"+LOCATION_CODE+"'";
				System.out.println("sql update = "+sql_update);
				int t = DBOperator.DoUpdate(sql_update);
				if(t>0){
					Message.showInfomationMessage("更新成功!");
					enableAll(true);
					initTableData("");
				}else{
					Message.showInfomationMessage("出错了,更新失败!");
				}
			}
			//滚动 定位Table保存数据行
			Rectangle rect = new Rectangle(0, table_location.getHeight(), 20, 20);
			table_location.scrollRectToVisible(rect);
			table_location.setRowSelectionInterval(table_location.getRowCount() - 1, table_location.getRowCount() - 1);
			table_location.grabFocus();
			table_location.changeSelection(table_location.getRowIndexByColIndexAndColValue(table_location.getColumnModel().getColumnIndex("库位条码"),LOCATION_CODE), 0, false, true);
		}
	};
	ActionListener modifyListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(table_location.getModel().getRowCount()==0){
				Message.showInfomationMessage("不能进行修改操作还没有数据,请先新增");
				return;
			}
			if(textField_LOCATION_CODE.getText().length()<=0){
				Message.showInfomationMessage("请先点击下面表格选择一行要修改的数据");
				return;
			}
			lastStatus = ToolBarItem.Modify;
			enableCRUDbutton(false);
			enableEditComp(true);
			comboBox_warehouseCode.setEnabled(false);	
			textField_LOCATION_CODE.setEditable(false);
			enableSaveCancelButton(true);
			table_location.setEnabled(false);
		}
	};
	ActionListener deleteListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			lastStatus = ToolBarItem.Delete;
			if(table_location.getModel().getRowCount()==0){
				Message.showInfomationMessage("不能进行删除操作还没有数据,请先新增");
				return;
			}
			if(textField_LOCATION_CODE.getText().length()<=0){
				Message.showInfomationMessage("请先点击下面表格选择一行要删除的数据");
				return;
			}
			boolean b_confirm = Message.showOKorCancelMessage("是否删除所选行?");
			if(b_confirm){
				String LOCATION_CODE = textField_LOCATION_CODE.getText().trim();
				String warehouseCode = comboBox_warehouseCode.getSelectedOID();
				try {
					
					String sql_delete = "select bl.LOCATION_CODE from bas_location bl "
							+"where exists(select * from inv_inventory ii where ii.WAREHOUSE_CODE=bl.WAREHOUSE_CODE and ii.LOCATION_CODE=bl.LOCATION_CODE) "
							+"and bl.LOCATION_CODE='"+LOCATION_CODE+"' ";
					DataManager dm = DBOperator.DoSelect2DM(sql_delete);
					if(dm==null || dm.getCurrentCount()==0){
						
					}else{
						Message.showWarningMessage("此库位信息已经在系统使用，不能删除");
						return;
					}
					
					sql_delete = "delete from bas_location where WAREHOUSE_CODE = '"+warehouseCode
							+"' and LOCATION_CODE='"+LOCATION_CODE+"'";
					int t = DBOperator.DoUpdate(sql_delete);
					if(t>0){
						int selectedRow = table_location.getSelectedRow();
						((DefaultTableModel) table_location.getModel()).removeRow(selectedRow);
						Message.showInfomationMessage("删除成功!");
						enableAll(true);
						initTableData("");
					}
					
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	};
	ActionListener queryListener = new ActionListener() {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void actionPerformed(ActionEvent e) {
			ArrayList fieldList = new ArrayList();
			fieldList.add("bas_warehouse.WAREHOUSE_CODE:仓库号");
			fieldList.add("bas_warehouse.WAREHOUSE_NAME:仓库名称");
			fieldList.add("bas_location.LOCATION_CODE:库位条码");
			fieldList.add("bas_location.LOGICAL_LOCATION_CODE:逻辑库位条码");
			fieldList.add("bas_location.LOCATION_TYPE_CODE:库位类型");
			fieldList.add("bas_location.TEMPLATE_FIELD1:属性1");
			fieldList.add("bas_location.TEMPLATE_FIELD2:属性2");
			fieldList.add("bas_location.TEMPLATE_FIELD3:属性3");
			fieldList.add("bas_location.TEMPLATE_FIELD4:属性4");
			fieldList.add("bas_location.TEMPLATE_FIELD5:属性5");
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
			initTableData(retWhere);
			
			
		}
	};
	ActionListener cancelListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			lastStatus = ToolBarItem.Cancel;
			enableAll(true);
		}
	};
	private JLabel lblNewLabel_8;
	private JComboBox cb_location_type;
	private void enableEditComp(boolean b){
		comboBox_warehouseCode.setEnabled(b);
		cb_location_type.setEnabled(b);
		textField_LOCATION_CODE.setEditable(b);
		textField_LOGICAL_LOCATION_CODE.setEditable(b);
		textField_TEMPLATE1.setEditable(b);
		textField_TEMPLATE2.setEditable(b);
		textField_TEMPLATE3.setEditable(b);
		textField_TEMPLATE4.setEditable(b);
		textField_TEMPLATE5.setEditable(b);
	}
	private void enableAll(boolean b){
		enableCRUDbutton(b);
		enableEditComp(!b);
		enableSaveCancelButton(!b);
		clearEditUI();
		table_location.setEnabled(b);
		table_location.addMouseListener(mouseAdapter);
		
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
	private void clearEditUI(){
		textField_LOCATION_CODE.setText("");
		textField_LOGICAL_LOCATION_CODE.setText("");
		textField_TEMPLATE1.setText("");
		textField_TEMPLATE2.setText("");
		textField_TEMPLATE3.setText("");
		textField_TEMPLATE4.setText("");
		textField_TEMPLATE5.setText("");
		comboBox_warehouseCode.setSelectedItem("");
		table_location.clearSelection();
	}
	private void initTableData(String strWhere){
	       
	       String sql = "SELECT"
	    		   +" `bas_warehouse`.`WAREHOUSE_NAME`"
	    		   +", `bas_location`.`LOCATION_CODE`"
	    		   +", `bas_location`.`LOGICAL_LOCATION_CODE`"
	    		   +", `bas_location`.`LOCATION_TYPE_CODE`"
	    		   +", ifnull(`bas_location`.`TEMPLATE_FIELD1`,'')"
	    		   +", ifnull(`bas_location`.`TEMPLATE_FIELD2`,'')"
	    		   +", ifnull(`bas_location`.`TEMPLATE_FIELD3`,'')"
	    		   +", ifnull(`bas_location`.`TEMPLATE_FIELD4`,'')"
	    		   +", ifnull(`bas_location`.`TEMPLATE_FIELD5`,'')"
	    		   +",round(ifnull(inv.qty,0),2) qty "
	    		   +"FROM"
	    		   +".`bas_location`"
	    		   +"INNER JOIN .`bas_warehouse` "
	    		   +"ON (`bas_location`.`WAREHOUSE_CODE` = `bas_warehouse`.`WAREHOUSE_CODE`) "
	    		   +"LEFT JOIN (select WAREHOUSE_CODE,LOCATION_CODE,sum(ON_HAND_QTY-ALLOCATED_QTY-PICKED_QTY) qty from inv_inventory inv group by WAREHOUSE_CODE,LOCATION_CODE having sum(ON_HAND_QTY-ALLOCATED_QTY-PICKED_QTY)>0) inv on `bas_location`.WAREHOUSE_CODE=inv.WAREHOUSE_CODE and `bas_location`.LOCATION_CODE=inv.LOCATION_CODE "
	    		   +" where 1=1 and bas_location.warehouse_code='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' ";
	        if(!strWhere.equals("")){
				sql = sql + strWhere;
			}
	        System.out.println("SQL QUERY = "+sql);
	        table_location.removeRowAll();
	        Vector tableData = DBOperator.DoSelectAddSequenceRow(sql);
	        table_location.setData(tableData);
	        JTableUtil.fitTableColumns(table_location);
	        table_location.setColumnEditableAll(false);
		}
	public static BasLocationFrm getInstance() {
		if(instance == null) { 
			 synchronized(BasLocationFrm.class){
				 if(instance == null) {
					 instance = new BasLocationFrm();
				 }
			 }
	        }  
	        return instance;
		 
	 }
	
	public static synchronized boolean getOpenStatus() {
		 if(instance == null) {    
	            instance = new BasLocationFrm();  
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
					BasLocationFrm frame = new BasLocationFrm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public static String NulltoSpace(Object o) {
		if (o == null)
			return "";
		else if (o.equals("null")) {
			return "";
		} else
			return o.toString().trim();
	}
}
