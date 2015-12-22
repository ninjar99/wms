package inbound;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import DBUtil.DBOperator;
import comUtil.comData;
import dmdata.DataManager;
import main.PBSUIBaseGrid;
import sys.InnerFrame;
import sys.JTableUtil;
import sys.MainFrm;
import sys.tableQueryDialog;
import util.JTNumEdit;
import util.Math_SAM;
import util.MyTableCellRenderrer;

import javax.swing.JTabbedPane;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class inbScanFrm extends InnerFrame {

	private JPanel contentPane;
	private static inbScanFrm instance;
	private static boolean isOpen = false;
	private JTextField txt_po_no;
	private JTextField txt_container_code;
	private JTextField txt_item_barcode;
	private PBSUIBaseGrid receiptHeaderTable;
	private PBSUIBaseGrid receiptDetailTable;
	private JTNumEdit txt_scan_qty;
	
	public static synchronized inbScanFrm getInstance() {
		if(instance == null) { 
			 synchronized(inbScanFrm.class){
				 if(instance == null) {
					 instance = new inbScanFrm();
				 }
			 }
	        }  
	        return instance;
	 }
	
	public static synchronized boolean getOpenStatus() {
		 if(instance == null) {    
	            instance = new inbScanFrm();  
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
					inbScanFrm frame = new inbScanFrm();
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
	public inbScanFrm() {
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.addVetoableChangeListener(new VetoableChangeListener() {

			public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException {
				JInternalFrame frame = (JInternalFrame) e.getSource();
				String name = e.getPropertyName();
				Object value = e.getNewValue();
				if (name.equals("closed") && value.equals(Boolean.TRUE)) // 窗口被关闭
				{
					System.out.println("inbScanFrm窗口被关闭");
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
		setBounds(100, 100, 760, 440);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel mainPanel = new JPanel();
		contentPane.add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabPane = new JTabbedPane(JTabbedPane.TOP);
		mainPanel.add(tabPane, BorderLayout.CENTER);
		
		JPanel tabPO = new JPanel();
		tabPane.addTab("\u3010OP\u6536\u8D27\u3011", null, tabPO, null);
		tabPO.setLayout(new GridLayout(2, 1, 0, 0));
		
		JPanel inbHeaderPanel = new JPanel();
		tabPO.add(inbHeaderPanel);
		inbHeaderPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		inbHeaderPanel.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JPanel editPanel = new JPanel();
		panel.add(editPanel);
		editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.X_AXIS));
		
		JLabel lblNewLabel = new JLabel("PO\uFF1A");
		editPanel.add(lblNewLabel);
		
		txt_po_no = new JTextField();
		editPanel.add(txt_po_no);
		txt_po_no.setColumns(10);
		txt_po_no.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent arg0) {
				
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				if(txt_po_no.getText().trim().equals("")){
					return;
				}
				String receiptNo = createReceiptHeader(txt_po_no.getText().trim());
				if(receiptNo.length()>0){
					//显示表头
					getHeaderTableData(" and irh.receipt_no='"+receiptNo+"' and irh.warehouse_code='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' ");
					
					//显示明细
					Vector headerRow = receiptHeaderTable.getData();
					Object[] headerRows = (Object[]) headerRow.get(0); 
					getDetailTableData(" and ird.receipt_no='"+receiptNo+"' ");
				}else{
					JOptionPane.showMessageDialog(null, "请重新输入PO","提示",JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			
		});
		txt_po_no.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyChar() == '\n') {
					txt_container_code.requestFocus();
					txt_container_code.selectAll();
				}
			}
		});
		
		JButton txt_po_query = new JButton("PO\u67E5\u8BE2");
		txt_po_query.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = "select iph.PO_NO,iph.ERP_PO_NO,iph.WAREHOUSE_CODE 仓库号,bw.WAREHOUSE_NAME 仓库名称,iph.STORER_CODE 货主编码,bs.STORER_NAME 货主名称,iph.VENDOR_CODE 供应商编码,bv.VENDOR_NAME 供应商名称,"
						+"case iph.status when '100' then '新建' when '300' then '收货中' when '900' then '关闭' else iph.status end PO状态 "
						+" from inb_po_header iph "
						+"inner join bas_storer bs on iph.STORER_CODE=bs.STORER_CODE "
						+"inner join bas_warehouse bw on iph.WAREHOUSE_CODE=bw.WAREHOUSE_CODE "
						+"inner join bas_vendor bv on iph.VENDOR_CODE=bv.VENDOR_CODE "
						+"where 1=1 and iph.warehouse_code='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' ";
				tableQueryDialog tableQuery = new tableQueryDialog(sql);
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
				Object obj = dm.getObject("PO_NO", 0);
				if(obj==null || obj.equals("")){
					return ;
				}else{
					txt_po_no.setText((String) dm.getObject("PO_NO", 0));
					txt_po_no.requestFocus();
					txt_po_no.selectAll();
				}
			}
		});
		editPanel.add(txt_po_query);
		
		JLabel lblNewLabel_1 = new JLabel("\u5468\u88C5\u7BB1\u53F7\uFF1A");
		editPanel.add(lblNewLabel_1);
		
		txt_container_code = new JTextField();
		editPanel.add(txt_container_code);
		txt_container_code.setColumns(10);
		txt_container_code.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent arg0) {
				
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				String containerCode = txt_container_code.getText().trim();
				if(containerCode.equals("")){
					return;
				}
				String sql = "select container_code from bas_container where container_code='"+containerCode+"' and status in('0','1') ";
				DataManager dm = DBOperator.DoSelect2DM(sql);
				if(dm==null || dm.getString(0, 0).length()<=0){
					JOptionPane.showMessageDialog(null, "周装箱号输入不正确","提示",JOptionPane.WARNING_MESSAGE);
					txt_container_code.setText("");
					txt_container_code.selectAll();
					txt_container_code.requestFocus();
					return;
				}
			}
		});
		txt_container_code.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyChar() == '\n') {
					txt_item_barcode.requestFocus();
					txt_item_barcode.selectAll();
				}
			}
		});
		
		JButton btnContainerQuery = new JButton("\u5468\u88C5\u7BB1\u67E5\u8BE2");
		btnContainerQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = "select bw.WAREHOUSE_NAME 仓库名称,bc.CONTAINER_CODE 箱号,(case bc.`STATUS` when '0' then '空箱' when '1' then '收货中' when '2' then '收货完成' else bc.`STATUS` end) 状态 "
						+",case bc.USE_TYPE when 'normal' then '固定' when 'temp' then '临时' else bc.USE_TYPE end 用途类型 "
						+"from bas_container bc inner join bas_warehouse bw on bc.WAREHOUSE_CODE=bw.WAREHOUSE_CODE "
						+"where bc.warehouse_code='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' and bc.status in ('0','1') "
						+"order by bc.status,bc.container_code ";
				tableQueryDialog tableQuery = new tableQueryDialog(sql);
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
				Object obj = dm.getObject("箱号", 0);
				if(obj==null || obj.equals("")){
					return;
				}else{
					txt_container_code.setText((String) dm.getObject("箱号", 0));
					txt_container_code.requestFocus();
					txt_container_code.selectAll();
				}
			}
		});
		editPanel.add(btnContainerQuery);
		
		JLabel lblNewLabel_2 = new JLabel("\u5546\u54C1\u6761\u7801\uFF1A");
		editPanel.add(lblNewLabel_2);
		
		txt_item_barcode = new JTextField();
		editPanel.add(txt_item_barcode);
		txt_item_barcode.setColumns(10);
		txt_item_barcode.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent arg0) {
				
			}

			@Override
			public void focusLost(FocusEvent arg0) {
//				checkItemBarCodeInput(true);//条码输入校验
			}
			
		});
		txt_item_barcode.addKeyListener(new KeyAdapter() {
			@SuppressWarnings("rawtypes")
			public void keyReleased(KeyEvent e) {
				if (e.getKeyChar() == '\n') {
					if(checkItemBarCodeInput(true)){
//						String sql = "select ";
//						Object[] rowdata = new Object[receiptDetailTable.getColumnCount()];
//						receiptDetailTable.addRow(rowdata);
						if(createReceiptDetail()){
							Vector headerRow = receiptHeaderTable.getData();
							Object[] headerRows = (Object[]) headerRow.get(0);
							String receiptNo = (String) headerRows[receiptHeaderTable.getColumnModel().getColumnIndex("入库单号")]; 
							getDetailTableData(" and ird.receipt_no='"+receiptNo+"' ");
							//滚动 定位Table保存数据行
							Rectangle rect = new Rectangle(0, receiptDetailTable.getHeight(), 20, 20);
							receiptDetailTable.scrollRectToVisible(rect);
							receiptDetailTable.setRowSelectionInterval(receiptDetailTable.getRowCount() - 1, receiptDetailTable.getRowCount() - 1);
							receiptDetailTable.grabFocus();
							receiptDetailTable.changeSelection(
									receiptDetailTable.getRowIndexByColIndexAndColValue(
											receiptDetailTable.getColumnModel().getColumnIndex("周装箱号"),txt_container_code.getText().trim()), 0, false, true);
							//更新周装箱状态为使用
							updateContainerCode(txt_container_code.getText(),"1");
							txt_item_barcode.requestFocus();
							txt_item_barcode.selectAll();
						}
					}
				}
			}
		});
		
		JLabel lblNewLabel_3 = new JLabel("\u6570\u91CF\uFF1A");
		editPanel.add(lblNewLabel_3);
		
		txt_scan_qty = new JTNumEdit(10, "#####",true);
		editPanel.add(txt_scan_qty);
		txt_scan_qty.setColumns(5);
		
		JPanel buttonPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel.add(buttonPanel);
		
		JButton btnNewButton = new JButton("\u6362\u7BB1");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txt_container_code.selectAll();
				txt_container_code.requestFocus();
			}
		});
		buttonPanel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("\u6536\u8D27\u5B8C\u6210");
		btnNewButton_1.addActionListener(new ActionListener() {
			@SuppressWarnings("rawtypes")
			public void actionPerformed(ActionEvent arg0) {
				Vector headerRow = receiptHeaderTable.getData();
				if(headerRow == null || headerRow.size()<=0){
					return;
				}
				Object[] headerRows = (Object[]) headerRow.get(0);
				String receiptNo = (String) headerRows[receiptHeaderTable.getColumnModel().getColumnIndex("入库单号")];
				int t = JOptionPane.showConfirmDialog(null, "是否确认该入库单【"+receiptNo+"】收货完成？");
				if(t==0){
					//更新入库单状态900，收货完成
					String sql = "update inb_receipt_header set status='900' where receipt_no='"+receiptNo+"'";
					int updateRow = DBOperator.DoUpdate(sql);
					if(updateRow==1){
						//生成库存
						if(generateInventory(receiptNo)){
							//更新周装箱状态=2
							sql = "update bas_container set status='2' where status='1' and container_code in (select distinct container_code from inb_receipt_detail where receipt_no='"+receiptNo+"') ";
							DBOperator.DoUpdate(sql);
							JOptionPane.showMessageDialog(null, "收货完成，生成库存记录成功");
							txt_po_no.setText("");
							txt_container_code.setText("");
							txt_item_barcode.setText("");
						}else{
							JOptionPane.showMessageDialog(null, "操作失败，请联系系统管理员");
						}
					}
				}
			}
		});
		buttonPanel.add(btnNewButton_1);
		
		JButton btnClose = new JButton("\u5173\u95ED");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					instance.setClosed(true);
				} catch (PropertyVetoException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		buttonPanel.add(btnClose);
		
		JPanel showPanel = new JPanel();
		inbHeaderPanel.add(showPanel, BorderLayout.CENTER);
		showPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		showPanel.add(scrollPane, BorderLayout.CENTER);
		
		receiptHeaderTable = new PBSUIBaseGrid();
		receiptHeaderTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()>=2){
					receiptHeaderTable.setColumnSelectionAllowed(true);
				}
			}
		});
		scrollPane.setViewportView(receiptHeaderTable);
		//receiptHeaderTable
		String[] RHColumnNames = {"入库单号","PO单号","ERP_PO号","仓库编码","仓库名称","货主编号","货主名称","供应商编号","供应商名称","开始收货时间","收货人"};
		receiptHeaderTable.setColumn(RHColumnNames);
		receiptHeaderTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		JPanel inbDetailPanel = new JPanel();
		tabPO.add(inbDetailPanel);
		inbDetailPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		inbDetailPanel.add(scrollPane_1, BorderLayout.CENTER);
		
		receiptDetailTable = new PBSUIBaseGrid();
		receiptDetailTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()>=2){
					receiptDetailTable.setColumnSelectionAllowed(true);
				}
			}
		});
		scrollPane_1.setViewportView(receiptDetailTable);
		//receiptDetailTable
		String[] RDColumnNames = {"入库单号","行号","商品编码","条码","商品名称","规格","收货数量","单位","周装箱号","库位号",
				"批次属性1","批次属性2","批次属性3","批次属性4","批次属性5","批次属性6","批次属性7","批次属性8","批次属性9","批次属性10",
				"收货时间","收货人"};
		receiptDetailTable.setColumn(RDColumnNames);
		receiptDetailTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		receiptDetailTable.setColumnEditableAll(false);
		receiptDetailTable.setColumnEditable(true, receiptDetailTable.getColumnModel().getColumnIndex("收货数量"));
		receiptDetailTable.setComponent(new JTNumEdit(15, "#####",true), receiptDetailTable.getColumnModel().getColumnIndex("收货数量"));
		receiptDetailTable.getColumn("收货数量").setCellRenderer(new MyTableCellRenderrer());
		JPanel tabNoPO = new JPanel();
		tabPane.addTab("\u3010\u65E0PO\u6536\u8D27\u3011", null, tabNoPO, null);
	}
	
	@SuppressWarnings("rawtypes")
	public String createReceiptHeader(String po_no){
		String sql = "select iph.INB_PO_HEADER_ID,iph.WAREHOUSE_CODE,bw.WAREHOUSE_NAME,iph.PO_NO,iph.ERP_PO_NO,iph.STORER_CODE,bs.STORER_NAME,iph.VENDOR_CODE,bv.VENDOR_NAME,iph.CREATED_DTM_LOC,iph.CREATED_BY_USER "
				+" ,sum(ifnull(ipd.TOTAL_QTY,0)-ifnull(ipd.RECEIVED_QTY,0)) openQty "
				+ " from inb_po_header iph "
				+ " inner join inb_po_detail ipd on iph.po_no=ipd.po_no and iph.storer_code=ipd.storer_code "
				+ " inner join bas_warehouse bw on iph.WAREHOUSE_CODE=bw.WAREHOUSE_CODE"
				+ " inner join bas_storer bs on iph.STORER_CODE=bs.STORER_CODE"
				+ " inner join bas_vendor bv on bv.VENDOR_CODE=iph.VENDOR_CODE" + " where iph.po_no ='"+po_no+"' ";
		DataManager dm = DBOperator.DoSelect2DM(sql);
		if(dm==null || dm.getCurrentCount()<=0){
			txt_po_no.setText("");
			txt_po_no.requestFocus();
			txt_po_no.selectAll();
			return "";
		}else{
			if(Math_SAM.str2Double(dm.getString("openQty", 0).toString())==0){
				JOptionPane.showMessageDialog(null, "PO已全部收货完成，请选择其他PO","提示",JOptionPane.WARNING_MESSAGE);
				return "";
			}
			sql = "select receipt_no from inb_receipt_header where po_no='"+po_no+"' and status='100' ";
			DataManager dm2 = DBOperator.DoSelect2DM(sql);
			if(dm2==null || dm2.getCurrentCount()<=0){
				String receiptNo = comData.getValueFromBasNumRule("inb_receipt_header", "RECEIPT_NO");
				sql = "insert into inb_receipt_header(RECEIPT_NO,INB_PO_HEADER_ID,PO_NO,ERP_PO_NO,WAREHOUSE_CODE,VENDOR_CODE,STORER_CODE,CREATED_DTM_LOC,CREATED_BY_USER )"
						+" select '"+receiptNo+"',INB_PO_HEADER_ID,PO_NO,ERP_PO_NO,WAREHOUSE_CODE,VENDOR_CODE,STORER_CODE,now(),'"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"' "
						+"from inb_po_header where po_no='"+po_no+"' ";
				int t = DBOperator.DoUpdate(sql);
				if(t>0){
					return receiptNo;
				}else{
					return "";
				}
			}else{
				return dm2.getString("receipt_no", 0);
			}
			
		}
	}
	
	@SuppressWarnings("rawtypes")
	public boolean createReceiptDetail(){
		Vector headerRow = receiptHeaderTable.getData();
		if(headerRow == null || headerRow.size()<=0){
			return false;
		}
		Object[] headerRows = (Object[]) headerRow.get(0);
		String receiptNo = (String) headerRows[receiptHeaderTable.getColumnModel().getColumnIndex("入库单号")];
		String poNo = (String) headerRows[receiptHeaderTable.getColumnModel().getColumnIndex("PO单号")];
		String itemBarCode = txt_item_barcode.getText().trim();
		String containerCode = txt_container_code.getText();
		
		String sql = "select iph.INB_PO_HEADER_ID,ipd.INB_PO_DETAIL_ID,"
				+"iph.WAREHOUSE_CODE,iph.STORER_CODE,iph.PO_NO,ipd.LINE_NUMBER,ipd.ITEM_CODE,'"+txt_container_code.getText().trim()+"'"
				+"from inb_po_header iph "
				+"inner join inb_po_detail ipd on iph.PO_NO=ipd.PO_NO and iph.INB_PO_HEADER_ID=ipd.INB_PO_HEADER_ID "
				+"inner join bas_item bi on ipd.ITEM_CODE=bi.ITEM_CODE "
				+"inner join bas_item_unit biu on bi.UNIT_CODE=biu.unit_code "
				+"where ipd.PO_NO='"+poNo+"' and bi.ITEM_BAR_CODE='"+itemBarCode+"' "
				+" and ifnull(ipd.TOTAL_QTY,0)-ifnull(ipd.RECEIVED_QTY,0)>0 "
				+"order by ifnull(ipd.TOTAL_QTY,0)-ifnull(ipd.RECEIVED_QTY,0) "
				+"limit 1 ";
		DataManager dmtmp = DBOperator.DoSelect2DM(sql);
		if(dmtmp==null || dmtmp.getCurrentCount()<=0){
			return false;
		}else{
			String itemCode = dmtmp.getString("ITEM_CODE", 0);
			String poLineNumber = dmtmp.getString("LINE_NUMBER", 0);
			sql = "select ird.receipt_no from inb_receipt_detail ird "
			+"where ird.receipt_no='"+receiptNo+"' and ird.item_code='"+itemCode+"' and ird.PO_LINE_NO='"+poLineNumber+"' and ird.container_code='"+containerCode+"' ";
			DataManager dmtmp2 = DBOperator.DoSelect2DM(sql);
			if(dmtmp2==null || dmtmp2.getCurrentCount()<=0){
				//增加收货单明细行
				sql = "insert into inb_receipt_detail(INB_RECEIPT_HEADER_ID,RECEIPT_NO,INB_PO_HEADER_ID,INB_PO_DETAIL_ID,"
						+"WAREHOUSE_CODE,STORER_CODE,PO_NO,PO_LINE_NO,RECEIPT_LINE_NO,ITEM_CODE,CONTAINER_CODE,LOCATION_CODE"
						+",LOTTABLE01,LOTTABLE02,LOTTABLE03,LOTTABLE04,LOTTABLE05,LOTTABLE06,LOTTABLE07,LOTTABLE08,LOTTABLE09,LOTTABLE10"
						+",TOTAL_QTY,TOTAL_UOM,RECEIVED_QTY,RECEIVED_UOM,CREATED_DTM_LOC,CREATED_BY_USER) "
						
						+"select (select INB_RECEIPT_HEADER_ID from inb_receipt_header where RECEIPT_NO='"+receiptNo+"' limit 1),'"+receiptNo+"',iph.INB_PO_HEADER_ID,ipd.INB_PO_DETAIL_ID,"
						+"iph.WAREHOUSE_CODE,iph.STORER_CODE,iph.PO_NO,ipd.LINE_NUMBER,ifnull((select count(1) from inb_receipt_detail where RECEIPT_NO='"+receiptNo+"'),0)+1,ipd.ITEM_CODE,'"+txt_container_code.getText().trim()+"',"
						+"(select location_code from bas_location where warehouse_code='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' and location_type_code='Dock' order by location_code limit 1 )"
						+",ipd.LOTTABLE01,ipd.LOTTABLE02,ipd.LOTTABLE03,ipd.LOTTABLE04,ipd.LOTTABLE05,ipd.LOTTABLE06,ipd.LOTTABLE07,ipd.LOTTABLE08,ipd.LOTTABLE09,ipd.LOTTABLE10"
						+",ipd.TOTAL_QTY,ipd.UOM,"+txt_scan_qty.getText()+",biu.unit_name,now(),'"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"' "
						+"from inb_po_header iph "
						+"inner join inb_po_detail ipd on iph.PO_NO=ipd.PO_NO and iph.INB_PO_HEADER_ID=ipd.INB_PO_HEADER_ID "
						+"inner join bas_item bi on ipd.ITEM_CODE=bi.ITEM_CODE "
						+"inner join bas_item_unit biu on bi.UNIT_CODE=biu.unit_code "
						+"where ipd.PO_NO='"+poNo+"' and bi.ITEM_BAR_CODE='"+itemBarCode+"' "
						+" and ifnull(ipd.TOTAL_QTY,0)-ifnull(ipd.RECEIVED_QTY,0)>0 "
						+"order by ifnull(ipd.TOTAL_QTY,0)-ifnull(ipd.RECEIVED_QTY,0) "
						+"limit 1 ";
			}else{
				//更新收货单明细行
				String scanQtyStr = txt_scan_qty.getText();
				Double scanQty = 0.0;
				try{
					scanQty = Double.parseDouble(scanQtyStr);
				}catch(Exception e){
					scanQty = 1.0;
					txt_scan_qty.setText("1");
				}
				
				if(Double.parseDouble(getPODetailLineNumberAvailableQty(poNo,itemCode,poLineNumber))-scanQty<0){
					JOptionPane.showMessageDialog(null, "不能超出PO明细数量","提示",JOptionPane.ERROR_MESSAGE);
					return false;
				}
				//更新收货单明细数量
				sql = "update inb_receipt_detail set RECEIVED_QTY=RECEIVED_QTY+("+txt_scan_qty.getText()+") "
						+"where receipt_no='"+receiptNo+"' and item_code='"+itemCode+"' and PO_LINE_NO='"+poLineNumber+"' "
						+"and container_code='"+containerCode+"' ";
			}
			int t = DBOperator.DoUpdate(sql);
			if(t>0){
				//收货单明细操作成功后，更新PO明细实际收货数量
				sql = "update inb_po_detail set RECEIVED_QTY=RECEIVED_QTY+("+txt_scan_qty.getText()+") "
						+" where PO_NO='"+poNo+"' and LINE_NUMBER="+poLineNumber+" and ITEM_CODE='"+itemCode+"' ";
				int poUpdate = DBOperator.DoUpdate(sql);
				if(poUpdate==1){
					//更新PO表头状态=300 入库收货中
					sql = "update inb_po_header set status='300' where PO_NO='"+poNo+"' and status='100' ";
					DBOperator.DoUpdate(sql);
					return true;
				}else{
					JOptionPane.showMessageDialog(null, "更新PO明细实际收货数量失败","提示",JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}else{
				JOptionPane.showMessageDialog(null, "保存入库单明细失败","提示",JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}		
	}
	
	private String getPODetailLineNumberAvailableQty(String poNo,String itemCode,String poLineNumber){
		String sql = "select ifnull(ipd.TOTAL_QTY,0)-ifnull(ipd.RECEIVED_QTY,0) openQty from inb_po_detail ipd "
				+"where ipd.po_no='"+poNo+"' and ipd.item_code='"+itemCode+"' and ipd.Line_number="+poLineNumber+" ";
		Vector vec = DBOperator.DoSelect(sql);
		if(vec==null || vec.size()==0){
			return "0.0";
		}else{
			Object[] obj = (Object[]) vec.get(0);
			return obj[0].toString(); 
		}
		
	}
	
	@SuppressWarnings("rawtypes")
	private void getHeaderTableData(String strWhere){
		receiptHeaderTable.setColumnEditableAll(false);
		receiptHeaderTable.removeRowAll();
		String sql = "select irh.RECEIPT_NO,irh.PO_NO,irh.ERP_PO_NO,irh.WAREHOUSE_CODE,bw.WAREHOUSE_NAME,irh.VENDOR_CODE,bv.VENDOR_NAME,"
				+"irh.STORER_CODE,bs.STORER_NAME,DATE_FORMAT(irh.CREATED_DTM_LOC,'%Y-%c-%d %h:%i:%s') CREATED_DTM_LOC,irh.CREATED_BY_USER "
				+ " from inb_receipt_header irh  " 
				+ " inner join bas_warehouse bw on irh.WAREHOUSE_CODE=bw.WAREHOUSE_CODE "
				+ " inner join bas_vendor bv on bv.VENDOR_CODE=irh.VENDOR_CODE "
				+ " inner join bas_storer bs on bs.STORER_CODE=irh.STORER_CODE " 
				+ " where 1=1 ";
		if(!strWhere.equals("")){
			sql = sql + strWhere;
		}
		Vector receiptHeaderData = DBOperator.DoSelect(sql);
		receiptHeaderTable.setData(receiptHeaderData);
		if(receiptHeaderTable.getRowCount()>1){
			receiptHeaderTable.setRowSelectionInterval(0, 0);//默认选中第一行
		}
		JTableUtil.fitTableColumns(receiptHeaderTable);
//		headerTableClick();
	}
	
	@SuppressWarnings("rawtypes")
	private void getDetailTableData(String strWhere){
		receiptDetailTable.setColumnEditableAll(false);
		receiptDetailTable.removeRowAll();
		String sql = "select ird.RECEIPT_NO,ird.RECEIPT_LINE_NO,ird.ITEM_CODE,bi.ITEM_BAR_CODE,bi.ITEM_NAME,bi.ITEM_SPEC,ird.RECEIVED_QTY,biu.unit_name "
				+",ird.CONTAINER_CODE,ird.LOCATION_CODE"
				+",ird.LOTTABLE01,ird.LOTTABLE02,ird.LOTTABLE03,ird.LOTTABLE04,ird.LOTTABLE05,ird.LOTTABLE06,ird.LOTTABLE07,ird.LOTTABLE08,ird.LOTTABLE09,ird.LOTTABLE10"
				+",DATE_FORMAT(ird.CREATED_DTM_LOC,'%Y-%c-%d %h:%i:%s') CREATED_DTM_LOC,ird.CREATED_BY_USER "
				+" from inb_receipt_detail ird "
				+"inner join bas_item bi on ird.storer_code=bi.storer_code and ird.ITEM_CODE=bi.ITEM_CODE "
				+"inner join bas_item_unit biu on bi.UNIT_CODE=biu.unit_code "
				+" where 1=1 ";
		if(!strWhere.equals("")){
			sql = sql + strWhere;
		}
		Vector receiptDetailTableData = DBOperator.DoSelect(sql);
		receiptDetailTable.setData(receiptDetailTableData);
		if(receiptDetailTable.getRowCount()>1){
			receiptDetailTable.setRowSelectionInterval(0, 0);//默认选中第一行
		}
		JTableUtil.fitTableColumns(receiptDetailTable);
//		headerTableClick();
	}
	
	private boolean checkItemBarCodeInput(boolean showTips){
		String barCode = txt_item_barcode.getText().trim();
		if(barCode.equals("")){
			return false;
		}
		String sql = "select ipd.item_code,bi.ITEM_BAR_CODE from inb_po_detail ipd "
				+ " inner join bas_item bi on ipd.item_code=bi.item_code " + " where po_no='"
				+ txt_po_no.getText().trim() + "' and bi.ITEM_BAR_CODE='" + barCode + "' "
				+" and ifnull(ipd.TOTAL_QTY,0)-ifnull(ipd.RECEIVED_QTY,0)>0 ";
		DataManager dm = DBOperator.DoSelect2DM(sql);
		if(dm==null || dm.getString(0, 0).length()<=0){
			txt_item_barcode.selectAll();
			txt_item_barcode.requestFocus();
			txt_scan_qty.setText("0");
			if(showTips){
				JOptionPane.showMessageDialog(null, "输入的条码不正确或者与该PO明细不匹配或超出PO明细","提示",JOptionPane.WARNING_MESSAGE);
			}
			return false;
		}else{
			String qty = txt_scan_qty.getText();
			try {
				if (Double.parseDouble(qty) < 1) {
					txt_scan_qty.setText("1");
				}
			} catch (Exception e) {
				txt_scan_qty.setText("1");
			}
			return true;
		}
	}
	
	private boolean updateContainerCode(String containerCode,String status){
		String sql = "update bas_container set status='1' where container_code='"+containerCode+"' ";
		int t = DBOperator.DoUpdate(sql);
		if(t==1){
			return true;
		}else{
			return false;
		}
	}
	
	private boolean generateInventory(String receiptNo){
		String warehouseCode ="";
		String storerCode = "";
		String itemCode = "";
		String lotNo = "";
		String inventoryID = "";
		String lottable01 = "";
		String lottable02 = "";
		String lottable03 = "";
		String lottable04 = "";
		String lottable05 = "";
		String lottable06 = "";
		String lottable07 = "";
		String lottable08 = "";
		String lottable09 = "";
		String lottable10 = "";
		String locationCode = "";
		String containCode = "";
		String receivedQty = "";
		String receivedUOM = "";
		String sql = "select RECEIPT_NO,WAREHOUSE_CODE,STORER_CODE,ITEM_CODE,CONTAINER_CODE,LOCATION_CODE,"
				+"LOTTABLE01,LOTTABLE02,LOTTABLE03,LOTTABLE04,LOTTABLE05,LOTTABLE06,LOTTABLE07,LOTTABLE08,LOTTABLE09,LOTTABLE10,"
				+"RECEIVED_QTY,RECEIVED_UOM "
		+"from inb_receipt_detail "
		+"where receipt_no='"+receiptNo+"' ";
		DataManager dm = DBOperator.DoSelect2DM(sql);
		for(int i=0;i<dm.getCurrentCount();i++){
			warehouseCode = dm.getString("WAREHOUSE_CODE", i);
			storerCode = dm.getString("STORER_CODE", i);
			itemCode = dm.getString("ITEM_CODE", i);
			locationCode = dm.getString("LOCATION_CODE", i);
			containCode = dm.getString("CONTAINER_CODE", i);
			receivedQty = dm.getString("RECEIVED_QTY", i);
			receivedUOM = dm.getString("RECEIVED_UOM", i);
			lottable01 = dm.getString("LOTTABLE01", i);
			lottable02 = dm.getString("LOTTABLE02", i);
			lottable03 = dm.getString("LOTTABLE03", i);
			lottable04 = dm.getString("LOTTABLE04", i);
			lottable05 = dm.getString("LOTTABLE05", i);
			lottable06 = dm.getString("LOTTABLE06", i);
			lottable07 = dm.getString("LOTTABLE07", i);
			lottable08 = dm.getString("LOTTABLE08", i);
			lottable09 = dm.getString("LOTTABLE09", i);
			lottable10 = dm.getString("LOTTABLE10", i);
			//先生成库存批次号
			lotNo = getInventoryLotNo(storerCode,itemCode,lottable01,lottable02,lottable03,lottable04,lottable05,lottable06,lottable07,lottable08,lottable09,lottable10);
			if(!lotNo.equals("")){
				//插入库存表
				inventoryID = getInventoryID(warehouseCode,storerCode,itemCode,lotNo,locationCode,containCode,receivedQty);
				if(!inventoryID.equals("")){
					//库存表写入成功
					continue;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}
		return true;
	}
	
	private String getInventoryLotNo(String storerCode,String itemCode,String lot1,String lot2,String lot3,String lot4,String lot5,String lot6,String lot7,String lot8,String lot9,String lot10){
		String lotNo = "";
		String sql = "select LOT_NO from inv_lot where STORER_CODE='"+storerCode+"' and ITEM_CODE='"+itemCode+"' "
				+" and LOTTABLE01='"+lot1+"' and LOTTABLE02='"+lot2+"' and LOTTABLE03='"+lot3+"' and LOTTABLE04='"+lot4+"' "
				+" and LOTTABLE05='"+lot5+"' and LOTTABLE06='"+lot6+"' and LOTTABLE07='"+lot7+"' and LOTTABLE08='"+lot8+"' "
				+" and LOTTABLE09='"+lot9+"' and LOTTABLE10='"+lot10+"'";
		Vector vec = DBOperator.DoSelect(sql);
		if(vec==null || vec.size()==0){
			lotNo = comData.getValueFromBasNumRule("inv_lot", "lot_no");
			sql = "insert into inv_lot(LOT_NO,STORER_CODE,ITEM_CODE,LOTTABLE01,LOTTABLE02,LOTTABLE03,LOTTABLE04,LOTTABLE05,LOTTABLE06,LOTTABLE07,LOTTABLE08,LOTTABLE09,LOTTABLE10) "
					+"select '"+lotNo+"','"+storerCode+"','"+itemCode+"','"+lot1+"','"+lot2+"','"+lot3+"','"+lot4+"','"+lot5+"' "
					+",'"+lot6+"','"+lot7+"','"+lot8+"','"+lot9+"','"+lot10+"' ";
			int t = DBOperator.DoUpdate(sql);
			if(t==1){
				return lotNo;
			}else{
				return "";
			}
		}else{
			Object[] obj = (Object[]) vec.get(0);
			lotNo = obj[0].toString();
		}
		return lotNo;
	}
	
	@SuppressWarnings("rawtypes")
	private String getInventoryID(String warehouseCode,String storerCode,String itemCode,String lotNo,
			String locationCode,String containerCode,String onHandQty){
		String INV_INVENTORY_ID = "";
		String sql = "select INV_INVENTORY_ID from inv_inventory "
				+"where WAREHOUSE_CODE='"+warehouseCode+"' and STORER_CODE='"+storerCode+"' and ITEM_CODE='"+itemCode+"' and LOT_NO='"+lotNo+"' "
				+" and LOCATION_CODE='"+locationCode+"' and CONTAINER_CODE='"+containerCode+"' ";
		Vector vec = DBOperator.DoSelect(sql);
		if(vec==null || vec.size()==0){
			//插入新的库存行记录
			sql = "insert into inv_inventory(WAREHOUSE_CODE,STORER_CODE,ITEM_CODE,ITEM_NAME,INV_LOT_ID,LOT_NO,LOCATION_CODE"
					+ ",CONTAINER_CODE,ON_HAND_QTY,CREATED_BY_USER,CREATED_DTM_LOC) " 
					+ "select '"+warehouseCode+"','"+storerCode+"','"+itemCode+"',(select ITEM_NAME from bas_item where storer_code='"+storerCode+"' and item_code='"+itemCode+"') "
					+",(select INV_LOT_ID from inv_lot where LOT_NO='"+lotNo+"' and STORER_CODE='"+storerCode+"' and ITEM_CODE='"+itemCode+"'),"
					+"'"+lotNo+"','"+locationCode+"','"+containerCode+"',"+onHandQty+",'"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"',now() ";
			int t = DBOperator.DoUpdate(sql);
			if(t==1){
				sql = "select INV_INVENTORY_ID from inv_inventory "
						+"where WAREHOUSE_CODE='"+warehouseCode+"' and STORER_CODE='"+storerCode+"' and ITEM_CODE='"+itemCode+"' and LOT_NO='"+lotNo+"' "
						+" and LOCATION_CODE='"+locationCode+"' and CONTAINER_CODE='"+containerCode+"' ";
				vec = DBOperator.DoSelect(sql);
				if(vec==null || vec.size()==0){
					return "";
				}else{
					Object[] obj = (Object[]) vec.get(0);
					INV_INVENTORY_ID = obj[0].toString();
					return INV_INVENTORY_ID;
				}
			}else{
				return "";
			}
		}else{
			//增加库存数量
			Object[] obj = (Object[]) vec.get(0);
			INV_INVENTORY_ID = obj[0].toString();
			sql = "update inv_inventory set ON_HAND_QTY=ON_HAND_QTY+("+onHandQty+") "
					+"where INV_INVENTORY_ID="+INV_INVENTORY_ID;
			int t = DBOperator.DoUpdate(sql);
			if(t==1){
				return INV_INVENTORY_ID;
			}else{
				return "";
			}
		}
	}

	private void setExtendedState(int maximizedBoth) {
		// TODO Auto-generated method stub
		
	}

}
