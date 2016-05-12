package outbound;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang.StringEscapeUtils;

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

import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;

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
import javax.swing.JCheckBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class ShipmentOubCheck extends InnerFrame {

	private JPanel contentPane;
	private static volatile ShipmentOubCheck instance;
	private static boolean isOpen = false;
	private JTextField txt_tracking_no;
	private PBSUIBaseGrid table;
	private JTextField txt_wrap_no;
	private JButton btnNextWrap;
	private JButton btnWrapQuery;
	private JButton btnNewWrap;
	private JButton btnWrapPrint;
	
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
		topPanel.setLayout(new GridLayout(2, 1, 0, 0));
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		topPanel.add(panel);
		
		JCheckBox cb_abroad = new JCheckBox("\u6D77\u5916\u76F4\u90AE\u6253\u5305");
		cb_abroad.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if(cb_abroad.isSelected()){
					btnNextWrap.setEnabled(true);
					btnWrapQuery.setEnabled(true);
					btnNewWrap.setEnabled(true);
					btnWrapPrint.setEnabled(true);
				}else{
					btnNextWrap.setEnabled(false);
					btnWrapQuery.setEnabled(false);
					btnNewWrap.setEnabled(false);
					btnWrapPrint.setEnabled(false);
				}
			}
		});
		panel.add(cb_abroad);
		
		JLabel label = new JLabel("\u5305\u88F9\u53F7\uFF1A");
		panel.add(label);
		
		txt_wrap_no = new JTextField();
		txt_wrap_no.setEditable(false);
		panel.add(txt_wrap_no);
		txt_wrap_no.setColumns(12);
		
		btnNextWrap = new JButton("\u6362\u5305\u88F9\u53F7");
		btnNextWrap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean b_confirm = Message.showOKorCancelMessage("��Ҫ���ύ��ǰҳ�涩�������ܻ�ȡ��һ�������ţ��Ƿ��ύ?");
				if(!b_confirm){
					return;
				}
				if(!txt_wrap_no.getText().trim().equals("")){
					String wrap_no = comData.getValueFromBasNumRule("oub_wrap_header", "WRAP_NO");
					txt_wrap_no.setText(wrap_no);
				}
			}
		});
		
		btnWrapQuery = new JButton("\u67E5\u8BE2");
		btnWrapQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = "select WRAP_NO ������,WRAP_NAME ��������,WRAP_START_DATE ���������ʼʱ��  "
						+ "from oub_wrap_header "
						+ "where WAREHOUSE_CODE= '"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' "
						+ "order by WRAP_START_DATE desc";
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
				Object obj = dm.getObject("������", 0);
				if(obj==null || obj.equals("")){
					return;
				}else{
					txt_wrap_no.setText((String) dm.getObject("������", 0));
				}
			}
		});
		btnWrapQuery.setEnabled(false);
		panel.add(btnWrapQuery);
		
		btnNewWrap = new JButton("\u65B0\u5305\u88F9\u53F7");
		btnNewWrap.setEnabled(false);
		btnNewWrap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(txt_wrap_no.getText().trim().equals("")){
					String wrap_no = comData.getValueFromBasNumRule("oub_wrap_header", "WRAP_NO");
					txt_wrap_no.setText(wrap_no);
				}else{
					Message.showWarningMessage("���а����ţ����ύ���»�ȡ�°�����!");
				}
			}
		});
		panel.add(btnNewWrap);
		btnNextWrap.setEnabled(false);
		panel.add(btnNextWrap);
		
		btnWrapPrint = new JButton("\u6253\u5370\u5305\u88F9\u53F7");
		btnWrapPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(txt_wrap_no.getText().trim().equals("")){
					Message.showWarningMessage("������Ϊ�գ����ѯѡ������»�ȡ�°�����!");
					return;
				}else{
					Process process;
					String wrap_no = txt_wrap_no.getText().trim();
					Runtime runtime = Runtime.getRuntime(); 
					try {
						String sql = "select '"+wrap_no+"' wrap_no from dual";
						DataManager dm = DBOperator.DoSelect2DM(sql);
						HashMap<String, String> hm = new HashMap<String, String>();
						hm.put("reportname", "�����Ŵ�ӡ");
						String data = "\""+(DBOperator.DataManager2JSONString(dm,"data",hm)).replaceAll("\"", "\\\\\"").replaceAll("&", "")+"\"";
						//String data = "\"{\\\"reportname\\\":\\\"SKU��ѯ\\\",\\\"data\\\":[{\\\"ITEM_CODE\\\":\\\"AZKJPB1E7670001\\\",\\\"ITEM_NAME\\\":\\\"�ձ���Ұ��֬�޹���Ȼ����������ϴ��ˮ600ml\\\",\\\"RETAIL_PRICE\\\":\\\"\\\"},{\\\"ITEM_CODE\\\":\\\"AZKJPB1E7670002\\\",\\\"ITEM_NAME\\\":\\\"�ձ���Ұ��֬�޹���Ȼ���������ͻ�����600ml\\\",\\\"RETAIL_PRICE\\\":\\\"\\\"},{\\\"ITEM_CODE\\\":\\\"0094-00031\\\",\\\"ITEM_NAME\\\":\\\"LOSHI ������˪ 220g /ƿ\\\",\\\"RETAIL_PRICE\\\":\\\"0.000\\\"}]}\"";
						System.out.println((data));
						runtime.exec("cmd /c "+System.getProperty("user.dir")+"/wmsReport.exe "+(data));
						//runtime.exec("cmd /c "+System.getProperty("user.dir")+"/Report_SKU.exe "+txt1.getText().trim()+","+txt2.getText().trim());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnWrapPrint.setEnabled(false);
		panel.add(btnWrapPrint);
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_1.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		topPanel.add(panel_1);
		
		JLabel lblNewLabel = new JLabel("\u8FD0\u5355\u53F7\uFF1A");
		panel_1.add(lblNewLabel);
		
		txt_tracking_no = new JTextField();
		panel_1.add(txt_tracking_no);
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
					if(cb_abroad.isSelected() && txt_wrap_no.getText().trim().equals("")){
						Message.showWarningMessage("���ѯѡ������Ż��ȡ�°�����!");
						btnWrapQuery.requestFocus();
						return;
					}
					if(!trackingNo.equals("")){
						sql = "select SHIPMENT_NO,status,CHECK_BY_USER,CHECK_DTM_LOC "
							+ "from oub_shipment_header "
							+ "where TRANSFER_ORDER_NO='"+trackingNo+"' "
							+ "and WAREHOUSE_CODE='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' "
							+ " ";
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
							//���Ķ���״̬ǰ���жϣ��������ֱ�ʲ֣���Ҫ�жϸð�����ϸ�Ƿ�����ͬ�ջ��˵����֤�ţ�����У���������뵽�ö���������ϸ
							String wrap_no = txt_wrap_no.getText().trim();
							String warehouse_code = MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0);
							String user_code = MainFrm.getUserInfo().getString("USER_CODE", 0);
							if(cb_abroad.isSelected() && !wrap_no.equals("")){
								sql = "select SHIP_TO_CONTACT_IDCARD from oub_shipment_header "
									+ "where SHIPMENT_NO='"+shipmentNo+"' and WAREHOUSE_CODE='"+warehouse_code+"' "
									+ "and SHIP_TO_CONTACT_IDCARD in "
									+ "( "
									+ "select osh.SHIP_TO_CONTACT_IDCARD "
									+ "from oub_wrap_detail owd "
									+ "left join oub_shipment_header osh on owd.SHIPMENT_NO=osh.SHIPMENT_NO and owd.WAREHOUSE_CODE=osh.WAREHOUSE_CODE "
									+ "where owd.WRAP_NO='"+wrap_no+"' and owd.WAREHOUSE_CODE='"+warehouse_code+"' "
									+ ")";
								DataManager dmcheck = DBOperator.DoSelect2DM(sql);
								if(dmcheck.getCurrentCount()>0){
									Message.showWarningMessage("�������ܼ��뵽�˰�����ͬһ�����֤���롾"+dmcheck.getString("SHIP_TO_CONTACT_IDCARD", 0)+"����һ��������ֻ�ܴ���һ������!");
									txt_tracking_no.setText("");
									txt_tracking_no.requestFocus();
									return;
								}
							}
							//���¶���״̬
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
								//д�붩��������
								if(cb_abroad.isSelected() && !wrap_no.equals("")){
									//д���ͷ
									sql = "insert into oub_wrap_header(WRAP_NO,WRAP_NAME,WAREHOUSE_CODE,WRAP_START_DATE"
										+ ",CREATED_BY_USER,CREATED_DTM_LOC) "
										+ "select '"+wrap_no+"','���ⶩ���������',"
										+ "'"+warehouse_code+"',"
										+ "now(),'"+user_code+"',now() "
										+ "from dual where not exists(select WRAP_NO from oub_wrap_header where WRAP_NO='"+wrap_no+"')";
									t = DBOperator.DoUpdate(sql);
									//д����ϸ
									sql = "insert into oub_wrap_detail(OUB_WRAP_HEADER_ID,WRAP_NO,STATUS,WAREHOUSE_CODE,"
										+ "SHIPMENT_NO,CREATED_BY_USER,CREATED_DTM_LOC) "
										+ "select (select OUB_WRAP_HEADER_ID from oub_wrap_header where WAREHOUSE_CODE='"+warehouse_code+"' and WRAP_NO='"+wrap_no+"' limit 1),"
										+ "'"+wrap_no+"',100,'"+warehouse_code+"','"+shipmentNo+"','"+user_code+"',now() from dual "
										+ "where not exists(select WRAP_NO from oub_wrap_detail "
										+ "where WRAP_NO='"+wrap_no+"' and SHIPMENT_NO='"+shipmentNo+"' "
										+ "and WAREHOUSE_CODE='"+warehouse_code+"')";
									t = DBOperator.DoUpdate(sql);
									
								}
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
		txt_tracking_no.setColumns(12);
		
		JButton btn_submit = new JButton("\u63D0\u4EA4");
		panel_1.add(btn_submit);
		
		JButton btnClose = new JButton("\u5173\u95ED");
		panel_1.add(btnClose);
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
					String shipmentNo = table.getValueAt(i, table.getColumnModel().getColumnIndex("���ⵥ��")).toString();
					if(status.equals("���⸴�����")){
						txt_tracking_no.setText("");
						txt_tracking_no.requestFocus();
						sb.append("�˵��ţ�"+trackingNo+" �Ѿ���ɳ��⸴�ˣ��������ݺ���\n");
						continue;
					}
					if(status.equals("���⸴����") || status.equals("��װ��") || status.equals("��װ���")){
						//...
					}else{
						txt_tracking_no.setText("");
						txt_tracking_no.requestFocus();
						sb.append("�˵��ţ�"+trackingNo+" δ��ɼ�����ˣ��������ݺ���\n");
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
							+"inner join (select t.WAREHOUSE_CODE,t.SHIPMENT_NO,t.INV_INVENTORY_ID,sum(t.PICKED_QTY) PICKED_QTY from oub_pick_detail t where t.SHIPMENT_NO=(select SHIPMENT_NO from oub_shipment_header where TRANSFER_ORDER_NO='"+trackingNo+"' limit 1) and t.`STATUS`<>'999' group by t.WAREHOUSE_CODE,t.SHIPMENT_NO,t.INV_INVENTORY_ID) opd on ii.INV_INVENTORY_ID=opd.INV_INVENTORY_ID "
							+"inner join oub_shipment_header osh on osh.WAREHOUSE_CODE=opd.WAREHOUSE_CODE and osh.SHIPMENT_NO=opd.SHIPMENT_NO "
							+" set ii.ON_HAND_QTY=ii.ON_HAND_QTY-(opd.PICKED_QTY),"
							+ "ii.PICKED_QTY=ii.PICKED_QTY-(opd.PICKED_QTY),"
							+ "ii.OUB_TOTAL_QTY=ii.OUB_TOTAL_QTY+opd.PICKED_QTY "
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
						}else{
							sql = "select CONFIG_VALUE1,CONFIG_VALUE2 from sys_config_detail where CONFIG_CODE='IS_REDUCE_MATERIAL' and CONFIG_VALUE1='1' ";
							DataManager dm = DBOperator.DoSelect2DM(sql);
							if(dm==null || dm.getCurrentCount()==0){
								
							}else{
								//�ۼ����Ŀ������
								sql = "update inv_inventory ii "
										+ "inner join ( "
										+ "select osd.STORER_CODE,osd.ITEM_CODE,osd.OQC_QTY,bim.ITEM_CODE_MATERIAL,bim.MATCH_QTY,(bim.MATCH_QTY*osd.OQC_QTY) MATERIAL_QTY "
										+ "from oub_shipment_detail osd "
										+ "left join bas_item_material bim on osd.STORER_CODE=bim.STORER_CODE and osd.ITEM_CODE=bim.ITEM_CODE "
										+ "where osd.SHIPMENT_NO='"+shipmentNo+"') tmp on tmp.ITEM_CODE_MATERIAL=ii.ITEM_CODE "
										+ "set ii.ON_HAND_QTY=ii.ON_HAND_QTY-(tmp.MATERIAL_QTY),"
										+ "ii.OUB_TOTAL_QTY=ii.OUB_TOTAL_QTY+(tmp.MATERIAL_QTY) "
										+ "";
								t = DBOperator.DoUpdate(sql);
								if(t==0){
//									Message.showWarningMessage("�۳����Ŀ������ʧ�ܣ�");
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
										list.set(dmProcess.getCol("MESSAGE"), "�۳����Ŀ������ʧ��:"+StringEscapeUtils.escapeSql(sql));
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
							}
							
						}
					}
				}
				if(!sb.toString().equals("")){
					Message.showWarningMessage(sb.toString());
				}
				txt_wrap_no.setText("");
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
				+"and osh.STATUS>=500 and osh.STATUS<800 ";
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
