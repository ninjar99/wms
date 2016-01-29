package outbound;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;

import DBUtil.DBOperator;
import comUtil.comData;
import dmdata.DataManager;
import dmdata.xArrayList;
import main.PBSUIBaseGrid;
import sys.InnerFrame;
import sys.JTableUtil;
import sys.MainFrm;
import sys.Message;
import sys.tableQueryDialog;
import util.Math_SAM;
import util.WaitingSplash;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.BoxLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TrackingNoScanFrm extends InnerFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8748627027059428869L;
	private JPanel contentPane;
	private static TrackingNoScanFrm instance;
	private static boolean isOpen = false;
	private JTextField txt_trackingNo;
	private JTextArea txt_trackingNoList;
	private PBSUIBaseGrid detailTable;
	private PBSUIBaseGrid headerTable;
	private JButton btnPrint;
	private JLabel lbl_total_qty;
	private JScrollPane scrollPane;
	private final static byte[] locks = new byte[0];
	
	public static synchronized TrackingNoScanFrm getInstance() {
		 if(instance == null) {    
	            instance = new TrackingNoScanFrm();  
	        }  
	        return instance;
		 
	 }
	
	public static synchronized boolean getOpenStatus() {
		 if(instance == null) {    
	            instance = new TrackingNoScanFrm();  
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
					TrackingNoScanFrm frame = new TrackingNoScanFrm();
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
	public TrackingNoScanFrm() {
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.addVetoableChangeListener(new VetoableChangeListener() {

			public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException {
				String name = e.getPropertyName();
				Object value = e.getNewValue();
				if (name.equals("closed") && value.equals(Boolean.TRUE)) // 窗口被关闭
				{
					System.out.println("TrackingNoScanFrm窗口被关闭");
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
		
		setBounds(100, 100, 890, 481);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel headerPanel = new JPanel();
		contentPane.add(headerPanel, BorderLayout.NORTH);
		headerPanel.setLayout(new GridLayout(2, 1, 0, 0));
		
		JPanel queryPane2 = new JPanel();
		FlowLayout fl_queryPane2 = (FlowLayout) queryPane2.getLayout();
		fl_queryPane2.setAlignment(FlowLayout.LEFT);
		headerPanel.add(queryPane2);
		
		JButton btnClear = new JButton("\u91CD\u7F6E");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txt_trackingNoList.setText("");
				lbl_total_qty.setText("0");
			}
		});
		queryPane2.add(btnClear);
		
		JLabel lblNewLabel = new JLabel("\u5FEB\u9012\u5355\u53F7\uFF1A");
		queryPane2.add(lblNewLabel);
		
		txt_trackingNo = new JTextField();
		txt_trackingNo.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				String trackingno = txt_trackingNo.getText().trim().replaceAll("\n", "");
				if(!trackingno.equals("")){
					if(txt_trackingNoList.getText().indexOf(trackingno)<0){
						txt_trackingNoList.setText(txt_trackingNoList.getText().trim()+(txt_trackingNoList.getText().trim().equals("")?"":";")+trackingno);
						txt_trackingNo.setText("");
						txt_trackingNo.setToolTipText("");
						lbl_total_qty.setText(String.valueOf(Math_SAM.str2Double(lbl_total_qty.getText())+1));
					}else{
						txt_trackingNo.selectAll();
						txt_trackingNo.requestFocus();
						txt_trackingNo.setToolTipText("单号重复");
					}
				}
			}
		});
		txt_trackingNo.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		txt_trackingNo.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyChar() == '\n') {
					String trackingno = txt_trackingNo.getText().trim();
					if(!trackingno.equals("")){
						if(txt_trackingNoList.getText().indexOf(trackingno)<0){
							txt_trackingNoList.setText(txt_trackingNoList.getText().trim()+(txt_trackingNoList.getText().trim().equals("")?"":";")+trackingno);
							txt_trackingNo.setText("");
							txt_trackingNo.setToolTipText("");
							lbl_total_qty.setText(String.valueOf(Math_SAM.str2Double(lbl_total_qty.getText())+1));
						}else{
							txt_trackingNo.selectAll();
							txt_trackingNo.requestFocus();
							txt_trackingNo.setToolTipText("单号重复");
						}
					}
				}
			}
		});
		queryPane2.add(txt_trackingNo);
		txt_trackingNo.setColumns(20);
		
		JButton btn_generate_shipment = new JButton("\u8BA2\u5355\u5BFC\u5165");
		btn_generate_shipment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String userCode = MainFrm.getUserInfo().getString("USER_CODE", 0);
				String userSelectWarehouse = MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0);
				String trackingNoList = txt_trackingNoList.getText();
				String[] trackingNoLists = trackingNoList.split(";");
				StringBuffer instr = new StringBuffer(" (");
				if(trackingNoList.trim().equals("")){
					return;
				}
				for(int i=0;i<trackingNoLists.length;i++){
					instr.append("'"+trackingNoLists[i].toString()+"'");
					if(i==trackingNoLists.length-1){
						instr.append(")");
					}else{
						instr.append(",");
					}
				}
				//oub_shipment_header 表头
				String sql = "insert into oub_shipment_header(storer_code,shipment_no,external_order_no,erp_order_no,warehouse_code,create_order_date,"
						+ "TRANSFER_ORDER_NO,ship_to_name,ship_to_contact,ship_to_contact_idcard,"
						+ "ship_to_country,ship_to_province_code,ship_to_city_code,ship_to_region_code,ship_to_street_code,ship_to_address1,ship_to_cell,"
						+ "ship_to_tel,ship_to_email,created_dtm_loc,created_by_user) "
						
						+ "select distinct o.supplier_id,o.order_sn,o.external_order,o.external_order,'"+userSelectWarehouse+"',o.created_date,o.invoice_no,o.consignee_name,o.consignee_name,o.consignee_idcard "
						+ ",o.consignee_country,o.consignee_province,o.consignee_city,o.consignee_district,'',o.consignee_address,o.consignee_mobile,o.consignee_mobile,o.consignee_email,"
						+"now(),'"+userCode+"' "
						+ "from sandwich.ag_order o "
						+ "inner join sandwich.ag_order_commodity od on o.order_id=od.order_id "
						+ "where o.invoice_no in "+instr.toString()
						+" and not exists(select shipment_no from oub_shipment_header osh where osh.shipment_no=o.order_sn) "
						
						+"union all "
						
						+"select distinct eoi.shop_no,eoi.order_sn,eoi.external_order,eoi.external_order,'"+userSelectWarehouse+"',FROM_UNIXTIME(eoi.add_time,'%Y-%m-%d %H:%i:%S'),eoi.invoice_no,eoi.consignee,eoi.consignee,eu.identity "
						+",'',er.region_name,er2.region_name,er3.region_name,'',eoi.address,eoi.mobile,eoi.tel,eoi.email,now(),'"+userCode+"' "
						+"from namacity.ecs_order_info eoi "
						+"inner join namacity.ecs_order_goods eog on eoi.order_id=eog.order_id "
						+"left outer join namacity.ecs_users eu on eu.user_id=eoi.user_id "
						+"left outer join namacity.ecs_region er on eoi.province=er.region_id "
						+"left outer join namacity.ecs_region er2 on eoi.city=er2.region_id "
						+"left outer join namacity.ecs_region er3 on eoi.district=er3.region_id "
						+"where eoi.invoice_no in "+instr.toString()
						+" and not exists(select shipment_no from oub_shipment_header osh where osh.shipment_no=eoi.order_sn) ";
				int t = DBOperator.DoUpdate(sql);
				if(t>0){
					//oub_shipment_detail 订单明细
					sql = "insert into oub_shipment_detail(shipment_no,shipment_line_no,status,create_order_date,erp_order_no,erp_order_line_no,warehouse_code,storer_code"
							+",item_code,req_qty,is_gift,price,item_retail_price,item_net_price,tax_rate,tax,created_dtm_loc,created_by_user,USER_DEF8) "
							
							+"select o.order_sn,(@rowNum:=@rowNum+1) as rowNo,'100',o.created_date,o.external_order,(@rowNum:=@rowNum) as rowNo,'"+userSelectWarehouse+"',o.supplier_id "
							+",p.part_number,od.commodity_amount,od.is_gift,od.market_price,od.market_price,od.unit_price,od.tax_rate,od.tax,now(),'"+userCode+"',o.order_id "
							+"from sandwich.ag_order o ,(Select (@rowNum :=0) ) r  "
							+",sandwich.ag_order_commodity od ,sandwich.ag_product p "
							+"where o.order_id=od.order_id and p.product_id=od.product_id and o.invoice_no in "+instr.toString()
							+" and not exists(select shipment_no from oub_shipment_detail osd where osd.shipment_no=o.order_sn) "
							
							+" union all "
							
							+"select eoi.order_sn,@rowNum := if(@orderID = eog.order_id, @rowNum + 1, 1) as rowNo,'100',FROM_UNIXTIME(eoi.add_time,'%Y-%m-%d %H:%i:%S'),eoi.external_order,(@rowNum:=@rowNum) as rowNo2,'"+userSelectWarehouse+"',eoi.shop_no"
							+",eg.MATRRIAL_NUM,eog.goods_number,eog.is_gift,eog.goods_price,eog.market_price,eog.goods_price,eog.tax_rate,eog.tax,now(),'"+userCode+"',@orderID := eog.order_id as dummy "
							+"from namacity.ecs_order_info eoi, namacity.ecs_order_goods eog,namacity.ecs_goods eg,(Select @rowNum :=0,@orderID :='' ) r "
							+"where eoi.order_id=eog.order_id and eog.goods_id=eg.goods_id "
							+"and eoi.invoice_no in "+instr.toString()
							+" and not exists(select shipment_no from oub_shipment_detail osd where osd.shipment_no=eoi.order_sn) ";
					t = DBOperator.DoUpdate(sql);
					if(t>0){
//						Message.showInfomationMessage("操作成功！");
						
						//namacity的货主转换成WMS的货主
						sql = "update oub_shipment_header osh inner join bas_storer bs on osh.storer_code=bs.EXTERN_STORER_CODE "
								+"set osh.STORER_CODE=case when bs.STORER_CODE is not null then bs.STORER_CODE else osh.STORER_CODE end "
								+"where osh.TRANSFER_ORDER_NO in "+instr.toString();
						t = DBOperator.DoUpdate(sql);
						sql = "update oub_shipment_detail osd inner join bas_storer bs on osd.storer_code=bs.EXTERN_STORER_CODE "
								+"inner join oub_shipment_header osh on osh.SHIPMENT_NO=osd.SHIPMENT_NO "
								+"set osd.STORER_CODE=case when bs.STORER_CODE is not null then bs.STORER_CODE else osd.STORER_CODE end "
								+"where osh.TRANSFER_ORDER_NO in " +instr.toString();
						t = DBOperator.DoUpdate(sql);
						showData(instr.toString());
						//记录操作日志
						DataManager dmProcess = comData.getSysProcessHistoryDataModel("sys_process_history");
						if (dmProcess!=null) {
							dmdata.xArrayList list = (xArrayList) dmProcess.getRow(0);
							list.set(dmProcess.getCol("SYS_PROCESS_HISTORY_ID"), "null");
							list.set(dmProcess.getCol("PROCESS_CODE"), "ShipmentImport");
							list.set(dmProcess.getCol("PROCESS_NAME"), "订单导入");
							list.set(dmProcess.getCol("STORER_CODE"), "");
							list.set(dmProcess.getCol("WAREHOUSE_CODE"), MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0));
							list.set(dmProcess.getCol("FROM_LOCATION_CODE"), "");
							list.set(dmProcess.getCol("FROM_CONTAINER_CODE"), "");
							list.set(dmProcess.getCol("QTY"), "");
							list.set(dmProcess.getCol("REFERENCE_NO"), "");
							list.set(dmProcess.getCol("REFERENCE_LINE_NO"), "");
							list.set(dmProcess.getCol("REFERENCE_TYPE"), "");
							list.set(dmProcess.getCol("LOT_NO"), "");
							list.set(dmProcess.getCol("MESSAGE"), trackingNoList);
							list.set(dmProcess.getCol("PROCESS_TIME"), "now()");
							list.set(dmProcess.getCol("CREATED_BY_USER"),MainFrm.getUserInfo().getString("USER_CODE", 0) );
							list.set(dmProcess.getCol("CREATED_DTM_LOC"), "now()");
							list.set(dmProcess.getCol("UPDATED_DTM_LOC"), "now()");
							dmProcess.RemoveRow(0);
							dmProcess.AddNewRow(list);
							boolean bool = comData.addSysProcessHistory("sys_process_history", dmProcess);
							System.out.println("写入操作日志："+bool);
						}
					}else{
//						Message.showErrorMessage("创建出库单表明细失败，可能订单已经导入！");
						showData(instr.toString());
//						headerTable.setSortEnable();
//						headerTable.setSortable(false);
						headerTable.updateUI();
						return;
					}
				}else{
//					Message.showErrorMessage("创建出库单表头失败，可能订单已经导入！");
					showData(instr.toString());
//					headerTable.setSortEnable();
					headerTable.updateUI();
					return;
				}
				btnClear.doClick();
				
			}
		});
		queryPane2.add(btn_generate_shipment);
		
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
		
		JButton btn_wave = new JButton("\u6CE2\u6B21\u5E93\u5B58\u5206\u914D");
		btn_wave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(headerTable.getRowCount()==0){
					Message.showWarningMessage("无订单信息，不能产生波次！");
					return;
				}
				StringBuffer sbuf = new StringBuffer();
				for(int i=0;i<headerTable.getRowCount();i++){
					String status = headerTable.getValueAt(i, headerTable.getColumnModel().getColumnIndex("状态")).toString();
					String shipmentWaveNo = headerTable.getValueAt(i, headerTable.getColumnModel().getColumnIndex("波次号")).toString();
					String shipmentNo = headerTable.getValueAt(i, headerTable.getColumnModel().getColumnIndex("出库单号")).toString();
					if(!status.equals("新建") && !status.equals("库存分配中")){
						sbuf.append("订单："+shipmentNo+" 已经分配库存，系统忽略此行数据\n");
					}
				}
				if(!sbuf.toString().equals("")){
					Message.showWarningMessage(sbuf.toString());
//					return;
				}
				new SwingWorker<String, Void>() {
					WaitingSplash splash = new WaitingSplash();

		            @Override
		            protected String doInBackground() throws Exception {
		            	//分配库存
		            	btn_wave.setEnabled(false);
		            	headerTable.setEnabled(false);
//		            	headerTable.setColumnEditableAll(false);
//		            	headerTable.getTableHeader().setVisible(false);
		        		headerTable.setSortable(false);
		        		headerTable.getTableHeader().setReorderingAllowed(false); //不可整列移动 
		        		headerTable.updateUI();
		            	splash.start(); // 运行启动界面
		            	try{
		            	invAllocate();
		            	}catch(Exception e){
		            		Message.showWarningMessage(e.getMessage());
		            	}
		                return "";
		            }

		            @Override
		            protected void done() {
		            	splash.stop(); // 运行启动界面
		                System.out.println("分配库存结束");
						JOptionPane.showMessageDialog(null, "分配库存完成");
						btn_wave.setEnabled(true);
						headerTable.setEnabled(true);
//						headerTable.setSortEnable();
						headerTable.getTableHeader().setReorderingAllowed(true); //不可整列移动 
//						headerTable.getTableHeader().setVisible(true);
		            }
		        }.execute();
			}
		});
		queryPane2.add(btn_wave);
		
		btnPrint = new JButton("\u6253\u5370\u62E3\u8D27\u5355");
		btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String sql = "select distinct '' 序号,opd.FROM_LOCATION_CODE 库位,opd.FROM_CONTAINER_CODE 箱号,bi.ITEM_BAR_CODE 条码,bi.ITEM_NAME 商品名称,sum(opd.PICKED_QTY) 数量,biu.unit_name 单位 "
						+" from oub_pick_detail opd "
						+"inner join oub_shipment_header osh on osh.SHIPMENT_NO=opd.SHIPMENT_NO and osh.STORER_CODE=opd.STORER_CODE "
						+"inner join bas_item bi on opd.ITEM_CODE=bi.ITEM_CODE and opd.STORER_CODE=bi.STORER_CODE "
						+"inner join bas_item_unit biu on bi.unit_code=biu.unit_code "
						+"where 1=1 and opd.PICKED_QTY>0 and opd.`STATUS`<>'999' "
						+" ";
				StringBuffer sb = new StringBuffer();
				for(int i=0;i<headerTable.getRowCount();i++){
					String shipmentWarehouseCode = headerTable.getValueAt(i, headerTable.getColumnModel().getColumnIndex("仓库编码")).toString();
					String shipmentNo = headerTable.getValueAt(i, headerTable.getColumnModel().getColumnIndex("出库单号")).toString();
					String shipmentWaveNo = headerTable.getValueAt(i, headerTable.getColumnModel().getColumnIndex("波次号")).toString();
					//如果订单已经产生波次号，忽略此行，循环到下一行
					if(shipmentWaveNo.trim().equals("")){
						if(sb.toString().trim().length()>1){
							if(i==headerTable.getRowCount()-1){
								sb.append(") ");
							}
						}
						continue;
					}
					if(sb.toString().equals("")){
						sb.append(" and osh.shipment_no in('"+shipmentNo+"'");
					}else{
						sb.append(",'"+shipmentNo+"'");
					}
					if(i==headerTable.getRowCount()-1){
						sb.append(") ");
					}
				}
				if(sb.toString().trim().length()>1){
					sql = sql + sb.toString();
				}
				sql = sql + " group by opd.FROM_LOCATION_CODE,opd.FROM_CONTAINER_CODE,bi.ITEM_BAR_CODE,bi.ITEM_NAME,biu.unit_name "
						+" order by opd.FROM_LOCATION_CODE ";
				if(sb.toString().trim().equals("")){
					return;
				}
				PickListPrintFrm printFrm = new PickListPrintFrm(sql);
				Toolkit toolkit = Toolkit.getDefaultToolkit();
				int x = (int)(toolkit.getScreenSize().getWidth()-printFrm.getWidth())/2;
				int y = (int)(toolkit.getScreenSize().getHeight()-printFrm.getHeight())/2;
				printFrm.setResizable(true);
				printFrm.setMaximumSize(new Dimension((int)toolkit.getScreenSize().getWidth(),(int)toolkit.getScreenSize().getHeight()));
				printFrm.setMinimumSize(new Dimension((int)toolkit.getScreenSize().getWidth(),(int)toolkit.getScreenSize().getHeight()));
				printFrm.setLocation(0, 0);
				printFrm.setModal(true);
				printFrm.setVisible(true);
			}
		});
		queryPane2.add(btnPrint);
		queryPane2.add(btnClose);
		
		JPanel panel_2 = new JPanel();
		headerPanel.add(panel_2);
		panel_2.setLayout(new GridLayout(1, 1, 0, 0));
		
		txt_trackingNoList = new JTextArea();
		txt_trackingNoList.setEditable(false);
		txt_trackingNoList.setWrapStyleWord(true);
		txt_trackingNoList.setLineWrap(true);
		txt_trackingNoList.setForeground(Color.BLUE);
		txt_trackingNoList.setBackground(Color.LIGHT_GRAY);
		txt_trackingNoList.setFont(new Font("微软雅黑", Font.BOLD, 20));
		txt_trackingNoList.setColumns(1000);
		panel_2.add(txt_trackingNoList);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(2, 1, 0, 0));
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		panel_1.add(scrollPane, BorderLayout.CENTER);
		
		headerTable = new PBSUIBaseGrid();
		headerTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()>=2){
					headerTable.setColumnSelectionAllowed(true);
				}
				int r= headerTable.getSelectedRow();
		        if(headerTable.getRowCount()>0){
			        String shipmentNo = headerTable.getValueAt(r, headerTable.getColumnModel().getColumnIndex("出库单号")).toString();
			        getDetailTableData(shipmentNo);
		        }
			}
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					JPopupMenu popupmenu = new JPopupMenu();
					JMenuItem menuItem1 = new JMenuItem();
					menuItem1.setLabel("波次库存分配回滚");
					menuItem1.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(ActionEvent e) {
							boolean b_confirm = Message.showOKorCancelMessage("是否库存分配回滚所选行?");
							if(b_confirm){
								String waveNo = (String) headerTable.getValueAt(headerTable.getSelectedRow(), headerTable.getColumnModel().getColumnIndex("波次号"));
								String shipmentNo = (String) headerTable.getValueAt(headerTable.getSelectedRow(), headerTable.getColumnModel().getColumnIndex("出库单号"));
								String shipmentStatus = (String) headerTable.getValueAt(headerTable.getSelectedRow(), headerTable.getColumnModel().getColumnIndex("状态"));
								String sql = " ";
								if(shipmentStatus.equalsIgnoreCase("库存分配完成")){
									String ret = WaveCancel.waveCancelNew(waveNo, shipmentNo);
									if(ret.substring(0, 3).equalsIgnoreCase("ERR")){
										Message.showErrorMessage(ret);
									}else{
										Message.showInfomationMessage(ret);
										headerTable.setValueAt("新建", headerTable.getSelectedRow(), headerTable.getColumnModel().getColumnIndex("状态"));
										headerTable.setValueAt("", headerTable.getSelectedRow(), headerTable.getColumnModel().getColumnIndex("波次号"));
										headerTable.updateUI();
									}
								}else{
									Message.showWarningMessage("订单只有是【库存分配完成】状态才能做库存分配回滚");
								}
							}
						}
						});
					popupmenu.add(menuItem1);
					popupmenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
		scrollPane.setViewportView(headerTable);
		
		JPanel panel_3 = new JPanel();
		panel.add(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		panel_3.add(scrollPane_1, BorderLayout.CENTER);
		
		detailTable = new PBSUIBaseGrid();
		detailTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()>=2){
					detailTable.setColumnSelectionAllowed(true);
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					String barcode = (String) detailTable.getValueAt(detailTable.getSelectedRow(), detailTable.getColumnModel().getColumnIndex("条码"));
					String status = (String) detailTable.getValueAt(detailTable.getSelectedRow(), detailTable.getColumnModel().getColumnIndex("订单状态"));
//					if(!barcode.equals("")){
//						return;
//					}
					if(!status.equals("100")){
						return;
					}
					JPopupMenu popupmenu = new JPopupMenu();
					JMenuItem menuItem1 = new JMenuItem();
					menuItem1.setLabel("修改订单商品编码");
					menuItem1.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(ActionEvent e) {
							boolean b_confirm = Message.showOKorCancelMessage("是否确认修改订单商品编码?\n");
							if(b_confirm){
								String storerName = (String) detailTable.getValueAt(detailTable.getSelectedRow(), detailTable.getColumnModel().getColumnIndex("货主"));
								String sql = "select item.STORER_CODE 货主编码,bs.STORER_NAME 货主名称,item.ITEM_CODE 货品编码,item.ITEM_NAME 货品名称,item.ITEM_BAR_CODE 货品条码,biu.unit_name 单位  "
										+ "from bas_item item " + "inner join bas_storer bs on item.STORER_CODE=bs.STORER_CODE "
										+ "inner join bas_item_unit biu on biu.unit_code=item.UNIT_CODE " 
										+ "where bs.STORER_NAME ='"+storerName+"' ";
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
									String lineNo = (String) detailTable.getValueAt(detailTable.getSelectedRow(), detailTable.getColumnModel().getColumnIndex("行号"));
									String oldItemCode = (String) detailTable.getValueAt(detailTable.getSelectedRow(), detailTable.getColumnModel().getColumnIndex("商品编码"));
									String shipmentNo = (String) headerTable.getValueAt(headerTable.getSelectedRow(), headerTable.getColumnModel().getColumnIndex("出库单号"));
									sql = "update oub_shipment_detail set ITEM_CODE='"+dm.getString("货品编码", 0)+"',UPDATED_DTM_LOC=now(),UPDATED_BY_USER='"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"' "
											+ "where SHIPMENT_NO = '"+shipmentNo+"' and SHIPMENT_LINE_NO="+lineNo +" and ITEM_CODE='"+oldItemCode+"'";
									int t = DBOperator.DoUpdate(sql);
									if(t==1){
										detailTable.setValueAt(dm.getString("货品编码", 0), detailTable.getSelectedRow(), detailTable.getColumnModel().getColumnIndex("商品编码"));
										detailTable.setValueAt(dm.getString("货品条码", 0), detailTable.getSelectedRow(), detailTable.getColumnModel().getColumnIndex("条码"));
										detailTable.setValueAt(dm.getString("货品名称", 0), detailTable.getSelectedRow(), detailTable.getColumnModel().getColumnIndex("商品名称"));
										detailTable.setValueAt(dm.getString("单位", 0), detailTable.getSelectedRow(), detailTable.getColumnModel().getColumnIndex("单位"));
										Message.showInfomationMessage("操作成功!");
										//记录操作日志
										DataManager dmProcess = comData.getSysProcessHistoryDataModel("sys_process_history");
										if (dmProcess!=null) {
											dmdata.xArrayList list = (xArrayList) dmProcess.getRow(0);
											list.set(dmProcess.getCol("SYS_PROCESS_HISTORY_ID"), "null");
											list.set(dmProcess.getCol("PROCESS_CODE"), "SHIPMENT_UPDATE");
											list.set(dmProcess.getCol("PROCESS_NAME"), "订单明细修改_商品编码");
											list.set(dmProcess.getCol("STORER_CODE"), "");
											list.set(dmProcess.getCol("WAREHOUSE_CODE"), MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0));
											list.set(dmProcess.getCol("FROM_LOCATION_CODE"), "");
											list.set(dmProcess.getCol("FROM_CONTAINER_CODE"), "");
											list.set(dmProcess.getCol("QTY"), "");
											list.set(dmProcess.getCol("REFERENCE_NO"), shipmentNo);
											list.set(dmProcess.getCol("REFERENCE_LINE_NO"), lineNo);
											list.set(dmProcess.getCol("REFERENCE_TYPE"), "");
											list.set(dmProcess.getCol("LOT_NO"), "");
											list.set(dmProcess.getCol("PROCESS_TIME"), "now()");
											list.set(dmProcess.getCol("CREATED_BY_USER"),MainFrm.getUserInfo().getString("USER_CODE", 0) );
											list.set(dmProcess.getCol("CREATED_DTM_LOC"), "now()");
											list.set(dmProcess.getCol("UPDATED_DTM_LOC"), "now()");
											dmProcess.RemoveRow(0);
											dmProcess.AddNewRow(list);
											boolean bool = comData.addSysProcessHistory("sys_process_history", dmProcess);
											System.out.println("写入操作日志："+bool);
										}
									}else{
										Message.showErrorMessage("操作失败!");
									}
								}
							}
						}
						});
					popupmenu.add(menuItem1);
					popupmenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
		scrollPane_1.setViewportView(detailTable);
		showData("(0)");
		txt_trackingNo.setFocusable(true);
		
		lbl_total_qty = new JLabel("0");
		lbl_total_qty.setForeground(Color.RED);
		lbl_total_qty.setFont(new Font("宋体", Font.BOLD, 18));
		queryPane2.add(lbl_total_qty);
		txt_trackingNo.requestFocus();
	}
	
	private void showData(String instr){
		String sql = "select bs.storer_name 货主,osh.shipment_no 出库单号,osh.external_order_no 外部订单号,osh.warehouse_code 仓库编码,bw.warehouse_name 仓库名称,osh.create_order_date 订单创建时间,"
				+"case osh.status when '100' then '新建' when '150' then '库存分配中' when '190' then '部分分配' when '200' then '库存分配完成' when '300' then '拣货中' when '400' then '分拣中' when '500' then '包装中' when '600' then '包装完成' when '700' then '出库复核中' when '800' then '出库复核完成' when '900' then '已出库交接' else osh.status end 状态,"
				+ "osh.TRANSFER_ORDER_NO 运单号,osh.wave_no 波次号,osh.ship_to_name 收货人,osh.ship_to_contact 收货联系人,osh.ship_to_contact_idcard 收货人身份证号,"
				+ "osh.ship_to_country 国家,osh.ship_to_province_code 省,osh.ship_to_city_code 市,osh.ship_to_region_code 区,osh.ship_to_street_code 街道,osh.ship_to_address1 详细地址,osh.ship_to_cell 电话1,"
				+ "osh.ship_to_tel 电话2,osh.ship_to_email 电子邮箱,osh.created_dtm_loc WMS创建时间,osh.created_by_user WMS创建用户 "
				+ " from oub_shipment_header osh "
				+" inner join bas_storer bs on osh.storer_code=bs.storer_code "
				+" inner join bas_warehouse bw on osh.warehouse_code=bw.warehouse_code ";
		if(!instr.equals("")){
			sql = sql + " where osh.TRANSFER_ORDER_NO in "+instr;
		}
		sql = sql + " order by osh.shipment_no ";
		DataManager dm = DBOperator.DoSelect2DM(sql);
		if(dm.getCurrentCount()==0){
			detailTable.removeRowAll();
		}
		if (headerTable.getColumnCount() == 0) {
			headerTable.setColumn(dm.getCols());
		}
		headerTable.removeRowAll();
		headerTable.setData(dm.getDataStrings());
		headerTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		headerTable.setColumnEditableAll(false);
		JTableUtil.fitTableColumnsDoubleWidth(headerTable);
//		headerTable.setSortEnable();
//		headerTable.setSortable(false);
//		headerTable.updateUI();
		if(headerTable.getRowCount()>0){
			headerTable.setRowSelectionInterval(0, 0);//默认选中第一行
		}
		headerTableClick();
	}
	
	private void refreshShowData(){
		String trackingNoList = txt_trackingNoList.getText();
		String[] trackingNoLists = trackingNoList.split(";");
		StringBuffer instr = new StringBuffer(" (");
		if(trackingNoList.trim().equals("")){
			return;
		}
		for(int k=0;k<trackingNoLists.length;k++){
			instr.append("'"+trackingNoLists[k].toString()+"'");
			if(k==trackingNoLists.length-1){
				instr.append(")");
			}else{
				instr.append(",");
			}
		}
		showData(instr.toString());
	}
	
	private void headerTableClick(){
		int r= headerTable.getSelectedRow();
        if(headerTable.getRowCount()>0){
        	String shipmentNo = headerTable.getValueAt(r, headerTable.getColumnModel().getColumnIndex("出库单号")).toString();
	        getDetailTableData(shipmentNo);
        }
	}
	
	private void getDetailTableData(String shipmentNo){
		String sql = "select osd.shipment_line_no 行号,osd.status 订单状态,osd.create_order_date 订单创建日期,bw.warehouse_name 仓库,bs.storer_name 货主"
				+",osd.item_code 商品编码,bi.item_bar_code 条码,bi.item_name 商品名称,osd.req_qty 订单数量,osd.ALLOCATED_QTY 已分配数,biu.unit_name 单位,osd.is_gift 是否礼品,osd.price 价格,osd.item_retail_price 零售价格,osd.tax_rate 税率,osd.tax 税额,osd.created_dtm_loc WMS创建时间,osd.created_by_user WMS创建用户 "
				+"from oub_shipment_detail osd "
				+"inner join oub_shipment_header osh on osd.shipment_no=osh.shipment_no "
				+"left outer join bas_item bi on bi.storer_code=osh.storer_code and bi.item_code=osd.item_code "
				+"left outer join bas_item_unit biu on bi.unit_code=biu.unit_code "
				+"left outer join bas_warehouse bw on osd.warehouse_code=bw.warehouse_code "
				+"left outer join bas_storer bs on osd.storer_code=bs.storer_code "
				+"where osh.shipment_no='"+shipmentNo+"'";
		sql = sql+" order by osd.shipment_no,osd.shipment_line_no ";
		DataManager dm = DBOperator.DoSelect2DM(sql);
		if (detailTable.getColumnCount() == 0) {
			detailTable.setColumn(dm.getCols());
		}
		detailTable.removeRowAll();
		detailTable.setData(dm.getDataStrings());
		detailTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		detailTable.setColumnEditableAll(false);
		JTableUtil.fitTableColumnsDoubleWidth(detailTable);
//		detailTable.setSortEnable();
//		detailTable.updateUI();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void tableRowColorSetup(PBSUIBaseGrid tab){
		Vector cellColor = new Vector();
		for(int i=0;i<tab.getRowCount();i++){
			Vector rowColor = new Vector();
			String waveNo = (String) tab.getValueAt(i, tab.getColumnModel().getColumnIndex("波次号"));
//			String allocatedQty = (String) tab.getValueAt(i, tab.getColumnModel().getColumnIndex("分配数量"));
//			if(Math_SAM.str2Double(totalQty)-Math_SAM.str2Double(allocatedQty)==0){
			if(!waveNo.equals("")){
				Object[] rc1Cell = new Object[3];
		        rc1Cell[0] = new Integer(i);
		        rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("波次号"));
		        rc1Cell[2] = Color.ORANGE;
		        cellColor.addElement(rc1Cell);
		        rowColor.addElement(new Integer(i));
		        tab.setRowColor(rowColor, Color.lightGray);
			}
		}
		tab.setCellColor(cellColor);
		tab.updateUI();
	}
	
	private void invAllocate(){
		synchronized(locks){
		try{
		String waveNo = comData.getValueFromBasNumRule("oub_wave_header", "wave_no");
		String sql = "insert into oub_wave_header(WAVE_NO,WAVE_NAME,WAREHOUSE_CODE,CREATED_BY_USER,CREATED_DTM_LOC) "
				+"select '"+waveNo+"','Normal Wave','"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"','"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"',now()";
		int t = DBOperator.DoUpdate(sql);
		if(t==1){
			for(int i=0;i<headerTable.getRowCount();i++){
				String shipmentWarehouseCode = headerTable.getValueAt(i, headerTable.getColumnModel().getColumnIndex("仓库编码")).toString();
				String shipmentNo = headerTable.getValueAt(i, headerTable.getColumnModel().getColumnIndex("出库单号")).toString();
				String transportNo = headerTable.getValueAt(i, headerTable.getColumnModel().getColumnIndex("运单号")).toString();
				String shipmentWaveNo = headerTable.getValueAt(i, headerTable.getColumnModel().getColumnIndex("波次号")).toString();
				//如果订单已经产生波次号，忽略此行，循环到下一行
				if(!shipmentWaveNo.trim().equals("")){
					continue;
				}
				sql = "insert into oub_wave_detail(OUB_WAVE_HEADER_ID,WAVE_NO,WAREHOUSE_CODE,SHIPMENT_NO,CREATED_BY_USER,CREATED_DTM_LOC) "
						+"select (select OUB_WAVE_HEADER_ID from oub_wave_header where WAVE_NO='"+waveNo+"'),'"
						+waveNo+"','"+shipmentWarehouseCode+"','"+shipmentNo+"','"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"',now() ";
				t = DBOperator.DoUpdate(sql);
				if(t==1){
					//更新订单表头波次号
					sql = "update oub_shipment_header set status='150', wave_no='"+waveNo+"' where shipment_no='"+shipmentNo+"' ";
					DBOperator.DoUpdate(sql);
					headerTable.setValueAt(waveNo, i, headerTable.getColumnModel().getColumnIndex("波次号"));
					headerTable.setValueAt("库存分配中", i, headerTable.getColumnModel().getColumnIndex("状态"));
					JTableUtil.fitTableColumnsDoubleWidth(headerTable);
//					headerTable.updateUI();
				}else{
					Message.showWarningMessage("订单："+transportNo+" 创建波次明细表失败,系统自动忽略此订单");
					continue;
				}
				//开始分配库存
				String pickNo = comData.getValueFromBasNumRule("oub_pick_header", "pick_no");
				sql = "insert into oub_pick_header(WAREHOUSE_CODE,STORER_CODE,WAVE_NO,PICK_NO,OUB_SHIPMENT_HEADER_ID,SHIPMENT_NO,CREATED_BY_USER,CREATED_DTM_LOC) "
						+"select osh.WAREHOUSE_CODE,osh.STORER_CODE,'"+waveNo+"','"+pickNo+"',osh.OUB_SHIPMENT_HEADER_ID,osh.SHIPMENT_NO,'"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"',now() "
						+" from oub_shipment_header osh where osh.SHIPMENT_NO='"+shipmentNo+"' and not exists"
						+ "(select PICK_NO from oub_pick_header where SHIPMENT_NO='"+shipmentNo+"' and STATUS='100') ";
				t = DBOperator.DoUpdate(sql);
				if(t==0){
					Message.showWarningMessage("创建订单【"+shipmentNo+"】拣货表头失败,系统自动忽略此订单!");
					continue;
				}else{
					sql = "select WAREHOUSE_CODE,STORER_CODE,SHIPMENT_LINE_NO,ITEM_CODE,REQ_QTY,LOTTABLE01,LOTTABLE02"
							+",LOTTABLE03,LOTTABLE04,LOTTABLE05,LOTTABLE06,LOTTABLE07,LOTTABLE08,LOTTABLE09,LOTTABLE10 "
							+"from oub_shipment_detail where shipment_no='"+shipmentNo+"' order by SHIPMENT_LINE_NO ";
					DataManager dm = DBOperator.DoSelect2DM(sql);
					if(dm==null || dm.getCurrentCount()==0){
						
					}else{
						for(int k=0;k<dm.getCurrentCount();k++){
							shipmentWarehouseCode = dm.getString("WAREHOUSE_CODE", k);
							String shipmentStorerCode = dm.getString("STORER_CODE", k);
							String shipmentItemCode = dm.getString("ITEM_CODE", k);
							String shipmentLineNo = dm.getString("SHIPMENT_LINE_NO", k);
							String shipmemtReqQty = dm.getString("REQ_QTY", k);
							String shipmentLot01 = dm.getString("LOTTABLE01", k);
							String shipmentLot02 = dm.getString("LOTTABLE02", k);
							String shipmentLot03 = dm.getString("LOTTABLE03", k);
							String shipmentLot04 = dm.getString("LOTTABLE04", k);
							String shipmentLot05 = dm.getString("LOTTABLE05", k);
							String shipmentLot06 = dm.getString("LOTTABLE06", k);
							String shipmentLot07 = dm.getString("LOTTABLE07", k);
							String shipmentLot08 = dm.getString("LOTTABLE08", k);
							String shipmentLot09 = dm.getString("LOTTABLE09", k);
							String shipmentLot10 = dm.getString("LOTTABLE10", k);
							//获取分配的库存信息
							System.out.println("库存查找情况：");
							DataManager dmAllocated = getInventoryRowsByQtyASC(shipmentWarehouseCode,shipmentStorerCode,shipmentItemCode,shipmemtReqQty,
									shipmentLot01,shipmentLot02,shipmentLot03,shipmentLot04,shipmentLot05
									,shipmentLot06,shipmentLot07,shipmentLot08,shipmentLot09,shipmentLot10);
							//未找到库存
							if(dmAllocated==null || dmAllocated.getCurrentCount()==0){
								//查找该订单是否有其他明细行有 拣货记录， 如果有，修改订单表头status=190
								sql = "select shipment_no from oub_pick_detail where shipment_no='"+shipmentNo+"' ";
								DataManager dmtmp = DBOperator.DoSelect2DM(sql);
								if(dm==null || dm.getCurrentCount()==0){
									continue;
								}else{
									sql = "update oub_shipment_header set status='190' where shipment_no='"+shipmentNo+"' and status='150' ";
									t = DBOperator.DoUpdate(sql);
								}
								
								
							}
							for(int r=0;r<dmAllocated.getCurrentCount();r++){
								double AllocatedQty = Math_SAM.str2Double(dmAllocated.getString("AllocatedQty", r));
								if(AllocatedQty<=0){
									continue;
								}
								if(dmAllocated.getString("INV_INVENTORY_ID", r).equals("")){
									continue;
								}
								sql = "insert into oub_pick_detail(OUB_PICK_HEADER_ID,WAREHOUSE_CODE,STORER_CODE,WAVE_NO,PICK_NO,OUB_SHIPMENT_HEADER_ID,SHIPMENT_NO,SHIPMENT_LINE_NO,"
										+"LOT_NO,ITEM_CODE,REQ_QTY,PICKED_QTY,FROM_LOCATION_CODE,FROM_CONTAINER_CODE,INV_INVENTORY_ID,CREATED_BY_USER,CREATED_DTM_LOC) "
										+"select (select OUB_PICK_HEADER_ID from oub_pick_header where pick_no='"+pickNo+"'),osd.WAREHOUSE_CODE,osd.STORER_CODE,osh.WAVE_NO,'"+pickNo+"',osh.OUB_SHIPMENT_HEADER_ID,osh.SHIPMENT_NO,osd.SHIPMENT_LINE_NO, "
										+"'"+dmAllocated.getString("LOT_NO", r)+"',osd.ITEM_CODE,osd.REQ_QTY,'"+(dmAllocated.getString("AllocatedQty", r).equals("")?"0":dmAllocated.getString("AllocatedQty", r))+"', "
										+"'"+dmAllocated.getString("LOCATION_CODE", r)+"','"+dmAllocated.getString("CONTAINER_CODE", r)+"','"+dmAllocated.getString("INV_INVENTORY_ID", r)+"', "
										+"'"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"',now() "
										+" from oub_shipment_header osh "
										+"inner join oub_shipment_detail osd on osh.SHIPMENT_NO=osd.SHIPMENT_NO "
										+"where osh.SHIPMENT_NO='"+shipmentNo+"' and osd.SHIPMENT_LINE_NO= "+shipmentLineNo+" "
										+ "and not exists(select PICK_NO from oub_pick_detail where SHIPMENT_NO='"+shipmentNo+"' "
										+ "and SHIPMENT_LINE_NO="+shipmentLineNo+" and STATUS='100')";
								t = DBOperator.DoUpdate(sql);
								if(t>0){
									sql = "update oub_shipment_detail osd set status='200',osd.ALLOCATED_QTY=ifnull((select sum(PICKED_QTY) from oub_pick_detail opd "
											 +"where opd.status<>'999' and opd.SHIPMENT_NO='"+shipmentNo+"' and opd.SHIPMENT_LINE_NO="+shipmentLineNo+" and opd.ITEM_CODE='"+shipmentItemCode+"'),0) "
											 +" where osd.SHIPMENT_NO='"+shipmentNo+"' and osd.SHIPMENT_LINE_NO="+shipmentLineNo+" and osd.ITEM_CODE='"+shipmentItemCode+"' ";
									t = DBOperator.DoUpdate(sql);
								}
								
							}
						}
					}
				}
			}
		}
		
		String messageStr = "";
		//检查订单库存分配情况
		for(int i=0;i<headerTable.getRowCount();i++){
			String shipmentWarehouseCode = headerTable.getValueAt(i, headerTable.getColumnModel().getColumnIndex("仓库编码")).toString();
			String transportNo = headerTable.getValueAt(i, headerTable.getColumnModel().getColumnIndex("运单号")).toString();
			String shipmentNo = headerTable.getValueAt(i, headerTable.getColumnModel().getColumnIndex("出库单号")).toString();
			sql = "select PICK_NO,SHIPMENT_LINE_NO from oub_pick_detail where SHIPMENT_NO='"+shipmentNo+"' and status <>'999' and WAREHOUSE_CODE='"+shipmentWarehouseCode+"' "
					+" and not exists(select shipment_no from oub_shipment_detail where shipment_no='"+shipmentNo+"' and status<'200') ";
			DataManager dm = DBOperator.DoSelect2DM(sql);
			if(dm==null || dm.getCurrentCount()==0){
				//分配失败，清除订单表头的波次号
				sql = "update oub_shipment_header set WAVE_NO='',status='100' where SHIPMENT_NO='"+shipmentNo+"' and WAREHOUSE_CODE='"+shipmentWarehouseCode+"' ";
				DBOperator.DoUpdate(sql);
				sql = "update oub_shipment_detail set status='100',ALLOCATED_QTY=0 where SHIPMENT_NO='"+shipmentNo+"' and WAREHOUSE_CODE='"+shipmentWarehouseCode+"' ";
				DBOperator.DoUpdate(sql);
				//分配失败，清除拣货单表头 + 明细
				sql = "delete from oub_pick_header where SHIPMENT_NO='"+shipmentNo+"' and WAREHOUSE_CODE='"+shipmentWarehouseCode+"' ";
				DBOperator.DoUpdate(sql);
				sql = "delete from oub_pick_detail where SHIPMENT_NO='"+shipmentNo+"' and WAREHOUSE_CODE='"+shipmentWarehouseCode+"' ";
				DBOperator.DoUpdate(sql);
				//分配失败，清除波次明细表的订单行信息
				sql = "delete from oub_wave_detail where SHIPMENT_NO='"+shipmentNo+"' and WAREHOUSE_CODE='"+shipmentWarehouseCode+"' ";
				DBOperator.DoUpdate(sql);
				//分配失败，清除波次表头（如果该波次号没有明细）
				sql = "delete from oub_wave_header where wave_no not in (select wave_no from oub_wave_detail where oub_wave_header.WAVE_NO=oub_wave_detail.WAVE_NO)";
				DBOperator.DoUpdate(sql);
				headerTable.setValueAt("", i, headerTable.getColumnModel().getColumnIndex("波次号"));
				headerTable.setValueAt("新建", i, headerTable.getColumnModel().getColumnIndex("状态"));
				messageStr = messageStr + "订单号："+transportNo+" 库存分配失败\n";
			}else{
				//分配成功，订单状态status='200'
				sql = "update oub_shipment_header set status='200' where SHIPMENT_NO='"+shipmentNo+"' and WAREHOUSE_CODE='"+shipmentWarehouseCode+"' ";
				DBOperator.DoUpdate(sql);
//				sql = "update oub_shipment_detail set status='200' where SHIPMENT_NO='"+shipmentNo+"' and WAREHOUSE_CODE='"+shipmentWarehouseCode+"' ";
//				DBOperator.DoUpdate(sql);
//				sql = "update inv_inventory ii inner join oub_pick_detail opd on ii.INV_INVENTORY_ID=opd.INV_INVENTORY_ID "
//						+"set ii.ALLOCATED_QTY=ii.ALLOCATED_QTY-(opd.PICKED_QTY-opd.REQ_QTY) "
//						+"where opd.PICKED_QTY>opd.REQ_QTY and opd.SHIPMENT_NO='"+shipmentNo+"' ";
//				DBOperator.DoUpdate(sql);
				sql = "update oub_pick_detail set USER_DEF1 = PICKED_QTY,PICKED_QTY=REQ_QTY where status<>'999' and PICKED_QTY>REQ_QTY and SHIPMENT_NO='"+shipmentNo+"' ";
				DBOperator.DoUpdate(sql);
				sql = "update oub_shipment_detail set USER_DEF1 =ALLOCATED_QTY,ALLOCATED_QTY=REQ_QTY where ALLOCATED_QTY>REQ_QTY and SHIPMENT_NO='"+shipmentNo+"' ";
				DBOperator.DoUpdate(sql);
				refreshShowData();
			}
		}
		if(!messageStr.equals("")){
			Message.showWarningMessage(messageStr);
		}else{
			//记录操作日志
			DataManager dmProcess = comData.getSysProcessHistoryDataModel("sys_process_history");
			if (dmProcess!=null) {
				dmdata.xArrayList list = (xArrayList) dmProcess.getRow(0);
				list.set(dmProcess.getCol("SYS_PROCESS_HISTORY_ID"), "null");
				list.set(dmProcess.getCol("PROCESS_CODE"), "WAVE");
				list.set(dmProcess.getCol("PROCESS_NAME"), "波次库存分配");
				list.set(dmProcess.getCol("STORER_CODE"), "");
				list.set(dmProcess.getCol("WAREHOUSE_CODE"), MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0));
				list.set(dmProcess.getCol("FROM_LOCATION_CODE"), "");
				list.set(dmProcess.getCol("FROM_CONTAINER_CODE"), "");
				list.set(dmProcess.getCol("QTY"), "");
				list.set(dmProcess.getCol("REFERENCE_NO"), waveNo);
				list.set(dmProcess.getCol("REFERENCE_LINE_NO"), "");
				list.set(dmProcess.getCol("REFERENCE_TYPE"), "");
				list.set(dmProcess.getCol("LOT_NO"), "");
				list.set(dmProcess.getCol("PROCESS_TIME"), "now()");
				list.set(dmProcess.getCol("CREATED_BY_USER"),MainFrm.getUserInfo().getString("USER_CODE", 0) );
				list.set(dmProcess.getCol("CREATED_DTM_LOC"), "now()");
				list.set(dmProcess.getCol("UPDATED_DTM_LOC"), "now()");
				dmProcess.RemoveRow(0);
				dmProcess.AddNewRow(list);
				boolean bool = comData.addSysProcessHistory("sys_process_history", dmProcess);
				System.out.println("写入操作日志："+bool);
			}
			Message.showInfomationMessage("订单库存分配完成，请打印拣货单进行拣货");
			btnPrint.requestFocus();
		}
		
		tableRowColorSetup(headerTable);//设置有波次号的行此列的背景颜色
		}catch(Exception e){
			Message.showWarningMessage(e.getMessage());
		}
		}//end synchronized locks
	}
	
	@SuppressWarnings("unused")
	private DataManager getInventoryRowsByQtyASC(String warehouseCode,String storerCode,String itemCode,String qty,
			String lot01,String lot02,String lot03,String lot04,String lot05,
			String lot06,String lot07,String lot08,String lot09,String lot10){
		String sql = "";
		String lotNo = "";
		double shipmentQty = Math_SAM.str2Double(qty);
		DataManager dm = new DataManager();
		if(lot01.equals("") && lot02.equals("") && lot03.equals("") && lot04.equals("") && lot05.equals("") && 
				lot06.equals("") && lot07.equals("") && lot08.equals("") && lot09.equals("") && lot10.equals("")){
			//不需要按照批次信息分配库存
			sql = "select ii.INV_INVENTORY_ID,ii.WAREHOUSE_CODE,ii.STORER_CODE,ii.ITEM_CODE,ii.INV_LOT_ID,ii.LOT_NO,ii.LOCATION_CODE,ii.CONTAINER_CODE,"
					+"ii.ON_HAND_QTY+(ii.IN_TRANSIT_QTY)-(ii.ALLOCATED_QTY)-(ii.PICKED_QTY)-(ii.INACTIVE_QTY) QTY,0 AllocatedQty "
					+" from inv_inventory ii "
					+" inner join bas_location bl on ii.WAREHOUSE_CODE=bl.WAREHOUSE_CODE and ii.LOCATION_CODE=bl.LOCATION_CODE "
					+" where ii.WAREHOUSE_CODE='"+warehouseCode+"' and ii.STORER_CODE='"+storerCode+"' and ii.ITEM_CODE='"+itemCode+"' "
					+" and bl.LOCATION_TYPE_CODE not in ('Dock','Damage') "
					+" and ii.ON_HAND_QTY+(ii.IN_TRANSIT_QTY)-(ii.ALLOCATED_QTY)-(ii.PICKED_QTY)-(ii.INACTIVE_QTY)>0 "
					+" order by ii.ON_HAND_QTY+(ii.IN_TRANSIT_QTY)-(ii.ALLOCATED_QTY)-(ii.PICKED_QTY)-(ii.INACTIVE_QTY) asc";
			dm = DBOperator.DoSelect2DM(sql);
			for(int i=0;i<dm.getCurrentCount();i++){
				String INV_INVENTORY_ID = dm.getString("INV_INVENTORY_ID", i);
				double invQty = Math_SAM.str2Double(dm.getString("QTY", i));
				//循环订单数量，如果>0继续分配，否则退出分配
				if (shipmentQty > 0) {
					if ((shipmentQty - invQty) >= 0) {
						dm.setObject(dm.getCol("AllocatedQty"), i, invQty);
						//更新库存分配数量
						sql = "update inv_inventory set ALLOCATED_QTY=ALLOCATED_QTY+("+invQty+") "
							 +"where INV_INVENTORY_ID= '"+INV_INVENTORY_ID+"' and WAREHOUSE_CODE='"+warehouseCode+"' and STORER_CODE='"+storerCode+"' and ITEM_CODE='"+itemCode+"' "
							 +" and LOCATION_CODE='"+dm.getString("LOCATION_CODE", i)+"' and CONTAINER_CODE='"+dm.getString("CONTAINER_CODE", i)+"' ";
						DBOperator.DoUpdate(sql);
					} else {
						dm.setObject(dm.getCol("AllocatedQty"), i, shipmentQty);
						//更新库存分配数量
						sql = "update inv_inventory set ALLOCATED_QTY=ifnull(ALLOCATED_QTY,0)+("+shipmentQty+") "
							 +"where INV_INVENTORY_ID= '"+INV_INVENTORY_ID+"' and WAREHOUSE_CODE='"+warehouseCode+"' and STORER_CODE='"+storerCode+"' and ITEM_CODE='"+itemCode+"' "
							 +" and LOCATION_CODE='"+dm.getString("LOCATION_CODE", i)+"' and CONTAINER_CODE='"+dm.getString("CONTAINER_CODE", i)+"' ";
						DBOperator.DoUpdate(sql);
						return dm;
					}
					shipmentQty = shipmentQty - invQty;
				}else{
					return dm;
				}
			}
			
		}else{
			//先查找批次ID，根据批次ID分配库存
			lotNo = getLotID(storerCode,itemCode,lot01,lot02,lot03,lot04,lot05,lot06,lot07,lot08,lot09,lot10);
			sql = "select INV_INVENTORY_ID,WAREHOUSE_CODE,STORER_CODE,ITEM_CODE,INV_LOT_ID,LOT_NO,LOCATION_CODE,CONTAINER_CODE,"
					+"ON_HAND_QTY+(IN_TRANSIT_QTY)-(ALLOCATED_QTY)-(PICKED_QTY)-(INACTIVE_QTY) QTY,0 AllocatedQty "
					+" from inv_inventory "
					+" where WAREHOUSE_CODE='"+warehouseCode+"' and STORER_CODE='"+storerCode+"' and LOT_NO='"+lotNo+"' and ITEM_CODE='"+itemCode+"' "
					+" order by ON_HAND_QTY+(IN_TRANSIT_QTY)-(ALLOCATED_QTY)-(PICKED_QTY)-(INACTIVE_QTY) asc";
			dm = DBOperator.DoSelect2DM(sql);
			for(int i=0;i<dm.getCurrentCount();i++){
				String INV_INVENTORY_ID = dm.getString("INV_INVENTORY_ID", i);
				double invQty = Math_SAM.str2Double(dm.getString("QTY", i));
				//循环订单数量，如果>0继续分配，否则退出分配
				if (shipmentQty > 0) {
					if ((shipmentQty - invQty) >= 0) {
						dm.setObject(dm.getCol("AllocatedQty"), i, invQty);
						//更新库存分配数量
						sql = "update inv_inventory set ALLOCATED_QTY=ALLOCATED_QTY+("+invQty+") "
							 +"where  INV_INVENTORY_ID= '"+INV_INVENTORY_ID+"' and WAREHOUSE_CODE='"+warehouseCode+"' and STORER_CODE='"+storerCode+"' and LOT_NO='"+lotNo+"' and ITEM_CODE='"+itemCode+"'"
							 +" and LOCATION_CODE='"+dm.getString("LOCATION_CODE", i)+"' and CONTAINER_CODE='"+dm.getString("CONTAINER_CODE", i)+"' ";
						DBOperator.DoUpdate(sql);
					} else {
						dm.setObject(dm.getCol("AllocatedQty"), i, shipmentQty);
						//更新库存分配数量
						sql = "update inv_inventory set ALLOCATED_QTY=ALLOCATED_QTY+("+shipmentQty+") "
							 +"where  INV_INVENTORY_ID= '"+INV_INVENTORY_ID+"' and WAREHOUSE_CODE='"+warehouseCode+"' and STORER_CODE='"+storerCode+"' and LOT_NO='"+lotNo+"' and ITEM_CODE='"+itemCode+"'"
							 +" and LOCATION_CODE='"+dm.getString("LOCATION_CODE", i)+"' and CONTAINER_CODE='"+dm.getString("CONTAINER_CODE", i)+"' ";
						DBOperator.DoUpdate(sql);
					}
					shipmentQty = shipmentQty - invQty;
				}else{
					return dm;
				}
			}
		}
		
		return dm;
	}
	
	private String getLotID(String storerCode,String itemCode,String lot01,String lot02,String lot03,String lot04,String lot05,
			String lot06,String lot07,String lot08,String lot09,String lot10){
		String sql = "select LOT_NO from inv_lot where STORER_CODE='"+storerCode+"' and ITEM_CODE='"+itemCode+"' "
				+" and ifnull(LOTTABLE01,'')='"+lot01+"' and ifnull(LOTTABLE02,'')='"+lot02+"' and ifnull(LOTTABLE03,'')='"+lot03+"' "
				+" and ifnull(LOTTABLE04,'')='"+lot04+"' and ifnull(LOTTABLE05,'')='"+lot05+"' and ifnull(LOTTABLE06,'')='"+lot06+"' "
				+" and ifnull(LOTTABLE07,'')='"+lot07+"' and ifnull(LOTTABLE08,'')='"+lot08+"' and ifnull(LOTTABLE09,'')='"+lot09+"' "
				+" and ifnull(LOTTABLE10,'')='"+lot10+"' ";
		DataManager dm = DBOperator.DoSelect2DM(sql);
		if(dm==null || dm.getCurrentCount()==0){
			return "";
		}else{
			return dm.getString("LOT_NO", 0);
		}
	}

	private void setExtendedState(int maximizedBoth) {
		// TODO Auto-generated method stub
		
	}

}
