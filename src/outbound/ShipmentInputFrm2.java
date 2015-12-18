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
		// ���������б����һ�� VetoableChangeListener��Ϊ��������ע���������
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.addVetoableChangeListener(new VetoableChangeListener() {

			public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException {
				e.getSource();
				String name = e.getPropertyName();
				Object value = e.getNewValue();
				if (name.equals("closed") && value.equals(Boolean.TRUE)) // ���ڱ��ر�
				{
					System.out.println("ShipmentQueryFrm���ڱ��ر�");
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
				String sql = "select bw.warehouse_name �ֿ�,bs.storer_name ����,osh.TRANSFER_ORDER_NO �˵���,osh.shipment_no ���ⵥ��,osh.external_order_no �ⲿ������,"
						+ "case osh.status when '100' then '�½�' when '200' then '���������' when '300' then '�����' when '400' then '�ּ���' when '500' then '��װ��' when '600' then '��װ���' when '700' then '���⸴����' when '800' then '���⸴�����' when '900' then '�ѳ��⽻��' else osh.status end ״̬,"
						+ "osh.create_order_date ��������ʱ��,osh.wave_no ���κ�,osh.ship_to_name �ջ���,osh.ship_to_contact �ջ���ϵ��,osh.ship_to_contact_idcard �ջ������֤��,"
						+ "osh.ship_to_country ����,osh.ship_to_province_code ʡ,osh.ship_to_city_code ��,osh.ship_to_region_code ��,osh.ship_to_street_code �ֵ�,osh.ship_to_address1 ��ϸ��ַ,osh.ship_to_cell �绰1,"
						+ "osh.ship_to_tel �绰2,osh.ship_to_email ��������,osh.created_dtm_loc WMS����ʱ��,osh.created_by_user WMS�����û� "
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
				Object obj = dm.getObject("�ֿ����", 0);
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
				fieldList.add("osh.wave_no:���κ�");
				fieldList.add("osh.TRANSFER_ORDER_NO:�˵���");
				fieldList.add("osh.warehouse_code:�ֿ����");
				fieldList.add("osh.storer_code:��������");
				fieldList.add("osh.shipment_no:���ⵥ��");
				fieldList.add("osh.external_order_no:�ⲿ������");
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
		        	String shipmentNo = headerTable.getValueAt(r, headerTable.getColumnModel().getColumnIndex("���ⵥ��")).toString();
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
		String sql = "select bw.warehouse_name �ֿ�,bs.storer_name ����,osh.TRANSFER_ORDER_NO �˵���,osh.shipment_no ���ⵥ��,osh.external_order_no �ⲿ������,"
				+ "case osh.status when '100' then '�½�' when '200' then '���������' when '300' then '�����' when '400' then '�ּ���' when '500' then '��װ��' when '600' then '��װ���' when '700' then '���⸴����' when '800' then '���⸴�����' when '900' then '�ѳ��⽻��' else osh.status end ״̬,"
				+ "osh.create_order_date ��������ʱ��,osh.wave_no ���κ�,osh.ship_to_name �ջ���,osh.ship_to_contact �ջ���ϵ��,osh.ship_to_contact_idcard �ջ������֤��,"
				+ "osh.ship_to_country ����,osh.ship_to_province_code ʡ,osh.ship_to_city_code ��,osh.ship_to_region_code ��,osh.ship_to_street_code �ֵ�,osh.ship_to_address1 ��ϸ��ַ,osh.ship_to_cell �绰1,"
				+ "osh.ship_to_tel �绰2,osh.ship_to_email ��������,osh.created_dtm_loc WMS����ʱ��,osh.created_by_user WMS�����û� "
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
			headerTable.setRowSelectionInterval(0, 0);//Ĭ��ѡ�е�һ��
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
        	String shipmentNo = headerTable.getValueAt(r, headerTable.getColumnModel().getColumnIndex("���ⵥ��")).toString();
	        getDetailTableData(shipmentNo);
        }
	}
	
	private void getDetailTableData(String shipmentNo){
		String sql = "select osd.shipment_no ���ⵥ��,osd.shipment_line_no �к�,osd.status ����״̬,osd.create_order_date ������������,osd.erp_order_no �ⲿ������,osd.warehouse_code �ֿ����,osd.storer_code ����"
				+",osd.item_code ��Ʒ����,bi.item_bar_code ����,bi.item_name ��Ʒ����,osd.req_qty ��������,osd.ALLOCATED_QTY ��������,biu.unit_name ��λ,osd.is_gift �Ƿ���Ʒ,osd.price �۸�,osd.item_retail_price ���ۼ۸�,osd.tax_rate ˰��,osd.tax ˰��,osd.created_dtm_loc WMS����ʱ��,osd.created_by_user WMS�����û� "
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
			String totalQty = (String) tab.getValueAt(i, tab.getColumnModel().getColumnIndex("��������"));
			String allocatedQty = (String) tab.getValueAt(i, tab.getColumnModel().getColumnIndex("��������"));
			if(Math_SAM.str2Double(totalQty)-Math_SAM.str2Double(allocatedQty)==0){
				Object[] rc1Cell = new Object[3];
		        rc1Cell[0] = new Integer(i);
		        rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("��������"));
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
