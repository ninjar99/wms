package inbound;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.Toolkit;

import DBUtil.DBConnectionManager;
import DBUtil.DBOperator;
import DBUtil.LogInfo;
import comUtil.ExcelRead;
import comUtil.JTableExportExcel;
import comUtil.chooseFileList;
import comUtil.comData;
import dmdata.DataManager;
import main.PBSUIBaseGrid;
import sys.InnerFrame;
import sys.JTableUtil;
import sys.MainFrm;
import sys.Message;
import sys.QueryDialog;
import util.Math_SAM;
import util.MyTableCellRenderrer;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
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
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					poImportFrm frame = new poImportFrm();
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
				String fileDir = chooseFileList.ResultValue;
				if(fileDir.equals("")) return;
				ExcelRead excel = new ExcelRead(fileDir);
				ArrayList<?> list = null;
				DataManager excelDM = new DataManager();
				try {
					list = excel.getExcelData();
					if(list==null) return;
					excelDM = comData.list2DataManager(list);
				} catch (Exception e) {
					LogInfo.appendLog(e.getMessage());
					e.printStackTrace();
				}
				if (list.size() > 1) {
					String[] rowheader = (String[]) list.get(0);
					if(!checkExcelData(excelDM)){
						JOptionPane.showMessageDialog(null, "Excel��ʽ����ȷ","��ʾ",JOptionPane.WARNING_MESSAGE);
						return;
					}
					
					int line = 0;
					boolean changeLine = false;
					try {
						String sql = "";
						java.sql.Connection con = DBConnectionManager.getInstance().getConnection("wms");
						java.sql.Statement stmt = con.createStatement();
						ResultSet rs;
						StringBuffer sbf = new StringBuffer();
						for (int i = 0; i <excelDM.getCurrentCount() ; i++) {
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
								sbf.append("��������ȷ:"+excelDM.getString("����", i));
							}
							String VENDOR_CODE = "001";//Ĭ��001
							String ERP_PO_NO = excelDM.getString("���˵���", i);
							String LINE_NUMBER = String.valueOf(line);//row[4];
							String ITEM_CODE = excelDM.getString("�Ϻ�", i);
							boolean bool = checkItemCode(STORER_CODE,ITEM_CODE);//������Ϻ��Ƿ���ڣ���������ڣ���AOS����
							if(!bool){
								sbf.append("��AOS������Ʒ�ۿ���Ϣʧ��,���Ϻ�= ��"+ITEM_CODE+"��");
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
							String LOTTABLE10 = "";
							String USER_DEF1 = excelDM.getString("��������", i);
							String USER_DEF2 = excelDM.getString("����ԭ��", i);
							String USER_DEF3 = excelDM.getString("���ʱ��", i);
							String USER_DEF4 = excelDM.getString("���ʱ��", i);
							String USER_DEF5 = excelDM.getString("�����˾", i);
							String USER_DEF6 = excelDM.getString("�����Ա", i);
							String USER_DEF7 = excelDM.getString("���ع�˾", i);
							String status = checkERPPOStatus(STORER_CODE,ERP_PO_NO);
							if(!status.equals("100") && !status.equals("")){
								sbf.append(excelDM.getRow(i).toString()+" �Ѿ���ʼ�ջ������Դ��е���\n");
								continue;
							}
							sql = "select po_no,status from inb_po_header where storer_code='"+STORER_CODE+"' and erp_po_no='" + ERP_PO_NO + "'";
							LogInfo.appendLog("sql", sql);
							rs = stmt.executeQuery(sql);
							if (!rs.next()) {
								PO_NO = comData.getValueFromBasNumRule("inb_po_header", "po_no");
								//�����ͷ
								sql = "insert into inb_po_header(po_no,warehouse_code,storer_code,vendor_code,erp_po_no,created_dtm_loc,created_by_user,updated_dtm_loc,updated_by_user)"
										+ " select '" + PO_NO + "','" + WAREHOUSE_CODE + "','" + STORER_CODE + "','" + VENDOR_CODE
										+ "','" + ERP_PO_NO + "',now(),'"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"',now(),'"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"' ";
								int t = DBOperator.DoUpdate(sql);
								if (t != 1) {
									JOptionPane.showMessageDialog(null, "����PO��ͷ����\n" + sql, "����",
											JOptionPane.ERROR_MESSAGE);
									LogInfo.appendLog("����PO��ͷ����\n" + sql, "����");
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
								t = DBOperator.DoUpdate(sql);
								if (t != 1) {
									JOptionPane.showMessageDialog(null, "����PO����ϸ����\n" + sql, "����",
											JOptionPane.ERROR_MESSAGE);
									LogInfo.appendLog("����PO����ϸ����\n" + sql, "����");
								}
							}else{
								PO_NO = rs.getString("po_no");
								POStatus = rs.getString("status");
								if(!POStatus.equals("100")){
									sbf.append("PO:"+PO_NO+" ERP_PO_NO:"+ERP_PO_NO+" ��Ʒ����:"+ITEM_CODE+" ��¼���ڣ����Ҷ���״̬�ǳ�ʼ״̬�����Դ��е���\n");
									continue;
								}
								sql = "select po_no from inb_po_detail where po_no='"+PO_NO+"' and line_number = "+LINE_NUMBER+" ";
								LogInfo.appendLog("sql", sql);
								java.sql.Statement stmt2 = con.createStatement();
								ResultSet rs2 = stmt2.executeQuery(sql);
								if(rs2.next()){
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
									LogInfo.appendLog("����PO����ϸ����\n" + sql, "����");
								}
							}
						}
						if(sbf.toString().length()>1){
							Message.showWarningMessage(sbf.toString());
						}
					} catch (Exception e) {
						e.printStackTrace();
						LogInfo.appendLog(e.getMessage());
					}
				} else {
					JOptionPane.showMessageDialog(null, "Excel������", "��ʾ", JOptionPane.WARNING_MESSAGE);
				}
				JOptionPane.showMessageDialog(null, "���ݵ���ɹ���");
				getHeaderTableData("");
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
	
	private void headerTableClick(){
		int r= headerTable.getSelectedRow();
        if(headerTable.getRowCount()>0){
	        String str_po_no = headerTable.getValueAt(r, headerTable.getColumnModel().getColumnIndex("PO��")).toString();
	        getDetailTableData(" and ipd.po_no='"+str_po_no+"'");
        }
	}
	
	private void getHeaderTableData(String strWhere){
		String sql = "select iph.WAREHOUSE_CODE �ֿ����,bw.WAREHOUSE_NAME �ֿ�����,iph.PO_NO PO��,iph.ERP_PO_NO ERP_PO_NO,iph.STORER_CODE ��������,bs.STORER_NAME ��������,iph.VENDOR_CODE ��Ӧ�̱���,bv.VENDOR_NAME ��Ӧ������,iph.CREATED_DTM_LOC ����ʱ��,iph.CREATED_BY_USER ������ "
				+ " from inb_po_header iph  " + " inner join bas_warehouse bw on iph.WAREHOUSE_CODE=bw.WAREHOUSE_CODE"
				+ " inner join bas_storer bs on iph.STORER_CODE=bs.STORER_CODE"
				+ " inner join bas_vendor bv on bv.VENDOR_CODE=iph.VENDOR_CODE" + " where 1=1 " 
				+ " and iph.WAREHOUSE_CODE = '"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' ";
		if(!strWhere.equals("")){
			sql = sql + strWhere;
		}
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
			headerTable.setRowSelectionInterval(0, 0);//Ĭ��ѡ�е�һ��
		}
		headerTableClick();
	}
	
	private void getDetailTableData(String strWhere){
		DefaultTableModel dtm = (DefaultTableModel) detailTable.getModel();
        dtm.getDataVector().removeAllElements();
		dtm.setRowCount(0);
		String sql = "select ipd.LINE_NUMBER,ipd.ITEM_CODE,bi.ITEM_BAR_CODE,bi.ITEM_NAME,"
				+ "ipd.UOM,ipd.TOTAL_QTY,ipd.DAMAGE_QTY,ipd.SCRAP_QTY,ipd.RECEIVED_QTY,"
				+ "ipd.LOTTABLE01,ipd.LOTTABLE02,ipd.LOTTABLE03,ipd.LOTTABLE04,"
				+ "ipd.LOTTABLE05,ipd.LOTTABLE06,ipd.LOTTABLE07,ipd.LOTTABLE08,ipd.LOTTABLE09,ipd.LOTTABLE10,ipd.CREATED_BY_USER,ipd.CREATED_DTM_LOC "
				+ " from inb_po_detail ipd " + "inner join bas_item bi on ipd.storer_code=bi.storer_code and ipd.ITEM_CODE=bi.ITEM_CODE" + " where 1=1 ";
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
	
	private String getStorerCodeByName(String storerName){
		String sql = "select STORER_CODE from bas_storer where STORER_NAME like '%"+storerName+"%' ";
		DataManager dm = DBOperator.DoSelect2DM(sql);
		if(dm.getCurrentCount()>0){
			return dm.getString("STORER_CODE", 0);
		}else{
			return "";
		}
	}
	
	private boolean checkItemCode(String storerCode,String itemCode){
		String sql = "select * from bas_item where STORER_CODE='"+storerCode+"' and ITEM_CODE='"+itemCode+"' ";
		DataManager dm = DBOperator.DoSelect2DM(sql);
		if(dm.getCurrentCount()==0){
			sql = "insert into bas_item(storer_code,port_code,item_code,item_bar_code,item_name,brand_code,item_spec,"
				+ "unit_code,country_code,DESCRIPTION,RETAIL_PRICE,WEIGHT,TAX_NUMBER,CREATED_BY_USER,CREATED_DTM_LOC) "
				+"select '"+storerCode+"',a.PORT_ID,a.MATERIAL_CODE,a.GOODS_BAR_CODE,a.NAME_CN,a.BRAND_CODE,b.GOOD_SPEC,b.UNIT_CODE,a.ORIGIN,b.GOOD_SPEC,"
				+ "a.FINAL_PRICE,a.WEIGHT,a.TAX_NUMBER,'sys',now()  "
				+"from AOS.bas_goods_port a "
				+"inner join AOS.bas_goods b on a.GOOD_CODE=b.GOOD_CODE "
				+"where a.MATERIAL_CODE='"+itemCode+"' ";
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

	private void setExtendedState(int maximizedBoth) {
		// TODO Auto-generated method stub
		
	}
}


