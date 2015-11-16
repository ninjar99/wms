package inbound;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import bas.storerMasterFrm;
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

import javax.swing.JButton;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.DefaultListSelectionModel;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import DBUtil.DBConnectionManager;
import DBUtil.DBOperator;
import DBUtil.LogInfo;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class POFrm extends InnerFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1281449283666709019L;
	private JPanel contentPane;
	private static volatile POFrm instance;
	private static boolean isOpen = false;
	private boolean trigTable = true;
	private PBSUIBaseGrid headerTable;
	private PBSUIBaseGrid detailTable;
	private JTextField txt_po_no;
	private JTextField txt_storer_code;
	private JTextField txt_storer_name;
	private JTextField txt_warehouse_code;
	private JTextField txt_vendor_code;
	private JTextField txt_vendor_name;
	private JTextField txt_erp_po_no;
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
	private JButton btnVendorQuery;
	private JLabel label;
	private JTextField txt_status;
	
	public static POFrm getInstance() {
		if(instance == null) { 
			 synchronized(POFrm.class){
				 if(instance == null) {
					 instance = new POFrm();
				 }
			 }
	        }  
	        return instance;
		 
	 }
	
	public static synchronized boolean getOpenStatus() {
		 if(instance == null) {    
	            instance = new POFrm();  
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
					POFrm frame = new POFrm();
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
	public POFrm() {
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		BasicInternalFrameUI ui = (BasicInternalFrameUI)this.getUI(); //去除标题栏
//		ui.getNorthPane().setVisible(false);
		// 向侦听器列表添加一个 VetoableChangeListener。为所有属性注册该侦听器
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.addVetoableChangeListener(new VetoableChangeListener() {

			public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException {
				@SuppressWarnings("unused")
				JInternalFrame frame = (JInternalFrame) e.getSource();
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
				txt_erp_po_no.setEditable(true);
				txt_storer_code.setEditable(true);
				txt_warehouse_code.setEditable(true);
				txt_vendor_code.setEditable(true);
				btnAdd.setEnabled(false);
				btnModify.setEnabled(false);
				btnDelete.setEnabled(false);
				btnQuery.setEnabled(false);
				btnSave.setEnabled(true);
				btnCancel.setEnabled(true);
				btnClose.setEnabled(false);
				
				clearFrom();
				
				//增加一行空行
				Object [] addRowValues = new Object[19];
				addRowValues[0] = detailTable.getRowCount()+1;
				detailTable.addRow(addRowValues);
				detailTable.setComponent(new JTNumEdit(15, "#####",true), 5);
//				tblMain.setComponent((new JTNumEdit(15, "#####",true)), 0);
				
				detailTable.setColumnEditableAll(true);
				detailTable.setColumnEditable(false,0);
				detailTable.setColumnEditable(false,2);
				detailTable.setColumnEditable(false,3);
				detailTable.setColumnEditable(false,4);
				detailTable.setColumnEditable(false,6);
				detailTable.setColumnEditable(false,17);
				detailTable.setColumnEditable(false,18);
				
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
				detailTable.getColumn("数量").setCellRenderer(new MyTableCellRenderrer());
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
				if(txt_po_no.getText().trim().equals("")){
					JOptionPane.showMessageDialog(null, "请选择一行数据！");
					return;
				}
				String poStatus = checkPOStatus(txt_po_no.getText().trim());
				if(poStatus.equals("")){
					Message.showErrorMessage("PO号不存在");
					return;
				}else if(!poStatus.equals("100")){
					Message.showWarningMessage("PO已经开始收货，不能进行修改");
					return;
				}
				if(detailTable.getRowCount()==0){
					//增加一行空行
					Object [] addRowValues = new Object[19];
					addRowValues[0] = detailTable.getRowCount()+1;
					detailTable.addRow(addRowValues);
					detailTable.editCellAt(detailTable.getRowCount() - 1, 1);
				}
				
				txt_erp_po_no.setEditable(false);
				txt_storer_code.setEditable(true);
				txt_warehouse_code.setEditable(true);
				txt_vendor_code.setEditable(true);
				btnAdd.setEnabled(false);
				btnModify.setEnabled(false);
				btnDelete.setEnabled(false);
				btnQuery.setEnabled(false);
				btnSave.setEnabled(true);
				btnCancel.setEnabled(true);
				btnClose.setEnabled(false);
				headerTable.setEnabled(false);
				detailTable.setColumnEditableAll(true);
				detailTable.setColumnEditable(false,0);
				detailTable.setColumnEditable(false,2);
				detailTable.setColumnEditable(false,3);
				detailTable.setColumnEditable(false,4);
				detailTable.setColumnEditable(false,6);
				detailTable.setColumnEditable(false,17);
				detailTable.setColumnEditable(false,18);
			}
		});
		topPanel.add(btnModify);
		
		btnDelete = new JButton("\u5220\u9664");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(txt_po_no.getText().trim().equals("")){
					JOptionPane.showMessageDialog(null, "请选择一行数据！");
					return;
				}
				String poStatus = checkPOStatus(txt_po_no.getText().trim());
				if(poStatus.equals("")){
					Message.showErrorMessage("PO号不存在");
					return;
				}else if(!poStatus.equals("100")){
					Message.showWarningMessage("PO已经开始收货，不能删除");
					return;
				}
				int t = JOptionPane.showConfirmDialog(null, "是否删除该PO【"+txt_po_no.getText().trim()+"]？");
				if(t==0){
					//删除PO
					String sql = "delete from inb_po_header where po_no='"+txt_po_no.getText().trim()+"' and status='100' ";
					int delrow = DBOperator.DoUpdate(sql);
					if(delrow==1){
						sql = "delete from inb_po_detail where po_no='"+txt_po_no.getText().trim()+"' ";
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
				fieldList.add("iph.WAREHOUSE_CODE:仓库编码");
				fieldList.add("bw.WAREHOUSE_NAME:仓库名称");
				fieldList.add("iph.PO_NO:PO号");
				fieldList.add("iph.ERP_PO_NO:ERP_PO号");
				fieldList.add("iph.STORER_CODE:货主编码");
				fieldList.add("bs.STORER_NAME:货主名称");
				fieldList.add("iph.VENDOR_CODE:供应商编码");
				fieldList.add("bv.VENDOR_NAME:供应商名称");
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
				if(txt_erp_po_no.getText().equals("") || txt_storer_code.getText().equals("")){
					JOptionPane.showMessageDialog(null, "PO表头数据不完整，不能保存","提示",JOptionPane.WARNING_MESSAGE);
					return;
				}
				if(detailTable.getValueAt(0, 1)==null || detailTable.getValueAt(0, 1).toString().equals("")){
					int t = JOptionPane.showConfirmDialog(null, "明细数据不完整，是否取消？\n如果不取消，只保存PO表头数据");
					if(t==0 || t==2){
						txt_erp_po_no.setText("");
						txt_storer_code.setText("");
						txt_storer_name.setText("");
						txt_warehouse_code.setText("");
						txt_warehouse_name.setText("");
						txt_vendor_code.setText("");
						txt_vendor_code.setText("");
						txt_erp_po_no.setEditable(false);
						txt_storer_code.setEditable(false);
						txt_warehouse_code.setEditable(false);
						txt_vendor_code.setEditable(false);
						btnAdd.setEnabled(true);
						btnModify.setEnabled(true);
						btnDelete.setEnabled(true);
						btnQuery.setEnabled(true);
						btnSave.setEnabled(false);
						btnCancel.setEnabled(false);
						btnClose.setEnabled(true);
						headerTable.setEnabled(true);
						detailTable.setColumnEditableAll(false);
						clearDetailTable();
						return;
					}
				}
				txt_erp_po_no.setEditable(false);
				txt_storer_code.setEditable(false);
				txt_warehouse_code.setEditable(false);
				txt_vendor_code.setEditable(false);
				btnAdd.setEnabled(true);
				btnModify.setEnabled(true);
				btnDelete.setEnabled(true);
				btnQuery.setEnabled(true);
				btnSave.setEnabled(false);
				btnCancel.setEnabled(false);
				btnClose.setEnabled(true);
				headerTable.setEnabled(true);
				saveData();
				getHeaderTableData(" and iph.po_no='"+txt_po_no.getText().trim()+"'");
			}
		});
		btnSave.setEnabled(false);
		topPanel.add(btnSave);
		
		btnCancel = new JButton("\u53D6\u6D88");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txt_erp_po_no.setText("");
				txt_storer_code.setText("");
				txt_storer_name.setText("");
				txt_warehouse_code.setText("");
				txt_warehouse_name.setText("");
				txt_vendor_code.setText("");
				txt_vendor_code.setText("");
				txt_erp_po_no.setEditable(false);
				txt_storer_code.setEditable(false);
				txt_warehouse_code.setEditable(false);
				txt_vendor_code.setEditable(false);
				btnAdd.setEnabled(true);
				btnModify.setEnabled(true);
				btnDelete.setEnabled(true);
				btnQuery.setEnabled(true);
				btnSave.setEnabled(false);
				btnCancel.setEnabled(false);
				btnClose.setEnabled(true);
				headerTable.setEnabled(true);
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
		
		JPanel editPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) editPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		rightPanel.add(editPanel, BorderLayout.NORTH);
		editPanel.setPreferredSize(new Dimension(100, 80));
		
		JLabel lblNewLabel = new JLabel("PO:");
		editPanel.add(lblNewLabel);
		txt_po_no = new JTextField();
		txt_po_no.setEditable(false);
		editPanel.add(txt_po_no);
		txt_po_no.setColumns(8);
		
		label = new JLabel("\u72B6\u6001:");
		editPanel.add(label);
		
		txt_status = new JTextField();
		txt_status.setEditable(false);
		editPanel.add(txt_status);
		txt_status.setColumns(4);
		
		JLabel lblNewLabel_6 = new JLabel("ERP_PO(\u63D0\u5355\u53F7)\uFF1A");
		editPanel.add(lblNewLabel_6);
		
		txt_erp_po_no = new JTextField();
		txt_erp_po_no.setEditable(false);
		editPanel.add(txt_erp_po_no);
		txt_erp_po_no.setColumns(8);
		
		JLabel lblNewLabel_1 = new JLabel("\u8D27\u4E3B\u7F16\u7801\uFF1A");
		editPanel.add(lblNewLabel_1);
		
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
						Vector poheader = DBOperator.DoSelect("select po_no from inb_po_header where storer_code='"+txt_storer_code.getText().trim()+"' and erp_po_no='"+txt_erp_po_no.getText().trim()+"'");
						if(poheader.size()>0){
							JOptionPane.showMessageDialog(null, "该货主对应的ERP_PO_NO已经存在，请重新输入ERP_PO_NO","提示",JOptionPane.WARNING_MESSAGE);
							txt_erp_po_no.setFocusable(true);
							txt_erp_po_no.requestFocus();
							txt_erp_po_no.selectAll();
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
		editPanel.add(txt_storer_code);
		txt_storer_code.setColumns(8);
		
		btnStorerQuery = new JButton("<");
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
		editPanel.add(btnStorerQuery);
		
		JLabel lblNewLabel_2 = new JLabel("\u8D27\u4E3B\u540D\u79F0\uFF1A");
		editPanel.add(lblNewLabel_2);
		
		txt_storer_name = new JTextField();
		txt_storer_name.setEditable(false);
		editPanel.add(txt_storer_name);
		txt_storer_name.setColumns(20);
		
		JLabel lblNewLabel_3 = new JLabel("\u4ED3\u5E93\u7F16\u7801\uFF1A");
		editPanel.add(lblNewLabel_3);
		
		txt_warehouse_code = new JTextField();
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
		editPanel.add(txt_warehouse_code);
		txt_warehouse_code.setColumns(8);
		
		btnWarehouseQuery = new JButton("<");
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
		editPanel.add(btnWarehouseQuery);
		
		JLabel lblNewLabel_7 = new JLabel("\u4ED3\u5E93\u540D\u79F0\uFF1A");
		editPanel.add(lblNewLabel_7);
		
		txt_warehouse_name = new JTextField();
		txt_warehouse_name.setEditable(false);
		editPanel.add(txt_warehouse_name);
		txt_warehouse_name.setColumns(15);
		
		JLabel lblNewLabel_4 = new JLabel("\u4F9B\u5E94\u5546\u7F16\u7801\uFF1A");
		editPanel.add(lblNewLabel_4);
		
		txt_vendor_code = new JTextField();
		txt_vendor_code.setEditable(false);
		txt_vendor_code.addFocusListener(new FocusListener(){
			public void focusLost(FocusEvent e) {
				if(!txt_vendor_code.isEditable()) return;
				if(txt_vendor_code.getText().trim().equals("") && txt_warehouse_code.isEditable()){
//					JOptionPane.showMessageDialog(null, "请输入供应商编码");
//					txt_vendor_code.setFocusable(true);
//					txt_vendor_code.requestFocus();
				}else{
					Vector data = DBOperator.DoSelect("select vendor_name from bas_vendor where vendor_code='"+txt_vendor_code.getText().trim()+"'");
					if(data.size()>0){
						Object[] row = (Object[]) data.get(0);
						txt_vendor_name.setText(row[0].toString());
					}else{
						JOptionPane.showMessageDialog(null, "请输入正确供应商编码","提示",JOptionPane.WARNING_MESSAGE);
						txt_vendor_code.setFocusable(true);
						txt_vendor_code.requestFocus();
					}
				}
				
			}

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		editPanel.add(txt_vendor_code);
		txt_vendor_code.setColumns(8);
		
		btnVendorQuery = new JButton("<");
		btnVendorQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = "select distinct vendor_code 供应商编码,vendor_name 供应商名称  from bas_vendor ";
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
				Object obj = dm.getObject("供应商编码", 0);
				if(obj==null || obj.equals("")){
					return;
				}else{
					txt_vendor_code.setText((String) dm.getObject("供应商编码", 0));
					txt_vendor_code.requestFocus();
					txt_vendor_code.selectAll();
				}
			}
		});
		editPanel.add(btnVendorQuery);
		
		JLabel lblNewLabel_5 = new JLabel("\u4F9B\u5E94\u5546\u540D\u79F0\uFF1A");
		editPanel.add(lblNewLabel_5);
		
		txt_vendor_name = new JTextField();
		txt_vendor_name.setEditable(false);
		editPanel.add(txt_vendor_name);
		txt_vendor_name.setColumns(20);
		
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
				if (e.getButton() == MouseEvent.BUTTON3 && btnSave.isEnabled()) {
					JPopupMenu popupmenu = new JPopupMenu();
					JMenuItem menuItem1 = new JMenuItem();
					menuItem1.setLabel("添加行");
					menuItem1.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(ActionEvent e) {
							// 增加一行空行
							Object[] addRowValues = new Object[19];
							addRowValues[0] = detailTable.getRowCount() + 1;
							detailTable.addRow(addRowValues);
							detailTable.editCellAt(detailTable.getRowCount() - 1, 1);
						}
					});
					popupmenu.add(menuItem1);
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
						delDetailRows.put(txt_po_no.getText(), detailTable.getValueAt(selrow, 0).toString());
						detailTable.removeRow(selrow);
					}
				}
				if (e.getKeyChar() == '\n') {
					if(detailTable.getSelectedColumn()==17){
						//增加一行空行
						Object [] addRowValues = new Object[19];
						addRowValues[0] = detailTable.getRowCount()+1;
						detailTable.addRow(addRowValues);
						detailTable.editCellAt(detailTable.getRowCount() - 1, 1);
					}else{
						detailTable.editCellAt(detailTable.getSelectedRow(), detailTable.getSelectedColumn());
					}
				}
			}
		});
		//detailTable
		String[] SOColumnNames = {"PO行号","商品条码","商品编码","商品名称","单位","数量","已收数量","批次属性1","批次属性2","批次属性3","批次属性4","批次属性5","批次属性6"
				,"批次属性7","批次属性8","批次属性9","批次属性10","创建时间","创建用户"};
		detailTable.setColumn(SOColumnNames);
		detailTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//        DefaultTableModel dtm2 = (DefaultTableModel) detailTable.getModel();
