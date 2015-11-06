package inventory;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import DBUtil.DBConnectionManager;
import DBUtil.DBOperator;
import DBUtil.LogInfo;
import api.HttpClientUtil;
import comUtil.ExcelRead;
import comUtil.JTableExportExcel;
import comUtil.MD5;
import comUtil.chooseFileList;
import comUtil.comData;
import dmdata.DataManager;
import inbound.putawayFrm;
import main.PBSUIBaseGrid;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import sys.InnerFrame;
import sys.JTableUtil;
import sys.MainFrm;
import sys.Message;
import sys.QueryDialog;
import util.WaitingSplash;

import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.SwingWorker;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.FlowLayout;
import javax.swing.JTabbedPane;
import javax.swing.BoxLayout;

public class InvQueryFrm extends InnerFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2144940191537591911L;
	private JPanel contentPane;
	private static InvQueryFrm instance;
	public static boolean isOpen = false;
	private PBSUIBaseGrid table;
	private String strWheres = "";
	private PBSUIBaseGrid table2;
	private JTabbedPane tabPane;
	
	public static synchronized InvQueryFrm getInstance() {
		 if(instance == null) {    
	            instance = new InvQueryFrm();  
	        }  
	        return instance;
		 
	 }
	
	public static synchronized boolean getOpenStatus() {
		 if(instance == null) {    
	            instance = new InvQueryFrm();  
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
					InvQueryFrm frame = new InvQueryFrm();
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
	public InvQueryFrm() {
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.addVetoableChangeListener(new VetoableChangeListener() {

			public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException {
				String name = e.getPropertyName();
				Object value = e.getNewValue();
				if (name.equals("closed") && value.equals(Boolean.TRUE)) // ���ڱ��ر�
				{
					System.out.println("InvQueryFrm���ڱ��ر�");
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
		
		setBounds(100, 100, 450, 297);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		tabPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		tabPane.addTab("AGG\u5E93\u5B58\u67E5\u8BE2", null, panel, null);
		panel.setLayout(new BorderLayout(0, 0));
		tabPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
				int selectedIndex = tabbedPane.getSelectedIndex();
				System.out.println("��ǰѡ���"+selectedIndex);
				if(selectedIndex==1){
					//��������ݣ��Ͳ����ظ�����
					if(table2.getRowCount()>0){
						return;
					}
					//��̨�����̱߳���Excel����
            		new SwingWorker<String, Void>() {
						WaitingSplash splash = new WaitingSplash("������ʷ���ݣ����Ժ�... ...");

			            @Override
			            protected String doInBackground() throws Exception {
			            	splash.start(); // ������������
			            	//��ʾ֮ǰ���ݿ��ֵ
							String sql = "select * from tmp_jd_inv";
							DataManager dm = DBOperator.DoSelect2DM(sql);
							//table ��ʾ����
							if (table2.getColumnCount() == 0) {
		            			table2.setColumn(dm.getCols());
		            		}
		            		table2.removeRowAll();
		            		table2.setData(dm.getDataStrings());
		            		table2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		            		table2.setColumnEditableAll(false);
		            		JTableUtil.fitTableColumnsDoubleWidth(table2);
		            		table2.setSortEnable();
			                return "";
			            }

			            @Override
			            protected void done() {
			            	splash.stop(); // ������������
			                System.out.println("�������");
			            }
			        }.execute();
					
				}
			}

		});
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);
		
		table = new PBSUIBaseGrid();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()>=2){
					table.setColumnSelectionAllowed(true);
				}
			}
		});
		scrollPane.setViewportView(table);
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel.add(panel_1, BorderLayout.NORTH);
		
		JButton btnQuery = new JButton("\u67E5\u8BE2");
		btnQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ArrayList<String> fieldList = new ArrayList<String>();
				fieldList.add("ii.ITEM_CODE:��Ʒ����");
				fieldList.add("bi.ITEM_BAR_CODE:��Ʒ����");
				fieldList.add("bi.ITEM_NAME:��Ʒ����");
				fieldList.add("ii.LOCATION_CODE:��λ����");
				fieldList.add("ii.CONTAINER_CODE:���");
				fieldList.add("ii.WAREHOUSE_CODE:�ֿ����");
				fieldList.add("bw.WAREHOUSE_NAME:�ֿ�����");
				fieldList.add("ii.STORER_CODE:��������");
				fieldList.add("bs.STORER_NAME:��������");
				QueryDialog query = QueryDialog.getInstance(fieldList);
				Toolkit toolkit = Toolkit.getDefaultToolkit();
				int x = (int)(toolkit.getScreenSize().getWidth()-query.getWidth())/2;
				int y = (int)(toolkit.getScreenSize().getHeight()-query.getHeight())/2;
				query.setLocation(x, y);
				query.setVisible(true);
				String retWhere = QueryDialog.queryValueResult;
				if(retWhere.length()>0){
					retWhere = " and "+retWhere;
					strWheres = retWhere;
				}
				if(table.getRowCount()>0){
					table.setRowSelectionInterval(0, 0);//Ĭ��ѡ�е�һ��
				}
				getInvTableData(retWhere);
			}
		});
		panel_1.add(btnQuery);
		
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
		
		JButton btnExport = new JButton("\u5BFC\u51FAExcel");
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTableExportExcel exportExcel = new JTableExportExcel(table); 
				exportExcel.export();
			}
		});
		
		JButton btnRefresh = new JButton("\u5237\u65B0");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				getInvTableData(strWheres);
				if(table.getRowCount()>0){
					table.setRowSelectionInterval(0, 0);//Ĭ��ѡ�е�һ��
				}
			}
		});
		panel_1.add(btnRefresh);
		panel_1.add(btnExport);
		panel_1.add(btnClose);
		
		JPanel panel_2 = new JPanel();
		tabPane.addTab("\u5609\u5883\u901A\u5E93\u5B58\u67E5\u8BE2", null, panel_2, null);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_3.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		panel_2.add(panel_3, BorderLayout.NORTH);
		
		JButton btnExcel2 = new JButton("Excel\u5BFC\u5165");
		btnExcel2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chooseFileList chooseFile = chooseFileList.getInstance();
				Toolkit toolkit = Toolkit.getDefaultToolkit();
				int x = (int)(toolkit.getScreenSize().getWidth()-chooseFile.getWidth())/2;
				int y = (int)(toolkit.getScreenSize().getHeight()-chooseFile.getHeight())/2;
				chooseFile.setLocation(x, y);
				chooseFile.setVisible(true);
				String fileDir = chooseFileList.ResultValue;
				if(fileDir.equals("")) return;
				ExcelRead excel = new ExcelRead(fileDir);
				ArrayList<?> list = null;
				try {
					list = excel.getExcelData();
					if(list==null) return;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (list.size() > 1) {
					String[] rowheader = (String[]) list.get(0);
					if(rowheader[0].equals("")){
						JOptionPane.showMessageDialog(null, "Excel��ʽ����ȷ","��ʾ",JOptionPane.WARNING_MESSAGE);
						return ;
					}
					DataManager dm = comData.list2DataManager(list);
					//table ��ʾ����
					if (table2.getColumnCount() == 0) {
            			table2.setColumn(dm.getCols());
            		}
            		table2.removeRowAll();
            		table2.setData(dm.getDataStrings());
            		table2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            		table2.setColumnEditableAll(false);
            		JTableUtil.fitTableColumnsDoubleWidth(table2);
            		table2.setSortEnable();
            		//��̨�����̱߳���Excel����
            		new SwingWorker<String, Void>() {
						WaitingSplash splash = new WaitingSplash();

			            @Override
			            protected String doInBackground() throws Exception {
			            	splash.start(); // ������������
			            	//���ݱ��浽���ݿ�
		            		comData.addTempTable("tmp_jd_inv", dm);
			                return "";
			            }

			            @Override
			            protected void done() {
			            	splash.stop(); // ������������
			                System.out.println("�������");
			            }
			        }.execute();
			        
            		//��ȡ�ξ�ͨ���
            		refreshTable2(dm);
					
				} else {
					JOptionPane.showMessageDialog(null, "Excel������", "��ʾ", JOptionPane.WARNING_MESSAGE);
				}
//				JOptionPane.showMessageDialog(null, "���ݵ���ɹ���");
//				getHeaderTableData("");
			}
		});
		panel_3.add(btnExcel2);
		
		JButton btnRefresh2 = new JButton("\u5237\u65B0\u5609\u5883\u901A\u5E93\u5B58");
		btnRefresh2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DataManager dm = comData.jtable2DataManager(table2);
				//��ȡ�ξ�ͨ���
        		refreshTable2(dm);
			}
		});
		panel_3.add(btnRefresh2);
		
		JButton btnClose2 = new JButton("\u5173\u95ED");
		btnClose2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnClose.doClick();
			}
		});
		panel_3.add(btnClose2);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		panel_2.add(scrollPane_1, BorderLayout.CENTER);
		
		table2 = new PBSUIBaseGrid();
		table2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()>=2){
					table2.setColumnSelectionAllowed(true);
				}
			}
		});
		scrollPane_1.setViewportView(table2);
		getInvTableData(" and 1<>1 ");
	}
	
	private void getInvTableData(String retWhere){
		String sql = "select ii.WAREHOUSE_CODE �ֿ����,bw.WAREHOUSE_NAME �ֿ�����,ii.STORER_CODE ��������,bs.STORER_NAME ��������,ii.LOCATION_CODE ��λ����,ii.CONTAINER_CODE ���,"
				+"ii.ITEM_CODE ��Ʒ����,bi.ITEM_BAR_CODE ��Ʒ����,bi.ITEM_NAME ��Ʒ����,bi.ITEM_SPEC ��Ʒ���,ii.ON_HAND_QTY �������,ii.ALLOCATED_QTY �ѷ�������,ii.PICKED_QTY �Ѽ������,"
				+"ii.INACTIVE_QTY ��������,biu.unit_name ��λ,ii.lot_no ���κ�"
				+",il.LOTTABLE01 ��������1,il.LOTTABLE02 ��������2,il.LOTTABLE03 ��������3,il.LOTTABLE04 ��������4,il.LOTTABLE05 ��������5,"
				+"il.LOTTABLE06 ��������6,il.LOTTABLE07 ��������7,il.LOTTABLE08 ��������8,il.LOTTABLE09 ��������9,il.LOTTABLE10 ��������10 "
				+",ii.`STATUS` ���״̬,ii.QUALITY �������,ii.INV_INVENTORY_ID ���ID  "
				+"from inv_inventory ii "
				+"inner join bas_location bl on ii.LOCATION_CODE=bl.LOCATION_CODE and ii.WAREHOUSE_CODE=bl.WAREHOUSE_CODE "
				+"inner join bas_item bi on ii.ITEM_CODE=bi.ITEM_CODE and ii.STORER_CODE=bi.STORER_CODE "
				+"inner join bas_item_unit biu on bi.UNIT_CODE = biu.unit_code "
				+"inner join inv_lot il on ii.LOT_NO=il.LOT_NO "
				+"inner join bas_storer bs on ii.STORER_CODE=bs.STORER_CODE "
				+"inner join bas_warehouse bw on ii.WAREHOUSE_CODE=bw.WAREHOUSE_CODE "
				+"where 1=1 and ii.warehouse_code='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' ";
		if(!retWhere.equals("")){
			sql = sql + retWhere;
		}
		DataManager dm = DBOperator.DoSelect2DM(sql);
		if (table.getColumnCount() == 0) {
			table.setColumn(dm.getCols());
		}
		table.removeRowAll();
		table.setData(dm.getDataStrings());
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setColumnEditableAll(false);
		JTableUtil.fitTableColumnsDoubleWidth(table);
		table.setSortEnable();
	}
	
	public void refreshTable2(DataManager dm){
		new SwingWorker<String, Void>() {
			WaitingSplash splash = new WaitingSplash();

            @Override
            protected String doInBackground() throws Exception {
            	//��ȡ�ξ�ͨ���
            	splash.start(); // ������������
            	try{
            		int count = 0;
            		StringBuffer sbf = new StringBuffer();
            		//��� ���ۿ�� ����
            		for(int i=0;i<table2.getRowCount();i++){
            			table2.setValueAt("", i, table2.getColumnModel().getColumnIndex("���ۿ��"));
            		}
            		for(int i=1;i<dm.getCurrentCount();i++){
            			if(!dm.getString("����״̬", i).equals("�����ɹ�")){
            				continue;
            			}
            			count++;
            			String itemCode = dm.getString("��ƷID", i);
            			System.out.println(count+":"+itemCode);
            			sbf.append(dm.getString("��ƷID", i)+",");
            			if(count==20){
            				//ͨ��URL��ȡ�ξ�ͨ���
            				System.out.println(sbf.toString());
                    		getJiaJingTongInventory(sbf.toString());
                    		count=0;
                    		sbf = new StringBuffer();
            			}
            		}
            		//ѭ���������20��ҲҪִ��
            		if(sbf.toString().length()>1){
            			//ͨ��URL��ȡ�ξ�ͨ���
                		getJiaJingTongInventory(sbf.toString());
            		}
            		
            	}catch(Exception e){
            		Message.showWarningMessage(e.getMessage());
            	}
                return "";
            }

            @Override
            protected void done() {
            	splash.stop(); // ������������
                System.out.println("ˢ�¼ξ�ͨ������");
				JOptionPane.showMessageDialog(null, "ˢ�¿�����");
            }
        }.execute();
		
	}
	
	public void getJiaJingTongInventory(String itemList) throws Exception{
		String url = "http://api.kjt.com/open.api";
		String charset = "utf-8";
		HttpClientUtil httpClientUtil = new HttpClientUtil();
		String httpOrgCreateTest = url;
		Map<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("appid", "seller1280");
		hashMap.put("format", "json");
		hashMap.put("method", "Inventory.ChannelQ4SBatchGet");
		hashMap.put("timestamp", getDateTimeString());
		hashMap.put("data", "{\"ProductIDs\":\""+itemList+"\",\"SaleChannelSysNo\":\"44\"}");
		hashMap.put("nonce",(new Random().nextInt(100000000)) + "");
		hashMap.put("version", "1.0");
		String salt = createLinkString(hashMap) + "appsecret=0bae030e6a964214aca12698b4a52d5c";
		String mysign = MD5.GetMD5Code(salt.toLowerCase());
		hashMap.put("sign", mysign);
		String httpOrgCreateTestRtn = httpClientUtil.doPost(httpOrgCreateTest,hashMap,charset);
		System.out.println("result:"+httpOrgCreateTestRtn);
		
		JSONObject jo = new JSONObject();
		JSONObject dataJson = jo.fromObject(httpOrgCreateTestRtn);
		JSONArray data = null;
		if(dataJson.containsKey("Data")){
			data=dataJson.getJSONArray("Data");
			for(int i=0;i<data.size();i++){
				JSONObject info=data.getJSONObject(i);
				String ProductID = info.getString("ProductID");
				String OnlineQty = info.getString("OnlineQty");
				String WareHouseID = info.getString("WareHouseID");
				System.out.println(WareHouseID+" / "+ProductID +" : "+ OnlineQty);
				updateTable2InvQty(ProductID,OnlineQty);
			}
		}else if(dataJson.containsKey("Code")){
			throw new Exception(httpOrgCreateTestRtn);
		}
	}
	
	public void updateTable2InvQty(String itemCode,String invQty){
		for(int i=0;i<table2.getRowCount();i++){
			if(itemCode.equals(table2.getValueAt(i, table2.getColumnModel().getColumnIndex("��ƷID")))){
				table2.setValueAt(invQty, i, table2.getColumnModel().getColumnIndex("���ۿ��"));
//				table2.updateUI();
			}
		}
	}
	
	public String createLinkString(Map<String, String> params) {

		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < keys.size(); i++) {
			sb.append(keys.get(i)).append("=").append(params.get(keys.get(i))).append("&");
		}
		return sb.toString();
	}
	
	public static  String getDateTimeString(){
        Date now = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String result = dateFormat.format(now);
        return  result;
    }

	private void setExtendedState(int maximizedBoth) {
		// TODO Auto-generated method stub
		
	}

}
