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
import sys.tableEditDialog;
import sys.tableQueryDialog;
import util.Math_SAM;
import util.WaitingSplash;

import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ShipmentInputFrm2 extends InnerFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5982618691142416937L;
	private JPanel contentPane;
	private static volatile ShipmentInputFrm2 instance;
	private static boolean isOpen = false;
	private PBSUIBaseGrid headerTable;
	private PBSUIBaseGrid detailTable;
	
	public static ShipmentInputFrm2 getInstance() {
		 if(instance == null) { 
			 synchronized(ShipmentInputFrm2.class){
				 if(instance == null) {
					 instance = new ShipmentInputFrm2();
				 }
			 }
	        }  
	        return instance;
		 
	 }
	
	public static synchronized boolean getOpenStatus() {
		 if(instance == null) {    
	            instance = new ShipmentInputFrm2();  
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
					ShipmentInputFrm2 frame = new ShipmentInputFrm2();
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
	public ShipmentInputFrm2() {
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
		
		JButton btnAdd = new JButton("\u589E\u52A0");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = "select bw.warehouse_name 仓库,bs.storer_name 货主,osh.TRANSFER_ORDER_NO 运单号,osh.shipment_no 出库单号,osh.external_order_no 外部订单号,"
						+ "case osh.status when '100' then '新建' when '200' then '库存分配完成' when '300' then '拣货中' when '400' then '分拣中' when '500' then '包装中' when '600' then '包装完成' when '700' then '出库复核中' when '800' then '出库复核完成' when '900' then '已出库交接' else osh.status end 状态,"
						+ "osh.create_order_date 订单创建时间,osh.wave_no 波次号,osh.ship_to_name 收货人,osh.ship_to_contact 收货联系人,osh.ship_to_contact_idcard 收货人身份证号,"
						+ "osh.ship_to_country 国家,osh.ship_to_province_code 省,osh.ship_to_city_code 市,osh.ship_to_region_code 区,osh.ship_to_street_code 街道,osh.ship_to_address1 详细地址,osh.ship_to_cell 电话1,"
						+ "osh.ship_to_tel 电话2,osh.ship_to_email 电子邮箱,osh.created_dtm_loc WMS创建时间,osh.created_by_user WMS创建用户 "
						+ " from oub_shipment_header osh "
						+" inner join bas_storer bs on osh.storer_code=bs.storer_code "
						+" inner join bas_warehouse bw on osh.warehouse_code=bw.warehouse_code "
						+" where 1<>1 ";
				tableEditDialog tableQuery = new tableEditDialog(sql);
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
				}
			}
		});
		topPanel.add(btnAdd);
		
		JButton btnModify = new JButton("\u4FEE\u6539");
		topPanel.add(btnModify);
		
		JButton btnDelete = new JButton("\u5220\u9664");
		topPanel.add(btnDelete);
		
		JButton btnQuery = new JButton("\u67E5\u8BE2");
		btnQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ArrayList<String> fieldList = new ArrayList<String>();
				fieldList.add("osh.wave_no:波次号");
				fieldList.add("osh.TRANSFER_ORDER_NO:运单号");
				fieldList.add("osh.warehouse_code:仓库编码");
				fieldList.add("osh.storer_code:货主编码");
				fieldList.add("osh.shipment_no:出库单号");
				fieldList.add("osh.external_order_no:外部订单号");
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
		
		JButton btnExcel = new JButton("Excel\u5BFC\u5165");
		topPanel.add(btnExcel);
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
		getHeaderTableData(" and 1<>1 ");
	}
	
	private boolean getHeaderTableData(String strWhere){
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
		sql = sql + " order by osh.shipment_no ";
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
				+",osd.item_code 商品编码,bi.item_bar_code 条码,bi.item_name 商品名称,osd.req_qty 订单数量,osd.ALLOCATED_QTY 分配数量,biu.unit_name 单位,osd.is_gift 是否礼品,osd.price 价格,osd.item_retail_price 零售价格,osd.tax_rate 税率,osd.tax 税额,osd.created_dtm_loc WMS创建时间,osd.created_by_user WMS创建用户 "
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
		tab.setCellColor(cellColor);
		
	}

	private void setExtendedState(int maximizedBoth) {
		// TODO Auto-generated method stub
		
	}

}
