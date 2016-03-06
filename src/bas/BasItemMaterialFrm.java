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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
import comUtil.StringUtil;
import comUtil.WMSCombobox;
import dmdata.DataManager;
import main.PBSUIBaseGrid;
import sys.InnerFrame;
import sys.JTableUtil;
import sys.MainFrm;
import sys.Message;
import sys.QueryDialog;
import sys.ToolBarItem;
import sys.tableQueryDialog;
import util.JTNumEdit;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JComboBox;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import java.awt.Color;

public class BasItemMaterialFrm extends InnerFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5103547685919986546L;
	private JPanel contentPane;
	private static volatile BasItemMaterialFrm instance;
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
	private PBSUIBaseGrid table_itemMaterial;
	private JPanel panel;
	private JLabel label_storer;
	private WMSCombobox cb_storer;
	private JLabel label_1;
	private JTextField txt_item_code;
	private JTextField txt_item_name;
	private JButton btn_item_query;
	private JPanel panel_1;
	private JPanel panel_2;
	private JLabel lblNewLabel;
	private JTextField txt_item_material_code;
	private JTextField txt_item_material_name;
	private JButton btn_item_material_query;
	private JLabel lblNewLabel_1;
	private JTNumEdit txt_match_qty;
	int lastStatus;
	
	private String retWhere = "";
	
	public BasItemMaterialFrm() {
		BasicInternalFrameUI ui = (BasicInternalFrameUI)this.getUI(); //去除标题栏
		ui.getNorthPane().setVisible(false); 
		this.addVetoableChangeListener(new VetoableChangeListener() {
			public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException {
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
		
		btnRefresh = new JButton("\u5237\u65B0");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				initTableData(retWhere);
			}
		});
		btnRefresh.setForeground(Color.RED);
		topPanel.add(btnRefresh);
		
		centerPanel = new JPanel();
		contentPane.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new BorderLayout(0, 0));
		
		editPanel = new JPanel();
		centerPanel.add(editPanel, BorderLayout.NORTH);
		editPanel.setLayout(new GridLayout(3, 1, 0, 0));
		String storeSql = "SELECT DISTINCT storer_code,storer_short_name FROM bas_storer ORDER BY storer_short_name";
		
		panel_2 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_2.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		editPanel.add(panel_2);
		
		label_storer = new JLabel("*\u8D27\u4E3B\u7F16\u7801:");
		panel_2.add(label_storer);
		
		cb_storer = new WMSCombobox("SELECT DISTINCT storer_code,storer_short_name FROM bas_storer ORDER BY storer_short_name", true);
		cb_storer.setEnabled(false);
		cb_storer.setEditable(true);
		cb_storer.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				txt_item_code.setText("");
				txt_item_name.setText("");
				txt_item_material_code.setText("");
				txt_item_material_name.setText("");
			}
			
		});
		panel_2.add(cb_storer);
		
		panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		editPanel.add(panel);
		
		label_1 = new JLabel("*\u8D27\u54C1\u7F16\u7801:");
		panel.add(label_1);
		
		txt_item_code = new JTextField();
		txt_item_code.setEditable(false);
		txt_item_code.setColumns(15);
		panel.add(txt_item_code);
		
		txt_item_name = new JTextField();
		txt_item_name.setEditable(false);
		txt_item_name.setColumns(30);
		panel.add(txt_item_name);
		
		btn_item_query = new JButton("<");
		btn_item_query.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = "select item.STORER_CODE 货主编码,bs.STORER_NAME 货主名称,item.ITEM_CODE 货品编码,item.ITEM_NAME 货品名称,item.ITEM_BAR_CODE 货品条码,biu.unit_name 单位  "
						+ "from bas_item item " + "inner join bas_storer bs on item.STORER_CODE=bs.STORER_CODE "
						+ "left join bas_item_unit biu on biu.unit_code=item.UNIT_CODE " + "where 1=1 ";
				String storerCode = cb_storer.getSelectedOID().toString();
				if (!storerCode.equals("")) {
					sql = sql + " and item.STORER_CODE='" + storerCode + "' ";
				}
				tableQueryDialog tableQuery = new tableQueryDialog(sql, false);
				Toolkit toolkit = Toolkit.getDefaultToolkit();
				int x = (int) (toolkit.getScreenSize().getWidth() - tableQuery.getWidth()) / 2;
				int y = (int) (toolkit.getScreenSize().getHeight() - tableQuery.getHeight()) / 2;
				tableQuery.setLocation(x, y);
				tableQuery.setModal(true);
				tableQuery.setVisible(true);
				DataManager dm = tableQueryDialog.resultDM;
				if (dm == null) {
					return;
				}
				Object obj = dm.getObject("货品编码", 0);
				if (obj == null || obj.equals("")) {
					return;
				} else {
					txt_item_code.setText((String) dm.getObject("货品编码", 0));
					txt_item_name.setText((String) dm.getObject("货品名称", 0));
					txt_item_code.selectAll();
					txt_item_code.requestFocus();
				}
			}
		});
		panel.add(btn_item_query);
		
		panel_1 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_1.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		editPanel.add(panel_1);
		
		lblNewLabel = new JLabel("*\u8F85\u6599\u7F16\u7801:");
		panel_1.add(lblNewLabel);
		
		txt_item_material_code = new JTextField();
		txt_item_material_code.setEditable(false);
		panel_1.add(txt_item_material_code);
		txt_item_material_code.setColumns(15);
		
		txt_item_material_name = new JTextField();
		txt_item_material_name.setEditable(false);
		panel_1.add(txt_item_material_name);
		txt_item_material_name.setColumns(30);
		
		btn_item_material_query = new JButton("<");
		btn_item_material_query.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = "select item.STORER_CODE 货主编码,bs.STORER_NAME 货主名称,item.ITEM_CODE 货品编码,item.ITEM_NAME 货品名称,item.ITEM_BAR_CODE 货品条码,biu.unit_name 单位  "
						+ "from bas_item item " + "inner join bas_storer bs on item.STORER_CODE=bs.STORER_CODE "
						+ "left join bas_item_unit biu on biu.unit_code=item.UNIT_CODE " + "where 1=1 "
						+"and item.STORER_CODE='0001' ";
				tableQueryDialog tableQuery = new tableQueryDialog(sql, false);
				Toolkit toolkit = Toolkit.getDefaultToolkit();
				int x = (int) (toolkit.getScreenSize().getWidth() - tableQuery.getWidth()) / 2;
				int y = (int) (toolkit.getScreenSize().getHeight() - tableQuery.getHeight()) / 2;
				tableQuery.setLocation(x, y);
				tableQuery.setModal(true);
				tableQuery.setVisible(true);
				DataManager dm = tableQueryDialog.resultDM;
				if (dm == null) {
					return;
				}
				Object obj = dm.getObject("货品编码", 0);
				if (obj == null || obj.equals("")) {
					return;
				} else {
					txt_item_material_code.setText((String) dm.getObject("货品编码", 0));
					txt_item_material_name.setText((String) dm.getObject("货品名称", 0));
					txt_item_material_code.selectAll();
					txt_item_material_code.requestFocus();
				}
			}
		});
		panel_1.add(btn_item_material_query);
		
		lblNewLabel_1 = new JLabel("\u5339\u914D\u6570\u91CF:");
		panel_1.add(lblNewLabel_1);
		
		txt_match_qty = new  JTNumEdit(15, "####.00",true);
		panel_1.add(txt_match_qty);
		txt_match_qty.setColumns(10);
		
		
		
		JScrollPane scrollPane = new JScrollPane();
		centerPanel.add(scrollPane, BorderLayout.CENTER);
		
		table_itemMaterial = new PBSUIBaseGrid();
		scrollPane.setViewportView(table_itemMaterial);

		init();
		
	}
	private void init(){
		enableEditComp(false);
		enableSaveCancelButton(false);
		initTableData(" ");
		table_itemMaterial.addMouseListener(mouseAdapter);
	}
	MouseAdapter mouseAdapter = new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount()>=2){
				table_itemMaterial.setColumnSelectionAllowed(true);
			}
			int r = table_itemMaterial.getSelectedRow();
			cb_storer.setSelectedItem(NulltoSpace(table_itemMaterial.getValueAt(r, table_itemMaterial.getColumnModel().getColumnIndex("货主名称"))));
			txt_item_code.setText(StringUtil.NulltoSpace(table_itemMaterial.getValueAt(r, table_itemMaterial.getColumnModel().getColumnIndex("货品编码"))));
			txt_item_name.setText(StringUtil.NulltoSpace(table_itemMaterial.getValueAt(r, table_itemMaterial.getColumnModel().getColumnIndex("货品名称"))));
			txt_item_material_code.setText(StringUtil.NulltoSpace(table_itemMaterial.getValueAt(r, table_itemMaterial.getColumnModel().getColumnIndex("辅料编码"))));
			txt_item_material_name.setText(StringUtil.NulltoSpace(table_itemMaterial.getValueAt(r, table_itemMaterial.getColumnModel().getColumnIndex("辅料名称"))));
			txt_match_qty.setText(StringUtil.NulltoSpace(table_itemMaterial.getValueAt(r, table_itemMaterial.getColumnModel().getColumnIndex("匹配数量"))));
		}
	};
	private void enableEditComp(boolean b){
		txt_item_code.setEditable(b);
		cb_storer.setEnabled(b);
		txt_match_qty.setEditable(b);
		btn_item_query.setEnabled(b);
		btn_item_material_query.setEnabled(b);
	}
	private void initTableData(String strWhere){
		String sql = "select bim.STORER_CODE 货主编码,bs.STORER_NAME 货主名称,bim.ITEM_CODE 货品编码,item.ITEM_BAR_CODE 货品条码,item.ITEM_NAME 货品名称,"
				+"bim.ITEM_CODE_MATERIAL 辅料编码,item2.ITEM_BAR_CODE 辅料条码,item2.ITEM_NAME 辅料名称,bim.MATCH_QTY 匹配数量,"
				+"bim.CREATED_DTM_LOC 创建时间,su.USER_NAME 创建人,bim.UPDATED_DTM_LOC 更新时间,su2.USER_NAME 更新人 "
				+"from bas_item_material bim "
				+"inner join bas_storer bs on bim.STORER_CODE=bs.STORER_CODE "
				+"inner join bas_item item on bim.STORER_CODE=item.STORER_CODE and bim.ITEM_CODE=item.ITEM_CODE "
				+"inner join bas_item item2 on item2.STORER_CODE='0001' and bim.ITEM_CODE_MATERIAL=item2.ITEM_CODE "
				+"left join sys_user su on bim.CREATED_BY_USER=su.USER_CODE "
				+"left join sys_user su2 on bim.UPDATED_BY_USER=su2.USER_CODE "
				+"where 1=1 ";
		if(!strWhere.equals("")){
			sql = sql + strWhere;
		}
		
		DataManager dm = DBOperator.DoSelect2DM(sql);
		if (table_itemMaterial.getColumnCount() == 0) {
			table_itemMaterial.setColumn(dm.getCols());
		}
		table_itemMaterial.removeRowAll();
		table_itemMaterial.setData(dm.getDataStrings());
		table_itemMaterial.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table_itemMaterial.setColumnEditableAll(false);
		JTableUtil.fitTableColumnsDoubleWidth(table_itemMaterial);
		table_itemMaterial.setSortEnable();
	}
	ActionListener saveListener = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			String storer_code = cb_storer.getSelectedOID().toLowerCase();
			String item_code = txt_item_code.getText().trim();
			String item_material_code = txt_item_material_code.getText().trim();
			String match_qty = txt_match_qty.getText();
			String user_code = MainFrm.getUserInfo().getString("USER_CODE", 0);
			if(storer_code.equals("")||item_code.equals("")||item_material_code.equals("")||match_qty.equals("")){
				Message.showInfomationMessage("* 为必选项!");
				return;
			}else if (lastStatus==ToolBarItem.Create) {
				saveData(storer_code,item_code,item_material_code,match_qty,user_code);
			}else if(lastStatus == ToolBarItem.Modify){
				saveData(storer_code,item_code,item_material_code,match_qty,user_code);
			}
			//滚动 定位Table保存数据行
			Rectangle rect = new Rectangle(0, table_itemMaterial.getHeight(), 20, 20);
			table_itemMaterial.scrollRectToVisible(rect);
			table_itemMaterial.setRowSelectionInterval(table_itemMaterial.getRowCount() - 1, table_itemMaterial.getRowCount() - 1);
			table_itemMaterial.grabFocus();
            table_itemMaterial.changeSelection(table_itemMaterial.getRowIndexByColIndexAndColValue(1,storer_code), 0, false, true);
			//定位Table保存数据行
			table_itemMaterial.setRowSelectionInterval(0, table_itemMaterial.getRowIndexByColIndexAndColValue(1,storer_code));
		}
	};
	
	ActionListener cancelListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			lastStatus = ToolBarItem.Cancel;
			enableAll(true);
		}
	};
	ActionListener modifyListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(table_itemMaterial.getModel().getRowCount()==0){
				Message.showInfomationMessage("不能进行修改操作还没有数据,请先新增");
				return;
			}
