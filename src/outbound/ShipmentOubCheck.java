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
import main.PBSUIBaseGrid;
import sys.InnerFrame;
import sys.JTableUtil;
import sys.MainFrm;
import sys.Message;

import java.awt.GridLayout;
import java.awt.Rectangle;

import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ShipmentOubCheck extends InnerFrame {

	private JPanel contentPane;
	private static volatile ShipmentOubCheck instance;
	private static boolean isOpen = false;
	private JTextField txt_tracking_no;
	private PBSUIBaseGrid table;
	
	public static ShipmentOubCheck getInstance() {
		 if(instance == null) { 
			 synchronized(ShipmentOubCheck.class){
				 if(instance == null) {
					 instance = new ShipmentOubCheck();
				 }
			 }
	        }  
	        return instance;
		 
	 }
	
	public static synchronized boolean getOpenStatus() {
		 if(instance == null) {    
	            instance = new ShipmentOubCheck();  
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
					ShipmentOubCheck frame = new ShipmentOubCheck();
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
	public ShipmentOubCheck() {
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		
		setBounds(100, 100, 646, 437);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel topPanel = new JPanel();
		contentPane.add(topPanel, BorderLayout.NORTH);
		topPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		topPanel.add(panel);
		
		JLabel lblNewLabel = new JLabel("\u8FD0\u5355\u53F7\uFF1A");
		panel.add(lblNewLabel);
		
		txt_tracking_no = new JTextField();
		txt_tracking_no.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyChar()=='\n'){
					String sql = "";
					String trackingNo = txt_tracking_no.getText().trim();
					String shipmentNo = "";
					String status = "";
					String checkUser = "";
					String checkTime = "";
					if(!trackingNo.equals("")){
						sql = "select SHIPMENT_NO,status,CHECK_BY_USER,CHECK_DTM_LOC from oub_shipment_header where TRANSFER_ORDER_NO='"+trackingNo+"' ";
						DataManager dm = DBOperator.DoSelect2DM(sql);
						if(dm==null || dm.getCurrentCount()==0){
							Message.showWarningMessage("�˵��Ų���ȷ�����������룡");
							txt_tracking_no.setText("");
							txt_tracking_no.requestFocus();
							return;
						}else{
							status = dm.getString("status", 0);
							shipmentNo = dm.getString("SHIPMENT_NO", 0);
							checkUser = dm.getString("CHECK_BY_USER", 0);
							checkTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dm.getDate("CHECK_DTM_LOC", 0));
							if(status.equals("800")){
								if(checkUser.equals(MainFrm.getUserInfo().getString("USER_CODE", 0))){
									Message.showWarningMessage("�˵��Ѿ����⸴�����");
									txt_tracking_no.setText("");
									txt_tracking_no.requestFocus();
									return;
								}else{
									Message.showWarningMessage("���˵��������û������⸴�ˡ�"+checkUser+"  ʱ�䣺"+checkTime+"��");
									txt_tracking_no.setText("");
									txt_tracking_no.requestFocus();
									return;
								}
							}
							if(status.equals("700")){
								if(checkUser.equals(MainFrm.getUserInfo().getString("USER_CODE", 0))){
									Message.showWarningMessage("�ظ�ɨ��");
									txt_tracking_no.setText("");
									txt_tracking_no.requestFocus();
									return;
								}else{
									Message.showWarningMessage("���˵��������û������⸴�ˡ�"+checkUser+"  ʱ�䣺"+checkTime+"��");
									txt_tracking_no.setText("");
									txt_tracking_no.requestFocus();
									return;
								}
							}
							if(!status.equals("500")){
								Message.showWarningMessage("�˵�δ���������");
								txt_tracking_no.setText("");
								txt_tracking_no.requestFocus();
								return;
							}
							sql = "update oub_shipment_header set status='700',CHECK_BY_USER='"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"'"
									+",CHECK_DTM_LOC=now(),UPDATED_BY_USER='"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"',UPDATED_DTM_LOC=now() "
									+" where SHIPMENT_NO='"+shipmentNo+"' ";
							int t = DBOperator.DoUpdate(sql);
							if(t!=1){
								txt_tracking_no.setText("");
								txt_tracking_no.requestFocus();
								Message.showWarningMessage("��̨���ݸ���ʧ�ܣ�������ɨ�裡");
								return;
							}else{
								txt_tracking_no.setText("");
								txt_tracking_no.requestFocus();
								getHeaderTableData(" and osh.status='700' ");
								//���� ��λTable����������
								Rectangle rect = new Rectangle(0, table.getHeight(), 20, 20);
								table.scrollRectToVisible(rect);
								table.setRowSelectionInterval(table.getRowCount() - 1, table.getRowCount() - 1);
								table.grabFocus();
								table.changeSelection(table.getRowIndexByColIndexAndColValue(table.getColumnModel().getColumnIndex("�˵���"),trackingNo), 0, false, true);
							}
						}
					}
				}
			}
		});
		panel.add(txt_tracking_no);
		txt_tracking_no.setColumns(12);
		
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
		
		JButton btn_submit = new JButton("\u63D0\u4EA4");
		btn_submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String sql = "";
				StringBuffer sb = new StringBuffer();
				if(table.getRowCount()==0){
					Message.showWarningMessage("����ϸ���ݣ������ύ");
					return;
				}
				boolean b_confirm = Message.showOKorCancelMessage("�Ƿ�ȫ���ύ���ݣ��˵�״̬����Ϊ�����⸴����ɡ�?");
				if(!b_confirm){
					txt_tracking_no.setText("");
					txt_tracking_no.requestFocus();
					return;
				}
				for(int i=0;i<table.getRowCount();i++){
					String status = table.getValueAt(i, table.getColumnModel().getColumnIndex("״̬")).toString();
					String trackingNo = table.getValueAt(i, table.getColumnModel().getColumnIndex("�˵���")).toString();
					if(status.equals("���⸴�����")){
						txt_tracking_no.setText("");
						txt_tracking_no.requestFocus();
						sb.append("�˵��ţ�"+trackingNo+" �Ѿ���ɳ��⸴�ˣ��������ݺ���\n");
						continue;
					}
					//���¶�����ͷ״̬ status=800
					sql = "update oub_shipment_header set status='800',CHECK_BY_USER='"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"'"
							+",CHECK_DTM_LOC=now(),UPDATED_BY_USER='"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"',UPDATED_DTM_LOC=now() "
							+" where TRANSFER_ORDER_NO='"+trackingNo+"' ";
					int t = DBOperator.DoUpdate(sql);
					if(t!=1){
						txt_tracking_no.setText("");
						txt_tracking_no.requestFocus();
						sb.append("�˵��ţ�"+trackingNo+" ���³ɡ����⸴����ɡ�״̬ʧ�ܣ��������ݺ���\n");
						continue;
					}else{
						//���¶�����ϸ״̬
						sql = "update oub_shipment_detail set `STATUS`='800' "
								+" where SHIPMENT_NO=(select SHIPMENT_NO from oub_shipment_header where TRANSFER_ORDER_NO='"+trackingNo+"')";
						t = DBOperator.DoUpdate(sql);
						if(t==0){
							sb.append("�˵��ţ�"+trackingNo+" ��ϸ״̬����ʧ�ܣ��������ݺ���\n");
							continue;
						}
						//���¿����������ͼ������
						sql = "update inv_inventory ii "
							+"inner join oub_pick_detail opd on ii.INV_INVENTORY_ID=opd.INV_INVENTORY_ID "
							+"inner join oub_shipment_header osh on osh.WAREHOUSE_CODE=opd.WAREHOUSE_CODE and osh.SHIPMENT_NO=opd.SHIPMENT_NO "
							+" set ii.ON_HAND_QTY=ii.ON_HAND_QTY-(opd.PICKED_QTY),ii.PICKED_QTY=ii.PICKED_QTY-(opd.PICKED_QTY),ii.OUB_TOTAL_QTY=ii.OUB_TOTAL_QTY+opd.PICKED_QTY "
							+",ii.UPDATED_DTM_LOC=now(),ii.UPDATED_BY_USER='"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"'"
							+"where osh.TRANSFER_ORDER_NO='"+trackingNo+"' "
							+"";
						t = DBOperator.DoUpdate(sql);
						if(t==0){
							sb.append("�˵��ţ�"+trackingNo+" �ۼ����ʧ�ܣ��������ݺ���\n");
							//�۳����ʧ��    ����״̬����Ϊ700
							sql = "update oub_shipment_header set status='700',CHECK_BY_USER='"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"'"
									+",CHECK_DTM_LOC=now(),UPDATED_BY_USER='"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"',UPDATED_DTM_LOC=now() "
									+" where TRANSFER_ORDER_NO='"+trackingNo+"' ";
							t = DBOperator.DoUpdate(sql);
							continue;
						}
					}
				}
				if(!sb.toString().equals("")){
					Message.showWarningMessage(sb.toString());
				}
				txt_tracking_no.setText("");
				txt_tracking_no.requestFocus();
				getHeaderTableData(" and osh.status='800' ");
				
				//��¼������־
				DataManager dmProcess = comData.getSysProcessHistoryDataModel("sys_process_history");
				if (dmProcess != null) {
					dmdata.xArrayList list = (xArrayList) dmProcess.getRow(0);
					list.set(dmProcess.getCol("SYS_PROCESS_HISTORY_ID"), "null");
					list.set(dmProcess.getCol("PROCESS_CODE"), "OutboundCheck");
					list.set(dmProcess.getCol("PROCESS_NAME"), "���⸴��");
					list.set(dmProcess.getCol("STORER_CODE"), "");
					list.set(dmProcess.getCol("WAREHOUSE_CODE"),MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0));
					list.set(dmProcess.getCol("FROM_LOCATION_CODE"), "");
					list.set(dmProcess.getCol("FROM_CONTAINER_CODE"), "");
					list.set(dmProcess.getCol("QTY"), "");
					list.set(dmProcess.getCol("REFERENCE_NO"), txt_tracking_no.getText());
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
					System.out.println("д�������־��" + bool);
				}
				
			}
		});
		panel.add(btn_submit);
		panel.add(btnClose);
		
		JPanel centerPanel = new JPanel();
		contentPane.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		centerPanel.add(scrollPane, BorderLayout.CENTER);
		
		table = new PBSUIBaseGrid();
		scrollPane.setViewportView(table);
		getHeaderTableData(" ");
	}
	
	private void getHeaderTableData(String strWhere){
		String sql = "select osh.TRANSFER_ORDER_NO �˵���,osd.shipment_no ���ⵥ��,osd.shipment_line_no �к�,bs.storer_name ����"
				+",case osh.status when '100' then '�½�' when '200' then '���������' when '300' then '�����' when '400' then '�ּ���' when '500' then '��װ��' when '600' then '��װ���' when '700' then '���⸴����' when '800' then '���⸴�����' when '900' then '�ѳ��⽻��' end ״̬"
				+",bi.item_bar_code ����,osd.req_qty ��������,osd.ALLOCATED_QTY ��������,osd.PICKED_QTY �������,osd.OQC_QTY ��������,biu.unit_name ��λ,bi.item_name ��Ʒ����,osd.is_gift �Ƿ���Ʒ,osd.price �۸�,osd.item_retail_price ���ۼ۸�,osd.tax_rate ˰��,osd.tax ˰��,osd.created_dtm_loc WMS����ʱ��,osd.created_by_user WMS�����û� "
				+"from oub_shipment_detail osd "
				+"inner join oub_shipment_header osh on osd.shipment_no=osh.shipment_no "
				+"left outer join bas_item bi on bi.storer_code=osh.storer_code and bi.item_code=osd.item_code "
				+"left outer join bas_item_unit biu on bi.unit_code=biu.unit_code "
				+"inner join bas_storer bs on osh.storer_code=bs.storer_code "
				+"where osh.CHECK_BY_USER='"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"' "
				+"and osh.WAREHOUSE_CODE='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' "
				+"and osh.STATUS<'800' ";
		if(!strWhere.equals("")){
			sql = sql + strWhere;
		}
		sql = sql+" order by osd.shipment_no,osd.shipment_line_no ";
		DataManager dm = DBOperator.DoSelect2DM(sql);
		if (table.getColumnCount() == 0) {
			table.setColumn(dm.getCols());
		}
		table.removeRowAll();
		table.setData(dm.getDataStrings());
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setColumnEditableAll(false);
		JTableUtil.fitTableColumns(table);
//		headerTable.setSortEnable();
		table.setColumnSelectionAllowed(true);
		table.updateUI();
		tableRowColorSetup(table);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void tableRowColorSetup(PBSUIBaseGrid tab){
		Vector cellColor = new Vector();
		for(int i=0;i<tab.getRowCount();i++){
			Vector rowColor = new Vector();
			String totalQty = (String) tab.getValueAt(i, tab.getColumnModel().getColumnIndex("��������"));
			String oqcQty = (String) tab.getValueAt(i, tab.getColumnModel().getColumnIndex("��������"));
			Object[] rc1Cell = new Object[3];
	        rc1Cell[0] = new Integer(i);
	        rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("��������"));
	        rc1Cell[2] = Color.ORANGE;
	        cellColor.addElement(rc1Cell);
	        
	        Object[] rc1Cell2 = new Object[3];
	        rc1Cell2[0] = new Integer(i);
	        rc1Cell2[1] = new Integer(tab.getColumnModel().getColumnIndex("��������"));
	        rc1Cell2[2] = Color.lightGray;
	        cellColor.addElement(rc1Cell2);
//			if(Math_SAM.str2Double(totalQty)-Math_SAM.str2Double(oqcQty)>=0){
//				Object[] rc1Cell = new Object[3];
//		        rc1Cell[0] = new Integer(i);
//		        rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("��������"));
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
