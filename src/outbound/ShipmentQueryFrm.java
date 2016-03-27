package outbound;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import DBUtil.DBOperator;
import dmdata.DataManager;
import main.PBSUIBaseGrid;
import sys.InnerFrame;
import sys.JTableUtil;
import sys.MainFrm;
import sys.Message;
import sys.QueryDialog;
import util.Math_SAM;
import util.WaitingSplash;

import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComboBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class ShipmentQueryFrm extends InnerFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5982618691142416937L;
	private JPanel contentPane;
	private static volatile ShipmentQueryFrm instance;
	private static boolean isOpen = false;
	private PBSUIBaseGrid headerTable;
	private PBSUIBaseGrid detailTable;
	private static JComboBox cb_pageList;
	private static int page = 0;
	private static int onePage =100;
	private static String strWhere = "";
	
	public static ShipmentQueryFrm getInstance() {
		 if(instance == null) { 
			 synchronized(ShipmentQueryFrm.class){
				 if(instance == null) {
					 instance = new ShipmentQueryFrm();
				 }
			 }
	        }  
	        return instance;
		 
	 }
	
	public static synchronized boolean getOpenStatus() {
		 if(instance == null) {    
	            instance = new ShipmentQueryFrm();  
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
					ShipmentQueryFrm frame = new ShipmentQueryFrm();
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
	public ShipmentQueryFrm() {
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// 向侦听器列表添加一个 VetoableChangeListener。为所有属性注册该侦听器
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.addVetoableChangeListener(new VetoableChangeListener() {

			public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException {
				e.getSource();
				String name = e.getPropertyName();
				Object value = e.getNewValue();
				if (name.equals("closed") && value.equals(Boolean.TRUE)) // 窗口被关闭
				{
					System.out.println("ShipmentQueryFrm窗口被关闭");
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
				
		setBounds(100, 100, 656, 439);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel topPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) topPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		contentPane.add(topPanel, BorderLayout.NORTH);
		
		JButton btnQuery = new JButton("\u67E5\u8BE2");
		btnQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				new SwingWorker<String, Void>() {
					WaitingSplash splash = new WaitingSplash();

		            @Override
		            protected String doInBackground() throws Exception {
		            	
		            	try{
		            		ArrayList<String> fieldList = new ArrayList<String>();
		    				fieldList.add("osh.wave_no:波次号");
		    				fieldList.add("osh.TRANSFER_ORDER_NO:运单号");
		    				fieldList.add("osh.warehouse_code:仓库编码");
		    				fieldList.add("osh.storer_code:货主编码");
		    				fieldList.add("bs.storer_name:货主名称");
		    				fieldList.add("osh.shipment_no:出库单号");
		    				fieldList.add("osh.external_order_no:外部订单号");
		    				QueryDialog query = QueryDialog.getInstance(fieldList);
		    				Toolkit toolkit = Toolkit.getDefaultToolkit();
		    				int x = (int)(toolkit.getScreenSize().getWidth()-query.getWidth())/2;
		    				int y = (int)(toolkit.getScreenSize().getHeight()-query.getHeight())/2;
		    				query.setLocation(x, y);
		    				query.setVisible(true);
		    				strWhere = QueryDialog.queryValueResult;
		    				if(strWhere.length()>0){
		    					strWhere = " and "+strWhere;
		    				}
		    				//出现
			            	headerTable.setEnabled(false);
			            	headerTable.setColumnEditableAll(false);
			            	splash.start(); // 运行启动界面
		    				if(!getHeaderTableData(strWhere)){
		    					splash.stop();
		    					headerTable.setEnabled(true);
		    				}
		            	}catch(Exception e){
		            		Message.showWarningMessage(e.getMessage());
		            	}
		                return "";
		            }

		            @Override
		            protected void done() {
		            	splash.stop(); // 运行启动界面
		                System.out.println("数据查询结束");
						headerTable.setEnabled(true);
		            }
		        }.execute();
			}
		});
		topPanel.add(btnQuery);
		
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
		
		JButton btnWaiting = new JButton("\u5F85\u62E3\u8D27\u8BA2\u5355\u67E5\u8BE2");
		btnWaiting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new SwingWorker<String, Void>() {
					WaitingSplash splash = new WaitingSplash();

		            @Override
		            protected String doInBackground() throws Exception {
		            	//出现
		            	headerTable.setEnabled(false);
		            	headerTable.setColumnEditableAll(false);
		            	splash.start(); // 运行启动界面
		            	try{
		            		String warehouseCode = MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0);
		            		strWhere = " and osh.warehouse_code='"+warehouseCode+"' and osh.status='100' ";
		    				if(!getHeaderTableData(strWhere)){
		    					splash.stop();
		    					headerTable.setEnabled(true);
		    				}
		            	}catch(Exception e){
		            		Message.showWarningMessage(e.getMessage());
		            	}
		                return "";
		            }

		            @Override
		            protected void done() {
		            	splash.stop(); // 运行启动界面
		                System.out.println("数据查询结束");
						headerTable.setEnabled(true);
		            }
		        }.execute();
			}
		});
		topPanel.add(btnWaiting);
		
		JButton btnPacked = new JButton("\u5DF2\u6253\u5305\u8BA2\u5355\u67E5\u8BE2");
		btnPacked.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new SwingWorker<String, Void>() {
					WaitingSplash splash = new WaitingSplash();

		            @Override
		            protected String doInBackground() throws Exception {
		            	//出现
		            	headerTable.setEnabled(false);
		            	headerTable.setColumnEditableAll(false);
		            	splash.start(); // 运行启动界面
		            	try{
		            		String warehouseCode = MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0);
		            		strWhere = " and osh.warehouse_code='"+warehouseCode+"' and osh.status='500' ";
		            		if(!getHeaderTableData(strWhere)){
		    					splash.stop();
		    					headerTable.setEnabled(true);
		    				}
		            	}catch(Exception e){
		            		Message.showWarningMessage(e.getMessage());
		            	}
		                return "";
		            }

		            @Override
		            protected void done() {
		            	splash.stop(); // 运行启动界面
		                System.out.println("数据查询结束");
						headerTable.setEnabled(true);
		            }
		        }.execute();
				
			}
		});
		topPanel.add(btnPacked);
		topPanel.add(btnClose);
		
		JPanel centerpanel = new JPanel();
		contentPane.add(centerpanel, BorderLayout.CENTER);
		centerpanel.setLayout(new GridLayout(2, 1, 0, 0));
		
		JPanel headerPanel = new JPanel();
		centerpanel.add(headerPanel);
		headerPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		headerPanel.add(scrollPane, BorderLayout.CENTER);
		
		headerTable = new PBSUIBaseGrid();
		headerTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(arg0.getClickCount()>=2){
					headerTable.setColumnSelectionAllowed(true);
				}
				int r= headerTable.getSelectedRow();
		        if(headerTable.getRowCount()>0){
		        	String shipmentNo = headerTable.getValueAt(r, headerTable.getColumnModel().getColumnIndex("出库单号")).toString();
			        getDetailTableData(shipmentNo);
		        }
			}
		});
		scrollPane.setViewportView(headerTable);
		
		JPanel panel = new JPanel();
		headerPanel.add(panel, BorderLayout.SOUTH);
		
		JButton preButton = new JButton("\u4E0A\u4E00\u9875");
		preButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(page==1) {
					Message.showInfomationMessage("已经是第一页了");
					return;
				}
				if(page - 1 >0){
					page = page - 1;
				}else{
					page = 0;
				}
				new SwingWorker<String, Void>() {
					WaitingSplash splash = new WaitingSplash();

		            @Override
		            protected String doInBackground() throws Exception {
		            	//出现
		            	preButton.setEnabled(false);
		            	headerTable.setEnabled(false);
		            	headerTable.setColumnEditableAll(false);
		            	cb_pageList.setSelectedItem(String.valueOf(page));
		            	splash.start(); // 运行启动界面
		            	try{
		    				if(!getHeaderTableData("page_go")){
		    					splash.stop();
		    					headerTable.setEnabled(true);
		    				}
		            	}catch(Exception e){
		            		Message.showWarningMessage(e.getMessage());
		            	}
		                return "";
		            }

		            @Override
		            protected void done() {
		            	preButton.setEnabled(true);
		            	splash.stop(); // 运行启动界面
		                System.out.println("数据查询结束");
						headerTable.setEnabled(true);
		            }
		        }.execute();
			}
		});
		panel.add(preButton);
		
		JButton nextButton = new JButton("\u4E0B\u4E00\u9875");
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(page>=cb_pageList.getItemCount()){
					Message.showInfomationMessage("最后一页了");
					return;
				}
				page = page + 1;
				new SwingWorker<String, Void>() {
					WaitingSplash splash = new WaitingSplash();

		            @Override
		            protected String doInBackground() throws Exception {
		            	//出现
		            	nextButton.setEnabled(false);
		            	headerTable.setEnabled(false);
		            	headerTable.setColumnEditableAll(false);
		            	cb_pageList.setSelectedItem(String.valueOf(page));
		            	splash.start(); // 运行启动界面
		            	try{
		    				if(!getHeaderTableData("page_go")){
		    					splash.stop();
		    					headerTable.setEnabled(true);
		    				}
		            	}catch(Exception e){
		            		Message.showWarningMessage(e.getMessage());
		            	}
		                return "";
		            }

		            @Override
		            protected void done() {
		            	nextButton.setEnabled(true);
		            	splash.stop(); // 运行启动界面
		                System.out.println("数据查询结束");
						headerTable.setEnabled(true);
		            }
		        }.execute();
			}
		});
		panel.add(nextButton);
		
		//选择页面直接跳转
		cb_pageList = new JComboBox(new DefaultComboBoxModel());
		cb_pageList.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					if(cb_pageList.getItemCount()==0) return;
					page = Integer.parseInt(cb_pageList.getSelectedItem().toString());
					System.out.println("当前页:"+cb_pageList.getSelectedItem().toString());
					new SwingWorker<String, Void>() {
						WaitingSplash splash = new WaitingSplash();

			            @Override
			            protected String doInBackground() throws Exception {
			            	//出现
			            	preButton.setEnabled(false);
			            	nextButton.setEnabled(false);
			            	headerTable.setEnabled(false);
			            	headerTable.setColumnEditableAll(false);
			            	splash.start(); // 运行启动界面
			            	try{
			    				if(!getHeaderTableData("page_go")){
			    					splash.stop();
			    					headerTable.setEnabled(true);
			    				}
			            	}catch(Exception e){
			            		Message.showWarningMessage(e.getMessage());
			            	}
			                return "";
			            }

			            @Override
			            protected void done() {
			            	preButton.setEnabled(true);
			            	nextButton.setEnabled(true);
			            	splash.stop(); // 运行启动界面
			                System.out.println("数据查询结束");
							headerTable.setEnabled(true);
			            }
			        }.execute();
				}
			}
		});
		panel.add(cb_pageList);
		
		JPanel detailPanel = new JPanel();
		centerpanel.add(detailPanel);
		detailPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		detailPanel.add(scrollPane_1, BorderLayout.CENTER);
		
		detailTable = new PBSUIBaseGrid();
		detailTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()>=2){
					detailTable.setColumnSelectionAllowed(true);
				}
			}
		});
		scrollPane_1.setViewportView(detailTable);
		
		JPanel bottomPanel = new JPanel();
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
		strWhere = " and 1<>1 ";
		getHeaderTableData(strWhere);
	}
	
	private boolean getHeaderTableData(String param){
		try{
		String sql = "select bw.warehouse_name 仓库,bs.storer_name 货主,osh.TRANSFER_ORDER_NO 运单号,osh.shipment_no 出库单号,osh.external_order_no 外部订单号,"
				+ "case osh.status when '100' then '新建' when '200' then '库存分配完成' when '300' then '拣货中' when '400' then '分拣中' when '500' then '包装中' when '600' then '包装完成' when '700' then '出库复核中' when '800' then '出库复核完成' when '900' then '已出库交接' else osh.status end 状态,"
				+ "osh.create_order_date 订单创建时间,osh.wave_no 波次号,osh.ship_to_name 收货人,osh.ship_to_contact 收货联系人,osh.ship_to_contact_idcard 收货人身份证号,"
				+ "osh.ship_to_country 国家,osh.ship_to_province_code 省,osh.ship_to_city_code 市,osh.ship_to_region_code 区,osh.ship_to_street_code 街道,osh.ship_to_address1 详细地址,osh.ship_to_cell 电话1,"
				+ "osh.ship_to_tel 电话2,osh.ship_to_email 电子邮箱,osh.created_dtm_loc WMS创建时间,osh.created_by_user WMS创建用户 "
				+ " from oub_shipment_header osh "
				+" inner join bas_storer bs on osh.storer_code=bs.storer_code "
				+" inner join bas_warehouse bw on osh.warehouse_code=bw.warehouse_code "
				+" where 1=1 ";
		if(!strWhere.equals("")){
			sql = sql +strWhere;
		}
		//系统判断  翻页的时候不需要再次初始化页面数量
		if(!param.equals("page_go")){
			//根据SQL 得到总记录数
			DataManager counter = DBOperator.DoSelect2DM("select count(1) totalPage from ("+sql+") tab ");
			String totalPage = counter.getString("totalPage", 0);
			initTotalPage(totalPage);
		}
		sql = sql + " order by osh.shipment_no ";
		int tmp = (page-1) * onePage;
		if(tmp<0){
			tmp = 0;
			page = 1;
		}
		sql = sql + " limit "+tmp+","+onePage;
		DataManager dm = DBOperator.DoSelect2DM(sql);
		if (headerTable.getColumnCount() == 0) {
			headerTable.setColumn(dm.getCols());
		}
		headerTable.removeRowAll();
		headerTable.setData(dm.getDataStrings());
		headerTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		headerTable.setColumnEditableAll(false);
		JTableUtil.fitTableColumnsDoubleWidth(headerTable);
		if(headerTable.getRowCount()>0){
			headerTable.setRowSelectionInterval(0, 0);//默认选中第一行
		}
		headerTableClick();
		detailTable.removeRowAll();
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private void headerTableClick(){
		int r= headerTable.getSelectedRow();
        if(headerTable.getRowCount()>0){
        	String shipmentNo = headerTable.getValueAt(r, headerTable.getColumnModel().getColumnIndex("出库单号")).toString();
	        getDetailTableData(shipmentNo);
        }
	}
	
	private void getDetailTableData(String shipmentNo){
		String sql = "select osd.shipment_no 出库单号,osd.shipment_line_no 行号,osd.status 订单状态,osd.create_order_date 订单创建日期,osd.erp_order_no 外部订单号,osd.warehouse_code 仓库编码,osd.storer_code 货主"
				+",osd.item_code 商品编码,ifnull(bi.item_bar_code,'') 条码,ifnull(bi.item_name,'') 商品名称,osd.req_qty 订单数量,osd.ALLOCATED_QTY 分配数量,ifnull(biu.unit_name,'') 单位,osd.is_gift 是否礼品,osd.price 价格,osd.item_retail_price 零售价格,ifnull(osd.tax_rate,'') 税率,ifnull(osd.tax,'') 税额,osd.created_dtm_loc WMS创建时间,osd.created_by_user WMS创建用户 "
				+"from oub_shipment_detail osd "
				+"inner join oub_shipment_header osh on osd.shipment_no=osh.shipment_no "
				+"left outer join bas_item bi on bi.storer_code=osh.storer_code and bi.item_code=osd.item_code "
				+"left outer join bas_item_unit biu on bi.unit_code=biu.unit_code "
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
		detailTable.setSortEnable();
		detailTable.updateUI();
		tableRowColorSetup(detailTable);
	}
	
	public static void initTotalPage(String pages){
		System.out.println("总行数:"+pages);
		double totalPage = 0;
		try{
			totalPage = Math.ceil(Integer.parseInt(pages)/(onePage*1.00));
			System.out.println("总页数:"+totalPage);
		}catch(Exception e){
			totalPage = 0;
		}
		if(totalPage==0) return;
		cb_pageList.removeAllItems();
		for(int i=0;i<totalPage;i++){
			cb_pageList.addItem(String.valueOf(i+1));
		}
		cb_pageList.updateUI();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void tableRowColorSetup(PBSUIBaseGrid tab){
		Vector cellColor = new Vector();
		for(int i=0;i<tab.getRowCount();i++){
			Vector rowColor = new Vector();
			String totalQty = (String) tab.getValueAt(i, tab.getColumnModel().getColumnIndex("订单数量"));
			String allocatedQty = (String) tab.getValueAt(i, tab.getColumnModel().getColumnIndex("分配数量"));
			if(Math_SAM.str2Double(totalQty)-Math_SAM.str2Double(allocatedQty)==0){
				Object[] rc1Cell = new Object[3];
		        rc1Cell[0] = new Integer(i);
		        rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("分配数量"));
		        rc1Cell[2] = Color.ORANGE;
		        cellColor.addElement(rc1Cell);
		        rowColor.addElement(new Integer(i));
//		        tab.setRowColor(rowColor, Color.lightGray);
			}
		}
		//tab.setCellColor(cellColor);
		
	}

	private void setExtendedState(int maximizedBoth) {
		// TODO Auto-generated method stub
		
	}
	

}
