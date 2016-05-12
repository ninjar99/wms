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

public class ShipmentWrapQueryFrm extends InnerFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5982618691142416937L;
	private JPanel contentPane;
	private static volatile ShipmentWrapQueryFrm instance;
	private static boolean isOpen = false;
	private PBSUIBaseGrid headerTable;
	private PBSUIBaseGrid detailTable;
	private static JComboBox cb_pageList;
	private static int page = 0;
	private static int onePage =100;
	private static String strWhere = "";
	
	public static ShipmentWrapQueryFrm getInstance() {
		 if(instance == null) { 
			 synchronized(ShipmentWrapQueryFrm.class){
				 if(instance == null) {
					 instance = new ShipmentWrapQueryFrm();
				 }
			 }
	        }  
	        return instance;
		 
	 }
	
	public static synchronized boolean getOpenStatus() {
		 if(instance == null) {    
	            instance = new ShipmentWrapQueryFrm();  
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
					ShipmentWrapQueryFrm frame = new ShipmentWrapQueryFrm();
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
	public ShipmentWrapQueryFrm() {
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
					System.out.println("ShipmentWrapQueryFrm窗口被关闭");
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
		            		fieldList.add("owh.WRAP_NO:包裹号");
		    				fieldList.add("osh.TRANSFER_ORDER_NO:运单号");
		    				fieldList.add("owd.SHIPMENT_NO:出库单号");
		    				fieldList.add("bw.WAREHOUSE_NAME:仓库名称");
		    				fieldList.add("owh.WAREHOUSE_CODE:仓库编码");
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
		        	String WRAP_NO = headerTable.getValueAt(r, headerTable.getColumnModel().getColumnIndex("包裹号")).toString();
		        	String warehouseCode = headerTable.getValueAt(r, headerTable.getColumnModel().getColumnIndex("仓库编码")).toString();
			        getDetailTableData(WRAP_NO,warehouseCode);
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
					//如果表头有数据了，才能执行下拉框事件
					if(headerTable.getRowCount()==0){
						return;
					}
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
		strWhere = "  ";
		getHeaderTableData(strWhere);
	}
	
	private boolean getHeaderTableData(String param){
		try{
		String sql = "select owh.WAREHOUSE_CODE 仓库编码,bw.WAREHOUSE_NAME 仓库名称,owh.WRAP_NO 包裹号,owh.WRAP_NAME 包裹号描述,owh.WRAP_START_DATE 包裹开始时间  "
				+ "from oub_wrap_header owh "
				+ "left join bas_warehouse bw on owh.WAREHOUSE_CODE=bw.WAREHOUSE_CODE "
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
		sql = sql + " order by owh.WRAP_NO ";
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
		if(headerTable.getRowCount()>0){
			headerTable.removeRowAll();
		}
		headerTable.setData(dm.getDataStrings());
		headerTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		headerTable.setColumnEditableAll(false);
		JTableUtil.fitTableColumnsDoubleWidth(headerTable);
		if(detailTable.getRowCount()>0){
			detailTable.removeRowAll();
		}
		if(headerTable.getRowCount()>0){
			headerTable.setRowSelectionInterval(0, 0);//默认选中第一行
		}
		headerTableClick();
		
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private void headerTableClick(){
		int r= headerTable.getSelectedRow();
        if(headerTable.getRowCount()>0){
        	String WRAP_NO = headerTable.getValueAt(r, headerTable.getColumnModel().getColumnIndex("包裹号")).toString();
        	String warehouseCode = headerTable.getValueAt(r, headerTable.getColumnModel().getColumnIndex("仓库编码")).toString();
	        getDetailTableData(WRAP_NO,warehouseCode);
        }
	}
	
	private void getDetailTableData(String wrap_no,String warehouseCode){
		String sql = "select owh.WAREHOUSE_CODE 仓库编码,bw.WAREHOUSE_NAME 仓库名称,owh.WRAP_NO 包裹号,"
				+ "owd.CREATED_DTM_LOC 订单扫描时间,owd.SHIPMENT_NO 出库单号,osh.TRANSFER_ORDER_NO 运单号"
				+ ",osh.SHIP_TO_NAME 收货人姓名,osh.SHIP_TO_TEL 收货人电话,osh.SHIP_TO_CONTACT_IDCARD 收货人身份证号,osh.SHIP_TO_PROVINCE_CODE 省"
				+ ",osh.SHIP_TO_CITY_CODE 市,osh.SHIP_TO_REGION_CODE 区,osh.SHIP_TO_ADDRESS1 详细地址,osh.SHIP_TO_EMAIL EMAIL "
				+ "from oub_wrap_header owh "
				+ "inner join oub_wrap_detail owd on owh.WRAP_NO=owd.WRAP_NO and owh.WAREHOUSE_CODE=owd.WAREHOUSE_CODE "
				+ "inner join oub_shipment_header osh on owd.SHIPMENT_NO=osh.SHIPMENT_NO and owd.WAREHOUSE_CODE=osh.WAREHOUSE_CODE "
				+ "left join bas_warehouse bw on owh.WAREHOUSE_CODE=bw.WAREHOUSE_CODE "
				+ "where owh.WAREHOUSE_CODE='"+warehouseCode+"' and owh.WRAP_NO='"+wrap_no+"'";
		sql = sql+" order by owh.WRAP_NO,owd.SHIPMENT_NO ";
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
		detailTable.repaint();
//		tableRowColorSetup(detailTable);
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
		
	}
	

}
