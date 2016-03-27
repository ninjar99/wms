package outbound;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import DBUtil.DBConnectionManager;
import DBUtil.DBOperator;
import DBUtil.LogInfo;
import comUtil.WMSCombobox;
import comUtil.comData;
import dmdata.DataManager;
import main.PBSUIBaseGrid;
import sys.InnerFrame;
import sys.JTableUtil;
import sys.MainFrm;
import sys.Message;
import sys.QueryDialog;
import sys.tableQueryDialog;
import util.JTNumEdit;
import util.MyTableCellRenderrer;
import util.WaitingSplash;

public class ShipmentInputFrm extends InnerFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1281449283666709019L;
	private JPanel contentPane;
	private static volatile ShipmentInputFrm instance;
	private static boolean isOpen = false;
	private boolean trigTable = true;
	private PBSUIBaseGrid headerTable;
	private PBSUIBaseGrid detailTable;
	private JTextField txt_shipment_no;
	private JTextField txt_storer_code;
	private JTextField txt_storer_name;
	private JTextField txt_warehouse_code;
	private JTextField txt_erp_order_no;
	private JTextField txt_warehouse_name;
	private JButton btnAdd;
	private JButton btnModify;
	private JButton btnDelete;
	private JButton btnQuery;
	private JButton btnSave;
	private JButton btnCancel;
	private JButton btnClose;
	private HashMap delDetailRows = new HashMap();
	private JButton btnStorerQuery;
	private JButton btnWarehouseQuery;
	private JLabel label;
	private JTextField txt_status;
	private JPanel panel;
	private JPanel editPanel2;
	private JPanel editPanel3;
	private JLabel label_1;
	private JTextField txt_SHIP_TO_CONTACT;
	private JLabel label_2;
	private JTextField txt_SHIP_TO_TEL;
	private JLabel label_3;
	private JTextField txt_SHIP_TO_CONTACT_IDCARD;
	private JLabel label_4;
	private WMSCombobox cb_SHIP_TO_PROVINCE_CODE;
	private JLabel label_5;
	private WMSCombobox cb_SHIP_TO_CITY_CODE;
	private JLabel label_6;
	private WMSCombobox cb_SHIP_TO_REGION_CODE;
	private JLabel label_7;
	private WMSCombobox cb_SHIP_TO_STREET_CODE;
	private JLabel label_8;
	private JTextField txt_SHIP_TO_ADDRESS1;
	private JLabel lblEmail;
	private JTextField txt_SHIP_TO_EMAIL;
	private JPanel editPanel4;
	private JLabel lblNewLabel_4;
	private JTextField txt_remark;
	
	public static ShipmentInputFrm getInstance() {
		if(instance == null) { 
			 synchronized(ShipmentInputFrm.class){
				 if(instance == null) {
					 instance = new ShipmentInputFrm();
				 }
			 }
	        }  
	        return instance;
	 }
	
	public static synchronized boolean getOpenStatus() {
		 if(instance == null) {    
	            instance = new ShipmentInputFrm();  
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
					ShipmentInputFrm frame = new ShipmentInputFrm();
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
	public ShipmentInputFrm() {
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		BasicInternalFrameUI ui = (BasicInternalFrameUI)this.getUI(); //去除标题栏
//		ui.getNorthPane().setVisible(false);
		// 向侦听器列表添加一个 VetoableChangeListener。为所有属性注册该侦听器
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.addVetoableChangeListener(new VetoableChangeListener() {

			public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException {
				e.getSource();
				String name = e.getPropertyName();
				Object value = e.getNewValue();
				if (name.equals("closed") && value.equals(Boolean.TRUE)) // 窗口被关闭
				{
					System.out.println("POFrm窗口被关闭");
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
		
		setBounds(100, 100, 876, 477);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel topPanel = new JPanel();
		FlowLayout fl_topPanel = (FlowLayout) topPanel.getLayout();
		fl_topPanel.setAlignment(FlowLayout.LEFT);
		contentPane.add(topPanel, BorderLayout.NORTH);
		
		btnAdd = new JButton("\u589E\u52A0");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txt_erp_order_no.setEditable(true);
				txt_storer_code.setEditable(true);
				txt_warehouse_code.setEditable(true);
				txt_SHIP_TO_CONTACT.setEditable(true);
				txt_SHIP_TO_TEL.setEditable(true);
				txt_SHIP_TO_CONTACT_IDCARD.setEditable(true);
				cb_SHIP_TO_PROVINCE_CODE.setEnabled(true);
				cb_SHIP_TO_CITY_CODE.setEnabled(true);
				cb_SHIP_TO_REGION_CODE.setEnabled(true);
				cb_SHIP_TO_STREET_CODE.setEnabled(true);
				txt_SHIP_TO_ADDRESS1.setEditable(true);
				txt_SHIP_TO_EMAIL.setEditable(true);
				btnAdd.setEnabled(false);
				btnModify.setEnabled(false);
				btnDelete.setEnabled(false);
				btnQuery.setEnabled(false);
				btnSave.setEnabled(true);
				btnCancel.setEnabled(true);
				btnWarehouseQuery.setEnabled(true);
				btnStorerQuery.setEnabled(true);
				btnClose.setEnabled(false);
				
				clearFrom();
				
				//增加一行空行
				Object [] addRowValues = new Object[20];
				addRowValues[0] = detailTable.getRowCount()+1;
				addRowValues[1] = "新建";
				detailTable.addRow(addRowValues);
				detailTable.setComponent(new JTNumEdit(15, "#,##0.00",true), detailTable.getColumnModel().getColumnIndex("订单数量"));
				editDetailTableSetup();
//				tblMain.setComponent((new JTNumEdit(15, "#####",true)), 0);
				
//				Vector colID = new Vector();
//		        for(int i=0;i<detailTable.getRowCount();i++){
//		        	colID.addElement(new Integer(1));
//		        	colID.addElement(new Integer(3));
//		        	colID.addElement(new Integer(4));
//		        	colID.addElement(new Integer(5));
//		        	colID.addElement(new Integer(6));
//		        	colID.addElement(new Integer(7));
//		        	colID.addElement(new Integer(8));
//		        	colID.addElement(new Integer(9));
//		        	colID.addElement(new Integer(10));
//		        	colID.addElement(new Integer(11));
//		        	colID.addElement(new Integer(12));
//		        	colID.addElement(new Integer(13));
//		        	colID.addElement(new Integer(14));
//		        }
//				detailTable.setColColor(colID, Color.yellow);

				detailTable.getColumn("商品条码").setCellRenderer(new MyTableCellRenderrer());
				detailTable.getColumn("订单数量").setCellRenderer(new MyTableCellRenderrer());
				detailTable.getColumn("批次属性1").setCellRenderer(new MyTableCellRenderrer());
				detailTable.getColumn("批次属性2").setCellRenderer(new MyTableCellRenderrer());
				detailTable.getColumn("批次属性3").setCellRenderer(new MyTableCellRenderrer());
				detailTable.getColumn("批次属性4").setCellRenderer(new MyTableCellRenderrer());
				detailTable.getColumn("批次属性5").setCellRenderer(new MyTableCellRenderrer());
				detailTable.getColumn("批次属性6").setCellRenderer(new MyTableCellRenderrer());
				detailTable.getColumn("批次属性7").setCellRenderer(new MyTableCellRenderrer());
				detailTable.getColumn("批次属性8").setCellRenderer(new MyTableCellRenderrer());
				detailTable.getColumn("批次属性9").setCellRenderer(new MyTableCellRenderrer());
				detailTable.getColumn("批次属性10").setCellRenderer(new MyTableCellRenderrer());
				detailTable.updateUI();
//				makeFace(detailTable);
				detailTable.requestFocus();
				detailTable.changeSelection(detailTable.getRowCount()-1,5,false,false);
				JTableUtil.fitTableColumnsDoubleWidth(detailTable);
				
			}
		});
		topPanel.add(btnAdd);
		
		btnModify = new JButton("\u4FEE\u6539");
		btnModify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(txt_shipment_no.getText().trim().equals("")){
					JOptionPane.showMessageDialog(null, "请选择一行数据！");
					return;
				}
				String poStatus = checkShipmentNoStatus(txt_shipment_no.getText().trim());
				if(poStatus.equals("")){
					Message.showErrorMessage("订单号不存在");
					return;
				}else if(!poStatus.equals("100")){
					Message.showWarningMessage("订单已经开始出库拣货，不能进行修改");
					return;
				}
				if(detailTable.getRowCount()==0){
					//增加一行空行
					Object [] addRowValues = new Object[20];
					addRowValues[0] = detailTable.getRowCount()+1;
					addRowValues[1] = "新建";
					detailTable.addRow(addRowValues);
					editDetailTableSetup();
					detailTable.editCellAt(detailTable.getRowCount() - 1, 1);
				}
				
				txt_erp_order_no.setEditable(false);
				txt_storer_code.setEditable(false);
				txt_warehouse_code.setEditable(false);
				txt_SHIP_TO_CONTACT.setEditable(true);
				txt_SHIP_TO_TEL.setEditable(true);
				txt_SHIP_TO_CONTACT_IDCARD.setEditable(true);
				cb_SHIP_TO_PROVINCE_CODE.setEnabled(true);
				cb_SHIP_TO_CITY_CODE.setEnabled(true);
				cb_SHIP_TO_REGION_CODE.setEnabled(true);
				cb_SHIP_TO_STREET_CODE.setEnabled(true);
				txt_SHIP_TO_ADDRESS1.setEditable(true);
				txt_SHIP_TO_EMAIL.setEditable(true);
				btnAdd.setEnabled(false);
				btnModify.setEnabled(false);
				btnDelete.setEnabled(false);
				btnQuery.setEnabled(false);
				btnSave.setEnabled(true);
				btnCancel.setEnabled(true);
				btnWarehouseQuery.setEnabled(false);
				btnStorerQuery.setEnabled(false);
				btnClose.setEnabled(false);
				headerTable.setEnabled(false);
				detailTable.setColumnEditableAll(true);
				editDetailTableSetup();
			}
		});
		topPanel.add(btnModify);
		
		btnDelete = new JButton("\u5220\u9664");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(txt_shipment_no.getText().trim().equals("")){
					JOptionPane.showMessageDialog(null, "请选择一行数据！");
					return;
				}
				String poStatus = checkShipmentNoStatus(txt_shipment_no.getText().trim());
				if(poStatus.equals("")){
					Message.showErrorMessage("订单号不存在");
					return;
				}else if(!poStatus.equals("100")){
					Message.showWarningMessage("订单已经开始出库操作，不能删除");
					return;
				}
				int t = JOptionPane.showConfirmDialog(null, "是否删除该订单【"+txt_shipment_no.getText().trim()+"]？");
				if(t==0){
					//删除PO
					String sql = "delete from oub_shipment_header where SHIPMENT_NO='"+txt_shipment_no.getText().trim()+"' and status='100' ";
					int delrow = DBOperator.DoUpdate(sql);
					if(delrow==1){
						sql = "delete from oub_shipment_detail where SHIPMENT_NO='"+txt_shipment_no.getText().trim()+"' ";
						DBOperator.DoUpdate(sql);
						JOptionPane.showMessageDialog(null, "删除数据成功","提示",JOptionPane.INFORMATION_MESSAGE);
						getHeaderTableData("");
					}else{
						JOptionPane.showMessageDialog(null, "删除数据失败","提示",JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		topPanel.add(btnDelete);
		
		btnQuery = new JButton("\u67E5\u8BE2");
		btnQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> fieldList = new ArrayList<String>();
				fieldList.add("osh.TRANSFER_ORDER_NO:运单号");
				fieldList.add("osh.SHIPMENT_NO:订单号");
				fieldList.add("osh.ERP_ORDER_NO:外部订单号");
				fieldList.add("osh.WAREHOUSE_CODE:仓库编码");
				fieldList.add("bw.WAREHOUSE_NAME:仓库名称");
				fieldList.add("osh.STORER_CODE:货主编码");
				fieldList.add("bs.STORER_NAME:货主名称");
				QueryDialog query = QueryDialog.getInstance(fieldList);
				Toolkit toolkit = Toolkit.getDefaultToolkit();
				int x = (int)(toolkit.getScreenSize().getWidth()-query.getWidth())/2;
				int y = (int)(toolkit.getScreenSize().getHeight()-query.getHeight())/2;
				query.setLocation(x, y);
				query.setVisible(true);
				String retWhere = QueryDialog.queryValueResult;
				if(retWhere.length()>0){
					retWhere = " and "+retWhere+" order by osh.CREATED_DTM_LOC desc ";
				}
				getHeaderTableData(retWhere);
			}
		});
		topPanel.add(btnQuery);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		verticalStrut.setBackground(Color.BLUE);
		topPanel.add(verticalStrut);
		
		btnSave = new JButton("\u4FDD\u5B58");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(txt_storer_code.getText().equals("")){
					Message.showWarningMessage("请输入货主信息");
					return;
				}
				if(cb_SHIP_TO_STREET_CODE.getSelectedIndex()==-1 || cb_SHIP_TO_STREET_CODE.getSelectedItem().toString().equals("")){
					Message.showWarningMessage("请选择省市区地址信息");
					return ;
				}
				if(txt_SHIP_TO_CONTACT.getText().trim().equals("")){
					Message.showWarningMessage("请输入收货人信息");
					txt_SHIP_TO_CONTACT.requestFocus();
					return ;
				}
				if(txt_erp_order_no.getText().trim().equals("")){
					Message.showWarningMessage("请输入外部订单号");
					txt_erp_order_no.requestFocus();
					return ;
				}
				if(txt_warehouse_code.getText().trim().equals("")){
					Message.showWarningMessage("请输入仓库信息");
					txt_warehouse_code.requestFocus();
					return ;
				}
				if(txt_SHIP_TO_CONTACT_IDCARD.getText().trim().equals("")){
					Message.showWarningMessage("请输入身份证信息");
					txt_SHIP_TO_CONTACT_IDCARD.requestFocus();
					return ;
				}
				if(txt_SHIP_TO_ADDRESS1.getText().trim().equals("")){
					Message.showWarningMessage("请输入详细地址");
					txt_SHIP_TO_ADDRESS1.requestFocus();
					return ;
				}
				if(detailTable.getValueAt(0, 1)==null || detailTable.getValueAt(0, 1).toString().equals("")){
					int t = JOptionPane.showConfirmDialog(null, "明细数据不完整，是否取消？\n如果不取消，只保存订单表头数据");
					if(t==0 || t==2){
						txt_erp_order_no.setText("");
						txt_storer_code.setText("");
						txt_storer_name.setText("");
						txt_warehouse_code.setText("");
						txt_warehouse_name.setText("");
						txt_erp_order_no.setEditable(false);
						txt_storer_code.setEditable(false);
						txt_warehouse_code.setEditable(false);
						txt_SHIP_TO_CONTACT.setEditable(false);
						txt_SHIP_TO_TEL.setEditable(false);
						txt_SHIP_TO_CONTACT_IDCARD.setEditable(false);
						cb_SHIP_TO_PROVINCE_CODE.setEnabled(false);
						cb_SHIP_TO_CITY_CODE.setEnabled(false);
						cb_SHIP_TO_REGION_CODE.setEnabled(false);
						cb_SHIP_TO_STREET_CODE.setEnabled(false);
						txt_SHIP_TO_ADDRESS1.setEditable(false);
						txt_SHIP_TO_EMAIL.setEditable(false);
						btnAdd.setEnabled(true);
						btnModify.setEnabled(true);
						btnDelete.setEnabled(true);
						btnQuery.setEnabled(true);
						btnSave.setEnabled(false);
						btnCancel.setEnabled(false);
						btnClose.setEnabled(true);
						headerTable.setEnabled(true);
						btnWarehouseQuery.setEnabled(false);
						btnStorerQuery.setEnabled(false);
						detailTable.setColumnEditableAll(false);
						clearDetailTable();
						return;
					}
				}
				txt_erp_order_no.setEditable(false);
				txt_storer_code.setEditable(false);
				txt_warehouse_code.setEditable(false);
				txt_SHIP_TO_CONTACT.setEditable(false);
				txt_SHIP_TO_TEL.setEditable(false);
				txt_SHIP_TO_CONTACT_IDCARD.setEditable(false);
				cb_SHIP_TO_PROVINCE_CODE.setEnabled(false);
				cb_SHIP_TO_CITY_CODE.setEnabled(false);
				cb_SHIP_TO_REGION_CODE.setEnabled(false);
				cb_SHIP_TO_STREET_CODE.setEnabled(false);
				txt_SHIP_TO_ADDRESS1.setEditable(false);
				txt_SHIP_TO_EMAIL.setEditable(false);
				btnAdd.setEnabled(true);
				btnModify.setEnabled(true);
				btnDelete.setEnabled(true);
				btnQuery.setEnabled(true);
				btnSave.setEnabled(false);
				btnCancel.setEnabled(false);
				btnClose.setEnabled(true);
				headerTable.setEnabled(true);
				if(!saveData()){
					return;
				}
				getHeaderTableData(" order by osh.UPDATED_DTM_LOC desc ");//and osh.shipment_no='"+txt_shipment_no.getText().trim()+"'
			}
		});
		btnSave.setEnabled(false);
		topPanel.add(btnSave);
		
		btnCancel = new JButton("\u53D6\u6D88");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(txt_shipment_no.getText().equals("")){
					txt_erp_order_no.setText("");
					txt_storer_code.setText("");
					txt_storer_name.setText("");
					txt_warehouse_code.setText("");
					txt_warehouse_name.setText("");
					txt_SHIP_TO_CONTACT.setText("");
					txt_SHIP_TO_TEL.setText("");
					txt_SHIP_TO_CONTACT_IDCARD.setText("");
					txt_SHIP_TO_ADDRESS1.setText("");
					txt_SHIP_TO_EMAIL.setText("");
					cb_SHIP_TO_PROVINCE_CODE.setSelectedIndex(0);
					cb_SHIP_TO_CITY_CODE.setSelectedIndex(-1);
					cb_SHIP_TO_REGION_CODE.setSelectedIndex(-1);
					cb_SHIP_TO_STREET_CODE.setSelectedIndex(-1);
				}
				txt_erp_order_no.setEditable(false);
				txt_storer_code.setEditable(false);
				txt_warehouse_code.setEditable(false);
				txt_SHIP_TO_CONTACT.setEditable(false);
				txt_SHIP_TO_TEL.setEditable(false);
				txt_SHIP_TO_CONTACT_IDCARD.setEditable(false);
				cb_SHIP_TO_PROVINCE_CODE.setEnabled(false);
				cb_SHIP_TO_CITY_CODE.setEnabled(false);
				cb_SHIP_TO_REGION_CODE.setEnabled(false);
				cb_SHIP_TO_STREET_CODE.setEnabled(false);
				txt_SHIP_TO_ADDRESS1.setEditable(false);
				txt_SHIP_TO_EMAIL.setEditable(false);
				btnAdd.setEnabled(true);
				btnModify.setEnabled(true);
				btnDelete.setEnabled(true);
				btnQuery.setEnabled(true);
				btnSave.setEnabled(false);
				btnCancel.setEnabled(false);
				btnClose.setEnabled(true);
				headerTable.setEnabled(true);
				btnWarehouseQuery.setEnabled(false);
				btnStorerQuery.setEnabled(false);
				detailTable.setColumnEditableAll(false);
				clearDetailTable();
			}
		});
		btnCancel.setEnabled(false);
		topPanel.add(btnCancel);
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		verticalStrut_1.setBackground(Color.BLUE);
		topPanel.add(verticalStrut_1);
		
		btnClose = new JButton("\u5173\u95ED");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					instance.setClosed(true);
				} catch (PropertyVetoException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		topPanel.add(btnClose);
		
		JPanel leftPanel = new JPanel();
		contentPane.add(leftPanel, BorderLayout.WEST);
		leftPanel.setLayout(new BorderLayout(0, 0));
		leftPanel.setPreferredSize(new Dimension(100, 120));
		
		JScrollPane scrollPane = new JScrollPane();
		leftPanel.add(scrollPane, BorderLayout.CENTER);
		
		headerTable = new PBSUIBaseGrid();
		headerTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				headerTableClick();
			}
		});
		scrollPane.setViewportView(headerTable);
		
		JPanel rightPanel = new JPanel();
		contentPane.add(rightPanel, BorderLayout.CENTER);
		rightPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel showPanel = new JPanel();
		rightPanel.add(showPanel, BorderLayout.CENTER);
		showPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		showPanel.add(scrollPane_1, BorderLayout.CENTER);
		
		detailTable = new PBSUIBaseGrid();
		detailTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() >= 2) {
					detailTable.setColumnSelectionAllowed(true);
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3 && btnSave.isEnabled()) {
					detailTable.getValueAt(detailTable.getSelectedRow(), detailTable.getColumnModel().getColumnIndex("商品条码"));
					String status = (String) detailTable.getValueAt(detailTable.getSelectedRow(), detailTable.getColumnModel().getColumnIndex("状态"));
//					if(!barcode.equals("")){
//						return;
//					}
					if(!status.equals("新建")){
						return;
					}
					JPopupMenu popupmenu = new JPopupMenu();
					JMenuItem menuItem1 = new JMenuItem();
					menuItem1.setLabel("选择商品明细");
					JMenuItem menuItem2 = new JMenuItem();
					menuItem2.setLabel("添加行");
					JMenuItem menuItem3 = new JMenuItem();
					menuItem3.setLabel("删除行");
					menuItem1.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(ActionEvent e) {
//							boolean b_confirm = Message.showOKorCancelMessage("是否确认修改订单商品编码?\n");
//							if(b_confirm){
								String STORER_CODE = txt_storer_code.getText();
								if(STORER_CODE.equals("")){
									JOptionPane.showMessageDialog(null, "请输入货主编码");
									txt_storer_code.requestFocus();
									return;
								}
								String sql = "select item.STORER_CODE 货主编码,bs.STORER_NAME 货主名称,item.ITEM_CODE 货品编码,item.ITEM_NAME 货品名称,item.ITEM_BAR_CODE 货品条码,biu.unit_name 单位  "
										+ "from bas_item item " + "inner join bas_storer bs on item.STORER_CODE=bs.STORER_CODE "
										+ "left join bas_item_unit biu on biu.unit_code=item.UNIT_CODE " 
										+ "where bs.STORER_CODE ='"+STORER_CODE+"' ";
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
									detailTable.setValueAt(dm.getString("货品条码", 0), detailTable.getSelectedRow(), detailTable.getColumnModel().getColumnIndex("商品条码"));
									detailTable.setValueAt(dm.getString("货品编码", 0), detailTable.getSelectedRow(), detailTable.getColumnModel().getColumnIndex("商品编码"));
									detailTable.setValueAt(dm.getString("货品名称", 0), detailTable.getSelectedRow(), detailTable.getColumnModel().getColumnIndex("商品名称"));
									detailTable.setValueAt(dm.getString("单位", 0), detailTable.getSelectedRow(), detailTable.getColumnModel().getColumnIndex("单位"));
								}
//							}
						}
						});
					menuItem2.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(ActionEvent e) {
							// 增加一行空行
							Object[] addRowValues = new Object[20];
							addRowValues[0] = detailTable.getRowCount() + 1;
							addRowValues[1] = "新建";
							detailTable.addRow(addRowValues);
							editDetailTableSetup();
							detailTable.editCellAt(detailTable.getRowCount() - 1, 1);
						}
					});
					menuItem3.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(ActionEvent e) {
							// 删除行
							int t = JOptionPane.showConfirmDialog(null, "是否删除该改行明细？");
							if(t==0){
								int tableRowCount = detailTable.getRowCount();
								if(tableRowCount<=1){
									JOptionPane.showMessageDialog(null, "最后一行数据不能删除！");
									return;
								}
								int selrow = detailTable.getSelectedRow();
								delDetailRows.put(txt_shipment_no.getText(), detailTable.getValueAt(selrow, 0).toString());
								detailTable.removeRow(selrow);
							}
						}
					});
					popupmenu.add(menuItem1);
					popupmenu.add(menuItem2);
					popupmenu.add(menuItem3);
					popupmenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
		scrollPane_1.setViewportView(detailTable);
		detailTable.getModel().addTableModelListener(new TableModelListener()
	    {
	      public void tableChanged(TableModelEvent e)
	      {
	        tblMain_tableChanged(e);
	      }
	    });
		// 在进入编辑状态下才自动加行,未进入编辑直接输出修改不换行
		detailTable.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if(!btnSave.isEnabled()){
					return;
				}
				//155:insert 127:delete
				if(e.getKeyCode()==127){
					int t = JOptionPane.showConfirmDialog(null, "是否删除该改行明细？");
					if(t==0){
						int tableRowCount = detailTable.getRowCount();
						if(tableRowCount<=1){
							JOptionPane.showMessageDialog(null, "最后一行数据不能删除！");
							return;
						}
						int selrow = detailTable.getSelectedRow();
						delDetailRows.put(txt_shipment_no.getText(), detailTable.getValueAt(selrow, 0).toString());
						detailTable.removeRow(selrow);
					}
				}
				if (e.getKeyChar() == '\n') {
					if(detailTable.getSelectedColumn()==17){
						//增加一行空行
						Object [] addRowValues = new Object[20];
						addRowValues[0] = detailTable.getRowCount()+1;
						addRowValues[1] = "新建";
						detailTable.addRow(addRowValues);
						editDetailTableSetup();
						detailTable.editCellAt(detailTable.getRowCount() - 1, 1);
					}else{
						detailTable.editCellAt(detailTable.getSelectedRow(), detailTable.getSelectedColumn());
					}
				}
			}
		});
		//detailTable
		String[] SOColumnNames = {"行号","状态","商品条码","商品编码","商品名称","单位","订单数量","出库分拣数量","批次属性1","批次属性2","批次属性3","批次属性4","批次属性5","批次属性6"
				,"批次属性7","批次属性8","批次属性9","批次属性10","创建时间","创建用户","最近更新时间","最近更新用户"};
		detailTable.setColumn(SOColumnNames);
		detailTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		panel = new JPanel();
		rightPanel.add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(4, 1, 0, 0));
		
		JPanel editPanel1 = new JPanel();
		panel.add(editPanel1);
		FlowLayout fl_editPanel1 = (FlowLayout) editPanel1.getLayout();
		fl_editPanel1.setAlignment(FlowLayout.LEFT);
		editPanel1.setPreferredSize(new Dimension(100, 25));
		
		JLabel lblNewLabel = new JLabel("\u8BA2\u5355\u53F7:");
		editPanel1.add(lblNewLabel);
		txt_shipment_no = new JTextField();
		txt_shipment_no.setEditable(false);
		editPanel1.add(txt_shipment_no);
		txt_shipment_no.setColumns(10);
		
		label = new JLabel("\u72B6\u6001:");
		editPanel1.add(label);
		
		txt_status = new JTextField();
		txt_status.setEditable(false);
		editPanel1.add(txt_status);
		txt_status.setColumns(4);
		
		JLabel lblNewLabel_6 = new JLabel("\u5916\u90E8\u8BA2\u5355\u53F7\uFF1A");
		editPanel1.add(lblNewLabel_6);
		
		txt_erp_order_no = new JTextField();
		txt_erp_order_no.setEditable(false);
		editPanel1.add(txt_erp_order_no);
		txt_erp_order_no.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("\u8D27\u4E3B\u7F16\u7801\uFF1A");
		editPanel1.add(lblNewLabel_1);
		
		txt_storer_code = new JTextField();
		txt_storer_code.addFocusListener(new FocusListener(){
			public void focusLost(FocusEvent e) {
				if(!txt_storer_code.isEditable()) return;
				if(txt_storer_code.getText().trim().equals("") && txt_storer_code.isEditable()){
//					JOptionPane.showMessageDialog(null, "请输入货主编码");
//					txt_storer_code.setFocusable(true);
//					txt_storer_code.requestFocus();
				}else{
					Vector data = DBOperator.DoSelect("select storer_name from bas_storer where storer_code='"+txt_storer_code.getText().trim()+"'");
					if(data.size()>0){
						Object[] row = (Object[]) data.get(0);
						txt_storer_name.setText(row[0].toString());
						String erp_order_no = txt_erp_order_no.getText().trim();
						Vector shipmentHeader = DBOperator.DoSelect("select ERP_ORDER_NO from oub_shipment_header where storer_code='"+txt_storer_code.getText().trim()+"' and ERP_ORDER_NO='"+txt_erp_order_no.getText().trim()+"'");
						if(shipmentHeader.size()>0 && erp_order_no.length()>0){
							JOptionPane.showMessageDialog(null, "该货主对应的【外部订单号】已经存在，请重新输入【外部订单号】","提示",JOptionPane.WARNING_MESSAGE);
							txt_erp_order_no.setFocusable(true);
							txt_erp_order_no.requestFocus();
							txt_erp_order_no.selectAll();
						}
					}else{
						JOptionPane.showMessageDialog(null, "请输入正确货主编码","提示",JOptionPane.WARNING_MESSAGE);
						txt_storer_code.setFocusable(true);
						txt_storer_code.requestFocus();
					}
				}
				
			}

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		txt_storer_code.setEditable(false);
		editPanel1.add(txt_storer_code);
		txt_storer_code.setColumns(8);
		
		btnStorerQuery = new JButton("<");
		btnStorerQuery.setEnabled(false);
		btnStorerQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = "select distinct storer_code 货主编码,storer_name 货主名称 from bas_storer ";
				tableQueryDialog tableQuery = new tableQueryDialog(sql,false);
				Toolkit toolkit = Toolkit.getDefaultToolkit();
				int x = (int)(toolkit.getScreenSize().getWidth()-tableQuery.getWidth())/2;
				int y = (int)(toolkit.getScreenSize().getHeight()-tableQuery.getHeight())/2;
				tableQuery.setLocation(x, y);
				tableQuery.setModal(true);
				tableQuery.setVisible(true);
				DataManager dm = tableQueryDialog.resultDM;
				if(dm==null){
					return;
				}
				Object obj = dm.getObject("货主编码", 0);
				if(obj==null || obj.equals("")){
					return;
				}else{
					txt_storer_code.setText((String) dm.getObject("货主编码", 0));
					txt_storer_code.requestFocus();
					txt_storer_code.selectAll();
				}
			}
		});
		editPanel1.add(btnStorerQuery);
		
		JLabel lblNewLabel_2 = new JLabel("\u8D27\u4E3B\u540D\u79F0\uFF1A");
		editPanel1.add(lblNewLabel_2);
		
		txt_storer_name = new JTextField();
		txt_storer_name.setEditable(false);
		editPanel1.add(txt_storer_name);
		txt_storer_name.setColumns(20);
		
		editPanel2 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) editPanel2.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel.add(editPanel2);
		
		JLabel lblNewLabel_3 = new JLabel("\u4ED3\u5E93\u7F16\u7801\uFF1A");
		editPanel2.add(lblNewLabel_3);
		
		txt_warehouse_code = new JTextField();
		editPanel2.add(txt_warehouse_code);
		txt_warehouse_code.setEditable(false);
		txt_warehouse_code.addFocusListener(new FocusListener(){
			public void focusLost(FocusEvent e) {
				if(!txt_warehouse_code.isEditable()) return;
				if(txt_warehouse_code.getText().trim().equals("") && txt_warehouse_code.isEditable()){
//					JOptionPane.showMessageDialog(null, "请输入仓库编码");
//					txt_warehouse_code.setFocusable(true);
//					txt_warehouse_code.requestFocus();
				}else{
					Vector data = DBOperator.DoSelect("select warehouse_name from bas_warehouse where warehouse_code='"+txt_warehouse_code.getText().trim()+"' and warehouse_code='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' ");
					if(data.size()>0){
						Object[] row = (Object[]) data.get(0);
						txt_warehouse_name.setText(row[0].toString());
					}else{
						JOptionPane.showMessageDialog(null, "请输入正确仓库编码","提示",JOptionPane.WARNING_MESSAGE);
						txt_warehouse_code.setFocusable(true);
						txt_warehouse_code.requestFocus();
					}
				}
				
			}

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		txt_warehouse_code.setColumns(8);
		
		btnWarehouseQuery = new JButton("<");
		btnWarehouseQuery.setEnabled(false);
		editPanel2.add(btnWarehouseQuery);
		btnWarehouseQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = "select distinct warehouse_code 仓库编码,warehouse_name 仓库名称  from bas_warehouse where warehouse_code='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' ";
				tableQueryDialog tableQuery = new tableQueryDialog(sql,false);
				Toolkit toolkit = Toolkit.getDefaultToolkit();
				int x = (int)(toolkit.getScreenSize().getWidth()-tableQuery.getWidth())/2;
				int y = (int)(toolkit.getScreenSize().getHeight()-tableQuery.getHeight())/2;
				tableQuery.setLocation(x, y);
				tableQuery.setModal(true);
				tableQuery.setVisible(true);
				DataManager dm = tableQueryDialog.resultDM;
				if(dm==null){
					return;
				}
				Object obj = dm.getObject("仓库编码", 0);
				if(obj==null || obj.equals("")){
					return;
				}else{
					txt_warehouse_code.setText((String) dm.getObject("仓库编码", 0));
					txt_warehouse_code.requestFocus();
					txt_warehouse_code.selectAll();
				}
			}
		});
		
		JLabel lblNewLabel_7 = new JLabel("\u4ED3\u5E93\u540D\u79F0\uFF1A");
		editPanel2.add(lblNewLabel_7);
		
		txt_warehouse_name = new JTextField();
		editPanel2.add(txt_warehouse_name);
		txt_warehouse_name.setEditable(false);
		txt_warehouse_name.setColumns(15);
		
		label_1 = new JLabel("\u6536\u8D27\u4EBA\uFF1A");
		editPanel2.add(label_1);
		
		txt_SHIP_TO_CONTACT = new JTextField();
		txt_SHIP_TO_CONTACT.setEditable(false);
		editPanel2.add(txt_SHIP_TO_CONTACT);
		txt_SHIP_TO_CONTACT.setColumns(6);
		
		label_2 = new JLabel("\u8054\u7CFB\u7535\u8BDD\uFF1A");
		editPanel2.add(label_2);
		
		txt_SHIP_TO_TEL = new JTextField();
		txt_SHIP_TO_TEL.setEditable(false);
		editPanel2.add(txt_SHIP_TO_TEL);
		txt_SHIP_TO_TEL.setColumns(10);
		
		label_3 = new JLabel("\u8EAB\u4EFD\u8BC1\uFF1A");
		editPanel2.add(label_3);
		
		txt_SHIP_TO_CONTACT_IDCARD = new JTextField();
		txt_SHIP_TO_CONTACT_IDCARD.setEditable(false);
		editPanel2.add(txt_SHIP_TO_CONTACT_IDCARD);
		txt_SHIP_TO_CONTACT_IDCARD.setColumns(15);
		
		editPanel3 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) editPanel3.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		panel.add(editPanel3);
		
		label_4 = new JLabel("\u7701\uFF1A");
		editPanel3.add(label_4);
		
		cb_SHIP_TO_PROVINCE_CODE = new WMSCombobox("select code,name from sys_prov_city_area_street where level = 1 and parentId=0",true);
		cb_SHIP_TO_PROVINCE_CODE.setEnabled(false);
		cb_SHIP_TO_PROVINCE_CODE.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					cb_SHIP_TO_CITY_CODE.removeAllItems();
					String sql = "select code,name from sys_prov_city_area_street where level = 2 and parentId="+(cb_SHIP_TO_PROVINCE_CODE.getSelectedOID().equals("")?0:cb_SHIP_TO_PROVINCE_CODE.getSelectedOID());
	            	DataManager dm = DBOperator.DoSelect2DM(sql);
	            	if(dm.getCurrentCount()>0){
	            		for(int i=0;i<dm.getCurrentCount();i++){
	            			cb_SHIP_TO_CITY_CODE.hm_name_OID.put(dm.getString("name", i), dm.getString("code", i));
	            			cb_SHIP_TO_CITY_CODE.hm_OID_name.put(dm.getString("code", i),dm.getString("name", i));
	            			cb_SHIP_TO_CITY_CODE.addItem(dm.getString("name", i));
	            		}
	            	}
				}else if(e.getStateChange() == ItemEvent.ITEM_FIRST){
					cb_SHIP_TO_CITY_CODE.removeAllItems();
					String sql = "select code,name from sys_prov_city_area_street where level = 2 and parentId="+cb_SHIP_TO_PROVINCE_CODE.getSelectedOID();
	            	DataManager dm = DBOperator.DoSelect2DM(sql);
	            	if(dm.getCurrentCount()>0){
	            		for(int i=0;i<dm.getCurrentCount();i++){
	            			cb_SHIP_TO_CITY_CODE.hm_name_OID.put(dm.getString("name", i), dm.getString("code", i));
	            			cb_SHIP_TO_CITY_CODE.hm_OID_name.put(dm.getString("code", i),dm.getString("name", i));
	            			cb_SHIP_TO_CITY_CODE.addItem(dm.getString("name", i));
	            		}
	            	}
				}
				
			}
		});
		editPanel3.add(cb_SHIP_TO_PROVINCE_CODE);
		
		label_5 = new JLabel("\u5E02\uFF1A");
		editPanel3.add(label_5);
		
		cb_SHIP_TO_CITY_CODE = new WMSCombobox("select code,name from sys_prov_city_area_street where level = 2 and parentId=0",true);
		cb_SHIP_TO_CITY_CODE.setEnabled(false);
		cb_SHIP_TO_CITY_CODE.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					cb_SHIP_TO_REGION_CODE.removeAllItems();
					String sql = "select code,name from sys_prov_city_area_street where level = 3 and parentId="+(cb_SHIP_TO_CITY_CODE.getSelectedOID().equals("")?0:cb_SHIP_TO_CITY_CODE.getSelectedOID());
	            	DataManager dm = DBOperator.DoSelect2DM(sql);
	            	if(dm.getCurrentCount()>0){
	            		for(int i=0;i<dm.getCurrentCount();i++){
	            			cb_SHIP_TO_REGION_CODE.hm_name_OID.put(dm.getString("name", i), dm.getString("code", i));
	            			cb_SHIP_TO_REGION_CODE.hm_OID_name.put(dm.getString("code", i),dm.getString("name", i));
	            			cb_SHIP_TO_REGION_CODE.addItem(dm.getString("name", i));
	            		}
	            	}
				}
			}
		});
		editPanel3.add(cb_SHIP_TO_CITY_CODE);
		
		label_6 = new JLabel("\u533A\uFF1A");
		editPanel3.add(label_6);
		
		cb_SHIP_TO_REGION_CODE = new WMSCombobox("select code,name from sys_prov_city_area_street where level = 3 and parentId="+(cb_SHIP_TO_CITY_CODE.getSelectedOID().equals("")?0:cb_SHIP_TO_CITY_CODE.getSelectedOID()),true);
		cb_SHIP_TO_REGION_CODE.setEnabled(false);
		cb_SHIP_TO_REGION_CODE.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					cb_SHIP_TO_STREET_CODE.removeAllItems();
					String sql = "select code,name from sys_prov_city_area_street where level = 4 and parentId="+(cb_SHIP_TO_REGION_CODE.getSelectedOID().equals("")?0:cb_SHIP_TO_REGION_CODE.getSelectedOID());
	            	DataManager dm = DBOperator.DoSelect2DM(sql);
	            	if(dm.getCurrentCount()>0){
	            		for(int i=0;i<dm.getCurrentCount();i++){
	            			cb_SHIP_TO_STREET_CODE.hm_name_OID.put(dm.getString("name", i), dm.getString("code", i));
	            			cb_SHIP_TO_STREET_CODE.hm_OID_name.put(dm.getString("code", i),dm.getString("name", i));
	            			cb_SHIP_TO_STREET_CODE.addItem(dm.getString("name", i));
	            		}
	            	}
				}
			}
		});
		editPanel3.add(cb_SHIP_TO_REGION_CODE);
		
		label_7 = new JLabel("\u8857\u9053\uFF1A");
		editPanel3.add(label_7);
		
		cb_SHIP_TO_STREET_CODE = new WMSCombobox("select code,name from sys_prov_city_area_street where level = 4 and parentId="+(cb_SHIP_TO_REGION_CODE.getSelectedOID().equals("")?0:cb_SHIP_TO_REGION_CODE.getSelectedOID()),true);
		cb_SHIP_TO_STREET_CODE.setEnabled(false);
		editPanel3.add(cb_SHIP_TO_STREET_CODE);
		
		editPanel4 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) editPanel4.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		panel.add(editPanel4);
		
		label_8 = new JLabel("\u8BE6\u7EC6\u5730\u5740\uFF1A");
		editPanel4.add(label_8);
		
		txt_SHIP_TO_ADDRESS1 = new JTextField();
		txt_SHIP_TO_ADDRESS1.setEditable(false);
		editPanel4.add(txt_SHIP_TO_ADDRESS1);
		txt_SHIP_TO_ADDRESS1.setColumns(25);
		
		lblEmail = new JLabel("EMAIL\uFF1A");
		editPanel4.add(lblEmail);
		
		txt_SHIP_TO_EMAIL = new JTextField();
		txt_SHIP_TO_EMAIL.setEditable(false);
		editPanel4.add(txt_SHIP_TO_EMAIL);
		txt_SHIP_TO_EMAIL.setColumns(20);
		
		lblNewLabel_4 = new JLabel("\u5907\u6CE8\uFF1A");
		editPanel4.add(lblNewLabel_4);
		
		txt_remark = new JTextField();
		editPanel4.add(txt_remark);
		txt_remark.setColumns(20);
		getHeaderTableData(" and osh.status='100' order by osh.CREATED_DTM_LOC desc limit 20 ");
