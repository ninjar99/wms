package bas;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
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
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import DBUtil.DBConnectionManager;
import DBUtil.DBOperator;
import comUtil.WMSCombobox;
import comUtil.comData;
import dmdata.DataManager;
import main.PBSUIBaseGrid;
import sys.InnerFrame;
import sys.JTableUtil;
import sys.Message;
import sys.QueryDialog;
import sys.ToolBarItem;


import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTextField;
import javax.swing.JTextArea;

public class BasItemFrm extends InnerFrame{

	private static final long serialVersionUID = 1L;
	private static volatile BasItemFrm instance;
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
	private PBSUIBaseGrid table_item;
	int lastStatus;
	private WMSCombobox comboBox_STORER_CODE;
	private WMSCombobox comboBox_BRAND_CODE;
	private JTextField textField_itemCode;
	private JTextField textField_itemName;
	private WMSCombobox comboBox_port_no;
	private WMSCombobox comboBox_unit_code;
	private JTextField textField_item_spec;
	private WMSCombobox comboBox_country_code;
	private JTextField textField_BAR_CODE;
	private JTextArea textArea_DESCRIPTION;
	
	private JTextField textField_TAX_NUMBER;
	private JTextField textField_HSCODE;
	private JTextField textField_HSCODE_DESC;
	
	BasItemFrm(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 717, 459);
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
		
		JPanel panel = new JPanel();
		FlowLayout fl_panel = (FlowLayout) panel.getLayout();
		fl_panel.setVgap(10);
		fl_panel.setAlignment(FlowLayout.LEFT);
		editPanel.add(panel, BorderLayout.NORTH);
		
		String storeSql = "SELECT DISTINCT storer_code,storer_short_name FROM bas_storer ORDER BY storer_short_name";
		
		JLabel label = new JLabel("*\u8D27\u4E3B:");
		panel.add(label);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		comboBox_STORER_CODE = new WMSCombobox(storeSql, true);
		panel.add(comboBox_STORER_CODE);
		
		String brandSql = "SELECT DISTINCT BRAND_CODE,BRAND_SHORT_NAME FROM bas_brand order by BRAND_SHORT_NAME";
		
		JLabel lblNewLabel = new JLabel("\u54C1\u724C:");
		panel.add(lblNewLabel);
		comboBox_BRAND_CODE = new WMSCombobox(brandSql, true);
		panel.add(comboBox_BRAND_CODE);
		
		JLabel lblNewLabel_1 = new JLabel("*\u8D27\u54C1\u7F16\u7801:");
		panel.add(lblNewLabel_1);
		
		textField_itemCode = new JTextField();
		panel.add(textField_itemCode);
		textField_itemCode.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("*\u8D27\u54C1\u540D\u79F0:");
		panel.add(lblNewLabel_2);
		
		textField_itemName = new JTextField();
		panel.add(textField_itemName);
		textField_itemName.setColumns(35);
		
		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		editPanel.add(panel_2, BorderLayout.CENTER);
		
		JLabel lblNewLabel_4 = new JLabel("\u8D27\u54C1\u6761\u7801:");
		panel_2.add(lblNewLabel_4);
		
		textField_BAR_CODE = new JTextField();
		panel_2.add(textField_BAR_CODE);
		textField_BAR_CODE.setColumns(10);
		
		String portNoSql = "SELECT port_code,port_name FROM bas_port";
		
		String unitCodeSql = "SELECT UNIT_CODE,unit_name FROM bas_item_unit";
		
		String countryCodeSql = "SELECT COUNTRY_CODE,country_name FROM bas_country";
		
		JLabel lblNewLabel_5 = new JLabel("*\u53E3\u5CB8:");
		panel_2.add(lblNewLabel_5);
		comboBox_port_no = new WMSCombobox(portNoSql, true);
		panel_2.add(comboBox_port_no);
		
