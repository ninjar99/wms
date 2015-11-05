package outbound;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import DBUtil.DBOperator;
import comUtil.comData;
import dmdata.DataManager;
import dmdata.xArrayList;
import inventory.InvQueryFrm;
import main.PBSUIBaseGrid;
import sys.InnerFrame;
import sys.JTableUtil;
import sys.MainFrm;
import sys.Message;
import sys.tableQueryDialog;
import util.JTNumEdit;
import util.Math_SAM;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JCheckBox;

public class ShipmentOubScan extends InnerFrame {

	private JPanel contentPane;
	private static ShipmentOubScan instance;
	private static boolean isOpen = false;
	private JTextField txt_transfer_no;
	private PBSUIBaseGrid headerTable;
	private JTextField txt_item_bar_code;
	private JTNumEdit txt_scan_qty;
	private JButton btnOK;
	private JCheckBox cbIgnoreScanBarcode;
	
	public static synchronized ShipmentOubScan getInstance() {
		 if(instance == null) {    
	            instance = new ShipmentOubScan();  
	        }  
	        return instance;
		 
	 }
	
	public static synchronized boolean getOpenStatus() {
		 if(instance == null) {    
	            instance = new ShipmentOubScan();  
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
					ShipmentOubScan frame = new ShipmentOubScan();
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
	public ShipmentOubScan() {
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.addVetoableChangeListener(new VetoableChangeListener() {

			public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException {
				String name = e.getPropertyName();
				Object value = e.getNewValue();
				if (name.equals("closed") && value.equals(Boolean.TRUE)) // 窗口被关闭
				{
					System.out.println("InvQueryFrm窗口被关闭");
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
		
		setBounds(100, 100, 713, 434);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel topPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) topPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		contentPane.add(topPanel, BorderLayout.NORTH);
		
		cbIgnoreScanBarcode = new JCheckBox("\u8DF3\u8FC7\u6761\u7801\u626B\u63CF");
		topPanel.add(cbIgnoreScanBarcode);
		
		JLabel lblNewLabel = new JLabel("\u8FD0\u5355\u53F7\uFF1A");
		topPanel.add(lblNewLabel);
		
		txt_transfer_no = new JTextField();
		txt_transfer_no.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				String transferNo = txt_transfer_no.getText().trim();
				if(!transferNo.equals("")){
					if(!cbIgnoreScanBarcode.isSelected()){
						getHeaderTableData(transferNo);
						txt_transfer_no.selectAll();
					}
					
				}
			}
		});
		txt_transfer_no.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyChar() == '\n') {
					
					new SwingWorker<String, Void>() {

			            @Override
			            protected String doInBackground() throws Exception {
			            	//自动扫描条码
			            	autoScan();
			                return "";
			            }

			            @Override
			            protected void done() {
			                System.out.println("自动扫描条码结束");
			            }
			        }.execute();
			        
					
				}
			}
		});
		topPanel.add(txt_transfer_no);
		txt_transfer_no.setColumns(15);
		
		JButton btnTransferQuery = new JButton("<");
		btnTransferQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = "select distinct TRANSFER_ORDER_NO 运单号,WAREHOUSE_CODE 仓库编码,STORER_CODE 货主编码 from oub_shipment_header where status<500 and status>100 "
						+" and WAREHOUSE_CODE='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' ";
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
				Object obj = dm.getObject("运单号", 0);
				if(obj==null || obj.equals("")){
					return;
				}else{
					txt_transfer_no.setText((String) dm.getObject("运单号", 0));
					txt_transfer_no.requestFocus();
					txt_transfer_no.selectAll();
				}
			}
		});
		topPanel.add(btnTransferQuery);
		
		JLabel lblNewLabel_1 = new JLabel("\u6761\u7801\uFF1A");
		topPanel.add(lblNewLabel_1);
		
		txt_item_bar_code = new JTextField();
		txt_item_bar_code.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				txt_scan_qty.setText("1");
			}
			@Override
			public void focusGained(FocusEvent e) {
				
			}
		});
		txt_item_bar_code.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyChar() == '\n') {
					boolean isBarcode = false;
					boolean isUpdate = false;
					String itemBarCode = txt_item_bar_code.getText().trim();
					String transferNo = txt_transfer_no.getText().trim();
					String scanQty = txt_scan_qty.getText().trim();
					String reqQty = "";
					String pickQty = "";
					String oqcQty = "";
					String allocatedQty = "";
					if(itemBarCode.equals("") || headerTable.getRowCount()==0){
						return;
					}
					txt_scan_qty.setText("1");
					//输入 000 默认自动点击   复核确认
					if(itemBarCode.equals("000")){
						btnOK.doClick();
						return;
					}
					for(int i=0;i<headerTable.getRowCount();i++){
						String tmpBarCode = headerTable.getValueAt(i, headerTable.getColumnModel().getColumnIndex("条码")).toString();
						reqQty = headerTable.getValueAt(i, headerTable.getColumnModel().getColumnIndex("订单数量")).toString();
						pickQty = headerTable.getValueAt(i, headerTable.getColumnModel().getColumnIndex("拣货数量")).toString();
						oqcQty = headerTable.getValueAt(i, headerTable.getColumnModel().getColumnIndex("复核数量")).toString();
						allocatedQty = headerTable.getValueAt(i, headerTable.getColumnModel().getColumnIndex("分配数量")).toString();
						if(Math_SAM.str2Double(allocatedQty)==0){
							Message.showWarningMessage("此订单未分配库存，不允许做出库扫描");
							txt_item_bar_code.setText("");
							txt_item_bar_code.requestFocus();
							return;
						}
						if(itemBarCode.equalsIgnoreCase(tmpBarCode)){
							isBarcode = true;
							if(Math_SAM.str2Double(reqQty)-Math_SAM.str2Double(scanQty)>=Math_SAM.str2Double(oqcQty)){
								isUpdate = true;
								headerTable.setValueAt(String.valueOf(Math_SAM.str2Double(oqcQty)+Math_SAM.str2Double(scanQty)), 
										i, headerTable.getColumnModel().getColumnIndex("拣货数量"));
								headerTable.setValueAt(String.valueOf(Math_SAM.str2Double(oqcQty)+Math_SAM.str2Double(scanQty)), 
										i, headerTable.getColumnModel().getColumnIndex("复核数量"));
								txt_item_bar_code.setText("");;
								txt_item_bar_code.requestFocus();
							}
						}
					}
					if(!isBarcode){
						Message.showWarningMessage("扫描的商品条码不正确");
						txt_item_bar_code.setText("");
						txt_item_bar_code.requestFocus();
					}else{
						if(!isUpdate){
							Message.showWarningMessage("数量超出");
							txt_item_bar_code.setText("");
							txt_item_bar_code.requestFocus();
						}
					}
				}
			}
		});
		topPanel.add(txt_item_bar_code);
		txt_item_bar_code.setColumns(15);
		
		JLabel lblNewLabel_2 = new JLabel("\u6570\u91CF\uFF1A");
		topPanel.add(lblNewLabel_2);
		
		txt_scan_qty = new JTNumEdit(10, "#####",true);
		topPanel.add(txt_scan_qty);
		txt_scan_qty.setColumns(5);
		
		btnOK = new JButton("\u786E\u8BA4");
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = "";
				StringBuffer sb = new StringBuffer();
				if(headerTable.getRowCount()==0){
					Message.showWarningMessage("无扫描明细数据");
					return;
				}
				for(int i=0;i<headerTable.getRowCount();i++){
					String status = (String) headerTable.getValueAt(i, headerTable.getColumnModel().getColumnIndex("状态"));
					String shipmentNo = (String) headerTable.getValueAt(i, headerTable.getColumnModel().getColumnIndex("出库单号"));
					String shipmentLineNo = (String) headerTable.getValueAt(i, headerTable.getColumnModel().getColumnIndex("行号"));
					String allocatedQty = (String) headerTable.getValueAt(i, headerTable.getColumnModel().getColumnIndex("分配数量"));
					String oqcQty = (String) headerTable.getValueAt(i, headerTable.getColumnModel().getColumnIndex("复核数量"));
					if(status.equals("包装中")){
						Message.showWarningMessage("此订单已经完成拣货扫描");
						if(cbIgnoreScanBarcode.isSelected()){
							txt_transfer_no.setText("");
							txt_transfer_no.requestFocus();
						}else{
							txt_item_bar_code.setText("");;
							txt_item_bar_code.requestFocus();
						}
						
						return;
					}
					if(Math_SAM.str2Double(allocatedQty)==0){
						Message.showWarningMessage("此订单未分配库存，不允许做出库扫描");
						txt_item_bar_code.setText("");
						txt_item_bar_code.requestFocus();
						return;
					}
					if(Math_SAM.str2Double(oqcQty)==0){
						Message.showWarningMessage("条码未扫描，复核数量为0，不允许做出库扫描");
						txt_item_bar_code.setText("");;
						txt_item_bar_code.requestFocus();
						return;
					}
					if(Math_SAM.str2Double(oqcQty)<Math_SAM.str2Double(allocatedQty)){
						boolean bool = Message.showOKorCancelMessage("拣货复核扫描数量小于订单库存分配数量，是否继续？");
						if(!bool){
							return;
						}
					}
					sql = "update oub_shipment_detail set STATUS='500',PICKED_QTY="+oqcQty+",SORTED_QTY="+oqcQty+",OQC_QTY= "+oqcQty+",UPDATED_DTM_LOC=now(),UPDATED_BY_USER='"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"' "
							+" where SHIPMENT_NO='"+shipmentNo+"' and SHIPMENT_LINE_NO='"+shipmentLineNo+"' "
							+" and WAREHOUSE_CODE='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' and STATUS < '500' ";
					int t = DBOperator.DoUpdate(sql);
					if(t==0){
						sb.append("订单号："+shipmentNo+" 行号："+shipmentLineNo+" 数量："+oqcQty+" 更新失败\n");
					}else{
						//更新订单头 status='500'
						sql = "update oub_shipment_header set STATUS='500',UPDATED_DTM_LOC=now(),UPDATED_BY_USER='"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"' "
								+" where SHIPMENT_NO='"+shipmentNo+"' and status<'500' "
								+" and WAREHOUSE_CODE='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' ";
						t = DBOperator.DoUpdate(sql);
						//更新拣货单明细 status='900'
						sql = "update oub_pick_detail set STATUS='900',UPDATED_DTM_LOC=now(),UPDATED_BY_USER='"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"' "
								+" where SHIPMENT_NO='"+shipmentNo+"' and SHIPMENT_LINE_NO='"+shipmentLineNo+"' and status<'900' "
								+" and WAREHOUSE_CODE='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' ";
						t = DBOperator.DoUpdate(sql);
						//更新拣货单表头，如果所有明细都是900状态
						sql = "update oub_pick_header set `STATUS`='900',UPDATED_DTM_LOC=now(),UPDATED_BY_USER='"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"' "
								+" where not exists "
								+"(select PICK_NO from oub_pick_detail where oub_pick_header.PICK_NO=oub_pick_detail.PICK_NO and `STATUS`<'900') "
								+" and SHIPMENT_NO='"+shipmentNo+"' and WAREHOUSE_CODE='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' ";
						t = DBOperator.DoUpdate(sql);
						//更新库存分配数量和拣货数量
						sql = "update inv_inventory ii "
							+"inner join oub_pick_detail opd on opd.INV_INVENTORY_ID = ii.INV_INVENTORY_ID and opd.STORER_CODE=ii.STORER_CODE and opd.WAREHOUSE_CODE=ii.WAREHOUSE_CODE and opd.FROM_LOCATION_CODE=ii.LOCATION_CODE and opd.FROM_CONTAINER_CODE=ii.CONTAINER_CODE and opd.ITEM_CODE=ii.ITEM_CODE and opd.LOT_NO=ii.LOT_NO  "
							+"set ii.PICKED_QTY=ii.PICKED_QTY+opd.PICKED_QTY,ii.ALLOCATED_QTY=ii.ALLOCATED_QTY-(opd.PICKED_QTY) "
							+",ii.UPDATED_DTM_LOC=now(),ii.UPDATED_BY_USER='"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"'"
							+"where opd.SHIPMENT_NO='"+shipmentNo+"' and opd.SHIPMENT_LINE_NO='"+shipmentLineNo+"' "
							+"";
						t = DBOperator.DoUpdate(sql);
					}
				}
				if(sb.toString().trim().length()>1){
					Message.showWarningMessage(sb.toString());
				}else{
					String transferNo = txt_transfer_no.getText();
					Message.showInfomationMessage("拣货扫描复核完成!");
					txt_transfer_no.setText("");
					txt_item_bar_code.setText("");
					headerTable.removeRowAll();
					txt_transfer_no.requestFocus();
					//记录操作日志
					DataManager dmProcess = comData.getSysProcessHistoryDataModel("sys_process_history");
					if (dmProcess != null) {
						dmdata.xArrayList list = (xArrayList) dmProcess.getRow(0);
						list.set(dmProcess.getCol("SYS_PROCESS_HISTORY_ID"), "null");
						list.set(dmProcess.getCol("PROCESS_CODE"), "PickConfirm");
						list.set(dmProcess.getCol("PROCESS_NAME"), "拣货扫描确认");
						list.set(dmProcess.getCol("STORER_CODE"), "");
						list.set(dmProcess.getCol("WAREHOUSE_CODE"),MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0));
						list.set(dmProcess.getCol("FROM_LOCATION_CODE"), "");
						list.set(dmProcess.getCol("FROM_CONTAINER_CODE"), "");
						list.set(dmProcess.getCol("QTY"), "");
						list.set(dmProcess.getCol("REFERENCE_NO"), transferNo);
						list.set(dmProcess.getCol("REFERENCE_LINE_NO"), "");
						list.set(dmProcess.getCol("REFERENCE_TYPE"), "");
						list.set(dmProcess.getCol("LOT_NO"), "");
						list.set(dmProcess.getCol("MESSAGE"), "");
						list.set(dmProcess.getCol("PROCESS_TIME"), "now()");
						list.set(dmProcess.getCol("CREATED_BY_USER"),MainFrm.getUserInfo().getString("USER_CODE", 0));
						list.set(dmProcess.getCol("CREATED_DTM_LOC"), "now()");
						list.set(dmProcess.getCol("UPDATED_DTM_LOC"), "now()");
						dmProcess.RemoveRow(0);
						dmProcess.AddNewRow(list);
						boolean bool = comData.addSysProcessHistory("sys_process_history", dmProcess);
						System.out.println("写入操作日志：" + bool);
					}
				}
			}
		});
		topPanel.add(btnOK);
		
		JButton btnClose = new JButton("\u5173\u95ED");
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
		
		JPanel centerPanel = new JPanel();
		contentPane.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		centerPanel.add(scrollPane, BorderLayout.CENTER);
		
		headerTable = new PBSUIBaseGrid();
		headerTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()>=2){
					headerTable.setColumnSelectionAllowed(true);
				}
			}
		});
		scrollPane.setViewportView(headerTable);
		
		JPanel bottomPanel = new JPanel();
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
		getHeaderTableData("");
	}
	
	private void getHeaderTableData(String transferNo){
		String sql = "select osh.TRANSFER_ORDER_NO 运单号,osd.shipment_no 出库单号,osd.shipment_line_no 行号,bs.storer_name 货主"
				+",case osh.status when '100' then '新建' when '200' then '库存分配完成' when '300' then '拣货中' when '400' then '分拣中' when '500' then '包装中' when '600' then '包装完成' when '700' then '出库复核中' when '800' then '出库复核完成' when '900' then '已出库交接' end 状态"
				+",bi.item_bar_code 条码,osd.req_qty 订单数量,osd.ALLOCATED_QTY 分配数量,osd.PICKED_QTY 拣货数量,osd.OQC_QTY 复核数量,biu.unit_name 单位,bi.item_name 商品名称,osd.is_gift 是否礼品,osd.price 价格,osd.item_retail_price 零售价格,osd.tax_rate 税率,osd.tax 税额,osd.created_dtm_loc WMS创建时间,osd.created_by_user WMS创建用户 "
				+"from oub_shipment_detail osd "
				+"inner join oub_shipment_header osh on osd.shipment_no=osh.shipment_no "
				+"left outer join bas_item bi on bi.storer_code=osh.storer_code and bi.item_code=osd.item_code "
				+"left outer join bas_item_unit biu on bi.unit_code=biu.unit_code "
				+"inner join bas_storer bs on osh.storer_code=bs.storer_code "
				+"where osh.TRANSFER_ORDER_NO='"+transferNo+"' and osh.WAREHOUSE_CODE='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' ";
		sql = sql+" order by osd.shipment_no,osd.shipment_line_no ";
		DataManager dm = DBOperator.DoSelect2DM(sql);
		if (headerTable.getColumnCount() == 0) {
			headerTable.setColumn(dm.getCols());
		}
		headerTable.removeRowAll();
		headerTable.setData(dm.getDataStrings());
		headerTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		headerTable.setColumnEditableAll(false);
		JTableUtil.fitTableColumns(headerTable);
//		headerTable.setSortEnable();
		headerTable.setColumnSelectionAllowed(true);
		headerTable.updateUI();
		tableRowColorSetup(headerTable);
	}
	
	private void autoScan(){
		String transferNo = txt_transfer_no.getText().trim();
		if(!transferNo.equals("")){
			getHeaderTableData(transferNo);
			if(headerTable.getRowCount()==0){
				Message.showWarningMessage("运单号："+transferNo+"不存在！");
				txt_transfer_no.setText("");
				txt_transfer_no.selectAll();
				txt_transfer_no.requestFocus();
				return;
			}
			//跳过条码扫描
			if(cbIgnoreScanBarcode.isSelected()){
				for(int i=0;i<headerTable.getRowCount();i++){
					System.out.println(headerTable.getValueAt(i, headerTable.getColumnModel().getColumnIndex("分配数量")));
					headerTable.setValueAt(headerTable.getValueAt(i, headerTable.getColumnModel().getColumnIndex("分配数量")), i, headerTable.getColumnModel().getColumnIndex("拣货数量"));
					headerTable.setValueAt(headerTable.getValueAt(i, headerTable.getColumnModel().getColumnIndex("分配数量")), i, headerTable.getColumnModel().getColumnIndex("复核数量"));
				}
				btnOK.doClick();
			}else{
				txt_item_bar_code.selectAll();
				txt_item_bar_code.requestFocus();
			}
		}else{
			txt_transfer_no.requestFocus();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void tableRowColorSetup(PBSUIBaseGrid tab){
		Vector cellColor = new Vector();
		for(int i=0;i<tab.getRowCount();i++){
			Vector rowColor = new Vector();
			String totalQty = (String) tab.getValueAt(i, tab.getColumnModel().getColumnIndex("订单数量"));
			String oqcQty = (String) tab.getValueAt(i, tab.getColumnModel().getColumnIndex("复核数量"));
			Object[] rc1Cell = new Object[3];
	        rc1Cell[0] = new Integer(i);
	        rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("复核数量"));
	        rc1Cell[2] = Color.ORANGE;
	        cellColor.addElement(rc1Cell);
	        
	        Object[] rc1Cell2 = new Object[3];
	        rc1Cell2[0] = new Integer(i);
	        rc1Cell2[1] = new Integer(tab.getColumnModel().getColumnIndex("订单数量"));
	        rc1Cell2[2] = Color.lightGray;
	        cellColor.addElement(rc1Cell2);
//			if(Math_SAM.str2Double(totalQty)-Math_SAM.str2Double(oqcQty)>=0){
//				Object[] rc1Cell = new Object[3];
//		        rc1Cell[0] = new Integer(i);
//		        rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("复核数量"));
//		        rc1Cell[2] = Color.ORANGE;
//		        cellColor.addElement(rc1Cell);
//		        rowColor.addElement(new Integer(i));
//		        tab.setRowColor(rowColor, Color.lightGray);
//			}
		}
		tab.setCellColor(cellColor);
		
	}

	private void setExtendedState(int maximizedBoth) {
		// TODO Auto-generated method stub
		
	}

}