//        DefaultTableModel dtm2 = (DefaultTableModel) detailTable.getModel();
//        dtm2.addColumn("PO行号");
//        dtm2.addColumn("商品条码");
//        dtm2.addColumn("商品名称");
//        dtm2.addColumn("数量");
//        dtm2.addColumn("出库分拣数量");
//        dtm2.addColumn("批次属性1");
//        dtm2.addColumn("批次属性2");
//        dtm2.addColumn("批次属性3");
//        dtm2.addColumn("批次属性4");
//        dtm2.addColumn("批次属性5");
//        dtm2.addColumn("批次属性6");
//        dtm2.addColumn("批次属性7");
//        dtm2.addColumn("批次属性8");
//        dtm2.addColumn("批次属性9");
//        dtm2.addColumn("批次属性10");
//        dtm2.addColumn("导入时间");
//        dtm2.addColumn("导入用户");
        
        //单选
//        DefaultListSelectionModel tableSelectionModel2 = (DefaultListSelectionModel) headerTable.getSelectionModel();
//        tableSelectionModel2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        detailTable.setRowHeight(0, 20);//20
////        //横向滚动条显示
//        detailTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//        JTableUtil.fitTableColumns(detailTable);
//        detailTable.getTableHeader().setReorderingAllowed(false);
	}
	
	protected void editDetailTableSetup() {
		detailTable.setColumnEditableAll(true);
		detailTable.setColumnEditable(false,detailTable.getColumnModel().getColumnIndex("行号"));
		detailTable.setColumnEditable(false,detailTable.getColumnModel().getColumnIndex("状态"));
		detailTable.setColumnEditable(false,detailTable.getColumnModel().getColumnIndex("商品编码"));
		detailTable.setColumnEditable(false,detailTable.getColumnModel().getColumnIndex("商品名称"));
		detailTable.setColumnEditable(false,detailTable.getColumnModel().getColumnIndex("单位"));
		detailTable.setColumnEditable(false,detailTable.getColumnModel().getColumnIndex("出库分拣数量"));
		detailTable.setColumnEditable(false,detailTable.getColumnModel().getColumnIndex("创建时间"));
		detailTable.setColumnEditable(false,detailTable.getColumnModel().getColumnIndex("创建用户"));
	}

	public static void makeFace(JTable table) {
        try {
            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() {

                public Component getTableCellRendererComponent(JTable table,
                        Object value, boolean isSelected, boolean hasFocus,
                        int row, int column) {
                    if (row % 2 == 0) {
                        setBackground(Color.white); //设置奇数行底色
                    } else if (row % 2 == 1) {
                        setBackground(new Color(206, 231, 255)); //设置偶数行底色
                    }
                    if (Double.parseDouble(table.getValueAt(row, 0).toString()) > 0) {
                        setBackground(Color.yellow);
                    } //如果需要设置某一个Cell颜色，需要加上column过滤条件即可
                    return super.getTableCellRendererComponent(table, value,
                            isSelected, hasFocus, row, column);
                }
            };
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumn(table.getColumnName(i)).setCellRenderer(tcr);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
	
	private void tblMain_tableChanged(TableModelEvent e)
	  {
	    int selectRow = e.getFirstRow();
	    int column = e.getColumn();
	    int type = e.getType();

	    if (!trigTable) return;
	    if (selectRow<0) return;
	    switch(type)
	    {
	      case TableModelEvent.DELETE:
	        break;
	      case TableModelEvent.INSERT:
	        break;
	      case TableModelEvent.UPDATE:
			trigTable = false;
			if (column == detailTable.getColumnModel().getColumnIndex("商品条码")) {
				String storer_code = txt_storer_code.getText();
				if(storer_code.equals("")){
					JOptionPane.showMessageDialog(null, "请输入货主编码");
					detailTable.setValueAt("",selectRow, column);
					txt_storer_code.setFocusable(true);
					txt_storer_code.requestFocus();
					trigTable = true;
					break;
				}
				if (detailTable.getValueAt(selectRow, column) != null
						&& !detailTable.getValueAt(selectRow, column).equals("")) {
					String item_bar_code = detailTable.getValueAt(selectRow, column).toString();
					Vector data = DBOperator.DoSelect("select bi.item_code,bi.item_name,biu.unit_name from bas_item bi left join bas_item_unit biu on bi.unit_code=biu.unit_code where bi.item_bar_code='"+item_bar_code+"' and bi.storer_code='"+storer_code+"'");
					if(data.size()>0){
						Object[] item_name = (Object[]) data.get(0);
						detailTable.setValueAt(item_name[0].toString(), selectRow, detailTable.getColumnModel().getColumnIndex("商品编码"));
						detailTable.setValueAt(item_name[1].toString(), selectRow, detailTable.getColumnModel().getColumnIndex("商品名称"));
						detailTable.setValueAt(item_name[2].toString(), selectRow, detailTable.getColumnModel().getColumnIndex("单位"));
						JTableUtil.fitTableColumns(detailTable);
					}else{
						detailTable.setValueAt("",selectRow, column);
						detailTable.setValueAt("", selectRow, 2);
						detailTable.setValueAt("", selectRow, 3);
						detailTable.setValueAt("", selectRow, 4);
					}
					
				}
			}
			trigTable = true;
			break;
	    }
	  }
	
	@SuppressWarnings("rawtypes")
	public boolean saveData(){
		try {
			String sql = "";
			//保存表头
			String shipment_no = "";
			String WAREHOUSE_CODE = txt_warehouse_code.getText().trim();
			String STORER_CODE = txt_storer_code.getText().trim();
			String ERP_ORDER_NO = txt_erp_order_no.getText().trim();
			String SHIP_TO_CONTACT = txt_SHIP_TO_CONTACT.getText().trim();
			String SHIP_TO_TEL = txt_SHIP_TO_TEL.getText().trim();
			String SHIP_TO_CONTACT_IDCARD = txt_SHIP_TO_CONTACT_IDCARD.getText().trim();
			String SHIP_TO_PROVINCE_CODE = cb_SHIP_TO_PROVINCE_CODE.getSelectedItem().toString();
			String SHIP_TO_CITY_CODE = cb_SHIP_TO_CITY_CODE.getSelectedItem().toString();
			String SHIP_TO_REGION_CODE = cb_SHIP_TO_REGION_CODE.getSelectedItem().toString();
			String SHIP_TO_STREET_CODE = cb_SHIP_TO_STREET_CODE.getSelectedItem().toString();
			String SHIP_TO_ADDRESS1 = txt_SHIP_TO_ADDRESS1.getText();
			String SHIP_TO_EMAIL = txt_SHIP_TO_EMAIL.getText();
			String REMARK = txt_remark.getText().trim();
			sql = "select shipment_no from oub_shipment_header where storer_code='"+STORER_CODE+"' and ERP_ORDER_NO='" + ERP_ORDER_NO + "'";
			DataManager dm = DBOperator.DoSelect2DM(sql);
			if (dm==null || dm.getCurrentCount()==0) {
				shipment_no = comData.getValueFromBasNumRule("oub_shipment_header", "shipment_no");
				//插入表头
				sql = "insert into oub_shipment_header(SHIPMENT_NO,WAREHOUSE_CODE,STORER_CODE,ERP_ORDER_NO,"
						+ "SHIP_TO_CONTACT,SHIP_TO_TEL,SHIP_TO_CONTACT_IDCARD,SHIP_TO_PROVINCE_CODE,SHIP_TO_CITY_CODE,"
						+ "SHIP_TO_REGION_CODE,SHIP_TO_STREET_CODE,SHIP_TO_ADDRESS1,SHIP_TO_EMAIL,REMARK,"
						+ "CREATED_DTM_LOC,CREATED_BY_USER,UPDATED_DTM_LOC,UPDATED_BY_USER)"
						+ " select '" + shipment_no + "','" + WAREHOUSE_CODE + "','" + STORER_CODE 
						+ "','" + ERP_ORDER_NO + "','"+SHIP_TO_CONTACT+"','"+SHIP_TO_TEL+"','"+SHIP_TO_CONTACT_IDCARD+"',"
						+ "'"+SHIP_TO_PROVINCE_CODE+"','"+SHIP_TO_CITY_CODE+"','"+REMARK+"','"+SHIP_TO_REGION_CODE+"','"+SHIP_TO_STREET_CODE+"',"
						+ "'"+SHIP_TO_ADDRESS1+"','"+SHIP_TO_EMAIL+"',"
						+ "now(),'"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"',now(),'"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"' ";
				System.out.println(sql);
				int t = DBOperator.DoUpdate(sql);
				if (t != 1) {
					JOptionPane.showMessageDialog(null, "插入订单表头报错\n" + sql, "错误",
							JOptionPane.ERROR_MESSAGE);
					LogInfo.appendLog("插入订单表头报错\n" + sql, "错误");
					return false;//保存表头失败，返回
				}
			}else{
				shipment_no = dm.getString("shipment_no", 0);
				sql = "update oub_shipment_header set SHIP_TO_CONTACT='"+SHIP_TO_CONTACT+"',SHIP_TO_TEL='"+SHIP_TO_TEL+"',"
					+ "SHIP_TO_CONTACT_IDCARD='"+SHIP_TO_CONTACT_IDCARD+"',SHIP_TO_PROVINCE_CODE='"+SHIP_TO_PROVINCE_CODE+"',"
					+ "SHIP_TO_CITY_CODE='"+SHIP_TO_CITY_CODE+"',SHIP_TO_REGION_CODE='"+SHIP_TO_REGION_CODE+"',"
					+ "SHIP_TO_STREET_CODE='"+SHIP_TO_STREET_CODE+"',SHIP_TO_ADDRESS1='"+SHIP_TO_ADDRESS1+"',"
					+ "SHIP_TO_EMAIL='"+SHIP_TO_EMAIL+"',REMARK='"+REMARK+"',UPDATED_DTM_LOC=now(),UPDATED_BY_USER='"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"' "
					+"where shipment_no='"+shipment_no+"' and WAREHOUSE_CODE='"+WAREHOUSE_CODE+"' ";
				int t = DBOperator.DoUpdate(sql);
				if(t==0){
					Message.showErrorMessage("订单更新失败");
					
				}
			}
			
			//保存明细
			if(detailTable.isEditing()){
				detailTable.getCellEditor().stopCellEditing();
			}
			Vector datailData = detailTable.getData();
			for (int i = 0; i < datailData.size(); i++) {
				Object[] row = (Object[]) datailData.get(i);
				String LINE_NUMBER = object2String(row[detailTable.getColumnModel().getColumnIndex("行号")]);
				String ITEM_BAR_CODE = object2String(row[detailTable.getColumnModel().getColumnIndex("商品条码")]);
				if(ITEM_BAR_CODE.equals("")){
					continue;
				}
				String ITEM_CODE = object2String(row[detailTable.getColumnModel().getColumnIndex("商品编码")]);
				String TOTAL_QTY = object2String(row[detailTable.getColumnModel().getColumnIndex("订单数量")]);
				String UOM = object2String(row[detailTable.getColumnModel().getColumnIndex("单位")]);
				String LOTTABLE01 = object2String(row[detailTable.getColumnModel().getColumnIndex("批次属性1")]);
				String LOTTABLE02 = object2String(row[detailTable.getColumnModel().getColumnIndex("批次属性2")]);
				String LOTTABLE03 = object2String(row[detailTable.getColumnModel().getColumnIndex("批次属性3")]);
				String LOTTABLE04 = object2String(row[detailTable.getColumnModel().getColumnIndex("批次属性4")]);
				String LOTTABLE05 = object2String(row[detailTable.getColumnModel().getColumnIndex("批次属性5")]);
				String LOTTABLE06 = object2String(row[detailTable.getColumnModel().getColumnIndex("批次属性6")]);
				String LOTTABLE07 = object2String(row[detailTable.getColumnModel().getColumnIndex("批次属性7")]);
				String LOTTABLE08 = object2String(row[detailTable.getColumnModel().getColumnIndex("批次属性8")]);
				String LOTTABLE09 = object2String(row[detailTable.getColumnModel().getColumnIndex("批次属性9")]);
				String LOTTABLE10 = object2String(row[detailTable.getColumnModel().getColumnIndex("批次属性10")]);
				sql = "select shipment_no from oub_shipment_detail where shipment_no='"+shipment_no+"' and SHIPMENT_LINE_NO = "+LINE_NUMBER+" ";
				DataManager dmtmp = DBOperator.DoSelect2DM(sql);
				if(dmtmp==null ||dmtmp.getCurrentCount()==0){
				}else{
					System.out.println("订单明细重复，忽略改行数据:"+shipment_no+" "+LINE_NUMBER);
					LogInfo.appendLog("订单明细重复，忽略改行数据:"+shipment_no+" "+LINE_NUMBER);
					continue;
				}
				//插入明细
				sql = "insert into oub_shipment_detail(OUB_SHIPMENT_HEADER_ID,SHIPMENT_LINE_NO,SHIPMENT_NO,ERP_ORDER_NO,warehouse_code,storer_code,item_code,REQ_QTY,REQ_UOM,"
						+ "LOTTABLE01,LOTTABLE02,LOTTABLE03,LOTTABLE04,LOTTABLE05,LOTTABLE06,LOTTABLE07,LOTTABLE08,LOTTABLE09,LOTTABLE10,"
						+ "created_dtm_loc,created_by_user,updated_dtm_loc,updated_by_user) "
						+ "select (select OUB_SHIPMENT_HEADER_ID from oub_shipment_header where  storer_code='"+STORER_CODE+"' and ERP_ORDER_NO='"
						+ ERP_ORDER_NO + "')," + "'" + LINE_NUMBER + "','" + shipment_no + "','" + ERP_ORDER_NO + "','"
						+ WAREHOUSE_CODE +"','"+STORER_CODE+"','"+ITEM_CODE+"','"+TOTAL_QTY+"','"+UOM+"'," 
						+ "'"+LOTTABLE01+"','"+LOTTABLE02+"','"+LOTTABLE03+"','"+LOTTABLE04+"','"+LOTTABLE05+"'," 
						+ "'"+LOTTABLE06+"','"+LOTTABLE07+"','"+LOTTABLE08+"','"+LOTTABLE09+"','"+LOTTABLE10+"'"  
						+ ",now(),'"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"',now(),'"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"'" ;
				System.out.println(sql);
				int t = DBOperator.DoUpdate(sql);
				if (t != 1) {
					JOptionPane.showMessageDialog(null, "插入订单表明细报错\n" + sql, "错误",
							JOptionPane.ERROR_MESSAGE);
					LogInfo.appendLog("插入订单表明细报错\n" + sql, "错误");
				}
			}
			//detailTable删除行 后台数据删除
			Iterator iter = delDetailRows.entrySet().iterator();
			while(iter.hasNext()){
				Map.Entry entry = (Map.Entry) iter.next();
				Object key = entry.getKey();
				Object value = entry.getValue();
				DBOperator.DoUpdate("delete from oub_shipment_detail where SHIPMENT_NO='"+key.toString()+"' and SHIPMENT_LINE_NO="+value.toString()+"");
				delDetailRows.remove(key);
			}
			
			if(shipment_no.length()>0){
				txt_shipment_no.setText(shipment_no);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogInfo.appendLog(e.getMessage());
		}
		return true;
	}
	
	private void headerTableClick(){
		if(btnSave.isEnabled()){
			
			return;
		}
		int r= headerTable.getSelectedRow();
        if(headerTable.getRowCount()>0){
        	new SwingWorker<String, Void>() {
				WaitingSplash splash = new WaitingSplash();
	            @Override
	            protected String doInBackground() throws Exception {
	            	//出现
	            	splash.start(); // 运行启动界面
	            	headerTable.setEnabled(false);
	    	        String str_shipment_no = headerTable.getValueAt(r, headerTable.getColumnModel().getColumnIndex("订单号")).toString();
	    	        String sql = "select osh.WAREHOUSE_CODE,bw.WAREHOUSE_NAME,osh.shipment_no,osh.ERP_ORDER_NO,osh.STORER_CODE,bs.STORER_NAME,osh.CREATED_DTM_LOC,osh.CREATED_BY_USER "
	    	        		+",case osh.status when '100' then '新建' when '150' then '产生波次号' when '190' then '库存分配短少' when '200' then '库存分配完成' when '300' then '拣货中' when '400' then '分拣中' when '500' then '包装中' when '600' then '包装完成' when '700' then '出库复核中' when '800' then '出库复核完成' when '900' then '已出库交接' else osh.status end status"
	    	        		+ ",osh.SHIP_TO_CONTACT,osh.SHIP_TO_TEL,osh.SHIP_TO_CONTACT_IDCARD,osh.SHIP_TO_PROVINCE_CODE,osh.SHIP_TO_CITY_CODE,"
	    	        		+ "SHIP_TO_REGION_CODE,SHIP_TO_STREET_CODE,SHIP_TO_ADDRESS1,SHIP_TO_EMAIL,osh.REMARK "
	    					+ " from oub_shipment_header osh  " 
	    	        		+ " inner join bas_warehouse bw on osh.WAREHOUSE_CODE=bw.WAREHOUSE_CODE"
	    					+ " inner join bas_storer bs on osh.STORER_CODE=bs.STORER_CODE"
	    					+ " where osh.shipment_no='"+str_shipment_no+"' " + "";
	    	        DataManager dm = DBOperator.DoSelect2DM(sql);
	    	        if(dm!=null && dm.getCurrentCount()>0){
	    	        	txt_warehouse_code.setText(object2String(dm.getString("WAREHOUSE_CODE", 0)));
	    	        	txt_warehouse_name.setText(object2String(dm.getString("WAREHOUSE_NAME", 0)));
	    	        	txt_shipment_no.setText(object2String(dm.getString("shipment_no", 0)));
	    	        	txt_erp_order_no.setText(object2String(dm.getString("ERP_ORDER_NO", 0)));
	    	        	txt_storer_code.setText(object2String(dm.getString("STORER_CODE", 0)));
	    	        	txt_storer_name.setText(object2String(dm.getString("STORER_NAME", 0)));
	    	        	txt_status.setText(object2String(dm.getString("status", 0)));
	    	        	txt_SHIP_TO_CONTACT.setText(object2String(dm.getString("SHIP_TO_CONTACT", 0)));
	    				txt_SHIP_TO_TEL.setText(object2String(dm.getString("SHIP_TO_TEL", 0)));
	    				txt_SHIP_TO_CONTACT_IDCARD.setText(object2String(dm.getString("SHIP_TO_CONTACT_IDCARD", 0)));
	    				cb_SHIP_TO_PROVINCE_CODE.setSelectedItem(object2String(dm.getString("SHIP_TO_PROVINCE_CODE", 0)));
	    				cb_SHIP_TO_CITY_CODE.setSelectedItem(object2String(dm.getString("SHIP_TO_CITY_CODE", 0)));
	    				cb_SHIP_TO_REGION_CODE.setSelectedItem(object2String(dm.getString("SHIP_TO_REGION_CODE", 0)));
	    				cb_SHIP_TO_STREET_CODE.setSelectedItem(object2String(dm.getString("SHIP_TO_STREET_CODE", 0)));
	    				txt_SHIP_TO_ADDRESS1.setText(object2String(dm.getString("SHIP_TO_ADDRESS1", 0)));
	    				txt_SHIP_TO_EMAIL.setText(object2String(dm.getString("SHIP_TO_EMAIL", 0)));
	    				txt_remark.setText(object2String(dm.getString("REMARK", 0)));
	    	        	getDetailTableData(" and osd.shipment_no='"+str_shipment_no+"'");
	    	        }
	    	        headerTable.setEnabled(true);
	                return "";
	            }

	            @Override
	            protected void done() {
	            	splash.stop(); // 运行启动界面
					headerTable.setEnabled(true);
	            }
	        }.execute();
        	
        }
        detailTable.setColumnEditableAll(false);
	}
	
	private void getHeaderTableData(String strWhere){
//		DefaultTableModel dtm = (DefaultTableModel) headerTable.getModel();
//        dtm.getDataVector().removeAllElements();
//        dtm.setRowCount(0);
//		String sql = "select iph.WAREHOUSE_CODE,bw.WAREHOUSE_NAME,iph.PO_NO,iph.ERP_PO_NO,iph.STORER_CODE,bs.STORER_NAME,iph.VENDOR_CODE,bv.VENDOR_NAME,iph.CREATED_DTM_LOC,iph.CREATED_BY_USER "
				String sql = "select osh.SHIPMENT_NO 订单号"
				+ " from oub_shipment_header osh  " 
				+ " inner join bas_warehouse bw on osh.WAREHOUSE_CODE=bw.WAREHOUSE_CODE"
				+ " inner join bas_storer bs on osh.STORER_CODE=bs.STORER_CODE"
				+ " where 1=1 " 
				+ "and osh.warehouse_code='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' ";
		if(!strWhere.equals("")){
			sql = sql + strWhere;
		}
		DataManager dm = DBOperator.DoSelect2DM(sql);
		if (headerTable.getColumnCount() == 0) {
			headerTable.setColumn(dm.getCols());
		}
		headerTable.removeRowAll();
		headerTable.setData(dm.getDataStrings());
		headerTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		headerTable.setColumnEditableAll(false);
		JTableUtil.fitTableColumnsDoubleWidth(headerTable);
		headerTable.setSortEnable();
		headerTable.updateUI();
		
		if(headerTable.getRowCount()>0){
			headerTable.setRowSelectionInterval(0, 0);//默认选中第一行
		}
		headerTableClick();
	}
	
	private void getDetailTableData(String strWhere){
		DefaultTableModel dtm = (DefaultTableModel) detailTable.getModel();
        dtm.getDataVector().removeAllElements();
		dtm.setRowCount(0);
		String sql = "select osd.SHIPMENT_NO,osd.SHIPMENT_LINE_NO,bi.ITEM_BAR_CODE,osd.ITEM_CODE,bi.ITEM_NAME,biu.UNIT_NAME,osd.REQ_QTY,osd.OQC_QTY,"
				+ "case osd.status when '100' then '新建' when '150' then '产生波次号' when '190' then '库存分配短少' when '200' then '库存分配完成' when '300' then '拣货中' when '400' then '分拣中' when '500' then '包装中' when '600' then '包装完成' when '700' then '出库复核中' when '800' then '出库复核完成' when '900' then '已出库交接' else osd.status end status,"
				+ "osd.LOTTABLE01,osd.LOTTABLE02,osd.LOTTABLE03,osd.LOTTABLE04,"
				+ "osd.LOTTABLE05,osd.LOTTABLE06,osd.LOTTABLE07,osd.LOTTABLE08,osd.LOTTABLE09,osd.LOTTABLE10,osd.CREATED_DTM_LOC,osd.CREATED_BY_USER,"
				+ "osd.UPDATED_DTM_LOC,osd.UPDATED_BY_USER "
				+ " from oub_shipment_detail osd " 
				+ "left join bas_item bi on osd.storer_code=bi.storer_code and osd.ITEM_CODE=bi.ITEM_CODE" 
				+" left join bas_item_unit biu on bi.unit_code=biu.unit_code "
				+ " where 1=1 ";
		if(!strWhere.equals("")){
			sql = sql + strWhere;
		}
		sql = sql + " order by 1,2 ";
		System.out.println(sql);
		try{
			java.sql.Connection con = DBConnectionManager.getInstance().getConnection("wms");
			java.sql.Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Vector<String> rowdata = new Vector<String>();
				rowdata.add(rs.getString("SHIPMENT_LINE_NO"));
				rowdata.add(rs.getString("status"));
				rowdata.add(rs.getString("ITEM_BAR_CODE"));
				rowdata.add(rs.getString("ITEM_CODE"));
				rowdata.add(rs.getString("ITEM_NAME"));
				rowdata.add(rs.getString("UNIT_NAME"));
				rowdata.add(rs.getString("REQ_QTY"));
				rowdata.add(rs.getString("OQC_QTY"));
				rowdata.add(rs.getString("LOTTABLE01"));
				rowdata.add(rs.getString("LOTTABLE02"));
				rowdata.add(rs.getString("LOTTABLE03"));
				rowdata.add(rs.getString("LOTTABLE04"));
				rowdata.add(rs.getString("LOTTABLE05"));
				rowdata.add(rs.getString("LOTTABLE06"));
				rowdata.add(rs.getString("LOTTABLE07"));
				rowdata.add(rs.getString("LOTTABLE08"));
				rowdata.add(rs.getString("LOTTABLE09"));
				rowdata.add(rs.getString("LOTTABLE10"));
				rowdata.add(rs.getString("CREATED_DTM_LOC"));
				rowdata.add(rs.getString("CREATED_BY_USER"));
				rowdata.add(rs.getString("UPDATED_DTM_LOC"));
				rowdata.add(rs.getString("UPDATED_BY_USER"));
				dtm.addRow(rowdata);
			}
			JTableUtil.fitTableColumns(detailTable);
//			detailTable.setSortEnable();//列头点击排序
			detailTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			DBConnectionManager.getInstance().freeConnection("wms", con);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
	
	private String checkShipmentNoStatus(String nostr){
		String sql = "select shipment_no,status from oub_shipment_header where shipment_no='"+nostr+"' ";
		DataManager dm = DBOperator.DoSelect2DM(sql);
		if(dm==null || dm.getCurrentCount()==0){
			return "";
		}else{
			return dm.getString("status", 0);
		}
	}
	
	public String object2String(Object o){
		return o==null?"":o.toString();
	}
	
	public void clearFrom(){
		detailTable.removeRowAll();
		txt_shipment_no.setText("");
		txt_erp_order_no.setText("");
		txt_storer_code.setText("");
		txt_storer_name.setText("");
		txt_warehouse_code.setText("");
		txt_warehouse_name.setText("");
	}
	
	public void clearDetailTable(){
		int row = detailTable.getRowCount();
		for(int i=0;i<row;i++){
			String itemBarCode = detailTable.getValueAt(i, 1)==null?"":detailTable.getValueAt(i, 1).toString();
			if(itemBarCode.equals("")){
				detailTable.removeRow(i);
				i = i - 1;
				row = detailTable.getRowCount();
			}
		}
	}

	private void setExtendedState(int maximizedBoth) {
		// TODO Auto-generated method stub
		
	}

}