		JLabel lblNewLabel_6 = new JLabel("*\u8BA1\u91CF\u5355\u4F4D:");
		panel_2.add(lblNewLabel_6);
		comboBox_unit_code = new WMSCombobox(unitCodeSql, true);
		panel_2.add(comboBox_unit_code);
		
		JLabel lblNewLabel_7 = new JLabel("*\u8D27\u54C1\u89C4\u683C:");
		panel_2.add(lblNewLabel_7);
		
		textField_item_spec = new JTextField();
		textField_item_spec.setToolTipText("\u4F8B\u5982: 375g/\u6876");
		panel_2.add(textField_item_spec);
		textField_item_spec.setColumns(14);
		
		JLabel lblNewLabel_8 = new JLabel("*\u56FD\u5BB6:");
		panel_2.add(lblNewLabel_8);
		comboBox_country_code = new WMSCombobox(countryCodeSql, true);
		panel_2.add(comboBox_country_code);
		
		JPanel panel_3 = new JPanel();
		editPanel.add(panel_3, BorderLayout.SOUTH);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_4 = new JPanel();
		FlowLayout fl_panel_4 = (FlowLayout) panel_4.getLayout();
		fl_panel_4.setAlignment(FlowLayout.LEFT);
		panel_3.add(panel_4, BorderLayout.NORTH);
		
		JLabel lblNewLabel_9 = new JLabel("\u7A0E\u53F7:");
		panel_4.add(lblNewLabel_9);
		
		textField_TAX_NUMBER = new JTextField();
		panel_4.add(textField_TAX_NUMBER);
		textField_TAX_NUMBER.setColumns(10);
		
		JLabel lblNewLabel_10 = new JLabel("\u6D77\u5173\u7F16\u7801:");
		panel_4.add(lblNewLabel_10);
		
		textField_HSCODE = new JTextField();
		panel_4.add(textField_HSCODE);
		textField_HSCODE.setColumns(10);
		
		JLabel lblNewLabel_11 = new JLabel("\u7533\u62A5\u8981\u7D20:");
		panel_4.add(lblNewLabel_11);
		
		textField_HSCODE_DESC = new JTextField();
		panel_4.add(textField_HSCODE_DESC);
		textField_HSCODE_DESC.setColumns(55);
		
		JPanel panel_1 = new JPanel();
		panel_3.add(panel_1);
		FlowLayout fl_panel_1 = (FlowLayout) panel_1.getLayout();
		fl_panel_1.setAlignment(FlowLayout.LEFT);
		
		JLabel lblNewLabel_3 = new JLabel("\u8D27\u54C1\u63CF\u8FF0:");
		panel_1.add(lblNewLabel_3);
		
		textArea_DESCRIPTION = new JTextArea();
		textArea_DESCRIPTION.setWrapStyleWord(true);
		textArea_DESCRIPTION.setLineWrap(true);
		textArea_DESCRIPTION.setColumns(80);
		textArea_DESCRIPTION.setRows(2);
		panel_1.add(textArea_DESCRIPTION);
		
		
		JScrollPane scrollPane = new JScrollPane();
		centerPanel.add(scrollPane, BorderLayout.CENTER);
		
		table_item = new PBSUIBaseGrid();
		table_item.setSortEnable();
		table_item.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_item.setColumnEditableAll(false);
		scrollPane.setViewportView(table_item);

		String[] RHColumnNames ={"序号","货主","品牌","货品编码","货品名称","货品条码","口岸","税号","海关编码","申报要素","最小计量单位","货品规格","国家","货品描述"};
		table_item.setColumn(RHColumnNames);
		table_item.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JTableUtil.fitTableColumns(table_item);
		init();
		
