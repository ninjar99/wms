package outbound;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import dmdata.xArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;

import DBUtil.DBOperator;
import DBUtil.LogInfo;
import comUtil.WMSCombobox;
import comUtil.comData;
import dmdata.DataManager;
import main.PBSUIBaseGrid;
import sys.InnerFrame;
import sys.JTableUtil;
import sys.MainFrm;
import sys.Message;
import sys.tableQueryDialog;
import util.JTNumEdit;

import javax.swing.JLabel;
import javax.swing.JMenuItem;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.MenuItem;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import javax.swing.JCheckBox;

public class StockTakeFrm extends InnerFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5349333269282062259L;
	private JPanel contentPane;
	private static volatile StockTakeFrm instance;
	private static boolean isOpen = false;
	private WMSCombobox cb_warehouse;
	private WMSCombobox cb_storer;
	private PBSUIBaseGrid table;
	private JTextField txt_stocktake_no;
	private JTextField txt_location_code;
	private JTextField txt_container_code;
	private JTextField txt_item_barcode;
	private JTNumEdit txt_item_qty;
	private JButton btn_finish;
	private JTextField txt_stocktake_status;
	private JButton btn_stocktake_query;
	JCheckBox cb_by_storer;
	
	public static StockTakeFrm getInstance() {
		if(instance == null) { 
			 synchronized(StockTakeFrm.class){
				 if(instance == null) {
					 instance = new StockTakeFrm();
				 }
			 }
	        }  
	        return instance;
		 
	 }
	
	public static synchronized boolean getOpenStatus() {
		 if(instance == null) {    
	            instance = new StockTakeFrm();  
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
					StockTakeFrm frame = new StockTakeFrm();
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
	public StockTakeFrm() {
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.addVetoableChangeListener(new VetoableChangeListener() {

			public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException {
				String name = e.getPropertyName();
				Object value = e.getNewValue();
				if (name.equals("closed") && value.equals(Boolean.TRUE)) // ���ڱ��ر�
				{
					System.out.println("StockTakeFrm���ڱ��ر�");
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
		setBounds(100, 100, 662, 320);
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
		
		JLabel lblNewLabel = new JLabel("\u4ED3\u5E93\uFF1A");
		panel.add(lblNewLabel);
		
		cb_warehouse = new WMSCombobox("SELECT WAREHOUSE_CODE,WAREHOUSE_name FROM bas_warehouse",true);
		panel.add(cb_warehouse);
		
		JLabel lblNewLabel_1 = new JLabel("\u8D27\u4E3B\uFF1A");
		panel.add(lblNewLabel_1);
		
		cb_storer = new WMSCombobox("select storer_code,storer_name from bas_storer  order by storer_code",true);
		panel.add(cb_storer);
		
		JButton btn_generate_stocktake_no = new JButton("\u65B0\u5EFA\u76D8\u70B9\u5355");
		btn_generate_stocktake_no.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = "";
				String warehouseCode = cb_warehouse.getSelectedOID();
				String storerCode = cb_storer.getSelectedOID();
				String stockTakeNo = "";
				if(!txt_stocktake_no.getText().trim().equals("")){
					Message.showWarningMessage("�̵㵥���Ѵ��ڣ��������½��̵㵥");
					return;
				}
				if(warehouseCode.equals("") || storerCode.equals("")){
					Message.showWarningMessage("��ѡ��ֿ�ͻ�����Ϣ��");
					return;
				}else{
					String STOCKTAKE_TYPE = "100";
					if(cb_by_storer.isSelected()){
						STOCKTAKE_TYPE = "400";
					}
					stockTakeNo = comData.getValueFromBasNumRule("inv_stocktake_header", "STOCKTAKE_NO");
					sql = "insert into inv_stocktake_header(STOCKTAKE_NO,STOCKTAKE_NAME,STORER_CODE,WAREHOUSE_CODE,STOCKTAKE_TYPE,IS_DARK_STOCK_TAKE,CREATED_BY_USER,CREATED_DTM_LOC) "
							+"select '"+stockTakeNo+"','�ƻ��̵�','"+storerCode+"','"+warehouseCode+"','"+STOCKTAKE_TYPE+"','1','"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"',now() ";
					int t = DBOperator.DoUpdate(sql);
					if(t!=1){
						Message.showErrorMessage("�����̵㵥��ͷʧ�ܣ�����ϵϵͳ����Ա");
						LogInfo.appendLog(sql);
						return;
					}else{
						txt_stocktake_no.setText(stockTakeNo);
						if(cb_by_storer.isSelected()){
							saveStockDetailByStorer();
							getStockTakeDetail(" and isd.STOCKTAKE_NO ='"+stockTakeNo+"' ");
						}else{
							getStockTakeDetail(" and 1<>1 ");
						}
						txt_stocktake_no.setText(stockTakeNo);
						txt_location_code.selectAll();
						txt_location_code.requestFocus();
						btn_generate_stocktake_no.setEnabled(false);
						cb_warehouse.setEnabled(false);
						cb_storer.setEnabled(false);
						btn_finish.setEnabled(true);
						btn_stocktake_query.setEnabled(false);
						
						
						//��¼������־
						DataManager dmProcess = comData.getSysProcessHistoryDataModel("sys_process_history");
						if (dmProcess != null) {
							dmdata.xArrayList list = (xArrayList) dmProcess.getRow(0);
							list.set(dmProcess.getCol("SYS_PROCESS_HISTORY_ID"), "null");
							list.set(dmProcess.getCol("PROCESS_CODE"), "CreatedStockTake");
							list.set(dmProcess.getCol("PROCESS_NAME"), "�����̵㵥");
							list.set(dmProcess.getCol("STORER_CODE"), "");
							list.set(dmProcess.getCol("WAREHOUSE_CODE"),MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0));
							list.set(dmProcess.getCol("FROM_LOCATION_CODE"), "");
							list.set(dmProcess.getCol("FROM_CONTAINER_CODE"), "");
							list.set(dmProcess.getCol("QTY"), "");
							list.set(dmProcess.getCol("REFERENCE_NO"), stockTakeNo);
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
				}
			}
		});
		panel.add(btn_generate_stocktake_no);
		
		JButton btnClose = new JButton("\u5173\u95ED");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					instance.setClosed(true);
				} catch (PropertyVetoException e) {
					e.printStackTrace();
				}
			}
		});
		
		btn_finish = new JButton("\u76D8\u70B9\u5B8C\u6210");
		btn_finish.setEnabled(false);
		btn_finish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String stockTakeNo = txt_stocktake_no.getText().trim();
				if(stockTakeNo.equals("")){
					Message.showWarningMessage("�̵㵥�Ų���Ϊ�գ�");
					return;
				}
				if(table.getRowCount()==0){
					boolean b_confirm = Message.showOKorCancelMessage("���̵���ϸ,�Ƿ�ɾ�����̵㵥��?");
					if(b_confirm){
						String sql = "delete from inv_stocktake_header where STOCKTAKE_NO='"+stockTakeNo+"' and status='100' "
								+" and not exists(select * from inv_stocktake_detail where STOCKTAKE_NO='"+stockTakeNo+"' )";
						int t = DBOperator.DoUpdate(sql);
						if(t>0){
							Message.showInfomationMessage("ɾ�����̵㵥�ɹ�");
							cb_warehouse.setSelectedIndex(0);
							cb_storer.setSelectedIndex(0);
							txt_stocktake_no.setText("");
							txt_stocktake_status.setText("");
							return;
						}else{
							Message.showWarningMessage("ɾ�����̵㵥ʧ��");
							return;
						}
					}else{
						return;
					}
					
				}
				boolean b_confirm = Message.showOKorCancelMessage("�Ƿ���ɳ���?");
				if(!b_confirm){
					return;
				}
				String sql = "update inv_stocktake_header set status='300' where STOCKTAKE_NO='"+stockTakeNo+"' and status in ('100','200') ";
				int t = DBOperator.DoUpdate(sql);
				if(t==1){
					btn_generate_stocktake_no.setEnabled(true);
					cb_warehouse.setEnabled(true);
					cb_storer.setEnabled(true);
					btn_finish.setEnabled(false);
					txt_stocktake_no.setText("");
					txt_location_code.setText("");
					txt_container_code.setText("");
					txt_item_barcode.setText("");
					Message.showInfomationMessage("�̵㵥״̬���Ϊ��������ɡ�");
					//��¼������־
					DataManager dmProcess = comData.getSysProcessHistoryDataModel("sys_process_history");
					if (dmProcess != null) {
						dmdata.xArrayList list = (xArrayList) dmProcess.getRow(0);
						list.set(dmProcess.getCol("SYS_PROCESS_HISTORY_ID"), "null");
						list.set(dmProcess.getCol("PROCESS_CODE"), "CloseStockTake");
						list.set(dmProcess.getCol("PROCESS_NAME"), "�̵����");
						list.set(dmProcess.getCol("STORER_CODE"), "");
						list.set(dmProcess.getCol("WAREHOUSE_CODE"),MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0));
						list.set(dmProcess.getCol("FROM_LOCATION_CODE"), "");
						list.set(dmProcess.getCol("FROM_CONTAINER_CODE"), "");
						list.set(dmProcess.getCol("QTY"), "");
						list.set(dmProcess.getCol("REFERENCE_NO"), stockTakeNo);
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
						btn_generate_stocktake_no.setEnabled(true);
						btn_stocktake_query.setEnabled(true);
						btn_finish.setEnabled(false);
					}
				}else{
					Message.showErrorMessage("�̵㵥����ʧ��");
					return;
				}
			}
		});
		
		btn_stocktake_query = new JButton("\u9009\u62E9\u76D8\u70B9\u5355");
		panel.add(btn_stocktake_query);
		btn_stocktake_query.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!txt_stocktake_no.getText().trim().equals("")){
					Message.showWarningMessage("�̵㵥���Ѵ��ڣ�������ѡ���̵㵥");
					return;
				}
				String sql = "select ish.STOCKTAKE_NO �̵㵥��,case ish.status when '100' then '�½�' when '200' then '�̵���' when '300' then '�������' when '400' then '���ύ����' when '500' then '������ȷ��' when '600' then '��ִ�е���' when '900' then '�ѹر�' end ״̬,"
						+" ish.STOCKTAKE_NAME �̵㵥����,bs.STORER_NAME ��������,bw.WAREHOUSE_NAME �ֿ�����,ish.CREATED_BY_USER �̵㴴����,ish.CREATED_DTM_LOC �̵㴴��ʱ��  "
						+"from inv_stocktake_header ish "
						+"inner join bas_storer bs on ish.STORER_CODE=bs.STORER_CODE "
						+"inner join bas_warehouse bw on ish.WAREHOUSE_CODE=bw.WAREHOUSE_CODE "
						+" where ish.status in('100','200') ";
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
				Object obj = dm.getObject("�̵㵥��", 0);
				if(obj==null || obj.equals("")){
					return;
				}else{
					cb_warehouse.setSelectedItem(dm.getObject("�ֿ�����", 0));
					cb_storer.setSelectedItem(dm.getObject("��������", 0));
					txt_stocktake_no.setText((String) dm.getObject("�̵㵥��", 0));
					txt_stocktake_status.setText((String) dm.getObject("״̬", 0));
					txt_location_code.requestFocus();
					txt_location_code.selectAll();
					btn_finish.setEnabled(true);
					btn_generate_stocktake_no.setEnabled(false);
					getStockTakeDetail(" and isd.STOCKTAKE_NO ='"+txt_stocktake_no.getText().trim()+"' ");
				}
			}
		});
		panel.add(btn_finish);
		panel.add(btnClose);
		
		cb_by_storer = new JCheckBox("\u6309\u8D27\u4E3B\u5168\u76D8");
		panel.add(cb_by_storer);
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_1.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		topPanel.add(panel_1);
		
		JLabel lblNewLabel_2 = new JLabel("\u76D8\u70B9\u5355\u53F7\uFF1A");
		panel_1.add(lblNewLabel_2);
		
		txt_stocktake_no = new JTextField();
		txt_stocktake_no.setEditable(false);
		panel_1.add(txt_stocktake_no);
		txt_stocktake_no.setColumns(10);
		
		txt_stocktake_status = new JTextField();
		txt_stocktake_status.setForeground(Color.BLUE);
		txt_stocktake_status.setEditable(false);
		panel_1.add(txt_stocktake_status);
		txt_stocktake_status.setColumns(6);
		
		JLabel lblNewLabel_3 = new JLabel("\u5E93\u4F4D\u7F16\u7801\uFF1A");
		panel_1.add(lblNewLabel_3);
		
		txt_location_code = new JTextField();
		txt_location_code.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				String locationCode = txt_location_code.getText().trim();
				String warehouseCode = cb_warehouse.getSelectedOID();
				String sql = "select location_code from bas_location where warehouse_code='"+warehouseCode+"' and location_code='"+locationCode+"'";
				if (!locationCode.equals("")) {
					DataManager dm = DBOperator.DoSelect2DM(sql);
					if (dm == null || dm.getCurrentCount() == 0) {
						Message.showWarningMessage("��λ���벻��ȷ!");
						txt_location_code.setText("");
						txt_location_code.requestFocus();
						return;
					}
				}
			}
		});
		txt_location_code.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyChar() == '\n') {
					txt_container_code.selectAll();
					txt_container_code.requestFocus();
				}
			}
		});
		panel_1.add(txt_location_code);
		txt_location_code.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel("\u7BB1\u53F7\uFF1A");
		panel_1.add(lblNewLabel_4);
		
		txt_container_code = new JTextField();
		txt_container_code.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				String containerCode = txt_container_code.getText().trim();
				String warehouseCode = cb_warehouse.getSelectedOID();
				if(containerCode.equals("0") || containerCode.equals("*")){
					containerCode="*";
					txt_container_code.setText("*");
					return;
				}
				String sql = "select container_code from bas_container where warehouse_code='"+warehouseCode+"' and container_code='"+containerCode+"'";
				if (!containerCode.equals("")) {
					DataManager dm = DBOperator.DoSelect2DM(sql);
					if (dm == null || dm.getCurrentCount() == 0) {
						Message.showWarningMessage("������벻��ȷ!");
						txt_container_code.setText("");
						txt_container_code.requestFocus();
						return;
					}
				}
			}
		});
		txt_container_code.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyChar() == '\n') {
					txt_item_barcode.selectAll();
					txt_item_barcode.requestFocus();
				}
			}
		});
		panel_1.add(txt_container_code);
		txt_container_code.setColumns(10);
		
		JLabel lblNewLabel_5 = new JLabel("\u6761\u7801\uFF1A");
		panel_1.add(lblNewLabel_5);
		
		txt_item_barcode = new JTextField();
		txt_item_barcode.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				
			}
		});
		txt_item_barcode.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyChar()=='\n'){
					String itemCode = "";
					String itemQty = txt_item_qty.getText();
					String itemBarCode = txt_item_barcode.getText().trim();
					String storerCode = cb_storer.getSelectedOID();
					String warehouseCode = cb_warehouse.getSelectedOID();
					String locationCode = txt_location_code.getText();
					String containerCode = txt_container_code.getText();
					String stockTakeNo = txt_stocktake_no.getText().trim();
					String sql = "select item_code,unit_code,item_name from bas_item where storer_code='"+storerCode+"' and item_bar_code='"+itemBarCode+"'";
					if (!itemBarCode.equals("")) {
						if(warehouseCode.equals("")){
							Message.showWarningMessage("��ѡ���̵�ֿ�");
							cb_warehouse.requestFocus();
							return;
						}
						if(storerCode.equals("")){
							Message.showWarningMessage("��ѡ�������Ϣ");
							cb_storer.requestFocus();
							return;
						}
						if(locationCode.equals("")){
							Message.showWarningMessage("�������λ��");
							txt_location_code.requestFocus();
							return;
						}
						if(containerCode.equals("")){
							Message.showWarningMessage("���������");
							txt_container_code.requestFocus();
							return;
						}
						DataManager dm = DBOperator.DoSelect2DM(sql);
						if (dm == null || dm.getCurrentCount() == 0) {
							Message.showWarningMessage("��Ʒ�������벻��ȷ!");
							txt_item_barcode.selectAll();
							txt_item_barcode.requestFocus();
							return;
						}else{
							itemCode = dm.getString("item_code", 0);
							String unitCode = dm.getString("unit_code", 0);
							String itemName = dm.getString("item_name", 0);
							sql = "select STOCKTAKE_NO,STORER_CODE,WAREHOUSE_CODE,LOCATION_CODE,CONTAINER_CODE,ITEM_CODE "
									+"from inv_stocktake_detail isd "
									+" where isd.STOCKTAKE_NO='"+stockTakeNo+"' and isd.STORER_CODE='"+storerCode+"' "
									+" and isd.WAREHOUSE_CODE = '"+warehouseCode+"' and isd.LOCATION_CODE='"+locationCode+"' "
									+" and isd.CONTAINER_CODE='"+containerCode+"' and isd.ITEM_CODE='"+itemCode+"' ";
							dm = DBOperator.DoSelect2DM(sql);
							if(dm == null || dm.getCurrentCount() == 0){
								//�����̵��¼�¼
								sql = "insert into inv_stocktake_detail(STOCKTAKE_NO,STORER_CODE,WAREHOUSE_CODE,LOCATION_CODE,CONTAINER_CODE,"
										+"ITEM_CODE,GUIDE_QTY,GUIDE_UOM,CONF_QTY,CONF_UOM,FIRST_STOCKTAKE_QTY,FIRST_STOCKTAKE_UOM,CREATED_BY_USER,CREATED_DTM_LOC) "
										+"select '"+stockTakeNo+"','"+storerCode+"','"+warehouseCode+"','"+locationCode+"','"+containerCode+"', "
										+"'"+itemCode+"',"
										+"ifnull((select sum(ii.ON_HAND_QTY+IN_TRANSIT_QTY-(ALLOCATED_QTY)-(PICKED_QTY)-(INACTIVE_QTY)) qty from inv_inventory ii where ii.STORER_CODE='"+storerCode+"' and ii.WAREHOUSE_CODE='"+warehouseCode+"' and ii.ITEM_CODE='"+itemCode+"' and ii.LOCATION_CODE='"+locationCode+"' and ii.CONTAINER_CODE='"+containerCode+"'),0),"
										+"'"+unitCode+"',"+itemQty+",'"+unitCode+"',"+itemQty+",'"+unitCode+"','"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"',now() "
										+"";
								int t = DBOperator.DoUpdate(sql);
								if(t==0){
									Message.showErrorMessage("�����̵���ϸʧ��\n"+itemBarCode+"��"+itemName+" �̵�������"+itemQty);
									return;
								}
							}else{
								//�����̵�����
								sql = "update inv_stocktake_detail isd set CONF_QTY=CONF_QTY+("+itemQty+"),FIRST_STOCKTAKE_QTY=FIRST_STOCKTAKE_QTY+("+itemQty+") "
										+" where isd.STOCKTAKE_NO='"+stockTakeNo+"' and isd.STORER_CODE='"+storerCode+"' "
										+" and isd.WAREHOUSE_CODE = '"+warehouseCode+"' and isd.LOCATION_CODE='"+locationCode+"' "
										+" and isd.CONTAINER_CODE='"+containerCode+"' and isd.ITEM_CODE='"+itemCode+"' ";
								int t = DBOperator.DoUpdate(sql);
								if(t==0){
									Message.showErrorMessage("�����̵���ϸʧ��\n"+itemBarCode+"��"+itemName+" �̵�������"+itemQty);
									return;
								}
							}
						}
						//��ʾ�̵���ϸ
						getStockTakeDetail(" and isd.STOCKTAKE_NO ='"+stockTakeNo+"' ");
						//���� ��λTable����������
						Rectangle rect = new Rectangle(0, table.getHeight(), 20, 20);
						table.scrollRectToVisible(rect);
						table.setRowSelectionInterval(table.getRowCount() - 1, table.getRowCount() - 1);
//						table.grabFocus();
						table.changeSelection(table.getRowIndexByColIndexAndColValue(5,containerCode), 0, false, true);
						txt_item_barcode.grabFocus();
						txt_item_barcode.selectAll();
						txt_item_barcode.requestFocus();
					}
				}
			}
		});
		panel_1.add(txt_item_barcode);
		txt_item_barcode.setColumns(12);
		
		txt_item_qty = new JTNumEdit(8, "#####",true);
		txt_item_qty.setText("1");
		panel_1.add(txt_item_qty);
		txt_item_qty.setColumns(3);
		
		JPanel centerPanel = new JPanel();
		contentPane.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
		
		JScrollPane scrollPane = new JScrollPane();
		centerPanel.add(scrollPane);
		
		table = new PBSUIBaseGrid();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()>=2){
					table.setColumnSelectionAllowed(true);
				}
			}
			@SuppressWarnings("deprecation")
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					JPopupMenu popupmenu = new JPopupMenu();
					JMenuItem menuItem1 = new JMenuItem();
					menuItem1.setLabel("ɾ�������̵�����");
					menuItem1.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(ActionEvent e) {
							boolean b_confirm = Message.showOKorCancelMessage("�Ƿ�ɾ����ѡ��?");
							if(b_confirm){
								String sql = "delete from inv_stocktake_detail where STOCKTAKE_NO = '"+txt_stocktake_no.getText().trim()+"' "
										+" and LOCATION_CODE='"+table.getValueAt(table.getSelectedRow(), table.getColumnModel().getColumnIndex("��λ"))+"' "
										+"and CONTAINER_CODE='"+table.getValueAt(table.getSelectedRow(), table.getColumnModel().getColumnIndex("���"))+"' "
										+" and ITEM_CODE= '"+table.getValueAt(table.getSelectedRow(), table.getColumnModel().getColumnIndex("��Ʒ����"))+"' ";
								int t = DBOperator.DoUpdate(sql);
								if(t==0){
									Message.showWarningMessage("ɾ��ʧ��");
								}else{
									table.removeRow((table.getSelectedRow()));
								}
							}
						}
						});
					popupmenu.add(menuItem1);
					popupmenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
		scrollPane.setViewportView(table);
	}
	
	private void getStockTakeDetail(String strWhere){
		String sql = "select isd.STORER_CODE ��������,bs.STORER_NAME ��������,isd.WAREHOUSE_CODE �ֿ����,bw.WAREHOUSE_NAME �ֿ�����,isd.LOCATION_CODE ��λ,isd.CONTAINER_CODE ���"
				+",isd.ITEM_CODE ��Ʒ����,bi.ITEM_BAR_CODE ��Ʒ����,bi.ITEM_NAME ��Ʒ����,isd.CONF_QTY ʵ���̵�����,biu.unit_name ��λ,isd.CREATED_BY_USER �̵���,isd.CREATED_DTM_LOC �̵�ʱ��"
				+" from inv_stocktake_detail isd "
				+"inner join inv_stocktake_header ish on ish.STOCKTAKE_NO = isd.STOCKTAKE_NO "
				+"inner join bas_storer bs on isd.STORER_CODE=bs.STORER_CODE "
				+"inner join bas_warehouse bw on isd.WAREHOUSE_CODE=bw.WAREHOUSE_CODE "
				+"inner join bas_item bi on isd.STORER_CODE=bi.STORER_CODE and isd.ITEM_CODE=bi.ITEM_CODE "
				+"left join bas_item_unit biu on bi.UNIT_CODE=biu.unit_code "
				+"where 1=1 ";
		if(!strWhere.equals("")){
			sql = sql + strWhere;
		}
		sql = sql + " order by isd.LOCATION_CODE,isd.ITEM_CODE";
		DataManager dm = DBOperator.DoSelect2DM(sql);
		if (table.getColumnCount() == 0) {
			table.setColumn(dm.getCols());
		}
		table.removeRowAll();
		table.setData(dm.getDataStrings());
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setColumnEditableAll(false);
		JTableUtil.fitTableColumns(table);
//		table.setSortEnable();
	}
	
	private boolean saveStockDetailByStorer(){
		String sql = null;
		if(cb_by_storer.isSelected()){
			String storerCode = cb_storer.getSelectedOID();
			String warehouseCode = cb_warehouse.getSelectedOID();
			String stockTakeNo = txt_stocktake_no.getText().trim();
			sql = "insert into inv_stocktake_detail(STOCKTAKE_NO,STORER_CODE,WAREHOUSE_CODE,LOCATION_CODE,CONTAINER_CODE,"
					+"ITEM_CODE,GUIDE_QTY,GUIDE_UOM,CONF_QTY,CONF_UOM,FIRST_STOCKTAKE_QTY,FIRST_STOCKTAKE_UOM,CREATED_BY_USER,CREATED_DTM_LOC) "
					+"select '"+stockTakeNo+"','"+storerCode+"','"+warehouseCode+"',ii.LOCATION_CODE,ii.CONTAINER_CODE, "
					+"ii.ITEM_CODE,"
					+"ifnull(sum(ii.ON_HAND_QTY+IN_TRANSIT_QTY-(ALLOCATED_QTY)-(PICKED_QTY)-(INACTIVE_QTY)),0) qty "
					+",item.UNIT_CODE,0,item.UNIT_CODE,0,item.UNIT_CODE,'"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"',now() "
					+"from inv_inventory ii "
					+"left JOIN bas_item item on ii.STORER_CODE=item.STORER_CODE and ii.ITEM_CODE=item.ITEM_CODE "
					+"inner join bas_location bl on ii.WAREHOUSE_CODE=bl.WAREHOUSE_CODE and ii.LOCATION_CODE=bl.LOCATION_CODE "
					+"where ii.STORER_CODE='"+storerCode+"' and ii.WAREHOUSE_CODE='"+warehouseCode+"' and ii.ON_HAND_QTY+IN_TRANSIT_QTY-(ALLOCATED_QTY)-(PICKED_QTY)-(INACTIVE_QTY)>0 "
					+" and bl.LOCATION_TYPE_CODE not in ('Dock') "
					+"group by ii.WAREHOUSE_CODE,ii.STORER_CODE,ii.LOCATION_CODE,ii.CONTAINER_CODE,ii.ITEM_CODE ";
		}
		int t = DBOperator.DoUpdate(sql);
		if(t>0){
			return true;
		}else{
			return false;
		}
	}

	private void setExtendedState(int maximizedBoth) {
		// TODO Auto-generated method stub
		
	}

}
