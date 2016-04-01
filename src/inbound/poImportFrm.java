package inbound;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.net.URLEncoder;
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
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang.StringEscapeUtils;

import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.Toolkit;

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
import main.PBSUIBaseGrid;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import sys.InnerFrame;
import sys.JTableUtil;
import sys.MainFrm;
import sys.Message;
import sys.QueryDialog;
import sys.tableQueryDialog;
import util.Math_SAM;
import util.MyTableCellRenderrer;
import util.WaitingSplash;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class poImportFrm extends InnerFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1281449283666709019L;
	private JPanel contentPane;
	private static volatile poImportFrm instance;
	private static boolean isOpen = false;
	private PBSUIBaseGrid headerTable;
	private PBSUIBaseGrid detailTable;
	static String retWhere = "";
	
	public static poImportFrm getInstance() {
		 if(instance == null) { 
			 synchronized(poImportFrm.class){
				 if(instance == null) {
					 instance = new poImportFrm();
				 }
			 }
	        }  
	        return instance;
		 
	 }
	
	public static synchronized boolean getOpenStatus() {
		 if(instance == null) {    
	            instance = new poImportFrm();  
	        }  
	        return isOpen;
		 
	 }

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
//		String sql = "select item_code MerchantProductID,TOTAL_QTY Qty from inb_po_detail where PO_NO='PO00000233' ";
//		DataManager podetail = DBOperator.DoSelect2DM(sql);
//		JSONObject dataJson = JSONObject.fromObject(DBOperator.DataManager2JSONString(podetail, "Items"));
//		try {
//			new poImportFrm().openPOAPI("164027000093",dataJson.get("Items").toString());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		//����������POCreate API�ӿڣ�����POԤ��ⵥ
		String warehouseID="54";
		String etaTime = LogInfo.getCurrentDate_Short();
		String assBillNo = "";
		String sql = "select PO_NO,ERP_PO_NO from inb_po_header where PO_NO in('PO00000280')";
		DataManager poHeaderListDM = DBOperator.DoSelect2DM(sql);
		for(int i=0;i<poHeaderListDM.getCurrentCount();i++){
			String po_no = poHeaderListDM.getString("PO_NO", i);
			String ERP_PO_NO = poHeaderListDM.getString("ERP_PO_NO", i);
			assBillNo = ERP_PO_NO;
			sql = "select item_code commoditySn,TOTAL_QTY qty from inb_po_detail where PO_NO='"+po_no+"' ";
			DataManager podetailDM = DBOperator.DoSelect2DM(sql);
			JSONObject dataJson = JSONObject.fromObject(DBOperator.DataManager2JSONString(podetailDM, "Items"));
			try {
				new poImportFrm().poCreateAPI(warehouseID,assBillNo,etaTime,dataJson.get("Items").toString());
				Thread.sleep(1000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					poImportFrm frame = new poImportFrm();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
	}

	/**
	 * Create the frame.
	 */
	public poImportFrm() {
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		BasicInternalFrameUI ui = (BasicInternalFrameUI)this.getUI(); //ȥ��������
//		ui.getNorthPane().setVisible(false);
		
		// ���������б����һ�� VetoableChangeListener��Ϊ��������ע���������
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.addVetoableChangeListener(new VetoableChangeListener() {

			public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException {
				e.getSource();
				String name = e.getPropertyName();
				Object value = e.getNewValue();
				if (name.equals("closed") && value.equals(Boolean.TRUE)) // ���ڱ��ر�
				{
					System.out.println("poImportFrm���ڱ��ر�");
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
		
		setBounds(100, 100, 599, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel topPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) topPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		contentPane.add(topPanel, BorderLayout.NORTH);
		
		JButton btnQuery = new JButton("\u67E5\u8BE2");
		btnQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ArrayList<String> fieldList = new ArrayList<String>();
				fieldList.add("iph.PO_NO:PO��");
				fieldList.add("iph.ERP_PO_NO:ERP_PO_NO��");
				fieldList.add("iph.WAREHOUSE_CODE:�ֿ����");
				fieldList.add("iph.STORER_CODE:��������");
				fieldList.add("ipd.ITEM_CODE:��Ʒ����");
				fieldList.add("bi.ITEM_NAME:��Ʒ����");
				QueryDialog query = QueryDialog.getInstance(fieldList);
				Toolkit toolkit = Toolkit.getDefaultToolkit();
				int x = (int)(toolkit.getScreenSize().getWidth()-query.getWidth())/2;
				int y = (int)(toolkit.getScreenSize().getHeight()-query.getHeight())/2;
				query.setLocation(x, y);
				query.setVisible(true);
				retWhere = QueryDialog.queryValueResult;
				if(retWhere.length()>0){
					retWhere = " and "+retWhere;
				}
				if(headerTable.getRowCount()>0){
					headerTable.setRowSelectionInterval(0, 0);//Ĭ��ѡ�е�һ��
				}
				getHeaderTableData(retWhere);
			}
		});
		topPanel.add(btnQuery);
		
		JButton btnExcelImport = new JButton("Excel\u5BFC\u5165");
		btnExcelImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chooseFileList chooseFile = chooseFileList.getInstance();
				Toolkit toolkit = Toolkit.getDefaultToolkit();
				int x = (int)(toolkit.getScreenSize().getWidth()-chooseFile.getWidth())/2;
				int y = (int)(toolkit.getScreenSize().getHeight()-chooseFile.getHeight())/2;
				chooseFile.setLocation(x, y);
				chooseFile.setVisible(true);
				
				//
				new SwingWorker<String, Void>() {
					WaitingSplash splash = new WaitingSplash();
		            @Override
		            protected String doInBackground() throws Exception {
		            	//
		            	splash.start(); // ������������
		            	String fileDir = chooseFileList.ResultValue;
						if(fileDir.equals("")) return "";
						ExcelRead excel = new ExcelRead(fileDir);
						ArrayList<?> list = null;
						StringBuffer poNoList = new StringBuffer();
						DataManager excelDM = new DataManager();
						try {
							list = excel.getExcelData();
							if(list==null) return "";
							excelDM = comData.list2DataManager(list);
						} catch (Exception e) {
							LogInfo.appendLog(e.getMessage());
							e.printStackTrace();
						}
		            	if (list.size() > 1) {
							String[] rowheader = (String[]) list.get(0);
							if(!checkExcelData(excelDM)){
								JOptionPane.showMessageDialog(null, "Excel��ʽ����ȷ","��ʾ",JOptionPane.WARNING_MESSAGE);
								return "";
							}
							
							int line = 0;
							boolean changeLine = false;
							try {
								String sql = "";
//								java.sql.Connection con = DBConnectionManager.getInstance().getConnection("wms");
//								java.sql.Statement stmt = con.createStatement();
//								ResultSet rs;
								StringBuffer sbf = new StringBuffer();
								for (int i = 0; i <excelDM.getCurrentCount() ; i++) {
									if(excelDM.getString("�ֿ����", i).equals("")) continue;// Excel�հ��ַ����Զ�����
									int nextRow = (i==excelDM.getCurrentCount()?i:i+1);
									//����Excel��erp_po_no�Զ������к�
									if(excelDM.getString("���˵���", i).equals(excelDM.getString("���˵���", nextRow))){
										if(changeLine){
											line = 1;
										}else{
											line++;
										}
										changeLine = false;
									}else{
										line++;
										changeLine = true;
									}
									System.out.println(i+":"+line);
									
									String PO_NO = "";
									String POStatus = "";
									String WAREHOUSE_CODE = excelDM.getString("�ֿ����", i);
									String STORER_CODE = getStorerCodeByName(excelDM.getString("����", i));
									if(STORER_CODE.equals("")){
										sbf.append("\n��������ȷ:"+excelDM.getString("����", i)+"("+fileDir+")\n");
										continue;
									}
									String VENDOR_CODE = "001";//Ĭ��001
									String ERP_PO_NO = excelDM.getString("���˵���", i);
									String LINE_NUMBER = String.valueOf(line);//row[4];
									String ITEM_CODE = excelDM.getString("�Ϻ�", i);
									String ITEM_BAR_CODE = excelDM.getString("ʵ�ʵ�����Ʒ����", i);
									boolean bool = checkItemCode(STORER_CODE,ITEM_CODE,ITEM_BAR_CODE,ERP_PO_NO);//������Ϻ��Ƿ���ڣ���������ڣ���AOS����
									if(!bool){
										sbf.append("\n���ѱ�����Ʒ��Ϣ�ܱ��е�����Ʒ��Ϣʧ��,���Ϻ�= ��"+ITEM_CODE+"��"+"("+fileDir+")");
									}
									//����  ʵ�ʵ�����Ʒ����  Ϊ׼�����  ʵ�ʵ�����Ʒ���� = ��  ����ȡ   ������Ʒ����
									if(!ITEM_BAR_CODE.trim().equals("")){
										String sqlUpdate = "update bas_item set ITEM_BAR_CODE='"+ITEM_BAR_CODE+"' "
												+ "where STORER_CODE='"+STORER_CODE+"' and ITEM_CODE='"+ITEM_CODE+"'";
										int t = DBOperator.DoUpdate(sqlUpdate);
									}
									String TOTAL_QTY = comData.checkDouble(excelDM.getString("ʵ����������ϼ�", i));
									String UOM = excelDM.getString("��λ", i);
									String DAMAGE_QTY = comData.checkDouble(excelDM.getString("�д�Ʒ", i));
									String DAMAGE_UOM =excelDM.getString("��λ", i);
									String SCRAP_QTY = comData.checkDouble(excelDM.getString("����Ʒ", i));
									String SCRAP_UOM = excelDM.getString("��λ", i);
									String LOTTABLE01 = excelDM.getString("������", i);
									String LOTTABLE02 = "";
									String LOTTABLE03 = "";
									String LOTTABLE04 = "";
									String LOTTABLE05 = "";
									String LOTTABLE06 = "";
									String LOTTABLE07 = "";
									String LOTTABLE08 = "";
									String LOTTABLE09 = "";
									String LOTTABLE10 = ERP_PO_NO;
									String USER_DEF1 = excelDM.getString("��������", i);
									String USER_DEF2 = excelDM.getString("����ԭ��", i);
									String USER_DEF3 = excelDM.getString("���ʱ��", i);
									String USER_DEF4 = excelDM.getString("���ʱ��", i);
									String USER_DEF5 = excelDM.getString("�����˾", i);
									String USER_DEF6 = excelDM.getString("�����Ա", i);
									String USER_DEF7 = excelDM.getString("���ع�˾", i);
									String REMARK = excelDM.getString("��ע", i);
									String status = checkERPPOStatus(STORER_CODE,ERP_PO_NO);
									if(!status.equals("100") && !status.equals("")){
										sbf.append("\n"+excelDM.getRow(i).toString()+" �Ѿ���ʼ�ջ������Դ��е���\n");
										continue;
									}
									sql = "select po_no,status from inb_po_header where storer_code='"+STORER_CODE+"' and erp_po_no='" + ERP_PO_NO + "'";
									DataManager poDM = DBOperator.DoSelect2DM(sql);
									if (poDM.getCurrentCount()>0) {
										PO_NO = comData.getValueFromBasNumRule("inb_po_header", "po_no");
										poNoList.append("'"+PO_NO+"',");
										//�����ͷ
										sql = "insert into inb_po_header(po_no,warehouse_code,storer_code,vendor_code,erp_po_no,remark,created_dtm_loc,created_by_user,updated_dtm_loc,updated_by_user)"
												+ " select '" + PO_NO + "','" + WAREHOUSE_CODE + "','" + STORER_CODE + "','" + VENDOR_CODE
												+ "','" + ERP_PO_NO + "','"+REMARK+"',now(),'"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"',now(),'"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"' ";
										int t = DBOperator.DoUpdate(sql);
										if (t != 1) {
											JOptionPane.showMessageDialog(null, "����PO��ͷ����\n" + sql, "����",
													JOptionPane.ERROR_MESSAGE);
											LogInfo.appendLog("error","����PO��ͷ����\n" + sql);
										}
										
										//������ϸ
										sql = "insert into inb_po_detail(inb_po_header_id,line_number,po_no,erp_po_no,warehouse_code,storer_code,"
												+ "item_code,total_qty,uom,DAMAGE_QTY,DAMAGE_UOM,SCRAP_QTY,SCRAP_UOM,"
												+ "LOTTABLE01,LOTTABLE02,LOTTABLE03,LOTTABLE04,LOTTABLE05,LOTTABLE06,LOTTABLE07,LOTTABLE08,LOTTABLE09,LOTTABLE10,"
												+ "USER_DEF1,USER_DEF2,USER_DEF3,USER_DEF4,USER_DEF5,USER_DEF6,USER_DEF7,"
												+ "created_dtm_loc,created_by_user,updated_dtm_loc,updated_by_user) "
												+ "select (select inb_po_header_id from inb_po_header where ERP_PO_NO='"
												+ ERP_PO_NO + "' and WAREHOUSE_CODE='"+WAREHOUSE_CODE+"' order by inb_po_header_id desc limit 1)," + "'"+LINE_NUMBER+"','" + PO_NO + "','" + ERP_PO_NO + "','"
												+ WAREHOUSE_CODE +"','"+STORER_CODE+"','"+ITEM_CODE+"','"+TOTAL_QTY+"','"+UOM+"'," 
												+"'"+DAMAGE_QTY+"','"+DAMAGE_UOM+"','"+SCRAP_QTY+"','"+SCRAP_UOM+"',"
												+ "'"+LOTTABLE01+"','"+LOTTABLE02+"','"+LOTTABLE03+"','"+LOTTABLE04+"','"+LOTTABLE05+"'," 
												+ "'"+LOTTABLE06+"','"+LOTTABLE07+"','"+LOTTABLE08+"','"+LOTTABLE09+"','"+LOTTABLE10+"',"  
												+"'"+USER_DEF1+"','"+USER_DEF2+"','"+USER_DEF3+"','"+USER_DEF4+"','"+USER_DEF5+"','"+USER_DEF6+"','"+USER_DEF7+"'"
												+ ",now(),'"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"',now(),'"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"'" ;
										t = DBOperator.DoUpdate(sql);
										if (t != 1) {
											JOptionPane.showMessageDialog(null, "����PO����ϸ����\n" + sql, "����",
													JOptionPane.ERROR_MESSAGE);
											LogInfo.appendLog("error","����PO����ϸ����\n" + sql);
										}
									}else{
										PO_NO = poDM.getString("po_no",0);
										POStatus = poDM.getString("status",0);
										if(!POStatus.equals("100")){
											sbf.append("\nPO:"+PO_NO+" ERP_PO_NO:"+ERP_PO_NO+" ��Ʒ����:"+ITEM_CODE+" ��¼���ڣ����Ҷ���״̬�ǳ�ʼ״̬�����Դ��е���\n");
											continue;
										}
										sql = "select po_no from inb_po_detail where po_no='"+PO_NO+"' and line_number = "+LINE_NUMBER+" ";
										DataManager poDetailDM = DBOperator.DoSelect2DM(sql);
										if(poDetailDM.getCurrentCount()>0){
											System.out.println("PO��ϸ�ظ������Ը�������:"+PO_NO+" "+LINE_NUMBER);
											continue;
										}
										//������ϸ
										sql = "insert into inb_po_detail(inb_po_header_id,line_number,po_no,erp_po_no,warehouse_code,storer_code,"
												+ "item_code,total_qty,uom,DAMAGE_QTY,DAMAGE_UOM,SCRAP_QTY,SCRAP_UOM,"
												+ "LOTTABLE01,LOTTABLE02,LOTTABLE03,LOTTABLE04,LOTTABLE05,LOTTABLE06,LOTTABLE07,LOTTABLE08,LOTTABLE09,LOTTABLE10,"
												+ "USER_DEF1,USER_DEF2,USER_DEF3,USER_DEF4,USER_DEF5,USER_DEF6,USER_DEF7,"
												+ "created_dtm_loc,created_by_user,updated_dtm_loc,updated_by_user) "
												+ "select (select inb_po_header_id from inb_po_header where ERP_PO_NO='"
												+ ERP_PO_NO + "')," + "'"+LINE_NUMBER+"','" + PO_NO + "','" + ERP_PO_NO + "','"
												+ WAREHOUSE_CODE +"','"+STORER_CODE+"','"+ITEM_CODE+"','"+TOTAL_QTY+"','"+UOM+"'," 
												+"'"+DAMAGE_QTY+"','"+DAMAGE_UOM+"','"+SCRAP_QTY+"','"+SCRAP_UOM+"',"
												+ "'"+LOTTABLE01+"','"+LOTTABLE02+"','"+LOTTABLE03+"','"+LOTTABLE04+"','"+LOTTABLE05+"'," 
												+ "'"+LOTTABLE06+"','"+LOTTABLE07+"','"+LOTTABLE08+"','"+LOTTABLE09+"','"+LOTTABLE10+"',"  
												+"'"+USER_DEF1+"','"+USER_DEF2+"','"+USER_DEF3+"','"+USER_DEF4+"','"+USER_DEF5+"','"+USER_DEF6+"','"+USER_DEF7+"'"
												+ ",now(),'"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"',now(),'"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"'" ;
										int t = DBOperator.DoUpdate(sql);
										if (t != 1) {
											JOptionPane.showMessageDialog(null, "����PO����ϸ����\n" + sql, "����",
													JOptionPane.ERROR_MESSAGE);
											LogInfo.appendLog("error","����PO����ϸ����\n" + sql);
										}
									}
								}
								if(sbf.toString().length()>1){
									Message.showWarningMessage(sbf.toString());
									LogInfo.appendLog("error",sbf.toString());
								}
								//����������POCreate API�ӿڣ�����POԤ��ⵥ
								String warehouseID="54";
								String etaTime = LogInfo.getCurrentDate_Short();
								String assBillNo = "";
								poNoList.append("''");
								sql = "select PO_NO,ERP_PO_NO from inb_po_header where PO_NO in("+poNoList+")";
								DataManager poHeaderListDM = DBOperator.DoSelect2DM(sql);
								for(int i=0;i<poHeaderListDM.getCurrentCount();i++){
									String po_no = poHeaderListDM.getString("PO_NO", i);
									String ERP_PO_NO = poHeaderListDM.getString("ERP_PO_NO", i);
									assBillNo = ERP_PO_NO;
									sql = "select item_code commoditySn,TOTAL_QTY qty from inb_po_detail where PO_NO='"+po_no+"' ";
									DataManager podetailDM = DBOperator.DoSelect2DM(sql);
									JSONObject dataJson = JSONObject.fromObject(DBOperator.DataManager2JSONString(podetailDM, "Items"));
									poCreateAPI(warehouseID,assBillNo,etaTime,dataJson.get("Items").toString());
									Thread.sleep(1000);
								}
								
							} catch (Exception e) {
								e.printStackTrace();
								LogInfo.appendLog(e.getMessage());
							}
						} else {
							JOptionPane.showMessageDialog(null, "Excel������", "��ʾ", JOptionPane.WARNING_MESSAGE);
						}
		            	getHeaderTableData("");
		                return "";
		            }

		            @Override
		            protected void done() {
		            	splash.stop(); // ������������
		            	if(!chooseFileList.ResultValue.equals("")){
		            		System.out.println("Excel�������");
							JOptionPane.showMessageDialog(null, "Excel�������");
		            	}else{
		            		System.out.println("Excel����ȡ��");
							JOptionPane.showMessageDialog(null, "Excel����ȡ��");
		            	}
		                
		            }
		        }.execute();
			}
		});
		topPanel.add(btnExcelImport);
		
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
		
		JButton btnExcel = new JButton("Excel\u5BFC\u5165\u6A21\u677F\u4E0B\u8F7D");
		btnExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JTableExportExcel exportExcel = new JTableExportExcel();
				DataManager dm = new DataManager();
//				String sql = "select '�ֿ���룬���纼�ݣ�HZ' WAREHOUSE_CODE,'�������룬������ƣ�1117' STORER_CODE,'��Ӧ�̱��룬Ĭ��001' VENDOR_CODE,'�ᵥ��' ERP_PO_NO,'�к�' LINE_NUMBER,'�Ϻ�/��Ʒ����' ITEM_CODE,'����' TOTAL_QTY,'��λ' UOM,"
//						+ "'��������1' LOTTABLE01,'��������2' LOTTABLE02,'��������3' LOTTABLE03,'��������4' LOTTABLE04,'��������5' LOTTABLE05,'��������6' LOTTABLE06,'��������7' LOTTABLE07,'��������8' LOTTABLE08,'��������9' LOTTABLE09,'������10' LOTTABLE10 ";
				String sql = "select '' ���,'' �ֿ����,'' ����,'' ���˵���,'' ����Ʒ��,'' �Ϻ�,'' �����������,	'' ��λ,'' �ϸ�Ʒ,'' �д�Ʒ,'' ����Ʒ,'' ʵ����������ϼ�,"
					+ "'' ��������,'' ����ԭ��,'' ������Ʒ����,'' ʵ�ʵ�����Ʒ����,'' ������,'' ���ʱ��,'' ���ʱ��,'' �����˾,'' �����Ա,'' ���ع�˾ ";
				dm = DBOperator.DoSelect2DM(sql);
				exportExcel.exportExcelFromDataManagerByCols(dm);
			}
		});
		topPanel.add(btnExcel);
		
		JButton btnExcelExport = new JButton("Excel\u5BFC\u51FA");
		btnExcelExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				StringBuffer sbf = new StringBuffer();
				int[] selRow = headerTable.getSelectedRows();
				for(int i=0;i<selRow.length;i++){
					String po_no = headerTable.getValueAt(selRow[i], headerTable.getColumnModel().getColumnIndex("PO��")).toString();
					sbf.append("'"+po_no+"'");
					if(i<selRow.length-1){
						sbf.append(",");
					}
				}
				JTableExportExcel exportExcel = new JTableExportExcel();
				DataManager dm = new DataManager();
				String sql = "select a.WAREHOUSE_CODE �ֿ����,e.WAREHOUSE_NAME �ֿ�����,a.PO_NO PO��,a.ERP_PO_NO �ᵥ��,"
						+ "case a.status when '100' then '�½�' when '300' then '�ջ���' when '900' then '�ر�' else a.status end PO״̬,"
						+ "a.STORER_CODE ��������,d.STORER_NAME ��������,"
						+"a.REMARK ��ע,a.CREATED_DTM_LOC ����ʱ��,f.USER_NAME ������,"
						+"b.ITEM_CODE ���Ϻ�,c.ITEM_BAR_CODE ����,c.ITEM_NAME ��Ʒ����,b.UOM ��λ,b.TOTAL_QTY ����,b.DAMAGE_QTY �д�����,b.SCRAP_QTY ��������,"
						+"b.LOTTABLE01 ��������1,b.LOTTABLE02 ��������2,b.LOTTABLE03 ��������3,b.LOTTABLE04 ��������4,b.LOTTABLE05 ��������5,"
						+"b.LOTTABLE06 ��������6,b.LOTTABLE07 ��������7,b.LOTTABLE08 ��������8,b.LOTTABLE09 ��������9,b.LOTTABLE10 ��������10,"
						+"b.UPDATED_DTM_LOC ����ʱ��,g.USER_NAME �����û� "
						+"from inb_po_header a "
						+"inner join inb_po_detail b on a.PO_NO=b.PO_NO "
						+"inner join bas_item c on b.ITEM_CODE=c.ITEM_CODE and b.STORER_CODE=c.STORER_CODE "
						+"inner join bas_storer d on a.STORER_CODE=d.STORER_CODE "
						+"inner join bas_warehouse e on a.WAREHOUSE_CODE=e.WAREHOUSE_CODE "
						+"inner join sys_user f on a.CREATED_BY_USER=f.USER_CODE "
						+"inner join sys_user g on b.UPDATED_BY_USER=g.USER_CODE "
						+"where a.WAREHOUSE_CODE='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' "
						+"and a.PO_NO in ("+sbf.toString()+")"
						+""
						+"";
				dm = DBOperator.DoSelect2DM(sql);
				exportExcel.exportExcelFromDataManagerByCols(dm);
			}
		});
		btnExcelExport.setForeground(new Color(51, 0, 255));
		topPanel.add(btnExcelExport);
		topPanel.add(btnClose);
		
		JPanel centerPanel = new JPanel();
		contentPane.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new GridLayout(2, 1, 0, 0));
		
		JPanel headerPanel = new JPanel();
		centerPanel.add(headerPanel);
		headerPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		headerPanel.add(scrollPane, BorderLayout.CENTER);