		BasicInternalFrameUI ui = (BasicInternalFrameUI)this.getUI(); //去除标题栏
		ui.getNorthPane().setVisible(false); 
		this.addVetoableChangeListener(new VetoableChangeListener() {

			public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException {
				
				String name = e.getPropertyName();
				Object value = e.getNewValue();
				if (name.equals("closed") && value.equals(Boolean.TRUE)) // 窗口被关闭
				{
					System.out.println("BasItemFrm窗口被关闭");
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
	ActionListener addListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			lastStatus = ToolBarItem.Create;
		    
			enableCRUDbutton(false);
			enableEditComp(true);
			enableSaveCancelButton(true);
			clearEditUI();
			table_item.setEnabled(false);
			table_item.removeMouseListener(mouseAdapter);
		}
	};
	ActionListener saveListener = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			String store_code = comboBox_STORER_CODE.getSelectedOID();
			String brandCode = comboBox_BRAND_CODE.getSelectedOID();
			String itemCode = textField_itemCode.getText().trim();
			String itemName =textField_itemName.getText().trim(); 
			String barCode = textField_BAR_CODE.getText().trim(); //货品条码
			String portCode = comboBox_port_no.getSelectedOID();
			String unitCode = comboBox_unit_code.getSelectedOID();
			String itemSpec = textField_item_spec.getText().trim();
			String countryCode = comboBox_country_code.getSelectedOID();
			String description = textArea_DESCRIPTION.getText().trim();
			String TAX_NUMBER = textField_TAX_NUMBER.getText().trim();
			String HSCODE = textField_HSCODE.getText().trim();
			String HSCODE_DESC = textField_HSCODE_DESC.getText().trim();
			if(comboBox_STORER_CODE.getSelectedItem().toString().trim().equals("")||
					itemCode.equals("")||
					itemName.equals("")||
//					barCode.equals("")||
					comboBox_port_no.getSelectedItem().toString().trim().equals("")||
					comboBox_unit_code.getSelectedItem().toString().trim().equals("")||
					comboBox_country_code.getSelectedItem().toString().trim().equals("")||
					itemSpec.equals("")
					){
				Message.showInfomationMessage("* 为必选项!");
			}else if(lastStatus == ToolBarItem.Create){
				String validateInputItemCodeSql = "select ITEM_CODE from bas_item where STORER_CODE = '"
							+store_code+"' and ITEM_CODE='"+itemCode +"'";
				DataManager dmtmp2 = DBOperator.DoSelect2DM(validateInputItemCodeSql);
//				System.out.println("dmtmp2  = "+ dmtmp2 +" dmtmp2.getCurrentCount() = "+dmtmp2.getCurrentCount());
				if(dmtmp2.getCurrentCount()>0){
					Message.showInfomationMessage("货主为"+comboBox_STORER_CODE.getSelectedItem().toString()+"的商品编号"+itemCode+"已经存在不能添加!");
					return;
				}else{
					String sql = " insert into bas_item(STORER_CODE,BRAND_CODE,ITEM_CODE,ITEM_NAME,ITEM_BAR_CODE,"
							+ " PORT_CODE,UNIT_CODE,ITEM_SPEC,COUNTRY_CODE,DESCRIPTION,CREATED_BY_USER,CREATED_DTM_LOC,TAX_NUMBER,HSCODE,HSCODE_DESC) "
							+ " value('"+store_code+"','"+brandCode+"','"
							+ itemCode+"','"+itemName+"','"+barCode+"','"+portCode+"','"+unitCode+"','"
							+ itemSpec+"','"+countryCode+"','"+description
									+ "','sys',now(),'"+TAX_NUMBER+"','"+HSCODE+"','"+HSCODE_DESC+"' )";
//					System.out.println("sql = "+sql);
					comData.sqlValidate(sql);
					int t = DBOperator.DoUpdate(sql);
					if(t>0){
						Message.showInfomationMessage("保存成功!");
						enableAll(true);
						initTableData("");
					}else{
						Message.showInfomationMessage("出错了,保存失败!");
					}
				}
				
			}else if(lastStatus == ToolBarItem.Modify){
				String sql_update = "update bas_item set BRAND_CODE ='"+brandCode+"',ITEM_NAME='"+itemName+
										"',ITEM_BAR_CODE='"+barCode+"',PORT_CODE='"+portCode+
										"',UNIT_CODE='"+unitCode+"',ITEM_SPEC='"+itemSpec+
										"',COUNTRY_CODE='"+countryCode+"',DESCRIPTION='"+description+
										"',UPDATED_BY_USER='sys',UPDATED_DTM_LOC=now() "+
										",TAX_NUMBER='"+TAX_NUMBER+"',HSCODE='"+HSCODE+"',HSCODE_DESC='"+HSCODE_DESC+"'"+
										" where STORER_CODE='"+store_code+"' and ITEM_CODE='"+itemCode+"'";
//				System.out.println("sql_update  = "+sql_update);
				int t = DBOperator.DoUpdate(sql_update);
				if(t>0){
					Message.showInfomationMessage("更新成功!");
					enableAll(true);
					initTableData("");
				}else{
					Message.showInfomationMessage("出错了,更新失败!");
				}
			}
//			table_item.changeSelection(table_item.getRowCount()-1, 0, false, false);
			//滚动 定位Table保存数据行
			Rectangle rect = new Rectangle(0, table_item.getHeight(), 20, 20);
			table_item.scrollRectToVisible(rect);
			table_item.setRowSelectionInterval(table_item.getRowCount() - 1, table_item.getRowCount() - 1);
			table_item.grabFocus();
			table_item.changeSelection(table_item.getRowIndexByColIndexAndColValue(3,itemCode), 0, false, true);
		}
	};
		