//        dtm2.addColumn("PO行号");
//        dtm2.addColumn("商品条码");
//        dtm2.addColumn("商品名称");
//        dtm2.addColumn("数量");
//        dtm2.addColumn("已收数量");
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
			if (column == 1) {
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
					Vector data = DBOperator.DoSelect("select bi.item_code,bi.item_name,biu.unit_name from bas_item bi inner join bas_item_unit biu on bi.unit_code=biu.unit_code where bi.item_bar_code='"+item_bar_code+"' and bi.storer_code='"+storer_code+"'");
					if(data.size()>0){
						Object[] item_name = (Object[]) data.get(0);
						detailTable.setValueAt(item_name[0].toString(), selectRow, 2);
						detailTable.setValueAt(item_name[1].toString(), selectRow, 3);
						detailTable.setValueAt(item_name[2].toString(), selectRow, 4);
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
			java.sql.Connection con = DBConnectionManager.getInstance().getConnection("wms");
			java.sql.Statement stmt = con.createStatement();
			ResultSet rs;
			//保存表头
			String PO_NO = "";
			String WAREHOUSE_CODE = txt_warehouse_code.getText().trim();
			String STORER_CODE = txt_storer_code.getText().trim();
			String VENDOR_CODE = txt_vendor_code.getText().trim();
			String ERP_PO_NO = txt_erp_po_no.getText().trim();
			sql = "select po_no from inb_po_header where storer_code='"+STORER_CODE+"' and erp_po_no='" + ERP_PO_NO + "'";
			rs = stmt.executeQuery(sql);
			if (!rs.next()) {
				PO_NO = comData.getValueFromBasNumRule("inb_po_header", "po_no");
				//插入表头
				sql = "insert into inb_po_header(po_no,warehouse_code,storer_code,vendor_code,erp_po_no,created_dtm_loc,created_by_user,updated_dtm_loc,updated_by_user)"
						+ " select '" + PO_NO + "','" + WAREHOUSE_CODE + "','" + STORER_CODE + "','" + VENDOR_CODE
						+ "','" + ERP_PO_NO + "',now(),'sys',now(),'sys' ";
				System.out.println(sql);
				int t = stmt.executeUpdate(sql);
				if (t != 1) {
					JOptionPane.showMessageDialog(null, "插入PO表头报错\n" + sql, "错误",
							JOptionPane.ERROR_MESSAGE);
					LogInfo.appendLog("插入PO表头报错\n" + sql, "错误");
					return false;//保存表头失败，返回
				}
			}else{
				PO_NO = rs.getString("po_no");
			}
			
			//保存明细
			if(detailTable.isEditing()){
				detailTable.getCellEditor().stopCellEditing();
			}
			Vector datailData = detailTable.getData();
			for (int i = 0; i < datailData.size(); i++) {
				Object[] row = (Object[]) datailData.get(i);
				String LINE_NUMBER = object2String(row[0]);
				String ITEM_BAR_CODE = object2String(row[1]);
				if(ITEM_BAR_CODE.equals("")){
					continue;
				}
				String ITEM_CODE = object2String(row[2]);
				String TOTAL_QTY = object2String(row[5]);
				String UOM = object2String(row[4]);
				String LOTTABLE01 = object2String(row[7]);
				String LOTTABLE02 = object2String(row[8]);
				String LOTTABLE03 = object2String(row[9]);
				String LOTTABLE04 = object2String(row[10]);
				String LOTTABLE05 = object2String(row[11]);
				String LOTTABLE06 = object2String(row[12]);
				String LOTTABLE07 = object2String(row[13]);
				String LOTTABLE08 = object2String(row[14]);
				String LOTTABLE09 = object2String(row[15]);
				String LOTTABLE10 = object2String(row[16]);
				sql = "select po_no from inb_po_detail where po_no='"+PO_NO+"' and line_number = "+LINE_NUMBER+" ";
				java.sql.Statement stmt2 = con.createStatement();
				ResultSet rs2 = stmt2.executeQuery(sql);
				if(rs2.next()){
					System.out.println("PO明细重复，忽略改行数据:"+PO_NO+" "+LINE_NUMBER);
					continue;
				}
				//插入明细
				sql = "insert into inb_po_detail(inb_po_header_id,line_number,po_no,erp_po_no,warehouse_code,storer_code,item_code,total_qty,uom,"
						+ "LOTTABLE01,LOTTABLE02,LOTTABLE03,LOTTABLE04,LOTTABLE05,LOTTABLE06,LOTTABLE07,LOTTABLE08,LOTTABLE09,LOTTABLE10,"
						+ "created_dtm_loc,created_by_user,updated_dtm_loc,updated_by_user) "
						+ "select (select inb_po_header_id from inb_po_header where  storer_code='"+STORER_CODE+"' and ERP_PO_NO='"
						+ ERP_PO_NO + "')," + "'" + LINE_NUMBER + "','" + PO_NO + "','" + ERP_PO_NO + "','"
						+ WAREHOUSE_CODE +"','"+STORER_CODE+"','"+ITEM_CODE+"','"+TOTAL_QTY+"','"+UOM+"'," 
						+ "'"+LOTTABLE01+"','"+LOTTABLE02+"','"+LOTTABLE03+"','"+LOTTABLE04+"','"+LOTTABLE05+"'," 
						+ "'"+LOTTABLE06+"','"+LOTTABLE07+"','"+LOTTABLE08+"','"+LOTTABLE09+"','"+LOTTABLE10+"'"  
						+ ",now(),'sys',now(),'sys'" ;
				System.out.println(sql);
				int t = stmt.executeUpdate(sql);
				if (t != 1) {
					JOptionPane.showMessageDialog(null, "插入PO表明细报错\n" + sql, "错误",
							JOptionPane.ERROR_MESSAGE);
					LogInfo.appendLog("插入PO表明细报错\n" + sql, "错误");
				}
			}
			//detailTable删除行 后台数据删除
			Iterator iter = delDetailRows.entrySet().iterator();
			while(iter.hasNext()){
				Map.Entry entry = (Map.Entry) iter.next();
				Object key = entry.getKey();
				Object value = entry.getValue();
				DBOperator.DoUpdate("delete from inb_po_detail where po_no='"+key.toString()+"' and line_number="+value.toString()+"");
				delDetailRows.remove(key);
			}
			
			if(PO_NO.length()>0){
				txt_po_no.setText(PO_NO);
			}
			DBConnectionManager.getInstance().freeConnection("wms", con);
		} catch (Exception e) {
			e.printStackTrace();
			LogInfo.appendLog(e.getMessage());
		}
		return true;
	}
	
	private void headerTableClick(){
		int r= headerTable.getSelectedRow();
        if(headerTable.getRowCount()>0){
	        String str_po_no = headerTable.getValueAt(r, headerTable.getColumnModel().getColumnIndex("PO号")).toString();
	        String sql = "select iph.WAREHOUSE_CODE,bw.WAREHOUSE_NAME,iph.PO_NO,iph.ERP_PO_NO,iph.STORER_CODE,bs.STORER_NAME,iph.VENDOR_CODE,bv.VENDOR_NAME,iph.CREATED_DTM_LOC,iph.CREATED_BY_USER "
	        		+",case iph.status when '100' then '新建' when '300' then '收货中' when '900' then '关闭' else iph.status end status "
					+ " from inb_po_header iph  " + " inner join bas_warehouse bw on iph.WAREHOUSE_CODE=bw.WAREHOUSE_CODE"
					+ " inner join bas_storer bs on iph.STORER_CODE=bs.STORER_CODE"
					+ " inner join bas_vendor bv on bv.VENDOR_CODE=iph.VENDOR_CODE" + " where iph.po_no='"+str_po_no+"' " + "";
	        DataManager dm = DBOperator.DoSelect2DM(sql);
	        if(dm!=null && dm.getCurrentCount()>0){
	        	txt_warehouse_code.setText(object2String(dm.getString("WAREHOUSE_CODE", 0)));
	        	txt_warehouse_name.setText(object2String(dm.getString("WAREHOUSE_NAME", 0)));
	        	txt_po_no.setText(object2String(dm.getString("PO_NO", 0)));
	        	txt_erp_po_no.setText(object2String(dm.getString("ERP_PO_NO", 0)));
	        	txt_storer_code.setText(object2String(dm.getString("STORER_CODE", 0)));
	        	txt_storer_name.setText(object2String(dm.getString("STORER_NAME", 0)));
	        	txt_vendor_code.setText(object2String(dm.getString("VENDOR_CODE", 0)));
	        	txt_vendor_name.setText(object2String(dm.getString("VENDOR_NAME", 0)));
	        	txt_status.setText(object2String(dm.getString("status", 0)));
	        	getDetailTableData(" and ipd.po_no='"+str_po_no+"'");
	        }
        }
        detailTable.setColumnEditableAll(false);
	}
	
	private void getHeaderTableData(String strWhere){
//		DefaultTableModel dtm = (DefaultTableModel) headerTable.getModel();
//        dtm.getDataVector().removeAllElements();
//        dtm.setRowCount(0);
//		String sql = "select iph.WAREHOUSE_CODE,bw.WAREHOUSE_NAME,iph.PO_NO,iph.ERP_PO_NO,iph.STORER_CODE,bs.STORER_NAME,iph.VENDOR_CODE,bv.VENDOR_NAME,iph.CREATED_DTM_LOC,iph.CREATED_BY_USER "
				String sql = "select iph.PO_NO PO号"
				+ " from inb_po_header iph  " + " inner join bas_warehouse bw on iph.WAREHOUSE_CODE=bw.WAREHOUSE_CODE"
				+ " inner join bas_storer bs on iph.STORER_CODE=bs.STORER_CODE"
				+ " inner join bas_vendor bv on bv.VENDOR_CODE=iph.VENDOR_CODE" + " where 1=1 " 
				+ "and iph.warehouse_code='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' ";
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
		String sql = "select ipd.LINE_NUMBER,bi.ITEM_BAR_CODE,ipd.ITEM_CODE,bi.ITEM_NAME,biu.UNIT_NAME,ipd.TOTAL_QTY,ipd.RECEIVED_QTY,"
				+ "ipd.LOTTABLE01,ipd.LOTTABLE02,ipd.LOTTABLE03,ipd.LOTTABLE04,"
				+ "ipd.LOTTABLE05,ipd.LOTTABLE06,ipd.LOTTABLE07,ipd.LOTTABLE08,ipd.LOTTABLE09,ipd.LOTTABLE10,ipd.CREATED_BY_USER,ipd.CREATED_DTM_LOC "
				+ " from inb_po_detail ipd " + "inner join bas_item bi on ipd.storer_code=bi.storer_code and ipd.ITEM_CODE=bi.ITEM_CODE" 
				+" inner join bas_item_unit biu on bi.unit_code=biu.unit_code "
				+ " where 1=1 ";
		if(!strWhere.equals("")){
			sql = sql + strWhere;
		}
		System.out.println(sql);
		try{
			java.sql.Connection con = DBConnectionManager.getInstance().getConnection("wms");
			java.sql.Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Vector<String> rowdata = new Vector<String>();
				rowdata.add(rs.getString("LINE_NUMBER"));
				rowdata.add(rs.getString("ITEM_BAR_CODE"));
				rowdata.add(rs.getString("ITEM_CODE"));
				rowdata.add(rs.getString("ITEM_NAME"));
				rowdata.add(rs.getString("UNIT_NAME"));
				rowdata.add(rs.getString("TOTAL_QTY"));
				rowdata.add(rs.getString("RECEIVED_QTY"));
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
				rowdata.add(rs.getString("CREATED_BY_USER"));
				rowdata.add(rs.getString("CREATED_DTM_LOC"));
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
	
	private String checkPOStatus(String postr){
		String sql = "select po_no,status from inb_po_header where po_no='"+postr+"' ";
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
		txt_po_no.setText("");
		txt_erp_po_no.setText("");
		txt_storer_code.setText("");
		txt_storer_name.setText("");
		txt_warehouse_code.setText("");
		txt_warehouse_name.setText("");
		txt_vendor_code.setText("");
		txt_vendor_name.setText("");
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