//			if(textField_brand_code.getText().trim().length()<=0){
//				Message.showInfomationMessage("请先点击下面表格选择一行要修改的数据");
//				return;
//			}
			lastStatus = ToolBarItem.Modify;
			enableCRUDbutton(false);
			enableEditComp(true);
//			textField_brand_code.setEditable(false);
			enableSaveCancelButton(true);
			table_itemMaterial.setEnabled(false);
		}
	};
	ActionListener deleteListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			lastStatus = ToolBarItem.Delete;
			if(table_itemMaterial.getModel().getRowCount()==0){
				Message.showInfomationMessage("不能进行删除操作还没有数据,请先新增");
				return;
			}
			if(txt_item_code.getText().length()<=0){
				Message.showInfomationMessage("请先点击下面表格选择一行要删除的数据");
				return;
			}
			boolean b_confirm = Message.showOKorCancelMessage("是否删除所选行?");
			if(b_confirm){
				String brand_code = "";//textField_brand_code.getText().trim();
				try {
					
					String sql_delete = "select bb.BRAND_CODE from bas_brand bb "
							+"where exists(select bi.BRAND_CODE from bas_item bi where bi.BRAND_CODE=bb.BRAND_CODE) "
							+"and bb.BRAND_CODE= '"+brand_code+"' ";
//					DataManager dm = DBOperator.DoSelect2DM(sql_delete);
//					if(dm==null || dm.getCurrentCount()==0){
//						
//					}else{
//						Message.showWarningMessage("此辅料信息已经在系统使用，不能删除");
//						return;
//					}
					
					String storer_code = cb_storer.getSelectedOID().toLowerCase();
					String item_code = txt_item_code.getText().trim();
					String item_material_code = txt_item_material_code.getText().trim();
					sql_delete = " delete from bas_item_material where STORER_CODE = '"+storer_code+"' "
							+ "and ITEM_CODE='"+item_code+"' and ITEM_CODE_MATERIAL='"+item_material_code+"' ";
					int t = DBOperator.DoUpdate(sql_delete);
					if(t>0){
						int selectedRow = table_itemMaterial.getSelectedRow();
						((DefaultTableModel) table_itemMaterial.getModel()).removeRow(selectedRow);
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
		public void actionPerformed(ActionEvent e) {
			ArrayList fieldList = new ArrayList();
			fieldList.add("bim.STORER_CODE:货主编码");
			fieldList.add("bs.STORER_NAME:货主名称");
			fieldList.add("bim.ITEM_CODE:货品编码");
			fieldList.add("item.ITEM_BAR_CODE:货品条码");
			fieldList.add("item.ITEM_NAME:货品名称");
			fieldList.add("bim.ITEM_CODE_MATERIAL:辅料编码");
			fieldList.add("item2.ITEM_BAR_CODE:辅料条码");
			fieldList.add("item2.ITEM_NAME:辅料名称");
			QueryDialog query = QueryDialog.getInstance(fieldList);
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			int x = (int)(toolkit.getScreenSize().getWidth()-query.getWidth())/2;
			int y = (int)(toolkit.getScreenSize().getHeight()-query.getHeight())/2;
			query.setLocation(x, y);
			query.setVisible(true);
			retWhere = QueryDialog.queryValueResult;
			if(retWhere.length()>0){
				retWhere = " and "+retWhere;
			}
			initTableData(retWhere);
		}
	};
	ActionListener addListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			lastStatus = ToolBarItem.Create;
			enableCRUDbutton(false);
			enableEditComp(true);
			enableSaveCancelButton(true);
			clearEditUI();
			table_itemMaterial.setEnabled(false);
			table_itemMaterial.removeMouseListener(mouseAdapter);
		}
	};
	private JButton btnRefresh;
	
	private boolean saveData(String storer_code,String item_code,String item_material_code,String match_qty,String user_code) {
		String validateSql = "select bim.STORER_CODE from bas_item_material bim " + "where bim.STORER_CODE='"
				+ storer_code + "' and bim.ITEM_CODE='" + item_code + "' " + "and bim.ITEM_CODE_MATERIAL='"
				+ item_material_code + "'";
		DataManager dmtmp2 = DBOperator.DoSelect2DM(validateSql);
		if (dmtmp2 == null || dmtmp2.getCurrentCount() == 0) {
			String sql = "insert into bas_item_material(STORER_CODE,ITEM_CODE,ITEM_CODE_MATERIAL,MATCH_QTY,CREATED_BY_USER,CREATED_DTM_LOC) "
					+ "select '" + storer_code + "','" + item_code + "','" + item_material_code + "'," + match_qty
					+ ",'" + user_code + "',now() ";
			int t = DBOperator.DoUpdate(sql);
			if (t > 0) {
				Message.showInfomationMessage("保存成功!");
				enableAll(true);
				initTableData(" and bim.storer_code='" + storer_code + "' ");
				return true;
			} else {
				Message.showInfomationMessage("出错了,保存失败!");
				return false;
			}
		} else {
			String sql = "update bas_item_material bim set bim.MATCH_QTY=" + match_qty + " " + "where bim.STORER_CODE='"
					+ storer_code + "' and bim.ITEM_CODE='" + item_code + "' " + "and bim.ITEM_CODE_MATERIAL='"
					+ item_material_code + "'";
			;
			int t = DBOperator.DoUpdate(sql);
			if (t > 0) {
				Message.showInfomationMessage("更新成功!");
				enableAll(true);
				initTableData(" and bim.storer_code='" + storer_code + "' ");
				return true;
			} else {
				Message.showInfomationMessage("出错了,更新失败!");
				return false;
			}
		}
		
	}
	
	private void enableAll(boolean b){
		enableCRUDbutton(b);
		enableEditComp(!b);
		enableSaveCancelButton(!b);
		clearEditUI();
		table_itemMaterial.setEnabled(b);
		table_itemMaterial.addMouseListener(mouseAdapter);
		
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
		txt_item_code.setText("");
		table_itemMaterial.clearSelection();
	}
	public static BasItemMaterialFrm getInstance() {
		if(instance == null) { 
			 synchronized(BasItemMaterialFrm.class){
				 if(instance == null) {
					 instance = new BasItemMaterialFrm();
				 }
			 }
	        }  
	        return instance;
	 }
	public static synchronized boolean getOpenStatus() {
		 if(instance == null) {    
	            instance = new BasItemMaterialFrm();  
	        }  
	        return isOpen;
	 }
	
	public static String NulltoSpace(Object o) {
		if (o == null)
			return "";
		else if (o.equals("null")) {
			return "";
		} else
			return o.toString().trim();
	}
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BasItemMaterialFrm frame = new BasItemMaterialFrm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