	ActionListener modifyListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(table_item.getModel().getRowCount()==0){
				Message.showInfomationMessage("不能进行修改操作还没有数据,请先新增");
				return;
			}
			if(textField_itemCode.getText().length()<=0){
				Message.showInfomationMessage("请先点击下面表格选择一行要修改的数据");
				return;
			}
			lastStatus = ToolBarItem.Modify;
			enableCRUDbutton(false);
			enableEditComp(true);
			comboBox_STORER_CODE.setEnabled(false);
			textField_itemCode.setEditable(false);
			enableSaveCancelButton(true);
			table_item.setEnabled(false);
		}
	};
	ActionListener deleteListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			lastStatus = ToolBarItem.Delete;
			if(table_item.getModel().getRowCount()==0){
				Message.showInfomationMessage("不能进行删除操作还没有数据,请先新增");
				return;
			}
			if(textField_itemCode.getText().length()<=0){
				Message.showInfomationMessage("请先点击下面表格选择一行要删除的数据");
				return;
			}
			boolean b_confirm = Message.showOKorCancelMessage("是否删除所选行?");
			if(b_confirm){
				String itemCode = textField_itemCode.getText().trim();
				String store_code = comboBox_STORER_CODE.getSelectedOID();
				try {
					int selectedRow = table_item.getSelectedRow();
					((DefaultTableModel) table_item.getModel()).removeRow(selectedRow);
					String sql_delete = "select bi.item_code from bas_item bi "
							+"where (exists(select ii.item_code from inv_inventory ii where ii.storer_code=bi.storer_code and ii.item_code=bi.item_code) "
							+"or exists(select ird.CONTAINER_CODE from inb_receipt_header irh inner join inb_receipt_detail ird on irh.INB_RECEIPT_HEADER_ID=ird.INB_RECEIPT_HEADER_ID  "
							+"where irh.`STATUS`<'900' and ird.storer_code=bi.storer_code and bi.item_code = ird.item_code) "
							+"or exists(select osd.item_code from oub_shipment_detail osd inner join oub_shipment_header osh on osd.OUB_SHIPMENT_HEADER_ID=osh.OUB_SHIPMENT_HEADER_ID "
							+"where osh.`STATUS`<'900' and osd.storer_code=bi.storer_code and bi.item_code = osd.item_code) "
							+") and bi.ITEM_CODE='"+itemCode+"' ";
					DataManager dm = DBOperator.DoSelect2DM(sql_delete);
					if(dm==null || dm.getCurrentCount()==0){
						
					}else{
						Message.showWarningMessage("此商品信息已经在系统使用，不能删除");
						return;
					}
					
					sql_delete = "delete from bas_item where STORER_CODE='"+store_code+"' and ITEM_CODE='"+itemCode+"'";
					int t = DBOperator.DoUpdate(sql_delete);
					if(t>0){
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
			fieldList.add("bas_item.ITEM_CODE:货品编码");
			fieldList.add("bas_item.ITEM_BAR_CODE:货品条码");
			fieldList.add("bas_item.ITEM_NAME:货品名称");
			fieldList.add("bas_storer.STORER_CODE:货主编码");
			fieldList.add("bas_storer.STORER_NAME:货主名称");
			fieldList.add("bas_brand.BRAND_NAME:品牌");
			fieldList.add("bas_port.port_name:口岸");
			fieldList.add("bas_country.country_name:国家");
			fieldList.add("bas_item.TAX_NUMBER:税号");
			fieldList.add("bas_item.HSCODE:海关编码");
			fieldList.add("bas_item.HSCODE_DESC:申报要素");
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
	
	private void init(){
		enableEditComp(false);
		enableSaveCancelButton(false);
		initTableData("");
		
		table_item.addMouseListener(mouseAdapter);
	}
	MouseAdapter mouseAdapter = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()>=2){
					table_item.setColumnSelectionAllowed(true);
				}
				int r = table_item.getSelectedRow();
				comboBox_STORER_CODE.setSelectedItem(NulltoSpace(table_item.getValueAt(r, table_item.getColumnModel().getColumnIndex("货主"))));
				comboBox_BRAND_CODE.setSelectedItem(NulltoSpace(table_item.getValueAt(r, table_item.getColumnModel().getColumnIndex("品牌"))));
				textField_itemCode.setText(NulltoSpace(table_item.getValueAt(r, table_item.getColumnModel().getColumnIndex("货品编码"))));
				textField_itemName.setText(NulltoSpace(table_item.getValueAt(r, table_item.getColumnModel().getColumnIndex("货品名称"))));
				textField_BAR_CODE.setText(NulltoSpace(table_item.getValueAt(r, table_item.getColumnModel().getColumnIndex("货品条码"))));
				comboBox_port_no.setSelectedItem(NulltoSpace(table_item.getValueAt(r, table_item.getColumnModel().getColumnIndex("口岸"))));
				textField_TAX_NUMBER.setText(NulltoSpace(table_item.getValueAt(r, table_item.getColumnModel().getColumnIndex("税号"))));
				textField_HSCODE.setText(NulltoSpace(table_item.getValueAt(r, table_item.getColumnModel().getColumnIndex("海关编码"))));
				textField_HSCODE_DESC.setText(NulltoSpace(table_item.getValueAt(r, table_item.getColumnModel().getColumnIndex("申报要素"))));
				comboBox_unit_code.setSelectedItem(NulltoSpace(table_item.getValueAt(r, table_item.getColumnModel().getColumnIndex("最小计量单位"))));
				textField_item_spec.setText(NulltoSpace(table_item.getValueAt(r, table_item.getColumnModel().getColumnIndex("货品规格"))));
				comboBox_country_code.setSelectedItem(NulltoSpace(table_item.getValueAt(r, table_item.getColumnModel().getColumnIndex("国家"))));
				textArea_DESCRIPTION.setText(NulltoSpace(table_item.getValueAt(r, table_item.getColumnModel().getColumnIndex("货品描述"))));
			}
		};
	
	
	private void initTableData(String strWhere){
       
       String sql = "SELECT"
    		   +" `bas_storer`.`STORER_SHORT_NAME`"
    		   +", `bas_brand`.`BRAND_SHORT_NAME`"
    		   +", `bas_item`.`ITEM_CODE`"
    		   +", `bas_item`.`ITEM_NAME`"
    		   +", `bas_item`.`ITEM_BAR_CODE`"
    		   +", `bas_port`.`port_name`"
    		   +",  bas_item.TAX_NUMBER"
    		   +",  bas_item.HSCODE"
    		   +",  bas_item.HSCODE_DESC"
    		   +", `bas_item_unit`.`unit_name`"
    		   +", `bas_item`.`ITEM_SPEC`"
    		   +", `bas_country`.`country_name`"
    		   +", `bas_item`.`DESCRIPTION`"
    		   +"FROM"
    		   +".`bas_item`"
    		   +"INNER JOIN .`bas_storer` "
    		   +"ON (`bas_item`.`STORER_CODE` = `bas_storer`.`STORER_CODE`)"
    		   +"LEFT JOIN .`bas_brand` "
    		   +"ON (`bas_item`.`BRAND_CODE` = `bas_brand`.`BRAND_CODE`)"
    		   +"INNER JOIN .`bas_port` "
    		   +"ON (`bas_item`.`PORT_CODE` = `bas_port`.`port_code`)"
    		   +"LEFT JOIN .`bas_item_unit` "
    		   +"ON (`bas_item`.`UNIT_CODE` = `bas_item_unit`.`unit_code`)"
    		   +"LEFT JOIN .`bas_country` "
    		   +"ON (`bas_item`.`COUNTRY_CODE` = `bas_country`.`country_code`) "
    		   +" where 1=1 ";
        if(!strWhere.equals("")){
			sql = sql + strWhere;
		}
        sql = sql + " order by `bas_storer`.`STORER_CODE` ";
//        System.out.println("sql = "+sql);
        table_item.removeRowAll();
        Vector tableData = DBOperator.DoSelectAddSequenceRow(sql);
        table_item.setData(tableData);
        makeFace(table_item);
        JTableUtil.fitTableColumns(table_item);
        table_item.setColumnEditableAll(false);
	}
	
	private void enableAll(boolean b){
		enableCRUDbutton(b);
		enableEditComp(!b);
		enableSaveCancelButton(!b);
		clearEditUI();
		table_item.setEnabled(b);
		if(b==true){
			table_item.addMouseListener(mouseAdapter);
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
		comboBox_STORER_CODE.setEnabled(b);
		comboBox_BRAND_CODE.setEnabled(b);
		textField_itemCode.setEditable(b);
		textField_itemName.setEditable(b);
		comboBox_port_no.setEnabled(b);
		comboBox_unit_code.setEnabled(b);
		textField_item_spec.setEditable(b);
		comboBox_country_code.setEnabled(b);
		textField_BAR_CODE.setEditable(b);
		textArea_DESCRIPTION.setEditable(b);
		textField_TAX_NUMBER.setEditable(b);
		textField_HSCODE.setEditable(b);
		textField_HSCODE_DESC.setEditable(b);
	}
	private void clearEditUI(){
		textField_itemCode.setText("");
		textField_itemName.setText("");
		textArea_DESCRIPTION.setText("");
		textField_item_spec.setText("");
		textField_BAR_CODE.setText("");
		comboBox_STORER_CODE.setSelectedItem("");
		comboBox_BRAND_CODE.setSelectedItem("");
		comboBox_port_no.setSelectedItem("");
		comboBox_unit_code.setSelectedItem("");
		comboBox_country_code.setSelectedItem("");
		textField_TAX_NUMBER.setText("");
		textField_HSCODE.setText("");
		textField_HSCODE_DESC.setText("");
		table_item.clearSelection();
	}
	public static BasItemFrm getInstance() {
		if(instance == null) { 
			 synchronized(BasItemFrm.class){
				 if(instance == null) {
					 instance = new BasItemFrm();
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
	            instance = new BasItemFrm();  
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
					BasItemFrm frame = new BasItemFrm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
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
	public static String NulltoSpace(Object o) {
		if (o == null)
			return "";
		else if (o.equals("null")) {
			return "";
		} else
			return o.toString().trim();
	}
		
}