//		headerPanel.setPreferredSize(new Dimension(100, 100));
		
		headerTable = new PBSUIBaseGrid();
		headerTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()>=2){
					headerTable.setColumnSelectionAllowed(true);
				}
				headerTableClick();
			}
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					JPopupMenu popupmenu = new JPopupMenu();
					JMenuItem menuItem1 = new JMenuItem();
					menuItem1.setLabel("����ȷ��");
					menuItem1.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(ActionEvent e) {
							if(!comData.getUserMenuPower("PO����-ȷ��")){
								Message.showWarningMessage("�޴˹���Ȩ��");
								return;
							}
							if(!MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0).toString().equalsIgnoreCase("SHJD")){
								Message.showWarningMessage("�˹���Ŀǰֻ��ԡ��Ϻ��ζ��֡�����");
								return;
							}
							boolean b_confirm = Message.showOKorCancelMessage("PO����ȷ�Ϻ󣬾Ͳ�����ɾ����ͬʱϵͳ���ӿ�棬�Ƿ�ȷ��?");
							if(b_confirm){
								new SwingWorker<String, Void>() {
									WaitingSplash splash = new WaitingSplash("���ݴ����У����Ժ�... ...");

						            @Override
						            protected String doInBackground() throws Exception {
						            	splash.start(); // ������������
						            	StringBuffer sbf = new StringBuffer();
										int[] selRow = headerTable.getSelectedRows();
										for(int i=0;i<selRow.length;i++){
											String po_no = headerTable.getValueAt(selRow[i], headerTable.getColumnModel().getColumnIndex("PO��")).toString();
											String sql = "select PO_NO from inb_po_header where PO_NO='"+po_no+"' and STATUS<900 ";
											DataManager dmtmp = DBOperator.DoSelect2DM(sql);
											if(dmtmp.getCurrentCount()==0){
												po_no = "";
											}
											sbf.append("'"+po_no+"'");
											if(i<selRow.length-1){
												sbf.append(",");
											}
										}
										//����PO��ϸ������   ֻ����Ϻ��ζ��ֿ�
										boolean bool = generateInventoryByPO(sbf.toString());
										if(bool){
											//����PO״̬�� �½� -> �ջ��У���ֹ�������޸Ļ���ɾ�� 
											String sql = "update inb_po_header set status='900' where po_no in ("+sbf.toString()+") and status < 900 ";
											int t = DBOperator.DoUpdate(sql);
											if(t>0){
												//getHeaderTableData(retWhere);
												//д�������������  sandwich.ag_product_lock
//												sql = "insert into sandwich.ag_sku_batch_lock(ag_product_id,ag_batch,ag_total_num,ag_safe_num,created_time) "
//													+ "select (select product_id from sandwich.ag_product where part_number=ITEM_CODE limit 1) product_id,ERP_PO_NO "
//													+ ",sum(TOTAL_QTY) ag_total_num,0 ag_safe_num,now() "
//													+ "from inb_po_detail where WAREHOUSE_CODE='SHJD'  and PO_NO in ("+sbf.toString()+") "
//													+ "group by STORER_CODE,ITEM_CODE,ERP_PO_NO ";
//												t = DBOperator.DoUpdate(sql);
//												if(t>0){
												//֪ͨ�ⲿ�ӿڣ�PO�Ѿ�����ջ�
												for(int i=0;i<selRow.length;i++){
													String po_no = headerTable.getValueAt(selRow[i], headerTable.getColumnModel().getColumnIndex("PO��")).toString();
													String ERP_PO_NO = headerTable.getValueAt(selRow[i], headerTable.getColumnModel().getColumnIndex("ERP_PO_NO")).toString();
													sql = "select item_code MerchantProductID,TOTAL_QTY Qty from inb_po_detail where PO_NO='"+po_no+"' ";
													DataManager podetail = DBOperator.DoSelect2DM(sql);
													JSONObject dataJson = JSONObject.fromObject(DBOperator.DataManager2JSONString(podetail, "Items"));
													openPOAPI(ERP_PO_NO,dataJson.get("Items").toString());
													Thread.sleep(1000);
												}
//												}else{
//													Message.showErrorMessage("д�������������  sandwich.ag_product_lock ʧ�ܣ�����ϵ����Ա");
//													LogInfo.appendLog("error","д�������������  sandwich.ag_product_lock ʧ�ܣ�����ϵ����Ա:"+sql);
//												}	
											}
										}
						                return "";
						            }

						            @Override
						            protected void done() {
						            	splash.stop(); // ������������
						                System.out.println("�������");
						                getHeaderTableData(retWhere);
						            }
						        }.execute();
								
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
					JPopupMenu popupmenu = new JPopupMenu();
					JMenuItem menuItem1 = new JMenuItem();
					menuItem1.setLabel("��ѯ����Ʒ���");
					menuItem1.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(ActionEvent e) {
							int header_row = headerTable.getSelectedRow();
							int header_column = headerTable.getSelectedColumn();
							int detail_row = detailTable.getSelectedRow();
							int detail_column = detailTable.getSelectedColumn();
							String warrehouseCode = headerTable.getValueAt(header_row, headerTable.getColumnModel().getColumnIndex("�ֿ����")).toString();
							String storerCode = headerTable.getValueAt(header_row, headerTable.getColumnModel().getColumnIndex("��������")).toString();
							String itemCode = detailTable.getValueAt(detail_row, detailTable.getColumnModel().getColumnIndex("��Ʒ����")).toString();
							String sql = "select ii.INV_INVENTORY_ID ���ID,bi.ITEM_BAR_CODE ��Ʒ����,ii.ITEM_CODE ��Ʒ����,bi.ITEM_NAME ��Ʒ����,"
									+ "bi.RETAIL_PRICE ���ۼ�,ii.ON_HAND_QTY �ܿ��,ii.ALLOCATED_QTY �ѷ�������,ii.PICKED_QTY �Ѽ������,ii.ON_HAND_QTY+ii.IN_TRANSIT_QTY-(ii.ALLOCATED_QTY)-(ii.PICKED_QTY) ʵ�ʿ��ÿ��, "
									+"biu.unit_name ��λ,ii.LOCATION_CODE ��λ,ii.CONTAINER_CODE ���,ii.LOT_NO ����"
									+",il.LOTTABLE01 ��������1,il.LOTTABLE02 ��������2,il.LOTTABLE03 ��������3,il.LOTTABLE04 ��������4,il.LOTTABLE05 ��������5"
									+",il.LOTTABLE06 ��������6,il.LOTTABLE07 ��������7,il.LOTTABLE08 ��������8,il.LOTTABLE09 ��������9,il.LOTTABLE10 ��������10 "
									+"from inv_inventory ii "
									+"inner join bas_item bi on ii.STORER_CODE=bi.STORER_CODE and ii.ITEM_CODE=bi.ITEM_CODE "
									+"left join bas_item_unit biu on bi.UNIT_CODE=biu.unit_code "
									+"inner join inv_lot il on ii.LOT_NO=il.LOT_NO "
									+"where ii.warehouse_code='"+warrehouseCode+"' and ii.storer_code='"+storerCode+"' "
									+ "and ii.item_code='"+itemCode+"' ";
							tableQueryDialog tableQuery = new tableQueryDialog(sql,false);
							Toolkit toolkit = Toolkit.getDefaultToolkit();
							int x = (int)(toolkit.getScreenSize().getWidth()-tableQuery.getWidth())/2;
							int y = (int)(toolkit.getScreenSize().getHeight()-tableQuery.getHeight())/2;
							tableQuery.setLocation(x, y);
							tableQuery.setModal(true);
							tableQuery.setVisible(true);
						}
						});
					popupmenu.add(menuItem1);
					popupmenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
		scrollPane_1.setViewportView(detailTable);
		//detailTable
		detailTable.setColumnEditableAll(false);
		String[] RDColumnNames = {"PO�к�","��Ʒ����","��Ʒ����","��Ʒ����","��λ","����","�д�����","��������","��������",
				"��������1","��������2","��������3","��������4","��������5","��������6","��������7","��������8","��������9","��������10",
				"����ʱ��","�����û�"};
		detailTable.setColumn(RDColumnNames);
		
		JPanel bottomPanel = new JPanel();
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
		
		getHeaderTableData("");
	}
	
	public static  String getDateTimeString(){
        Date now = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String result = dateFormat.format(now);
        return  result;
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
	
	public String createLinkString_AGG(Map<String, String> params) {
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		String prestr = "";
		for (int i = 0; i < keys.size(); i++) {
			if (i < keys.size() - 1) {
				prestr = prestr + ((String) keys.get(i)) + "=" + (String) params.get(keys.get(i)) + "&";
			} else {
				prestr = prestr + ((String) keys.get(i)) + "=" + (String) params.get(keys.get(i));
			}
		}
		return prestr;
	}

	
	public void openPOAPI(String ERP_PO_NO,String Items) throws Exception{
		String url = "http://api.ajyaguru.com/openAPI.html";
		String charset = "utf-8";
		HttpClientUtil httpClientUtil = new HttpClientUtil();
		String httpOrgCreateTest = url;
		Map<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("appid", "seller1280");
		hashMap.put("format", "json");
		hashMap.put("method", "Inventory.ProductInStockNoSign");
		hashMap.put("timestamp", getDateTimeString());
		hashMap.put("data", "{\"POID\":\""+ERP_PO_NO+"\",\"WarehouseID\":\"54\",\"Items\":"+Items+"}");
		hashMap.put("nonce",(new Random().nextInt(100000000)) + "");
		hashMap.put("version", "1.0");
		String salt = createLinkString(hashMap) + "appsecret=0bae030e6a964214aca12698b4a52d5c";
		System.out.println(salt);
		String mysign = MD5.GetMD5Code(salt);
		hashMap.put("sign", mysign);
		String httpOrgCreateTestRtn = httpClientUtil.doPost(httpOrgCreateTest,hashMap,charset);
		System.out.println("result:"+httpOrgCreateTestRtn);
		LogInfo.appendLog("API",httpOrgCreateTestRtn);
		JSONObject dataJson = JSONObject.fromObject(httpOrgCreateTestRtn);
		JSONArray data = null;
		if(dataJson.containsKey("Data")){
//			data=dataJson.getJSONArray("Data");
//			for(int i=0;i<data.size();i++){
//				JSONObject info=data.getJSONObject(i);
//				String ProductID = info.getString("ProductID");
//				String OnlineQty = info.getString("OnlineQty");
//				String WareHouseID = info.getString("WareHouseID");
//				System.out.println(WareHouseID+" / "+ProductID +" : "+ OnlineQty);
//			}
		}else if(dataJson.containsKey("Code")){
			throw new Exception(httpOrgCreateTestRtn);
		}
	}
	
	public void poCreateAPI(String warehouseID,String assBillNo,String etaTime,String Items) throws Exception{
		String url = "http://api.ajyaguru.com/openAPI.html";
		String charset = "utf-8";
		HttpClientUtil httpClientUtil = new HttpClientUtil();
		String httpOrgCreateTest = url;
		/* ������ֵ����map */
		Map<String, String> map = new HashMap<String, String>();
		map.put("appid", "ed76a13b11b26e90246f365ceea1b3f9db0c40f8");
		map.put("format", "json");
		map.put("method", "POCreate");
		map.put("tstamp", (new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())));

		map.put("nonce", ((int) ((Math.random() * 9 + 1) * 100000)) + "");
		map.put("version", "1.0");
		String prestr = createLinkString_AGG(map);
		System.out.println(prestr);
		System.out.println(URLEncoder.encode(prestr).toLowerCase() + "8ece581d7ea7e37652579dbf0c5d08d9");
		String sign = MD5.GetMD5Code(URLEncoder.encode(prestr).toLowerCase() + "8ece581d7ea7e37652579dbf0c5d08d9").toLowerCase();
		System.out.println(sign);
		map.put("sign", sign);
		map.put("data", "{\"thdNum\":\""+assBillNo+"\",\"assBillNo\":\""+assBillNo+"\",\"warehouseID\":\""+warehouseID+"\",\"etaTime\":\""+etaTime+"\",\"Items\":"+Items+"}");
		
		String httpOrgCreateTestRtn = httpClientUtil.doPost(httpOrgCreateTest,map,charset);
		System.out.println("result:"+httpOrgCreateTestRtn);
		LogInfo.appendLog("API",httpOrgCreateTestRtn);
		JSONObject dataJson = JSONObject.fromObject(httpOrgCreateTestRtn);
		if(dataJson.containsKey("Data")){
		}else if(dataJson.containsKey("Code")){
			throw new Exception(httpOrgCreateTestRtn);
		}
	}
	
	private void headerTableClick(){
		int r= headerTable.getSelectedRow();
        if(headerTable.getRowCount()>0){
	        String str_po_no = headerTable.getValueAt(r, headerTable.getColumnModel().getColumnIndex("PO��")).toString();
	        getDetailTableData(" and ipd.po_no='"+str_po_no+"'");
        }
	}
	
	private void getHeaderTableData(String strWhere){
		String sql = "select distinct iph.WAREHOUSE_CODE �ֿ����,bw.WAREHOUSE_NAME �ֿ�����,iph.PO_NO PO��,iph.ERP_PO_NO ERP_PO_NO,"
				+ "case iph.status when '100' then '�½�' when '300' then '�ջ���' when '900' then '�ر�' else iph.status end PO״̬,"
				+ "iph.STORER_CODE ��������,bs.STORER_NAME ��������,iph.VENDOR_CODE ��Ӧ�̱���,bv.VENDOR_NAME ��Ӧ������,iph.REMARK ��ע, "
				+ "iph.CREATED_DTM_LOC ����ʱ��,user.USER_NAME ������ "
				+ " from inb_po_header iph  " 
				+ " inner join bas_warehouse bw on iph.WAREHOUSE_CODE=bw.WAREHOUSE_CODE"
				+ " inner join bas_storer bs on iph.STORER_CODE=bs.STORER_CODE"
				+ " inner join bas_vendor bv on bv.VENDOR_CODE=iph.VENDOR_CODE" 
				+ " inner join sys_user user on iph.CREATED_BY_USER=user.user_code "
				+ " inner join inb_po_detail ipd on iph.PO_NO=ipd.PO_NO "
				+ " inner join bas_item bi on iph.STORER_CODE=bi.STORER_CODE and ipd.ITEM_CODE=bi.ITEM_CODE "
				+ " where 1=1 " 
				+ " and iph.WAREHOUSE_CODE = '"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' ";
		if(!strWhere.equals("")){
			sql = sql + strWhere;
		}
		DataManager dm = DBOperator.DoSelect2DM(sql);
		if (headerTable.getColumnCount() == 0) {
			headerTable.setColumn(dm.getCols());
		}
		headerTable.removeRowAll();
		detailTable.removeRowAll();
		headerTable.setData(dm.getDataStrings());
		headerTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		headerTable.setColumnEditableAll(false);
		JTableUtil.fitTableColumnsDoubleWidth(headerTable);
		headerTable.setSortEnable();
		headerTable.updateUI();
		if(headerTable.getRowCount()>0){
			headerTable.setRowSelectionInterval(0, 0);//Ĭ��ѡ�е�һ��
		}
		tableHeaderRowColorSetup(headerTable);
		headerTableClick();
	}
	
	private void getDetailTableData(String strWhere){
		DefaultTableModel dtm = (DefaultTableModel) detailTable.getModel();
        dtm.getDataVector().removeAllElements();
		dtm.setRowCount(0);
		String sql = "select ipd.LINE_NUMBER,ipd.ITEM_CODE,bi.ITEM_BAR_CODE,bi.ITEM_NAME,"
				+ "ipd.UOM,ipd.TOTAL_QTY,ipd.DAMAGE_QTY,ipd.SCRAP_QTY,ipd.RECEIVED_QTY,"
				+ "ipd.LOTTABLE01,ipd.LOTTABLE02,ipd.LOTTABLE03,ipd.LOTTABLE04,"
				+ "ipd.LOTTABLE05,ipd.LOTTABLE06,ipd.LOTTABLE07,ipd.LOTTABLE08,ipd.LOTTABLE09,ipd.LOTTABLE10,"
				+ "user.user_name CREATED_BY_USER,ipd.CREATED_DTM_LOC "
				+ " from inb_po_detail ipd " 
				+ "inner join bas_item bi on ipd.storer_code=bi.storer_code and ipd.ITEM_CODE=bi.ITEM_CODE " 
				+ "inner join sys_user user on ipd.CREATED_BY_USER=user.user_code "
				+ " where 1=1 ";
		if(!strWhere.equals("")){
			sql = sql + strWhere;
		}
		System.out.println(sql);
		try{
			java.sql.Connection con = DBConnectionManager.getInstance().getConnection("wms");
			java.sql.Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Vector<String> rowdata = new Vector<String>();
				rowdata.add(rs.getString("LINE_NUMBER"));
				rowdata.add(rs.getString("ITEM_CODE"));
				rowdata.add(rs.getString("ITEM_BAR_CODE"));
				rowdata.add(rs.getString("ITEM_NAME"));
				rowdata.add(rs.getString("UOM"));
				rowdata.add(rs.getString("TOTAL_QTY"));
				rowdata.add(rs.getString("DAMAGE_QTY"));
				rowdata.add(rs.getString("SCRAP_QTY"));
				rowdata.add(rs.getString("RECEIVED_QTY"));
				rowdata.add(rs.getString("LOTTABLE01"));
				rowdata.add(rs.getString("LOTTABLE02"));
				rowdata.add(rs.getString("LOTTABLE03"));
				rowdata.add(rs.getString("LOTTABLE04"));
				rowdata.add(rs.getString("LOTTABLE05"));
				rowdata.add(rs.getString("LOTTABLE06"));
				rowdata.add(rs.getString("LOTTABLE07"));
				rowdata.add(rs.getString("LOTTABLE08"));
				rowdata.add(rs.getString("LOTTABLE09"));
				rowdata.add(rs.getString("LOTTABLE10"));
				rowdata.add(rs.getString("CREATED_DTM_LOC"));
				rowdata.add(rs.getString("CREATED_BY_USER"));
				dtm.addRow(rowdata);
			}
			JTableUtil.fitTableColumns(detailTable);
			detailTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			detailTable.setColumnEditableAll(false);
			DBConnectionManager.getInstance().freeConnection("wms", con);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		tableRowColorSetup(detailTable);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void tableRowColorSetup(PBSUIBaseGrid tab){
		Vector cellColor = new Vector();
		for(int i=0;i<tab.getRowCount();i++){
//			Vector rowColor = new Vector();
			String totalQty = (String) tab.getValueAt(i, tab.getColumnModel().getColumnIndex("����"));
			String receiveQty = (String) tab.getValueAt(i, tab.getColumnModel().getColumnIndex("��������"));
			if(Math_SAM.str2Double(totalQty)-Math_SAM.str2Double(receiveQty)==0){
				Object[] rc1Cell = new Object[3];
		        rc1Cell[0] = new Integer(i);
		        rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("��������"));
		        rc1Cell[2] = Color.ORANGE;
		        cellColor.addElement(rc1Cell);
//		        rowColor.addElement(new Integer(i));
//		        detailTable.setRowColor(rowColor, Color.lightGray);
			}else if(Math_SAM.str2Double(totalQty)-Math_SAM.str2Double(receiveQty)<0){
				Object[] rc1Cell = new Object[3];
		        rc1Cell[0] = new Integer(i);
		        rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("��������"));
		        rc1Cell[2] = Color.RED;
		        cellColor.addElement(rc1Cell);
//		        rowColor.addElement(new Integer(i));
//		        detailTable.setRowColor(rowColor, Color.lightGray);
			}
		}
		detailTable.setCellColor(cellColor);
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void tableHeaderRowColorSetup(PBSUIBaseGrid tab){
		Vector cellColor = new Vector();
		for(int i=0;i<tab.getRowCount();i++){
			Vector rowColor = new Vector();
			String status = (String) tab.getValueAt(i, tab.getColumnModel().getColumnIndex("PO״̬"));
			if(status.equals("�ջ���")){
				Object[] rc1Cell = new Object[3];
		        rc1Cell[0] = new Integer(i);
		        rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("PO״̬"));
		        rc1Cell[2] = Color.blue;
		        cellColor.addElement(rc1Cell);
		        rowColor.addElement(new Integer(i));
		        //tab.setRowColor(rowColor, Color.lightGray);
			}else if(status.equals("�ر�")){
				Object[] rc1Cell = new Object[3];
		        rc1Cell[0] = new Integer(i);
		        rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("PO״̬"));
		        rc1Cell[2] = Color.red;
		        cellColor.addElement(rc1Cell);
		        rowColor.addElement(new Integer(i));
		        //tab.setRowColor(rowColor, Color.lightGray);
			}
		}
		tab.setCellColor(cellColor);
	}
	
	private String getStorerCodeByName(String storerName){
		String sql = "select STORER_CODE from bas_storer where STORER_NAME like '%"+storerName+"%' ";
		DataManager dm = DBOperator.DoSelect2DM(sql);
		if(dm.getCurrentCount()>0){
			return dm.getString("STORER_CODE", 0);
		}else{
			return "";
		}
	}
	
	private boolean checkItemCode(String storerCode,String itemCode,String ITEM_BAR_CODE,String ERP_PO_NO){
		if(itemCode.trim().equals("")) return false;
		String sql = "select * from bas_item where STORER_CODE='"+storerCode+"' and ITEM_CODE='"+itemCode+"' ";
		DataManager dm = DBOperator.DoSelect2DM(sql);
		if(dm.getCurrentCount()==0){
			if(!ITEM_BAR_CODE.trim().equals("")){//���ʵ�����벻Ϊ�գ���Ҫ����ϵͳ����
//				sql = "insert into bas_item(storer_code,port_code,item_code,item_bar_code,item_name,brand_code,item_spec,"
//						+ "unit_code,country_code,DESCRIPTION,RETAIL_PRICE,WEIGHT,TAX_NUMBER,USER_DEF10,CREATED_BY_USER,CREATED_DTM_LOC) "
//						+"select '"+storerCode+"',a.PORT_ID,a.MATERIAL_CODE,'"+ITEM_BAR_CODE+"' GOODS_BAR_CODE,a.NAME_CN,"
//						+ "a.BRAND_CODE,b.GOOD_SPEC,b.UNIT_CODE,a.ORIGIN,b.GOOD_SPEC,"
//						+ "a.FINAL_PRICE,a.WEIGHT,a.TAX_NUMBER,'"+ERP_PO_NO+"','sys',now()  "
//						+"from AOS.bas_goods_port a "
//						+"inner join AOS.bas_goods b on a.GOOD_CODE=b.GOOD_CODE "
//						+"where a.MATERIAL_CODE='"+itemCode+"' ";
				sql = "insert into bas_item(storer_code,port_code,item_code,item_bar_code,item_name,brand_code,item_spec,"
						+ "unit_code,country_code,DESCRIPTION,RETAIL_PRICE,WEIGHT,TAX_NUMBER,USER_DEF10,CREATED_BY_USER,CREATED_DTM_LOC) "
						+"select '"+storerCode+"',a.`�ڰ�`,a.`�����Ϻ�`,'"+ITEM_BAR_CODE+"' GOODS_BAR_CODE,a.`��Ʒ����`,"
						+ "a.`Ʒ�ƣ���+Ӣ��`,a.`���`,a.`������λ`,a.`��������������`,a.`�ɷ�˵��`,"
						+ "a.`���ۼ۸�`,a.`*����(��λ:��)`,a.`����˰��`,'"+ERP_PO_NO+"','sys',now()  "
						+"from bas_sku a "
						+"where a.`�����Ϻ�`='"+itemCode+"' ";
			}else{
//				sql = "insert into bas_item(storer_code,port_code,item_code,item_bar_code,item_name,brand_code,item_spec,"
//						+ "unit_code,country_code,DESCRIPTION,RETAIL_PRICE,WEIGHT,TAX_NUMBER,USER_DEF10,CREATED_BY_USER,CREATED_DTM_LOC) "
//						+"select '"+storerCode+"',a.PORT_ID,a.MATERIAL_CODE,a.GOODS_BAR_CODE,a.NAME_CN,a.BRAND_CODE,"
//						+ "b.GOOD_SPEC,b.UNIT_CODE,a.ORIGIN,b.GOOD_SPEC,"
//						+ "a.FINAL_PRICE,a.WEIGHT,a.TAX_NUMBER,'"+ERP_PO_NO+"','sys',now()  "
//						+"from AOS.bas_goods_port a "
//						+"inner join AOS.bas_goods b on a.GOOD_CODE=b.GOOD_CODE "
//						+"where a.MATERIAL_CODE='"+itemCode+"' ";
				sql = "insert into bas_item(storer_code,port_code,item_code,item_bar_code,item_name,brand_code,item_spec,"
						+ "unit_code,country_code,DESCRIPTION,RETAIL_PRICE,WEIGHT,TAX_NUMBER,USER_DEF10,CREATED_BY_USER,CREATED_DTM_LOC) "
						+"select '"+storerCode+"',a.`�ڰ�`,a.`�����Ϻ�`,a.`��Ʒ������`,a.`��Ʒ����`,a.`Ʒ�ƣ���+Ӣ��`,"
						+ "a.`���`,a.`������λ`,a.`��������������`,a.`�ɷ�˵��`,"
						+ "a.`���ۼ۸�`,a.`*����(��λ:��)`,a.`����˰��`,'"+ERP_PO_NO+"','sys',now()  "
						+"from bas_sku a "
						+"where a.`�����Ϻ�`='"+itemCode+"' ";
			}
			
			int t = DBOperator.DoUpdate(sql);
			if(t>0){
				return true;
			}else{
				return false;
			}
		}
		
		return true;
	}
	
	private String checkERPPOStatus(String storerCode,String erppostr){
		String sql = "select po_no,status from inb_po_header where erp_po_no='"+erppostr+"' and storer_code='"+storerCode+"' ";
		DataManager dm = DBOperator.DoSelect2DM(sql);
		if(dm==null || dm.getCurrentCount()==0){
			return "";
		}else{
			return dm.getString("status", 0);
		}
	}
	
	
	
	@SuppressWarnings({ "rawtypes", "unused" })
	private boolean checkExcelData(DataManager dm){
		if(dm.getString(0, 0).equals("") || dm.getString(1, 0).equals("") || dm.getString(2, 0).equals("")){
			JOptionPane.showMessageDialog(null, "Excel��ʽ����ȷ","��ʾ",JOptionPane.WARNING_MESSAGE);
			return false;
		}
		for(int i=0;i<dm.getCurrentCount();i++){
			Vector vec = DBOperator.DoSelect("select warehouse_code from bas_warehouse where warehouse_code='"+dm.getString("�ֿ����", i)+"'");
			if(vec==null || vec.size()<=0){
				return false;
			}else{
				return true;
			}
		}
		return false;
	}
	
	//����PO,ϵͳֱ�����ӿ�棬 �˹������ڼζ��ֿ�
	public static boolean generateInventoryByPO(String polist){
		String po_no = "";
		String warehouseCode ="";
		String storerCode = "";
		String itemCode = "";
		String lotNo = "";
		String inventoryID = "";
		String lottable01 = "";
		String lottable02 = "";
		String lottable03 = "";
		String lottable04 = "";
		String lottable05 = "";
		String lottable06 = "";
		String lottable07 = "";
		String lottable08 = "";
		String lottable09 = "";
		String lottable10 = "";
		String locationCode = "";
		String containCode = "";
		String TOTAL_QTY = "";
		String UOM = "";
		String receivedQty = "";
		String receivedUOM = "";
		String DAMAGE_QTY = "";
		String DAMAGE_UOM = "";
		String SCRAP_QTY = "";
		String SCRAP_UOM = "";
		String normalLocationCode = "";
		
		if(polist.trim().equals("")){
			return false;
		}
		
		String sql = "select LOCATION_CODE from bas_location where LOCATION_TYPE_CODE='Normal' and WAREHOUSE_CODE='SHJD' "
				+ "order by LOCATION_CODE limit 1";
		DataManager dmloc = DBOperator.DoSelect2DM(sql);
		normalLocationCode = dmloc.getString("LOCATION_CODE", 0);

		sql = "select PO_NO,WAREHOUSE_CODE,STORER_CODE,ITEM_CODE,'*' CONTAINER_CODE,"
			+"(select LOCATION_CODE from bas_location where WAREHOUSE_CODE='SHJD' and LOCATION_TYPE_CODE='Damage' limit 1) LOCATION_CODE,"
			+"LOTTABLE01,LOTTABLE02,LOTTABLE03,LOTTABLE04,LOTTABLE05,LOTTABLE06,LOTTABLE07,LOTTABLE08,LOTTABLE09,LOTTABLE10,"
			+"TOTAL_QTY,UOM,DAMAGE_QTY,DAMAGE_UOM,SCRAP_QTY,SCRAP_UOM "
			+"from inb_po_detail where PO_NO in ("+polist+") and WAREHOUSE_CODE='SHJD' ";
		DataManager dmPO = DBOperator.DoSelect2DM(sql);
		for(int i=0;i<dmPO.getCurrentCount();i++){
			po_no = dmPO.getString("PO_NO", i);
			warehouseCode = dmPO.getString("WAREHOUSE_CODE", i);
			storerCode = dmPO.getString("STORER_CODE", i);
			itemCode = dmPO.getString("ITEM_CODE", i);
			locationCode = dmPO.getString("LOCATION_CODE", i);
			containCode = dmPO.getString("CONTAINER_CODE", i);
			TOTAL_QTY = dmPO.getString("TOTAL_QTY", i);
			UOM = dmPO.getString("UOM", i);
			DAMAGE_QTY = dmPO.getString("DAMAGE_QTY", i);
			DAMAGE_UOM = dmPO.getString("DAMAGE_UOM", i);
			SCRAP_QTY = dmPO.getString("SCRAP_QTY", i);
			SCRAP_UOM = dmPO.getString("SCRAP_UOM", i);
			lottable01 = dmPO.getString("LOTTABLE01", i);
			lottable02 = dmPO.getString("LOTTABLE02", i);
			lottable03 = dmPO.getString("LOTTABLE03", i);
			lottable04 = dmPO.getString("LOTTABLE04", i);
			lottable05 = dmPO.getString("LOTTABLE05", i);
			lottable06 = dmPO.getString("LOTTABLE06", i);
			lottable07 = dmPO.getString("LOTTABLE07", i);
			lottable08 = dmPO.getString("LOTTABLE08", i);
			lottable09 = dmPO.getString("LOTTABLE09", i);
			lottable10 = dmPO.getString("LOTTABLE10", i);
			//�����ɿ�����κ�
			lotNo = comData.getInventoryLotNo(storerCode,itemCode,lottable01,lottable02,lottable03,lottable04,lottable05,lottable06,lottable07,lottable08,lottable09,lottable10);
			if(!lotNo.equals("")){
				//�������  �����������    �������=��PO���� - �������� - ��������
				double availQty = Double.parseDouble(TOTAL_QTY)-Double.parseDouble(DAMAGE_QTY)-Double.parseDouble(SCRAP_QTY);
				if(availQty>0){
					inventoryID = comData.getInventoryID(warehouseCode,storerCode,itemCode,lotNo,normalLocationCode,containCode,String.valueOf(availQty),
							MainFrm.getUserInfo().getString("USER_CODE", 0));
					if(!inventoryID.equals("")){
						//����д��ɹ�
					}else{
						//����д��ʧ��
						LogInfo.appendLog("error","������汣�浽����ʧ��,PO:"+po_no+" ITEM_CODE:"+itemCode);
						return false;
					}
				}
				//�������  �д�
				if(Double.parseDouble(DAMAGE_QTY)>0){
					inventoryID = comData.getInventoryID(warehouseCode,storerCode,itemCode,lotNo,locationCode,containCode,DAMAGE_QTY,
							MainFrm.getUserInfo().getString("USER_CODE", 0));
					if(!inventoryID.equals("")){
						//����д��ɹ�
					}else{
						//����д��ʧ��
						LogInfo.appendLog("error","�дο�汣�浽����ʧ��,PO:"+po_no+" ITEM_CODE:"+itemCode);
						return false;
					}
				}
				//�������  ����
				if(Double.parseDouble(SCRAP_QTY)>0){
					inventoryID = comData.getInventoryID(warehouseCode,storerCode,itemCode,lotNo,locationCode,containCode,
							SCRAP_QTY,MainFrm.getUserInfo().getString("USER_CODE", 0));
					if(!inventoryID.equals("")){
						//����д��ɹ�
					}else{
						//����д��ʧ��
						LogInfo.appendLog("error","���Ͽ�汣�浽����ʧ��,PO:"+po_no+" ITEM_CODE:"+itemCode);
						return false;
					}
				}
				//���ȫ�����ӳɹ�������PO��ϸ�����ջ�����
				sql = "update inb_po_detail set RECEIVED_QTY=TOTAL_QTY "
					+ "where PO_NO='"+po_no+"' and ITEM_CODE='"+itemCode+"' and TOTAL_QTY="+TOTAL_QTY+" "
					+ "and DAMAGE_QTY="+DAMAGE_QTY+" and SCRAP_QTY="+SCRAP_QTY;
				int t = DBOperator.DoUpdate(sql);
				
			}
		}
		
		return true;
	}

	private void setExtendedState(int maximizedBoth) {
		// TODO Auto-generated method stub
		
	}
}


