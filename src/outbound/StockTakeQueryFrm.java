package outbound;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;

import DBUtil.DBOperator;
import comUtil.JTableExportExcel;
import dmdata.DataManager;
import inbound.GenerateINVFromStockTakeDetail;
import main.PBSUIBaseGrid;
import sys.InnerFrame;
import sys.JTableUtil;
import sys.Message;
import sys.QueryDialog;
import util.Math_SAM;

import javax.swing.BoxLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StockTakeQueryFrm extends InnerFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -15016738570171107L;
	private JPanel contentPane;
	private static volatile StockTakeQueryFrm instance;
	private static boolean isOpen = false;
	private PBSUIBaseGrid headerTable;
	private PBSUIBaseGrid detailTable;
	
	public static StockTakeQueryFrm getInstance() {
		if(instance == null) { 
			 synchronized(StockTakeQueryFrm.class){
				 if(instance == null) {
					 instance = new StockTakeQueryFrm();
				 }
			 }
	        }  
	        return instance;
		 
	 }
	
	public static synchronized boolean getOpenStatus() {
		 if(instance == null) {    
	            instance = new StockTakeQueryFrm();  
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
					StockTakeQueryFrm frame = new StockTakeQueryFrm();
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
	public StockTakeQueryFrm() {
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.addVetoableChangeListener(new VetoableChangeListener() {

			public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException {
				String name = e.getPropertyName();
				Object value = e.getNewValue();
				if (name.equals("closed") && value.equals(Boolean.TRUE)) // 窗口被关闭
				{
					System.out.println("StockTakeFrm窗口被关闭");
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
		
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel topPanel = new JPanel();
		contentPane.add(topPanel, BorderLayout.NORTH);
		
		JButton btnQuery = new JButton("\u67E5\u8BE2");
		btnQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> fieldList = new ArrayList<String>();
				fieldList.add("ish.STOCKTAKE_NO:盘点单号");
				fieldList.add("ish.STORER_CODE:货主编码");
				fieldList.add("bs.STORER_NAME:货主名称");
				fieldList.add("ish.WAREHOUSE_CODE:仓库编码");
				fieldList.add("bw.WAREHOUSE_NAME:仓库名称");
				fieldList.add("ish.CREATED_BY_USER:盘点创建人");
				fieldList.add("ish.CREATED_DTM_LOC:盘点创建时间");
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
				if(headerTable.getRowCount()>0){
					headerTable.setRowSelectionInterval(0, 0);//默认选中第一行
				}
				getHeaderTableData(retWhere);
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
		
		JButton btnExport = new JButton("\u5BFC\u51FAExcel");
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTableExportExcel exportExcel = new JTableExportExcel(detailTable); 
				exportExcel.export();
			}
		});
		topPanel.add(btnExport);
		topPanel.add(btnClose);
		
		JPanel centerPanel = new JPanel();
		contentPane.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new GridLayout(2, 1, 0, 0));
		
		JPanel headerPanel = new JPanel();
		centerPanel.add(headerPanel);
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
		        	String stockTakeNo = headerTable.getValueAt(r, headerTable.getColumnModel().getColumnIndex("盘点单号")).toString();
			        getDetailTableData(stockTakeNo);
		        }
			}
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					JPopupMenu popupmenu = new JPopupMenu();
					JMenuItem menuItem1 = new JMenuItem();
					menuItem1.setLabel("盘点差异处理");
					menuItem1.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(ActionEvent e) {
							boolean b_confirm = Message.showOKorCancelMessage("是否针对该盘点单做差异处理?\n系统会根据实际盘点数量更新库存");
							if(b_confirm){
								String stockTakeNo = (String) headerTable.getValueAt(headerTable.getSelectedRow(), headerTable.getColumnModel().getColumnIndex("盘点单号"));
								String stockTakeNoStatus = (String) headerTable.getValueAt(headerTable.getSelectedRow(), headerTable.getColumnModel().getColumnIndex("状态"));
								String sql = "";
								if(stockTakeNoStatus.equalsIgnoreCase("初盘完成")){
									String ret = stockTakeDiffUpdate(stockTakeNo);
									if(ret.substring(0, 3).equalsIgnoreCase("ERR")){
										Message.showErrorMessage(ret);
										getHeaderTableData(" and ish.STOCKTAKE_NO='"+stockTakeNo+"' ");
										return;
									}else{
										Message.showInfomationMessage(ret);
										getHeaderTableData(" and ish.STOCKTAKE_NO='"+stockTakeNo+"' ");
									}
								}else{
									Message.showWarningMessage("盘点单只有是【初盘完成】状态才能做盘点差异处理");
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
		
		JPanel detailPanel = new JPanel();
		centerPanel.add(detailPanel);
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
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
//					JPopupMenu popupmenu = new JPopupMenu();
//					JMenuItem menuItem1 = new JMenuItem();
//					menuItem1.setLabel("删除改行数据");
//					menuItem1.addActionListener(new java.awt.event.ActionListener() {
//						public void actionPerformed(ActionEvent e) {
//						  //Message.showInfomationMessage(String.valueOf(detailTable.getSelectedRow()));
//						}
//						});
//					popupmenu.add(menuItem1);
//					popupmenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
		scrollPane_1.setViewportView(detailTable);
	}
	
	private void getHeaderTableData(String strWhere){
		String sql = "select ish.STOCKTAKE_NO 盘点单号, "
				+"case ish.status when '100' then '新建' when '200' then '盘点中' when '300' then '初盘完成' when '400' then '已提交差异' when '500' then '差异已确认' when '600' then '已执行调整' when '900' then '已关闭' end 状态,"
				+"ish.STOCKTAKE_NAME 盘点单描述,ish.STORER_CODE 货主编码,bs.STORER_NAME 货主名称,ish.WAREHOUSE_CODE 仓库编码,bw.WAREHOUSE_NAME 仓库名称,ish.CREATED_BY_USER 盘点创建人,ish.CREATED_DTM_LOC 盘点创建时间  "
				+"from inv_stocktake_header ish "
				+"inner join bas_storer bs on ish.STORER_CODE=bs.STORER_CODE "
				+"inner join bas_warehouse bw on ish.WAREHOUSE_CODE=bw.WAREHOUSE_CODE "
				+" where 1=1 ";
		if(!strWhere.equals("")){
			sql = sql +strWhere;
		}
		sql = sql + " order by ish.STOCKTAKE_NO ";
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
		tableHeaderRowColorSetup(headerTable);
		headerTableClick();
	}
	
	private void headerTableClick(){
		int r= headerTable.getSelectedRow();
        if(headerTable.getRowCount()>0){
        	String stockTakeNo = headerTable.getValueAt(r, headerTable.getColumnModel().getColumnIndex("盘点单号")).toString();
	        getDetailTableData(stockTakeNo);
        }
	}
	
	private void getDetailTableData(String stockTakeNo){
		String sql = "select ish.STOCKTAKE_NO 盘点单号,case ifnull(isd.status,100) when 100 then '新建' when '900' then '差异处理完成' else ifnull(isd.status,100) end 状态, isd.STORER_CODE 货主编码,bs.STORER_NAME 货主名称,isd.WAREHOUSE_CODE 仓库编码,bw.WAREHOUSE_NAME 仓库名称,isd.LOCATION_CODE 库位,isd.CONTAINER_CODE 箱号 "
				+",isd.ITEM_CODE 商品编码,bi.ITEM_BAR_CODE 商品条码,bi.ITEM_NAME 商品名称,isd.GUIDE_QTY 当前库存数量,isd.CONF_QTY 实际盘点数量,biu.unit_name 单位,isd.CREATED_BY_USER 盘点人,isd.CREATED_DTM_LOC 盘点时间 "
				+" from inv_stocktake_detail isd "
				+"inner join inv_stocktake_header ish on ish.STOCKTAKE_NO = isd.STOCKTAKE_NO "
				+"inner join bas_storer bs on isd.STORER_CODE=bs.STORER_CODE "
				+"inner join bas_warehouse bw on isd.WAREHOUSE_CODE=bw.WAREHOUSE_CODE "
				+"inner join bas_item bi on isd.STORER_CODE=bi.STORER_CODE and isd.ITEM_CODE=bi.ITEM_CODE "
				+"inner join bas_item_unit biu on bi.UNIT_CODE=biu.unit_code "
				+"where ish.STOCKTAKE_NO='"+stockTakeNo+"'";
		sql = sql+" order by ish.STOCKTAKE_NO,isd.STORER_CODE,isd.WAREHOUSE_CODE ";
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
			String totalQty = (String) tab.getValueAt(i, tab.getColumnModel().getColumnIndex("当前库存数量"));
			String scanQty = (String) tab.getValueAt(i, tab.getColumnModel().getColumnIndex("实际盘点数量"));
			String status = (String) tab.getValueAt(i, tab.getColumnModel().getColumnIndex("状态"));
			if(Math_SAM.str2Double(totalQty)-Math_SAM.str2Double(scanQty)!=0){
				Object[] rc1Cell = new Object[3];
		        rc1Cell[0] = new Integer(i);
		        rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("实际盘点数量"));
		        rc1Cell[2] = Color.ORANGE;
		        cellColor.addElement(rc1Cell);
		        rowColor.addElement(new Integer(i));
		        //tab.setRowColor(rowColor, Color.lightGray);
			}
			if(status.equals("差异处理完成")){
				Object[] rc1Cell = new Object[3];
		        rc1Cell[0] = new Integer(i);
		        rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("状态"));
		        rc1Cell[2] = Color.ORANGE;
		        cellColor.addElement(rc1Cell);
		        rowColor.addElement(new Integer(i));
		        //tab.setRowColor(rowColor, Color.lightGray);
			}
		}
		tab.setCellColor(cellColor);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void tableHeaderRowColorSetup(PBSUIBaseGrid tab){
		Vector cellColor = new Vector();
		for(int i=0;i<tab.getRowCount();i++){
			Vector rowColor = new Vector();
			String status = (String) tab.getValueAt(i, tab.getColumnModel().getColumnIndex("状态"));
			if(status.equals("已执行调整")){
				Object[] rc1Cell = new Object[3];
		        rc1Cell[0] = new Integer(i);
		        rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("盘点单号"));
		        rc1Cell[2] = Color.red;
		        cellColor.addElement(rc1Cell);
		        rowColor.addElement(new Integer(i));
		        //tab.setRowColor(rowColor, Color.lightGray);
			}else if(status.equals("初盘完成")){
				Object[] rc1Cell = new Object[3];
		        rc1Cell[0] = new Integer(i);
		        rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("盘点单号"));
		        rc1Cell[2] = Color.blue;
		        cellColor.addElement(rc1Cell);
		        rowColor.addElement(new Integer(i));
		        //tab.setRowColor(rowColor, Color.lightGray);
			}else if(status.equals("盘点中")){
				Object[] rc1Cell = new Object[3];
		        rc1Cell[0] = new Integer(i);
		        rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("盘点单号"));
		        rc1Cell[2] = Color.orange;
		        cellColor.addElement(rc1Cell);
		        rowColor.addElement(new Integer(i));
		        //tab.setRowColor(rowColor, Color.lightGray);
			}
		}
		tab.setCellColor(cellColor);
	}
	
	private String stockTakeDiffUpdate(String stockTakeNo){
		String sql = "select isd.INV_STOCKTAKE_DETAIL_ID,isd.STOCKTAKE_NO,isd.STORER_CODE,isd.WAREHOUSE_CODE,isd.LOCATION_CODE,isd.CONTAINER_CODE,isd.ITEM_CODE,isd.GUIDE_QTY,isd.CONF_QTY "
				+"from inv_stocktake_detail isd "
				+"inner join inv_stocktake_header ish on ish.STOCKTAKE_NO=isd.STOCKTAKE_NO "
				+"where ish.STOCKTAKE_NO='"+stockTakeNo+"' and isd.GUIDE_QTY<>isd.CONF_QTY and ish.STATUS='300' and ifnull(isd.status,100)<'900' ";
		DataManager dm = DBOperator.DoSelect2DM(sql);
		if(dm==null || dm.getCurrentCount()==0){
			return "ERR-盘点单号不存在，或者改盘点单已经完成差异审核";
		}else{
			for(int i=0;i<dm.getCurrentCount();i++){
				String INV_STOCKTAKE_DETAIL_ID = dm.getString("INV_STOCKTAKE_DETAIL_ID", i);
				String STORER_CODE = dm.getString("STORER_CODE", i);
				String WAREHOUSE_CODE = dm.getString("WAREHOUSE_CODE", i);
				String LOCATION_CODE = dm.getString("LOCATION_CODE", i);
				String CONTAINER_CODE = dm.getString("CONTAINER_CODE", i);
				String ITEM_CODE = dm.getString("ITEM_CODE", i);
				String GUIDE_QTY = dm.getString("GUIDE_QTY", i);
				String CONF_QTY = dm.getString("CONF_QTY", i);
				double diff = Math_SAM.str2Double(GUIDE_QTY) - Math_SAM.str2Double(CONF_QTY);
				if(diff>0){
					//库存数量比实际盘点数量大
					sql = "select INV_INVENTORY_ID,STORER_CODE,WAREHOUSE_CODE,LOCATION_CODE,CONTAINER_CODE,ITEM_CODE,ON_HAND_QTY "
							+"from inv_inventory where ON_HAND_QTY>0 and STORER_CODE='"+STORER_CODE+"' "
							+" and WAREHOUSE_CODE='"+WAREHOUSE_CODE+"' and LOCATION_CODE='"+LOCATION_CODE+"' "
							+" and CONTAINER_CODE='"+CONTAINER_CODE+"' and ITEM_CODE='"+ITEM_CODE+"' order by ON_HAND_QTY desc limit 1";
					DataManager dmInv = DBOperator.DoSelect2DM(sql);
					if(dmInv==null || dmInv.getCurrentCount()==0){
						boolean b = GenerateINVFromStockTakeDetail.generateInventory(stockTakeNo, INV_STOCKTAKE_DETAIL_ID);
						if(!b){
							return "ERR-库存新增失败，无法处理盘点差异\n"+"STORER_CODE="+STORER_CODE+" WAREHOUSE_CODE="+WAREHOUSE_CODE
									+" LOCATION_CODE="+LOCATION_CODE+" CONTAINER_CODE="+CONTAINER_CODE+" ITEM_CODE="+ITEM_CODE
									+""; 
						}
//						return "ERR-库存查找失败，无法处理盘点差异\n"+"STORER_CODE="+STORER_CODE+" WAREHOUSE_CODE="+WAREHOUSE_CODE
//								+" LOCATION_CODE="+LOCATION_CODE+" CONTAINER_CODE="+CONTAINER_CODE+" ITEM_CODE="+ITEM_CODE
//								+"";
					}else{
						String INV_INVENTORY_ID = dmInv.getString("INV_INVENTORY_ID", 0);
						sql = "update inv_inventory set ON_HAND_QTY=ON_HAND_QTY - "+diff+" where INV_INVENTORY_ID='"+INV_INVENTORY_ID+"' " ;
						int t = DBOperator.DoUpdate(sql);
						if(t!=1){
							return "ERR-库存更新失败，无法处理盘点差异\n"+"STORER_CODE="+STORER_CODE+" WAREHOUSE_CODE="+WAREHOUSE_CODE
									+" LOCATION_CODE="+LOCATION_CODE+" CONTAINER_CODE="+CONTAINER_CODE+" ITEM_CODE="+ITEM_CODE
									+""; 
						}else{
							sql = "update inv_stocktake_detail set status='900' where STOCKTAKE_NO='"+stockTakeNo+"' and INV_STOCKTAKE_DETAIL_ID='"+INV_STOCKTAKE_DETAIL_ID+"' ";
							t = DBOperator.DoUpdate(sql);
						}
						
					}
					
				}else{
					//库存数量比实际盘点数量小
					sql = "select INV_INVENTORY_ID,STORER_CODE,WAREHOUSE_CODE,LOCATION_CODE,CONTAINER_CODE,ITEM_CODE,ON_HAND_QTY "
							+"from inv_inventory where ON_HAND_QTY>0 and STORER_CODE='"+STORER_CODE+"' "
							+" and WAREHOUSE_CODE='"+WAREHOUSE_CODE+"' and LOCATION_CODE='"+LOCATION_CODE+"' "
							+" and CONTAINER_CODE='"+CONTAINER_CODE+"' and ITEM_CODE='"+ITEM_CODE+"' order by ON_HAND_QTY desc limit 1";
					DataManager dmInv = DBOperator.DoSelect2DM(sql);
					if(dmInv==null || dmInv.getCurrentCount()==0){
						boolean b = GenerateINVFromStockTakeDetail.generateInventory(stockTakeNo, INV_STOCKTAKE_DETAIL_ID);
						if(!b){
							return "ERR-库存新增失败，无法处理盘点差异\n"+"STORER_CODE="+STORER_CODE+" WAREHOUSE_CODE="+WAREHOUSE_CODE
									+" LOCATION_CODE="+LOCATION_CODE+" CONTAINER_CODE="+CONTAINER_CODE+" ITEM_CODE="+ITEM_CODE
									+""; 
						}
//						return "ERR-库存查找失败，无法处理盘点差异\n"+"STORER_CODE="+STORER_CODE+" WAREHOUSE_CODE="+WAREHOUSE_CODE
//								+" LOCATION_CODE="+LOCATION_CODE+" CONTAINER_CODE="+CONTAINER_CODE+" ITEM_CODE="+ITEM_CODE
//								+"";
					}else{
						String INV_INVENTORY_ID = dmInv.getString("INV_INVENTORY_ID", 0);
						sql = "update inv_inventory set ON_HAND_QTY=ON_HAND_QTY + "+(-1*diff)+" where INV_INVENTORY_ID='"+INV_INVENTORY_ID+"' " ;
						int t = DBOperator.DoUpdate(sql);
						if(t!=1){
							return "ERR-库存更新失败，无法处理盘点差异\n"+"STORER_CODE="+STORER_CODE+" WAREHOUSE_CODE="+WAREHOUSE_CODE
									+" LOCATION_CODE="+LOCATION_CODE+" CONTAINER_CODE="+CONTAINER_CODE+" ITEM_CODE="+ITEM_CODE
									+""; 
						}else{
							sql = "update inv_stocktake_detail set status='900' where STOCKTAKE_NO='"+stockTakeNo+"' and INV_STOCKTAKE_DETAIL_ID='"+INV_STOCKTAKE_DETAIL_ID+"' ";
							t = DBOperator.DoUpdate(sql);
						}
						
					}
				}
			}
			sql = "update inv_stocktake_header set status='600' where STOCKTAKE_NO='"+stockTakeNo+"' and status='300' ";
			int t = DBOperator.DoUpdate(sql);
			return "OK-盘点差异处理完成";
		}
	}

	private void setExtendedState(int maximizedBoth) {
		// TODO Auto-generated method stub
		
	}

}
