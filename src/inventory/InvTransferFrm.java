package inventory;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import DBUtil.DBOperator;
import comUtil.WMSCombobox;
import comUtil.comData;
import dmdata.DataManager;
import dmdata.xArrayList;
import main.PBSUIBaseGrid;
import sys.InnerFrame;
import sys.JTableUtil;
import sys.MainFrm;
import sys.Message;
import sys.QueryDialog;
import sys.tableQueryDialog;
import util.JTNumEdit;
import util.Math_SAM;
import util.WaitingSplash;

import javax.swing.JButton;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;

@SuppressWarnings("serial")
public class InvTransferFrm extends InnerFrame {

	private JPanel contentPane;
	private static InvTransferFrm instance;
	private static boolean isOpen = false;
	private boolean trigTable = true;
	private PBSUIBaseGrid detailTable;
	private PBSUIBaseGrid headerTable;
	private JButton btnAdd;
	private JButton btnModify;
	private JButton btnDelete;
	private JButton btnQuery;
	private JButton btnSave;
	private JButton btnCancel;
	private JButton btnClose;
	private JPanel panel;
	private JLabel label;
	private JTextField txt_transfer_no;
	private JLabel label_1;
	private WMSCombobox cb_from_warehouse;
	private JLabel label_2;
	private WMSCombobox cb_from_storer;
	private JLabel label_3;
	private WMSCombobox cb_to_storer;
	private JTextField txt_remark;
	private JLabel label_5;
	private JLabel label_6;
	private JLabel label_7;
	private JLabel label_8;
	private JLabel label_9;
	private JLabel label_10;
	private JLabel label_11;
	private JLabel label_12;
	private JLabel label_13;
	private JPanel panel_1;
	private JLabel lblNewLabel;
	private WMSCombobox cb_to_warehouse;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JPanel panel_2;
	private JPanel panel_3;
	private JLabel lblNewLabel_3;
	private JTextField txt_create_date;
	private JLabel lblNewLabel_4;
	private JTextField txt_create_user;
	private JButton btnAudit;
	private JLabel lblNewLabel_5;
	private JTextField txt_status;

	public static synchronized InvTransferFrm getInstance() {
		if (instance == null) {
			instance = new InvTransferFrm();
		}
		return instance;

	}

	public static synchronized boolean getOpenStatus() {
		if (instance == null) {
			instance = new InvTransferFrm();
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
					InvTransferFrm frame = new InvTransferFrm();
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
	public InvTransferFrm() {
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

		setBounds(100, 100, 919, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel topPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) topPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		contentPane.add(topPanel, BorderLayout.NORTH);

		btnAdd = new JButton("\u589E\u52A0");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txt_transfer_no.setText("");
				txt_transfer_no.setEnabled(true);
				txt_remark.setText("");
				txt_remark.setEditable(true);
				txt_create_date.setText("");
				txt_create_user.setText("");
				cb_from_warehouse.setEnabled(true);
				cb_from_warehouse.setSelectedDisplayName(MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0));
				cb_from_storer.setEnabled(true);
				cb_to_storer.setEnabled(true);
				cb_to_warehouse.setEnabled(true);
				btnAdd.setEnabled(false);
				btnModify.setEnabled(false);
				btnDelete.setEnabled(false);
				btnQuery.setEnabled(false);
				btnSave.setEnabled(true);
				btnCancel.setEnabled(true);
				btnAudit.setEnabled(false);
				btnClose.setEnabled(false);

				detailTable.removeRowAll();

				// ����һ�п���
				String[] addRowValues = new String[detailTable.getColumnCount()];
				addRowValues[detailTable.getColumnModel().getColumnIndex("Ŀ������")]="0";
				detailTable.addRow(addRowValues);
				detailTable.setComponent(new JTNumEdit(15, "#####", true), detailTable.getColumnModel().getColumnIndex("Ŀ������"));

				detailTableEditSetup(true);
				tableRowColorSetup(detailTable);

				// detailTable.getColumn("��Ʒ����").setCellRenderer(new
				// MyTableCellRenderrer());
				// detailTable.getColumn("����").setCellRenderer(new
				// MyTableCellRenderrer());
				// detailTable.getColumn("��������1").setCellRenderer(new
				// MyTableCellRenderrer());
				// detailTable.getColumn("��������2").setCellRenderer(new
				// MyTableCellRenderrer());
				// detailTable.getColumn("��������3").setCellRenderer(new
				// MyTableCellRenderrer());
				// detailTable.getColumn("��������4").setCellRenderer(new
				// MyTableCellRenderrer());
				// detailTable.getColumn("��������5").setCellRenderer(new
				// MyTableCellRenderrer());
				// detailTable.getColumn("��������6").setCellRenderer(new
				// MyTableCellRenderrer());
				// detailTable.getColumn("��������7").setCellRenderer(new
				// MyTableCellRenderrer());
				// detailTable.getColumn("��������8").setCellRenderer(new
				// MyTableCellRenderrer());
				// detailTable.getColumn("��������9").setCellRenderer(new
				// MyTableCellRenderrer());
				// detailTable.getColumn("��������10").setCellRenderer(new
				// MyTableCellRenderrer());
				detailTable.updateUI();
				// makeFace(detailTable);
				detailTable.requestFocus();
				detailTable.changeSelection(detailTable.getRowCount() - 1, detailTable.getColumnModel().getColumnIndex("��Ʒ����"), false, false);
				detailTable.setColumnSelectionAllowed(true);
				detailTable.setRowHeight(30);
				JTableUtil.fitTableColumns(detailTable);
			}
		});
		topPanel.add(btnAdd);

		btnModify = new JButton("\u4FEE\u6539");
		btnModify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txt_transfer_no.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(null, "��ѡ��һ�����ݣ�");
					return;
				}
				String transferNoStatus = checkTransferNoStatus(txt_transfer_no.getText().trim());
				if (transferNoStatus.equals("")) {
					Message.showErrorMessage("������Ų�����");
					return;
				} else if (!transferNoStatus.equals("100")) {
					Message.showWarningMessage("������ǳ�ʼ״̬�����ܽ����޸�");
					return;
				}
				if (detailTable.getRowCount() == 0) {
					// ����һ�п���
					Object[] addRowValues = new Object[detailTable.getColumnCount()];
					detailTable.addRow(addRowValues);
					detailTable.editCellAt(detailTable.getRowCount() - 1, detailTable.getColumnModel().getColumnIndex("��Ʒ����"));
				}
				txt_remark.setEditable(true);
				cb_from_warehouse.setEnabled(false);
				cb_from_storer.setEnabled(false);
				cb_to_storer.setEnabled(false);
				btnAdd.setEnabled(false);
				btnModify.setEnabled(false);
				btnDelete.setEnabled(false);
				btnQuery.setEnabled(false);
				btnSave.setEnabled(true);
				btnCancel.setEnabled(true);
				btnClose.setEnabled(false);
				btnAudit.setEnabled(false);
				headerTable.setEnabled(false);
				detailTableEditSetup(true);
			}
		});
		topPanel.add(btnModify);

		btnDelete = new JButton("\u5220\u9664");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(txt_transfer_no.getText().trim().equals("")){
					JOptionPane.showMessageDialog(null, "��ѡ��һ�����ݣ�");
					return;
				}
				String transferNo = checkTransferNoStatus(txt_transfer_no.getText().trim());
				if(transferNo.equals("")){
					Message.showErrorMessage("���Ų�����");
					return;
				}else if(!transferNo.equals("100")){
					Message.showWarningMessage("�ǳ�ʼ����״̬������ɾ��");
					return;
				}
				int t = JOptionPane.showConfirmDialog(null, "�Ƿ�ɾ���õ��š�"+txt_transfer_no.getText().trim()+"]��");
				if(t==0){
					//ɾ��PO
					String sql = "delete from inv_transfer_header where TRANSFER_NO='"+txt_transfer_no.getText().trim()+"' and status='100' ";
					int delrow = DBOperator.DoUpdate(sql);
					if(delrow==1){
						sql = "delete from inv_transfer_detail where TRANSFER_NO='"+txt_transfer_no.getText().trim()+"' ";
						DBOperator.DoUpdate(sql);
						JOptionPane.showMessageDialog(null, "ɾ�����ݳɹ�","��ʾ",JOptionPane.INFORMATION_MESSAGE);
						getHeaderTableData(" and ith.status='100' ");
					}else{
						JOptionPane.showMessageDialog(null, "ɾ������ʧ��","��ʾ",JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		topPanel.add(btnDelete);

		btnQuery = new JButton("\u67E5\u8BE2");
		btnQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ArrayList<String> fieldList = new ArrayList<String>();
				fieldList.add("bs.STORER_NAME:ԭ��������");
				fieldList.add("ith.STORER_CODE:ԭ��������");
				fieldList.add("bs2.STORER_NAME:Ŀ���������");
				fieldList.add("ith.TO_STORER_CODE:Ŀ���������");
				fieldList.add("bw2.WAREHOUSE_NAME:Ŀ��ֿ�����");
				fieldList.add("ith.TO_WAREHOUSE_CODE:Ŀ��ֿ����");
				fieldList.add("su.USER_NAME:������");
				fieldList.add("ith.CREATED_DTM_LOC:��������");
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

		Component verticalStrut = Box.createVerticalStrut(20);
		verticalStrut.setBackground(Color.BLUE);
		topPanel.add(verticalStrut);

		btnSave = new JButton("\u4FDD\u5B58");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(saveData()){
					txt_remark.setEditable(false);
					cb_from_warehouse.setEnabled(false);
					cb_to_warehouse.setEnabled(false);
					cb_from_storer.setEnabled(false);
					cb_to_storer.setEnabled(false);
					btnAdd.setEnabled(true);
					btnModify.setEnabled(true);
					btnDelete.setEnabled(true);
					btnQuery.setEnabled(true);
					btnSave.setEnabled(false);
					btnCancel.setEnabled(false);
					btnAudit.setEnabled(true);
					btnClose.setEnabled(true);
					headerTable.setEnabled(true);
					detailTable.setColumnEditableAll(false);
//					getHeaderTableData(" and ith.TRANSFER_NO='"+txt_transfer_no.getText().trim()+"' ");
					getHeaderTableData(" and ith.status='100' ");
					int row = headerTable.getRowIndexByColIndexAndColValue(headerTable.getColumnModel().getColumnIndex("����"), txt_transfer_no.getText().trim());
					headerTable.setRowSelectionInterval(row,row);
				}
			}
		});
		btnSave.setEnabled(false);
		topPanel.add(btnSave);

		btnCancel = new JButton("\u53D6\u6D88");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txt_remark.setEditable(false);
				cb_from_warehouse.setEnabled(false);
				cb_to_warehouse.setEnabled(false);
				cb_from_storer.setEnabled(false);
				cb_to_storer.setEnabled(false);
				btnAdd.setEnabled(true);
				btnModify.setEnabled(true);
				btnDelete.setEnabled(true);
				btnQuery.setEnabled(true);
				btnSave.setEnabled(false);
				btnCancel.setEnabled(false);
				btnAudit.setEnabled(true);
				btnClose.setEnabled(true);
				headerTable.setEnabled(true);
				detailTable.setColumnEditableAll(false);
				clearDetailTable();
			}
		});
		btnCancel.setEnabled(false);
		topPanel.add(btnCancel);
		
		btnAudit = new JButton("\u5BA1\u6838");
		btnAudit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!comData.getUserMenuPower("������Ա��-���")){
					Message.showWarningMessage("�޴˹���Ȩ��");
					return;
				}
				String transferStatus = txt_status.getText().trim();
				if(transferStatus.equals("���")){
					Message.showWarningMessage("�����״̬�������");
					return;
				}
				
				int t = JOptionPane.showConfirmDialog(null, "�Ƿ���˸õ��š�"+txt_transfer_no.getText().trim()+"]��\n���֮����Ļ������������Իᷢ����Ӧ�仯�������أ�");
				if(t==0){
					new SwingWorker<String, Void>() {
						WaitingSplash splash = new WaitingSplash();

			            @Override
			            protected String doInBackground() throws Exception {
			            	//���
			            	btnAudit.setEnabled(false);
			            	headerTable.setEnabled(false);
			            	headerTable.setColumnEditableAll(false);
			            	splash.start(); // ������������
			            	try{
			            		auditData();
			            		//��¼������־
								DataManager dmProcess = comData.getSysProcessHistoryDataModel("sys_process_history");
								if (dmProcess != null) {
									dmdata.xArrayList list = (xArrayList) dmProcess.getRow(0);
									list.set(dmProcess.getCol("SYS_PROCESS_HISTORY_ID"), "null");
									list.set(dmProcess.getCol("PROCESS_CODE"), "INV_TRANSFER");
									list.set(dmProcess.getCol("PROCESS_NAME"), "�������Ա��");
									list.set(dmProcess.getCol("STORER_CODE"), cb_from_storer.getSelectedOID());
									list.set(dmProcess.getCol("TO_STORER_CODE"), cb_to_storer.getSelectedOID());
									list.set(dmProcess.getCol("WAREHOUSE_CODE"),cb_from_warehouse.getSelectedOID());
									list.set(dmProcess.getCol("TO_WAREHOUSE_CODE"),cb_to_warehouse.getSelectedOID());
									list.set(dmProcess.getCol("FROM_LOCATION_CODE"), "");
									list.set(dmProcess.getCol("TO_LOCATION_CODE"), "");
									list.set(dmProcess.getCol("FROM_CONTAINER_CODE"), "");
									list.set(dmProcess.getCol("TO_CONTAINER_CODE"), "");
									list.set(dmProcess.getCol("ITEM_CODE"), "");
									list.set(dmProcess.getCol("QTY"), "");
									list.set(dmProcess.getCol("REFERENCE_NO"), txt_transfer_no.getText());
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
			            	}catch(Exception e){
			            		Message.showWarningMessage(e.getMessage());
			            	}
			                return "";
			            }

			            @Override
			            protected void done() {
			            	splash.stop(); // ������������
			                System.out.println("������˽���");
							btnAudit.setEnabled(true);
							headerTable.setEnabled(true);
			            }
			        }.execute();
				}
			}
		});
		topPanel.add(btnAudit);

		Component verticalStrut_1 = Box.createVerticalStrut(20);
		verticalStrut_1.setBackground(Color.BLUE);
		topPanel.add(verticalStrut_1);

		btnClose = new JButton("\u5173\u95ED");
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

		JPanel rightPanel = new JPanel();
		contentPane.add(rightPanel, BorderLayout.CENTER);
		rightPanel.setLayout(new BorderLayout(0, 0));

		JPanel headerPanel = new JPanel();
		rightPanel.add(headerPanel, BorderLayout.NORTH);
		headerPanel.setLayout(new GridLayout(4, 1, 0, 0));

		panel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		headerPanel.add(panel);

		label = new JLabel("TransferNO\uFF1A");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(label);

		txt_transfer_no = new JTextField();
		txt_transfer_no.setFont(new Font("����", Font.BOLD, 14));
		txt_transfer_no.setEditable(false);
		txt_transfer_no.setColumns(12);
		panel.add(txt_transfer_no);
				
				lblNewLabel_5 = new JLabel("\u72B6\u6001\uFF1A");
				panel.add(lblNewLabel_5);
				
				txt_status = new JTextField();
				txt_status.setFont(new Font("����", Font.BOLD, 12));
				txt_status.setEditable(false);
				panel.add(txt_status);
				txt_status.setColumns(5);
		
				lblNewLabel_2 = new JLabel("\u5907\u6CE8\uFF1A");
				panel.add(lblNewLabel_2);
		
				txt_remark = new JTextField();
				txt_remark.setEditable(false);
				panel.add(txt_remark);
				txt_remark.setColumns(50);

		label_5 = new JLabel("");
		panel.add(label_5);

		label_6 = new JLabel("");
		panel.add(label_6);

		label_7 = new JLabel("");
		panel.add(label_7);

		label_8 = new JLabel("");
		panel.add(label_8);

		label_9 = new JLabel("");
		panel.add(label_9);

		label_10 = new JLabel("");
		panel.add(label_10);

		label_11 = new JLabel("");
		panel.add(label_11);

		label_12 = new JLabel("");
		panel.add(label_12);

		label_13 = new JLabel("");
		panel.add(label_13);

		panel_1 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_1.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		headerPanel.add(panel_1);

		label_3 = new JLabel("");
		label_3.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(label_3);
		
				label_1 = new JLabel("\u539F\u59CB\u4ED3\u5E93\uFF1A");
				panel_1.add(label_1);
				label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		
				cb_from_warehouse = new WMSCombobox(
						"SELECT WAREHOUSE_CODE,WAREHOUSE_name FROM bas_warehouse where warehouse_code='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+" ' order by WAREHOUSE_name ", true);
				cb_from_warehouse.setEnabled(false);
				panel_1.add(cb_from_warehouse);
		
				label_2 = new JLabel("\u539F\u59CB\u8D27\u4E3B\uFF1A");
				panel_1.add(label_2);
				label_2.setHorizontalAlignment(SwingConstants.RIGHT);
		
				cb_from_storer = new WMSCombobox("SELECT storer_code,storer_name FROM bas_storer order by storer_name ", true);
				cb_from_storer.setEnabled(false);
				panel_1.add(cb_from_storer);
		
		panel_2 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panel_2.getLayout();
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		headerPanel.add(panel_2);

		lblNewLabel = new JLabel("\u76EE\u6807\u4ED3\u5E93\uFF1A");
		panel_2.add(lblNewLabel);

		cb_to_warehouse = new WMSCombobox(
				"SELECT WAREHOUSE_CODE,WAREHOUSE_name FROM bas_warehouse order by WAREHOUSE_name ", true);
		cb_to_warehouse.setEnabled(false);
		panel_2.add(cb_to_warehouse);

		lblNewLabel_1 = new JLabel("\u76EE\u6807\u8D27\u4E3B\uFF1A");
		panel_2.add(lblNewLabel_1);

		cb_to_storer = new WMSCombobox("SELECT storer_code,storer_name FROM bas_storer order by storer_name ", true);
		cb_to_storer.setEnabled(false);
		panel_2.add(cb_to_storer);
		
		panel_3 = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) panel_3.getLayout();
		flowLayout_4.setAlignment(FlowLayout.LEFT);
		headerPanel.add(panel_3);
		
		lblNewLabel_3 = new JLabel("\u521B\u5EFA\u65F6\u95F4\uFF1A");
		panel_3.add(lblNewLabel_3);
		
		txt_create_date = new JTextField();
		txt_create_date.setEditable(false);
		panel_3.add(txt_create_date);
		txt_create_date.setColumns(15);
		
		lblNewLabel_4 = new JLabel("\u521B\u5EFA\u4EBA\uFF1A");
		panel_3.add(lblNewLabel_4);
		
		txt_create_user = new JTextField();
		txt_create_user.setEditable(false);
		panel_3.add(txt_create_user);
		txt_create_user.setColumns(10);

		JPanel detailPanel = new JPanel();
		rightPanel.add(detailPanel, BorderLayout.CENTER);
		detailPanel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		detailPanel.add(scrollPane, BorderLayout.CENTER);

		detailTable = new PBSUIBaseGrid();
		detailTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getClickCount()>=2){
					detailTable.setColumnSelectionAllowed(true);
					if(!btnSave.isEnabled()){
						return;
					}
					int row = detailTable.getSelectedRow();
					int column = detailTable.getSelectedColumn();
					String storerCode = cb_from_storer.getSelectedOID();
					String warrehouseCode = cb_from_warehouse.getSelectedOID();
					String to_storerCode = cb_to_storer.getSelectedOID();
					String to_warrehouseCode = cb_to_warehouse.getSelectedOID();
					if(storerCode.equals("")){
						JOptionPane.showMessageDialog(null, "������ԭʼ����");
						return;
					}
					if(to_storerCode.equals("")){
						JOptionPane.showMessageDialog(null, "������Ŀ�����");
						return;
					}
					if(to_warrehouseCode.equals("")){
						JOptionPane.showMessageDialog(null, "������Ŀ��ֿ�");
						return;
					}
					if(column==detailTable.getColumnModel().getColumnIndex("��Ʒ����")){
						String sql = "select ii.INV_INVENTORY_ID ���ID,bi.ITEM_BAR_CODE ��Ʒ����,ii.ITEM_CODE ��Ʒ����,bi.ITEM_NAME ��Ʒ����,ii.ON_HAND_QTY+ii.IN_TRANSIT_QTY-(ii.ALLOCATED_QTY)-(ii.PICKED_QTY) �������, "
								+"biu.unit_name ��λ,ii.LOCATION_CODE ��λ,ii.CONTAINER_CODE ���,ii.LOT_NO ����"
								+",il.LOTTABLE01 ��������1,il.LOTTABLE02 ��������2,il.LOTTABLE03 ��������3,il.LOTTABLE04 ��������4,il.LOTTABLE05 ��������5"
								+",il.LOTTABLE06 ��������6,il.LOTTABLE07 ��������7,il.LOTTABLE08 ��������8,il.LOTTABLE09 ��������9,il.LOTTABLE10 ��������10 "
								+"from inv_inventory ii "
								+"inner join bas_item bi on ii.STORER_CODE=bi.STORER_CODE and ii.ITEM_CODE=bi.ITEM_CODE "
								+"inner join bas_item_unit biu on bi.UNIT_CODE=biu.unit_code "
								+"inner join inv_lot il on ii.LOT_NO=il.LOT_NO "
								+"where ii.warehouse_code='"+warrehouseCode+"' and ii.storer_code='"+storerCode+"' ";
						tableQueryDialog tableQuery = new tableQueryDialog(sql,false);
						Toolkit toolkit = Toolkit.getDefaultToolkit();
						int x = (int)(toolkit.getScreenSize().getWidth()-tableQuery.getWidth())/2;
						int y = (int)(toolkit.getScreenSize().getHeight()-tableQuery.getHeight())/2;
						tableQuery.setLocation(x, y);
						tableQuery.setModal(true);
						tableQuery.setVisible(true);
						DataManager dm = tableQueryDialog.resultDM;
						if(dm==null || dm.getCurrentCount()==0){
							return;
						}
						detailTable.setValueAt(cb_from_storer.getSelectedItem().toString(), row, detailTable.getColumnModel().getColumnIndex("����"));
						detailTable.setValueAt(cb_from_warehouse.getSelectedItem().toString(), row, detailTable.getColumnModel().getColumnIndex("�ֿ�"));
						detailTable.setValueAt(cb_to_storer.getSelectedItem().toString(), row, detailTable.getColumnModel().getColumnIndex("Ŀ�����"));
						detailTable.setValueAt(cb_to_warehouse.getSelectedItem().toString(), row, detailTable.getColumnModel().getColumnIndex("Ŀ��ֿ�"));
						detailTable.setValueAt(dm.getString("��Ʒ����", 0), row, detailTable.getColumnModel().getColumnIndex("��Ʒ����"));
						detailTable.setValueAt(dm.getString("��Ʒ����", 0), row, detailTable.getColumnModel().getColumnIndex("��Ʒ����"));
						detailTable.setValueAt(dm.getString("��Ʒ����", 0), row, detailTable.getColumnModel().getColumnIndex("��Ʒ����"));
						detailTable.setValueAt(dm.getString("�������", 0), row, detailTable.getColumnModel().getColumnIndex("ԭ�������"));
						detailTable.setValueAt(dm.getString("��λ", 0), row, detailTable.getColumnModel().getColumnIndex("��λ"));
						detailTable.setValueAt(dm.getString("��λ", 0), row, detailTable.getColumnModel().getColumnIndex("ԭ��λ"));
						detailTable.setValueAt(dm.getString("���", 0), row, detailTable.getColumnModel().getColumnIndex("ԭ���"));
						detailTable.setValueAt(dm.getString("����", 0), row, detailTable.getColumnModel().getColumnIndex("ԭ���κ�"));
						detailTable.setValueAt(dm.getString("��������1", 0), row, detailTable.getColumnModel().getColumnIndex("ԭ��������1"));
						detailTable.setValueAt(dm.getString("��������2", 0), row, detailTable.getColumnModel().getColumnIndex("ԭ��������2"));
						detailTable.setValueAt(dm.getString("��������3", 0), row, detailTable.getColumnModel().getColumnIndex("ԭ��������3"));
						detailTable.setValueAt(dm.getString("��������4", 0), row, detailTable.getColumnModel().getColumnIndex("ԭ��������4"));
						detailTable.setValueAt(dm.getString("��������5", 0), row, detailTable.getColumnModel().getColumnIndex("ԭ��������5"));
						detailTable.setValueAt(dm.getString("��������6", 0), row, detailTable.getColumnModel().getColumnIndex("ԭ��������6"));
						detailTable.setValueAt(dm.getString("��������7", 0), row, detailTable.getColumnModel().getColumnIndex("ԭ��������7"));
						detailTable.setValueAt(dm.getString("��������8", 0), row, detailTable.getColumnModel().getColumnIndex("ԭ��������8"));
						detailTable.setValueAt(dm.getString("��������9", 0), row, detailTable.getColumnModel().getColumnIndex("ԭ��������9"));
						detailTable.setValueAt(dm.getString("��������10", 0), row, detailTable.getColumnModel().getColumnIndex("ԭ��������10"));
						//Ĭ��Ŀ���������Ժ�ԭ��������һ��
						detailTable.setValueAt(dm.getString("��������1", 0), row, detailTable.getColumnModel().getColumnIndex("Ŀ����������1"));
						detailTable.setValueAt(dm.getString("��������2", 0), row, detailTable.getColumnModel().getColumnIndex("Ŀ����������2"));
						detailTable.setValueAt(dm.getString("��������3", 0), row, detailTable.getColumnModel().getColumnIndex("Ŀ����������3"));
						detailTable.setValueAt(dm.getString("��������4", 0), row, detailTable.getColumnModel().getColumnIndex("Ŀ����������4"));
						detailTable.setValueAt(dm.getString("��������5", 0), row, detailTable.getColumnModel().getColumnIndex("Ŀ����������5"));
						detailTable.setValueAt(dm.getString("��������6", 0), row, detailTable.getColumnModel().getColumnIndex("Ŀ����������6"));
						detailTable.setValueAt(dm.getString("��������7", 0), row, detailTable.getColumnModel().getColumnIndex("Ŀ����������7"));
						detailTable.setValueAt(dm.getString("��������8", 0), row, detailTable.getColumnModel().getColumnIndex("Ŀ����������8"));
						detailTable.setValueAt(dm.getString("��������9", 0), row, detailTable.getColumnModel().getColumnIndex("Ŀ����������9"));
						detailTable.setValueAt(dm.getString("��������10", 0), row, detailTable.getColumnModel().getColumnIndex("Ŀ����������10"));
						detailTable.setValueAt(dm.getString("���ID", 0), row, detailTable.getColumnModel().getColumnIndex("ԭ���ID"));
						//���ԭ�ֿ��ԭ���� �� Ŀ��ֿ� Ŀ�����һ�£�˵�����ʱ���û�ֻ��������������ԣ�
						if(cb_from_warehouse.getSelectedOID().equals(cb_to_warehouse.getSelectedOID()) 
								&& cb_from_storer.getSelectedOID().equals(cb_to_storer.getSelectedOID())){
							detailTable.setValueAt(dm.getString("��λ", 0), row, detailTable.getColumnModel().getColumnIndex("Ŀ���λ"));
							detailTable.setValueAt(dm.getString("���", 0), row, detailTable.getColumnModel().getColumnIndex("Ŀ�����"));
						}
						
						
						JTableUtil.fitTableColumns(detailTable);
//						detailTable.editCellAt(detailTable.getSelectedRow(), detailTable.getColumnModel().getColumnIndex("Ŀ���λ"));
					}else if(column==detailTable.getColumnModel().getColumnIndex("Ŀ���λ")){
						String sql = "select bl.location_code ��λ from bas_location bl "
								+"where bl.warehouse_code='"+to_warrehouseCode+"' and bl.LOCATION_TYPE_CODE='Normal' ";
						tableQueryDialog tableQuery = new tableQueryDialog(sql,false);
						Toolkit toolkit = Toolkit.getDefaultToolkit();
						int x = (int)(toolkit.getScreenSize().getWidth()-tableQuery.getWidth())/2;
						int y = (int)(toolkit.getScreenSize().getHeight()-tableQuery.getHeight())/2;
						tableQuery.setLocation(x, y);
						tableQuery.setModal(true);
						tableQuery.setVisible(true);
						DataManager dm = tableQueryDialog.resultDM;
						if(dm==null || dm.getCurrentCount()==0){
							return;
						}
						detailTable.setValueAt(dm.getString("��λ", 0), row, detailTable.getColumnModel().getColumnIndex("Ŀ���λ"));
						JTableUtil.fitTableColumns(detailTable);
//						detailTable.editCellAt(detailTable.getSelectedRow(), detailTable.getColumnModel().getColumnIndex("Ŀ�����"));
					}else if(column==detailTable.getColumnModel().getColumnIndex("Ŀ�����")){
						String sql = "select '*' ���  union all select bc.container_code ��� from bas_container bc "
								+"where bc.warehouse_code='"+to_warrehouseCode+"' ";
						tableQueryDialog tableQuery = new tableQueryDialog(sql,false);
						Toolkit toolkit = Toolkit.getDefaultToolkit();
						int x = (int)(toolkit.getScreenSize().getWidth()-tableQuery.getWidth())/2;
						int y = (int)(toolkit.getScreenSize().getHeight()-tableQuery.getHeight())/2;
						tableQuery.setLocation(x, y);
						tableQuery.setModal(true);
						tableQuery.setVisible(true);
						DataManager dm = tableQueryDialog.resultDM;
						if(dm==null || dm.getCurrentCount()==0){
							return;
						}
						detailTable.setValueAt(dm.getObject("���", 0), row, detailTable.getColumnModel().getColumnIndex("Ŀ�����"));
						JTableUtil.fitTableColumns(detailTable);
//						detailTable.editCellAt(detailTable.getSelectedRow(), detailTable.getColumnModel().getColumnIndex("Ŀ������"));
					}
				}
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					JPopupMenu popupmenu = new JPopupMenu();
					JMenuItem menuItem1 = new JMenuItem();
					menuItem1.setLabel("����һ��");
					menuItem1.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(ActionEvent e) {
							// ����һ�п���
							Object[] addRowValues = new Object[detailTable.getColumnCount()];
							addRowValues[detailTable.getColumnModel().getColumnIndex("Ŀ������")]="0";
							detailTable.addRow(addRowValues);
							tableRowColorSetup(detailTable);
						}
						});
					popupmenu.add(menuItem1);
					popupmenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
		scrollPane.setViewportView(detailTable);
		detailTable.getModel().addTableModelListener(new TableModelListener()
	    {
	      public void tableChanged(TableModelEvent e)
	      {
	        tblMain_tableChanged(e);
	      }
	    });

		JPanel leftPanel = new JPanel();
		contentPane.add(leftPanel, BorderLayout.WEST);
		leftPanel.setLayout(new BorderLayout(0, 0));
		leftPanel.setPreferredSize(new Dimension(100, 120));

		JScrollPane scrollPane_1 = new JScrollPane();
		leftPanel.add(scrollPane_1, BorderLayout.CENTER);

		headerTable = new PBSUIBaseGrid();
		headerTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				headerTableClick();
			}
		});
		scrollPane_1.setViewportView(headerTable);
		getHeaderTableData("");
		if(detailTable.getRowCount()==0){
			initDetailTable(" and 1<>1 ");
		}
		
	}

	private void initDetailTable(String strWhere) {
//		String sql = "select bs.STORER_NAME ����,bw.WAREHOUSE_NAME �ֿ�,bi.ITEM_BAR_CODE ��Ʒ����,bi.ITEM_CODE ��Ʒ����,bi.ITEM_NAME ��Ʒ����,"
//				+ "ii.ON_HAND_QTY+ii.IN_TRANSIT_QTY-(ii.ALLOCATED_QTY)-(ii.PICKED_QTY) ԭ�������,biu.UNIT_NAME ��λ,ii.LOCATION_CODE ԭ��λ,ii.CONTAINER_CODE ԭ���,ii.LOT_NO ԭ���κ�"
//				+ ",il.LOTTABLE01 ԭ��������1,il.LOTTABLE02 ԭ��������2,il.LOTTABLE03 ԭ��������3,il.LOTTABLE04 ԭ��������4,il.LOTTABLE05 ԭ��������5,il.LOTTABLE06 ԭ��������6,il.LOTTABLE07 ԭ��������7,il.LOTTABLE08 ԭ��������8,il.LOTTABLE09 ԭ��������9,il.LOTTABLE10 ԭ��������10"
//				+ ",'' Ŀ�����,'' Ŀ��ֿ�,'' Ŀ���λ,'' Ŀ�����,'' Ŀ������"
//				+ ",'' Ŀ����������1,'' Ŀ����������2,'' Ŀ����������3,'' Ŀ����������4,'' Ŀ����������5,'' Ŀ����������6,'' Ŀ����������7,'' Ŀ����������8,'' Ŀ����������9,'' Ŀ����������10,ii.INV_INVENTORY_ID ԭ���ID "
//				+ "from inv_inventory ii " + "inner join inv_lot il on ii.LOT_NO=il.LOT_NO "
//				+ "inner join bas_storer bs on ii.STORER_CODE=bs.STORER_CODE "
//				+ "inner join bas_warehouse bw on ii.WAREHOUSE_CODE=bw.WAREHOUSE_CODE "
//				+ "inner join bas_item bi on ii.STORER_CODE=bi.STORER_CODE and ii.ITEM_CODE=bi.ITEM_CODE "
//				+ "inner join bas_item_unit biu on bi.UNIT_CODE=biu.unit_code " + "where 1=1 ";
		String sql = "select case itd.STATUS when '100' then '�½�' when '900' then '���' else itd.STATUS end ״̬,"
				+ "bs.STORER_NAME ����,bw.WAREHOUSE_NAME �ֿ�,bi.ITEM_BAR_CODE ��Ʒ����,bi.ITEM_CODE ��Ʒ����,bi.ITEM_NAME ��Ʒ����, "
        		+"itd.FROM_QTY ԭ�������,itd.FROM_UOM ��λ,itd.FROM_LOCATION_CODE ԭ��λ,"
        		+"itd.FROM_CONTAINER_CODE ԭ���,itd.FROM_LOT_NO ԭ���κ�,itd.LOTTABLE01 ԭ��������1,itd.LOTTABLE02 ԭ��������2,itd.LOTTABLE03 ԭ��������3,itd.LOTTABLE04 ԭ��������4,"
        		+"itd.LOTTABLE05 ԭ��������5,itd.LOTTABLE06 ԭ��������6,itd.LOTTABLE07 ԭ��������7,itd.LOTTABLE08 ԭ��������8,itd.LOTTABLE09 ԭ��������9,itd.LOTTABLE10 ԭ��������10,"
        		+"bs2.STORER_NAME Ŀ�����,bw2.WAREHOUSE_NAME Ŀ��ֿ�,itd.TO_LOCATION_CODE Ŀ���λ,itd.TO_CONTAINER_CODE Ŀ�����,itd.REQ_QTY Ŀ������,"
        		+"itd.TO_LOTTABLE01 Ŀ����������1,itd.TO_LOTTABLE02 Ŀ����������2,itd.TO_LOTTABLE03 Ŀ����������3,itd.TO_LOTTABLE04 Ŀ����������4,itd.TO_LOTTABLE05 Ŀ����������5,"
        		+"itd.TO_LOTTABLE06 Ŀ����������6,itd.TO_LOTTABLE07 Ŀ����������7,itd.TO_LOTTABLE08 Ŀ����������8,itd.TO_LOTTABLE09 Ŀ����������9,itd.TO_LOTTABLE10 Ŀ����������10,"
        		+"itd.INV_INVENTORY_ID ԭ���ID,itd.TO_INV_INVENTORY_ID Ŀ����ID "
        		+"from inv_transfer_detail itd "
        		+"inner join inv_transfer_header ith on ith.TRANSFER_NO=itd.TRANSFER_NO "
        		+"inner join bas_storer bs on itd.FROM_STORER_CODE=bs.STORER_CODE "
        		+"inner join bas_storer bs2 on itd.TO_STORER_CODE=bs2.STORER_CODE "
        		+"inner join bas_warehouse bw on itd.FROM_WAREHOUSE_CODE=bw.WAREHOUSE_CODE "
        		+"inner join bas_warehouse bw2 on itd.TO_WAREHOUSE_CODE=bw2.WAREHOUSE_CODE "
        		+"inner join bas_item bi on itd.FROM_STORER_CODE=bi.STORER_CODE and itd.ITEM_CODE=bi.ITEM_CODE "
        		+"where 1=1 ";
		if (!strWhere.equals("")) {
			sql = sql + strWhere;
		}
		DataManager dm = DBOperator.DoSelect2DM(sql);
		if (detailTable.getColumnCount() == 0) {
			detailTable.setColumn(dm.getCols());
		}
		detailTable.removeRowAll();
		detailTable.setData(dm.getDataStrings());
		detailTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		detailTable.setColumnEditableAll(false);
		detailTable.setColumnSelectionAllowed(true);
		JTableUtil.fitTableColumnsDoubleWidth(detailTable);
		tableRowColorSetup(detailTable);
		detailTable.setRowHeight(30);
	}

	private String checkTransferNoStatus(String transferNo) {
		String sql = "select TRANSFER_NO,STATUS from inv_transfer_header where TRANSFER_NO='" + transferNo + "' ";
		DataManager dm = DBOperator.DoSelect2DM(sql);
		if (dm == null || dm.getCurrentCount() == 0) {
			return "";
		} else {
			return dm.getString("STATUS", 0);
		}
	}
	
	public void clearDetailTable(){
		int row = detailTable.getRowCount();
		for(int i=0;i<row;i++){
			String itemBarCode = detailTable.getValueAt(i, 1)==null?"":detailTable.getValueAt(i, 1).toString();
			if(itemBarCode.equals("")){
				detailTable.removeRow(i);
				i = i - 1;
				row = detailTable.getRowCount();
			}
		}
	}
	
	private void tblMain_tableChanged(TableModelEvent e)
	  {
	    int selectRow = e.getFirstRow();
	    int column = e.getColumn();
	    int type = e.getType();

	    if (!trigTable) return;
	    if (selectRow<0) return;
	    switch(type)
	    {
	      case TableModelEvent.DELETE:
	        break;
	      case TableModelEvent.INSERT:
	        break;
	      case TableModelEvent.UPDATE:
			trigTable = false;
			if (column == detailTable.getColumnModel().getColumnIndex("Ŀ������")) {
				String fromQty = object2String(detailTable.getValueAt(selectRow, detailTable.getColumnModel().getColumnIndex("ԭ�������")));
				String toQty = object2String(detailTable.getValueAt(selectRow, detailTable.getColumnModel().getColumnIndex("Ŀ������")));
				if (detailTable.getValueAt(selectRow, column) != null
						&& !detailTable.getValueAt(selectRow, column).equals("")) {
					if(Math_SAM.str2Double(fromQty)<Math_SAM.str2Double(toQty)){
						Message.showWarningMessage("����Ŀ���������ܴ���ԭ���������"+fromQty+"��");
						detailTable.setValueAt("0", selectRow, column);
					}
				}
			}else if (column == detailTable.getColumnModel().getColumnIndex("Ŀ���λ")) {
				String value = object2String(detailTable.getValueAt(selectRow, column));
				if(!value.equals("")){
					String sql = "select location_code from bas_location where warehouse_code='"+cb_to_warehouse.getSelectedOID()+"' and location_code='"+value+"'";
					DataManager dm = DBOperator.DoSelect2DM(sql);
					if(dm==null || dm.getCurrentCount()==0){
						Message.showWarningMessage("����Ŀ�λ����ȷ");
						detailTable.setValueAt("", selectRow, column);
					}
				}
			}else if (column == detailTable.getColumnModel().getColumnIndex("Ŀ�����")) {
				String value = object2String(detailTable.getValueAt(selectRow, column));
				if(!value.equals("") && !value.equals("*")){
					String sql = "select container_code from bas_container where warehouse_code='"+cb_to_warehouse.getSelectedOID()+"' and container_code='"+value+"' ";
					DataManager dm = DBOperator.DoSelect2DM(sql);
					if(dm==null || dm.getCurrentCount()==0){
						Message.showWarningMessage("�������Ų���ȷ");
						detailTable.setValueAt("", selectRow, column);
					}
				}
			}
			trigTable = true;
			break;
	    }
	  }
	
	private void getHeaderTableData(String strWhere){
		String sql = "select ith.TRANSFER_NO ����,ith.WAREHOUSE_CODE ԭ�ֿ����,bw.WAREHOUSE_NAME ԭ�ֿ�����,ith.TO_WAREHOUSE_CODE Ŀ��ֿ����,bw2.WAREHOUSE_NAME Ŀ��ֿ����� " 
				+",ith.STORER_CODE ԭ��������,bs.STORER_NAME ԭ��������,ith.TO_STORER_CODE Ŀ���������,bs2.STORER_NAME Ŀ���������  "
				+",case ith.STATUS when '100' then '�½�' when '900' then '���' else ith.STATUS end ״̬ "
				+",ith.CREATED_DTM_LOC ��������,su.USER_NAME ������ "
				+"from inv_transfer_header ith "
				+"inner join bas_storer bs on ith.STORER_CODE=bs.STORER_CODE "
				+"inner join bas_storer bs2 on ith.TO_STORER_CODE=bs2.STORER_CODE "
				+"inner join bas_warehouse bw on ith.WAREHOUSE_CODE=bw.WAREHOUSE_CODE "
				+"inner join bas_warehouse bw2 on ith.TO_WAREHOUSE_CODE=bw2.WAREHOUSE_CODE "
				+"left outer join sys_user su on ith.CREATED_BY_USER=su.USER_CODE "
				+"where ith.WAREHOUSE_CODE='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' ";
		if(!strWhere.equals("")){
			sql = sql + strWhere;
		}
		sql = sql + " order by ith.CREATED_DTM_LOC ";
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
		//��ͷ����������
		for(int i=0;i<headerTable.getColumnCount();i++){
			if(i==headerTable.getColumnModel().getColumnIndex("����")) continue;
			headerTable.setColumnVisible(i, false, 0);
		}
		headerTable.updateUI();
		
		if(headerTable.getRowCount()>0){
			int row = headerTable.getSelectedRow();
			if(row<=0){
				row = headerTable.getRowIndexByColIndexAndColValue(headerTable.getColumnModel().getColumnIndex("����"), txt_transfer_no.getText().trim());
				headerTable.setRowSelectionInterval(row,row);
			}
		}
		headerTableClick();
	}
	
	private void headerTableClick(){
		int r= headerTable.getSelectedRow();
        if(headerTable.getRowCount()>0){
	        String transferNo = headerTable.getValueAt(r, headerTable.getColumnModel().getColumnIndex("����")).toString();
	        initDetailTable(" and itd.TRANSFER_NO='"+transferNo+"' ");
	        String sql = "select ith.TRANSFER_NO,case ith.STATUS when '100' then '�½�' when '900' then '���' else ith.STATUS end STATUS,"
	        		+ "ith.WAREHOUSE_CODE,ith.TO_WAREHOUSE_CODE,ith.STORER_CODE,ith.TO_STORER_CODE,ith.REMARK,ith.CREATED_DTM_LOC,su.USER_NAME  "
	        		+"from inv_transfer_header ith "
	        		+"inner join sys_user su on ith.CREATED_BY_USER=su.USER_CODE "
	        		+"where ith.TRANSFER_NO='"+transferNo+"'" ;
	        DataManager dm = DBOperator.DoSelect2DM(sql);
	        if(dm!=null){
	        	txt_status.setText(dm.getString("STATUS", 0));
	        	if(txt_status.getText().trim().equals("���")){
	        		txt_status.setForeground(Color.RED);
	        	}
	        	txt_transfer_no.setText(dm.getString("TRANSFER_NO", 0));
	        	txt_transfer_no.setForeground(Color.ORANGE);
	        	cb_from_warehouse.setSelectedDisplayName(dm.getString("WAREHOUSE_CODE", 0));
	        	cb_to_warehouse.setSelectedDisplayName(dm.getString("TO_WAREHOUSE_CODE", 0));
	        	cb_from_storer.setSelectedDisplayName(dm.getString("STORER_CODE", 0));
	        	cb_to_storer.setSelectedDisplayName(dm.getString("TO_STORER_CODE", 0));
	        	txt_remark.setText(dm.getString("REMARK", 0));
	        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E");
	        	txt_create_date.setText(sdf.format(dm.getDate("CREATED_DTM_LOC", 0)));
	        	txt_create_date.setForeground(Color.BLUE);
	        	txt_create_user.setText(dm.getString("USER_NAME", 0));
	        	txt_create_user.setForeground(Color.BLUE);
	        }
        }
        detailTable.setColumnEditableAll(false);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void tableRowColorSetup(PBSUIBaseGrid tab){
		Vector cellColor = new Vector();
		for(int i=0;i<tab.getRowCount();i++){
//			Vector rowColor = new Vector();
//			String totalQty = (String) tab.getValueAt(i, tab.getColumnModel().getColumnIndex("����"));
//			String receiveQty = (String) tab.getValueAt(i, tab.getColumnModel().getColumnIndex("��������"));
			// if(Math_SAM.str2Double(totalQty)-Math_SAM.str2Double(receiveQty)==0){
			
			Object[] rc1Cell = new Object[3];
			rc1Cell[0] = new Integer(i);
			rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("״̬"));
			rc1Cell[2] = Color.RED;
			String lineStatus = object2String(tab.getValueAt(i, tab.getColumnModel().getColumnIndex("״̬")));
			if(lineStatus.equals("���")){
				cellColor.addElement(rc1Cell);
			}
			

			rc1Cell = new Object[3];
			rc1Cell[0] = new Integer(i);
			rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("��Ʒ����"));
			rc1Cell[2] = Color.ORANGE;
			cellColor.addElement(rc1Cell);
			
			rc1Cell = new Object[3];
			rc1Cell[0] = new Integer(i);
			rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("ԭ�������"));
			rc1Cell[2] = Color.PINK;
			cellColor.addElement(rc1Cell);
			
			rc1Cell = new Object[3];
			rc1Cell[0] = new Integer(i);
			rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("ԭ��λ"));
			rc1Cell[2] = Color.PINK;
			cellColor.addElement(rc1Cell);

			rc1Cell = new Object[3];
			rc1Cell[0] = new Integer(i);
			rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("Ŀ���λ"));
			rc1Cell[2] = Color.ORANGE;
			cellColor.addElement(rc1Cell);

			rc1Cell = new Object[3];
			rc1Cell[0] = new Integer(i);
			rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("Ŀ�����"));
			rc1Cell[2] = Color.ORANGE;
			cellColor.addElement(rc1Cell);

			rc1Cell = new Object[3];
			rc1Cell[0] = new Integer(i);
			rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("Ŀ������"));
			rc1Cell[2] = Color.ORANGE;
			cellColor.addElement(rc1Cell);

			rc1Cell = new Object[3];
			rc1Cell[0] = new Integer(i);
			rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("Ŀ����������1"));
			rc1Cell[2] = Color.ORANGE;
			cellColor.addElement(rc1Cell);

			rc1Cell = new Object[3];
			rc1Cell[0] = new Integer(i);
			rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("Ŀ����������2"));
			rc1Cell[2] = Color.ORANGE;
			cellColor.addElement(rc1Cell);

			rc1Cell = new Object[3];
			rc1Cell[0] = new Integer(i);
			rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("Ŀ����������3"));
			rc1Cell[2] = Color.ORANGE;
			cellColor.addElement(rc1Cell);

			rc1Cell = new Object[3];
			rc1Cell[0] = new Integer(i);
			rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("Ŀ����������4"));
			rc1Cell[2] = Color.ORANGE;
			cellColor.addElement(rc1Cell);

			rc1Cell = new Object[3];
			rc1Cell[0] = new Integer(i);
			rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("Ŀ����������5"));
			rc1Cell[2] = Color.ORANGE;
			cellColor.addElement(rc1Cell);

			rc1Cell = new Object[3];
			rc1Cell[0] = new Integer(i);
			rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("Ŀ����������6"));
			rc1Cell[2] = Color.ORANGE;
			cellColor.addElement(rc1Cell);

			rc1Cell = new Object[3];
			rc1Cell[0] = new Integer(i);
			rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("Ŀ����������7"));
			rc1Cell[2] = Color.ORANGE;
			cellColor.addElement(rc1Cell);

			rc1Cell = new Object[3];
			rc1Cell[0] = new Integer(i);
			rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("Ŀ����������8"));
			rc1Cell[2] = Color.ORANGE;
			cellColor.addElement(rc1Cell);

			rc1Cell = new Object[3];
			rc1Cell[0] = new Integer(i);
			rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("Ŀ����������9"));
			rc1Cell[2] = Color.ORANGE;
			cellColor.addElement(rc1Cell);

			rc1Cell = new Object[3];
			rc1Cell[0] = new Integer(i);
			rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("Ŀ����������10"));
			rc1Cell[2] = Color.ORANGE;
			cellColor.addElement(rc1Cell);
			// rowColor.addElement(new Integer(i));
			// detailTable.setRowColor(rowColor, Color.lightGray);
			// }
		}
		tab.setCellColor(cellColor);
	}
	
	public boolean saveData(){
		try{
			if(detailTable.isEditing()){
				detailTable.getCellEditor().stopCellEditing();
			}
			if(!checkDetailTable()){
				Message.showWarningMessage("��ϸ���벻���������ܱ���");
				return false;
			}
			String transferNo = "";
			if(txt_transfer_no.getText().trim().equals("")){
				transferNo = comData.getValueFromBasNumRule("inv_transfer_header", "transfer_no");
				if(transferNo.equals("")){
					Message.showWarningMessage("���ɵ��ų�������ϵϵͳ����Ա");
					return false;
				}
			}else{
				transferNo = txt_transfer_no.getText().trim();
			}
			String storerCode = cb_from_storer.getSelectedOID();
			String warrehouseCode = cb_from_warehouse.getSelectedOID();
			String to_storerCode = cb_to_storer.getSelectedOID();
			String to_warrehouseCode = cb_to_warehouse.getSelectedOID();
			String remark = txt_remark.getText().trim();
			//�����ͷ
			String sql = "select TRANSFER_NO from inv_transfer_header where TRANSFER_NO='"+transferNo+"'";
			DataManager dm = DBOperator.DoSelect2DM(sql);
			if(dm==null || dm.getCurrentCount()==0){
				sql = "insert into inv_transfer_header(TRANSFER_NO,WAREHOUSE_CODE,TO_WAREHOUSE_CODE,"
						+"STORER_CODE,TO_STORER_CODE,TYPE,STATUS,REMARK,CREATED_BY_USER,CREATED_DTM_LOC) "
						+"select '"+transferNo+"','"+warrehouseCode+"','"+to_warrehouseCode+"', "
						+"'"+storerCode+"','"+to_storerCode+"','Normal','100','"+remark+"','"
						+MainFrm.getUserInfo().getString("USER_CODE", 0)+"',now() "
						+"";
			}else{
				sql = "update inv_transfer_header set REMARK='"+remark+"'"
						+ ",UPDATED_BY_USER='"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"',UPDATED_DTM_LOC=now() "
						+ "where TRANSFER_NO='"+transferNo+"' "
						+"";
			}
			int t = DBOperator.DoUpdate(sql);
			if(t==1){
				txt_transfer_no.setText(transferNo);
				//������ϸ ��ɾ���ٲ���
				if(detailTable.isEditing()){
					detailTable.getCellEditor().stopCellEditing();
				}
				sql = "delete from inv_transfer_detail where TRANSFER_NO='"+transferNo+"' ";
				DBOperator.DoUpdate(sql);
				for(int i=0;i<detailTable.getRowCount();i++){
					String INV_INVENTORY_ID=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("ԭ���ID")));
					String TO_LOCATION_CODE=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("Ŀ���λ"))).toString();
					String TO_CONTAINER_CODE=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("Ŀ�����"))).toString();
					String FROM_QTY=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("ԭ�������"))).toString();
					String REQ_QTY=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("Ŀ������"))).toString();
					String REQ_UOM=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("��λ"))).toString();
					String LOTTABLE01=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("ԭ��������1"))).toString();
					String LOTTABLE02=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("ԭ��������2"))).toString();
					String LOTTABLE03=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("ԭ��������3"))).toString();
					String LOTTABLE04=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("ԭ��������4"))).toString();
					String LOTTABLE05=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("ԭ��������5"))).toString();
					String LOTTABLE06=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("ԭ��������6"))).toString();
					String LOTTABLE07=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("ԭ��������7"))).toString();
					String LOTTABLE08=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("ԭ��������8"))).toString();
					String LOTTABLE09=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("ԭ��������9"))).toString();
					String LOTTABLE10=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("ԭ��������10"))).toString();
					String TO_LOTTABLE01=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("Ŀ����������1"))).toString();
					String TO_LOTTABLE02=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("Ŀ����������2"))).toString();
					String TO_LOTTABLE03=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("Ŀ����������3"))).toString();
					String TO_LOTTABLE04=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("Ŀ����������4"))).toString();
					String TO_LOTTABLE05=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("Ŀ����������5"))).toString();
					String TO_LOTTABLE06=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("Ŀ����������6"))).toString();
					String TO_LOTTABLE07=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("Ŀ����������7"))).toString();
					String TO_LOTTABLE08=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("Ŀ����������8"))).toString();
					String TO_LOTTABLE09=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("Ŀ����������9"))).toString();
					String TO_LOTTABLE10=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("Ŀ����������10"))).toString();
					sql = "insert into inv_transfer_detail(INV_TRANSFER_HEADER_ID,TRANSFER_NO,STATUS,INV_INVENTORY_ID,FROM_WAREHOUSE_CODE,TO_WAREHOUSE_CODE"
							+",FROM_STORER_CODE,TO_STORER_CODE,FROM_LOCATION_CODE,TO_LOCATION_CODE,FROM_CONTAINER_CODE,TO_CONTAINER_CODE,FROM_LOT_NO"
							+",ITEM_CODE,FROM_QTY,FROM_UOM,REQ_QTY,REQ_UOM,LOTTABLE01,LOTTABLE02,LOTTABLE03,LOTTABLE04,LOTTABLE05"
							+",LOTTABLE06,LOTTABLE07,LOTTABLE08,LOTTABLE09,LOTTABLE10,TO_LOTTABLE01,TO_LOTTABLE02,TO_LOTTABLE03,TO_LOTTABLE04,TO_LOTTABLE05"
							+",TO_LOTTABLE06,TO_LOTTABLE07,TO_LOTTABLE08,TO_LOTTABLE09,TO_LOTTABLE10,CREATED_BY_USER,CREATED_DTM_LOC) "
							+"select (select INV_TRANSFER_HEADER_ID from inv_transfer_header where TRANSFER_NO='"+transferNo+"' limit 1),'"+transferNo+"'"
							+",'100','"+INV_INVENTORY_ID+"','"+warrehouseCode+"','"+to_warrehouseCode+"' "
							+",'"+storerCode+"','"+to_storerCode+"',ii.location_code,'"+TO_LOCATION_CODE+"',ii.container_code,'"+TO_CONTAINER_CODE+"',ii.lot_no"
							+",ii.item_code,"+FROM_QTY+",'"+REQ_UOM+"',"+REQ_QTY+",'"+REQ_UOM+"','"+LOTTABLE01+"','"+LOTTABLE02+"','"+LOTTABLE03+"','"+LOTTABLE04+"'"
							+",'"+LOTTABLE05+"','"+LOTTABLE06+"','"+LOTTABLE07+"','"+LOTTABLE08+"','"+LOTTABLE09+"','"+LOTTABLE10+"','"+TO_LOTTABLE01+"','"+TO_LOTTABLE02+"','"+TO_LOTTABLE03+"','"+TO_LOTTABLE04+"'"
							+",'"+TO_LOTTABLE05+"','"+TO_LOTTABLE06+"','"+TO_LOTTABLE07+"','"+TO_LOTTABLE08+"','"+TO_LOTTABLE09+"','"+TO_LOTTABLE10+"'"
							+",'"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"',now() "
							+"from inv_inventory ii where ii.INV_INVENTORY_ID='"+INV_INVENTORY_ID+"' ";
					t = DBOperator.DoUpdate(sql);
					if(t==0){
						sql = "delete from inv_transfer_detail where TRANSFER_NO='"+transferNo+"' ";
						DBOperator.DoUpdate(sql);
						sql = "delete from inv_transfer_header where TRANSFER_NO='"+transferNo+"' ";
						DBOperator.DoUpdate(sql);
						return false;
					}
				}
				
			}else{
				sql = "delete from inv_transfer_detail where TRANSFER_NO='"+transferNo+"' ";
				DBOperator.DoUpdate(sql);
				sql = "delete from inv_transfer_header where TRANSFER_NO='"+transferNo+"' ";
				DBOperator.DoUpdate(sql);
				Message.showErrorMessage("��ͷ���ݱ���ʧ��");
				return false;
			}
			
		}catch(Exception e){
			String transferNo = txt_transfer_no.getText().trim();
			String sql = "delete from inv_transfer_detail where TRANSFER_NO='"+transferNo+"' ";
			DBOperator.DoUpdate(sql);
			sql = "delete from inv_transfer_header where TRANSFER_NO='"+transferNo+"' ";
			DBOperator.DoUpdate(sql);
			e.printStackTrace();
			Message.showErrorMessage(e.getMessage());
			return false;
		}
		return true;
	}
	
	public boolean auditData(){
		String lineStatus = "";
		String itemCode = "";
		String invID = "";
		String REQ_QTY = "";
		String transferNo = txt_transfer_no.getText().trim();
		String storerCode = cb_from_storer.getSelectedOID();
		String storerName = cb_from_storer.getSelectedItem().toString();
		String warrehouseCode = cb_from_warehouse.getSelectedOID();
		String to_storerCode = cb_to_storer.getSelectedOID();
		String to_warrehouseCode = cb_to_warehouse.getSelectedOID();
		for(int i=0;i<detailTable.getRowCount();i++){
			lineStatus = object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("״̬")));
			if(lineStatus.equals("���")){
				continue;
			}
			//���Ŀ���������Ʒ�����Ƿ���ڣ���������Ҫ����
			itemCode = object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("��Ʒ����")));
			invID = object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("ԭ���ID")));
			REQ_QTY=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("Ŀ������"))).toString();
			String sql = "select item_code from bas_item where storer_code='"+to_storerCode+"' and item_code='"+itemCode+"' ";
			DataManager dm = DBOperator.DoSelect2DM(sql);
			if(dm==null || dm.getCurrentCount()==0){
				sql = "INSERT INTO `bas_item` (`BAS_ITEM_ID`, `STORER_CODE`, `WAREHOUSE_CODE`, `BRAND_CODE`, `ITEM_CODE`, `ITEM_NAME`, `DESCRIPTION`, `ITEM_BAR_CODE`, `PORT_CODE`, `PLATFORM_CODE`, `VENDOR_CODE`, `UNIT_CODE`, `ITEM_SPEC`, `HSCODE`, `HSCODE_DESC`, `TAX_NUMBER`, `ITEM_CLASS_CODE`, `COUNTRY_CODE`, `VALIDITY`, `VALIDITY_UOM`, `DIVISION`, `DEPARTMENT`, `COST`, `ITEM_STYLE`, `ITEM_COLOR`, `ITEM_SIZE`, `IS_LOT_CONTROLLED`, `IS_CATCH_WEIGHT_REQD`, `IS_FRANGIBLE`, `IS_VALUABLES`, `IS_CONTAIN_FITTING`, `IS_TRACKING_SERIAL_NUM`, `PROCESS_STAMP`, `LIST_PRICE`, `NET_PRICE`, `PACKING_CLASS`, `WEIGHT`, `VOLUME`, `VOLUME_UOM`, `LENGTH`, `WIDTH`, `HEIGHT`, `DIMENSION_UOM`, `PIC_URL`, `ITEM_CATEGORY1`, `ITEM_CATEGORY2`, `ITEM_CATEGORY3`, `ITEM_CATEGORY4`, `ITEM_CATEGORY5`, `ITEM_CATEGORY6`, `ITEM_CATEGORY7`, `ITEM_CATEGORY8`, `ITEM_CATEGORY9`, `ITEM_CATEGORY10`, `NEED_QC`, `INBOUND_QC_UM`, `ALTERNATE_ITEM`, `WEB_THUMBNAILIMG`, `INBOUND_QC_AMOUNT`, `INBOUND_QC_AMOUNT_TYPE`, `WEB_IMG`, `NEED_PC`, `REMARK`, `TEMPERATURE`, `IS_LARGE`, `TRANSFER_DATE`, `IS_AIRLINE_BAN_PRODUCTS`, `WEIGHT_UOM`, `USER_DEF1`, `USER_DEF2`, `USER_DEF3`, `USER_DEF4`, `USER_DEF5`, `USER_DEF6`, `USER_DEF7`, `USER_DEF8`, `USER_DEF9`, `USER_DEF10`, `CREATED_BY_USER`, `CREATED_OFFICE`, `CREATED_DTM_LOC`, `CREATED_TIME_ZONE`, `UPDATED_BY_USER`, `UPDATED_OFFICE`, `UPDATED_DTM_LOC`, `UPDATED_TIME_ZONE`, `RECORD_VERSION`, `ITEM_CODE_CAPITAL`, `SHELFLIFE`, `INSURANCE_LIFE`, `IS_SHELFLIFE_CONTROL`, `SHELFLIFE_TYPE`, `INB_LIFE`, `OUB_LIFE`, `ONLINE_LIFE`, `VALUE_LEVEL`, `GROSS_WEIGHT`, `NET_WEIGHT`, `TARE_WEIGHT`, `TI`, `HI`, `IS_QC`, `REC_QC_LOC`, `REC_LOC`, `PICKTO_LOC`, `RETURN_LOC`, `TURNOVER_LOC`, `PUTAWAY_ZONE`, `PUTAWAY_LOC`, `PUTAWAY_RULE`, `PUTAWAY_CALCULATE`, `REC_VERIFY`, `ADDINVENTORY_STRATEGY`, `USER_ANNOTATION1`, `USER_ANNOTATION2`, `pack_spec`, `invoice_company_code`, `invoice_item_code`, `oub_invoice_template_id`, `vendor_item`, `brand_code_id`, `IS_SORTABLE`, `PACK_CODE`, `LABEL_LANG`, `COUNTRY_OF_ORIGIN`, `BRAND_NAME`)  "
						+"select null, '"+to_storerCode+"', `WAREHOUSE_CODE`, `BRAND_CODE`, `ITEM_CODE`, `ITEM_NAME`, `DESCRIPTION`, `ITEM_BAR_CODE`, `PORT_CODE`, `PLATFORM_CODE`, `VENDOR_CODE`, `UNIT_CODE`, `ITEM_SPEC`, `HSCODE`, `HSCODE_DESC`, `TAX_NUMBER`, `ITEM_CLASS_CODE`, `COUNTRY_CODE`, `VALIDITY`, `VALIDITY_UOM`, `DIVISION`, `DEPARTMENT`, `COST`, `ITEM_STYLE`, `ITEM_COLOR`, `ITEM_SIZE`, `IS_LOT_CONTROLLED`, `IS_CATCH_WEIGHT_REQD`, `IS_FRANGIBLE`, `IS_VALUABLES`, `IS_CONTAIN_FITTING`, `IS_TRACKING_SERIAL_NUM`, `PROCESS_STAMP`, `LIST_PRICE`, `NET_PRICE`, `PACKING_CLASS`, `WEIGHT`, `VOLUME`, `VOLUME_UOM`, `LENGTH`, `WIDTH`, `HEIGHT`, `DIMENSION_UOM`, `PIC_URL`, `ITEM_CATEGORY1`, `ITEM_CATEGORY2`, `ITEM_CATEGORY3`, `ITEM_CATEGORY4`, `ITEM_CATEGORY5`, `ITEM_CATEGORY6`, `ITEM_CATEGORY7`, `ITEM_CATEGORY8`, `ITEM_CATEGORY9`, `ITEM_CATEGORY10`, `NEED_QC`, `INBOUND_QC_UM`, `ALTERNATE_ITEM`, `WEB_THUMBNAILIMG`, `INBOUND_QC_AMOUNT`, `INBOUND_QC_AMOUNT_TYPE`, `WEB_IMG`, `NEED_PC`, `REMARK`, `TEMPERATURE`, `IS_LARGE`, `TRANSFER_DATE`, `IS_AIRLINE_BAN_PRODUCTS`, `WEIGHT_UOM`, `USER_DEF1`, `USER_DEF2`, `USER_DEF3`, `USER_DEF4`, `USER_DEF5`, `USER_DEF6`, `USER_DEF7`, `USER_DEF8`, `USER_DEF9`, `USER_DEF10`, `CREATED_BY_USER`, `CREATED_OFFICE`, `CREATED_DTM_LOC`, `CREATED_TIME_ZONE`, `UPDATED_BY_USER`, `UPDATED_OFFICE`, `UPDATED_DTM_LOC`, `UPDATED_TIME_ZONE`, `RECORD_VERSION`, `ITEM_CODE_CAPITAL`, `SHELFLIFE`, `INSURANCE_LIFE`, `IS_SHELFLIFE_CONTROL`, `SHELFLIFE_TYPE`, `INB_LIFE`, `OUB_LIFE`, `ONLINE_LIFE`, `VALUE_LEVEL`, `GROSS_WEIGHT`, `NET_WEIGHT`, `TARE_WEIGHT`, `TI`, `HI`, `IS_QC`, `REC_QC_LOC`, `REC_LOC`, `PICKTO_LOC`, `RETURN_LOC`, `TURNOVER_LOC`, `PUTAWAY_ZONE`, `PUTAWAY_LOC`, `PUTAWAY_RULE`, `PUTAWAY_CALCULATE`, `REC_VERIFY`, `ADDINVENTORY_STRATEGY`, `USER_ANNOTATION1`, `USER_ANNOTATION2`, `pack_spec`, `invoice_company_code`, `invoice_item_code`, `oub_invoice_template_id`, `vendor_item`, `brand_code_id`, `IS_SORTABLE`, `PACK_CODE`, `LABEL_LANG`, `COUNTRY_OF_ORIGIN`, `BRAND_NAME`"
						+" from bas_item bi "
						+"where bi.storer_code='"+storerCode+"' and bi.item_code='"+itemCode+"'";
				int updateCount = DBOperator.DoUpdate(sql);
				if(updateCount==0){
					Message.showErrorMessage("��Ʒ��Ϣ���뵽Ŀ�����ʧ�ܣ�\n"+"Ŀ�������"+to_storerCode+"\n��Ʒ���룺"+itemCode);
					return false;
				}
			}
			sql = "select ON_HAND_QTY+IN_TRANSIT_QTY-(ALLOCATED_QTY)-(PICKED_QTY) availabledQty from inv_inventory where INV_INVENTORY_ID='"+invID+"' ";
			dm = DBOperator.DoSelect2DM(sql);
			if(dm==null || dm.getCurrentCount()==0){
				Message.showErrorMessage("ԭ�����Ϣ�����ڣ�\n"+"������"+storerName+"\n��Ʒ���룺"+itemCode);
				return false;
			}else{
				if(Math_SAM.str2Double(REQ_QTY)>Math_SAM.str2Double(dm.getString("availabledQty", 0))){
					Message.showErrorMessage("ԭ�������С�ڱ��������������\n"+"������"+storerName+"\n��Ʒ���룺"+itemCode);
					return false;
				}
			}
		}
		//�������ת��
		for(int i=0;i<detailTable.getRowCount();i++){
			lineStatus = object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("״̬")));
			if(lineStatus.equals("���")){
				continue;
			}
			itemCode = object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("��Ʒ����")));
			String INV_INVENTORY_ID=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("ԭ���ID")));
			String FROM_LOCATION_CODE=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("ԭ��λ"))).toString();
			String TO_LOCATION_CODE=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("Ŀ���λ"))).toString();
			String FROM_CONTAINER_CODE=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("ԭ���"))).toString();
			String TO_CONTAINER_CODE=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("Ŀ�����"))).toString();
			REQ_QTY=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("Ŀ������"))).toString();
			String TO_LOTTABLE01=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("Ŀ����������1"))).toString();
			String TO_LOTTABLE02=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("Ŀ����������2"))).toString();
			String TO_LOTTABLE03=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("Ŀ����������3"))).toString();
			String TO_LOTTABLE04=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("Ŀ����������4"))).toString();
			String TO_LOTTABLE05=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("Ŀ����������5"))).toString();
			String TO_LOTTABLE06=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("Ŀ����������6"))).toString();
			String TO_LOTTABLE07=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("Ŀ����������7"))).toString();
			String TO_LOTTABLE08=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("Ŀ����������8"))).toString();
			String TO_LOTTABLE09=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("Ŀ����������9"))).toString();
			String TO_LOTTABLE10=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("Ŀ����������10"))).toString();
			//�����ɿ�����κ�
			String lotNo = comData.getInventoryLotNo(to_storerCode,itemCode,TO_LOTTABLE01,TO_LOTTABLE02,TO_LOTTABLE03,TO_LOTTABLE04,TO_LOTTABLE05,TO_LOTTABLE06,TO_LOTTABLE07,TO_LOTTABLE08,TO_LOTTABLE09,TO_LOTTABLE10);
			if(!lotNo.equals("")){
				//�������
				String inventoryID = comData.getInventoryID(to_warrehouseCode,to_storerCode,itemCode,lotNo,TO_LOCATION_CODE,TO_CONTAINER_CODE,REQ_QTY,"sys");
				if(!inventoryID.equals("")){
					//����д��ɹ�
					//�ۼ�ԭ���
					String sql = "update inv_inventory ii set ii.ON_HAND_QTY=ii.ON_HAND_QTY - ("+REQ_QTY+") "
							+"where ii.INV_INVENTORY_ID='"+INV_INVENTORY_ID+"' "
							+"";
					int t = DBOperator.DoUpdate(sql);
					if(t==0){
						Message.showErrorMessage("�ۼ�ԭ���ʧ�ܣ�\n"+"������"+storerName+"\n��Ʒ���룺"+itemCode);
						return false;
					}else{
						sql = "update inv_transfer_detail set STATUS='900',TO_INV_INVENTORY_ID='"+inventoryID+"', "
							+ "UPDATED_BY_USER='"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"',UPDATED_DTM_LOC=now() "
							+ "where TRANSFER_NO='"+transferNo+"' "
							+ "and FROM_WAREHOUSE_CODE='"+warrehouseCode+"' and TO_WAREHOUSE_CODE='"+to_warrehouseCode+"' "
							+ "and FROM_STORER_CODE='"+storerCode+"' and TO_STORER_CODE='"+to_storerCode+"' "
							+ "and FROM_LOCATION_CODE='"+FROM_LOCATION_CODE+"' and TO_LOCATION_CODE='"+TO_LOCATION_CODE+"' "
							+ "and FROM_CONTAINER_CODE='"+FROM_CONTAINER_CODE+"' and TO_CONTAINER_CODE='"+TO_CONTAINER_CODE+"' "
							+ "and ITEM_CODE='"+itemCode+"' and INV_INVENTORY_ID='"+INV_INVENTORY_ID+"' ";
						t = DBOperator.DoUpdate(sql);
					}
				}else{
					return false;
				}
			}else{
				return false;
			}
		}
		//���±�ͷ״̬ status=900
		String sql = "update inv_transfer_header set STATUS='900',"
				+ "UPDATED_BY_USER='"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"',UPDATED_DTM_LOC=now() "
				+ "where TRANSFER_NO='"+transferNo+"' ";
		int t = DBOperator.DoUpdate(sql);
		Message.showInfomationMessage("�����ɹ�");
		getHeaderTableData(" and (ith.status='100' or TRANSFER_NO='"+txt_transfer_no.getText().trim()+"') ");
		int row = headerTable.getRowIndexByColIndexAndColValue(headerTable.getColumnModel().getColumnIndex("����"), txt_transfer_no.getText().trim());
		headerTable.setRowSelectionInterval(row,row);
		return true;
	}
	
	public boolean checkDetailTable(){
		for(int i=0;i<detailTable.getRowCount();i++){
			String itemBarcode = object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("��Ʒ����")));
			String toLocation = object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("Ŀ���λ")));
			String toContainer = object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("Ŀ�����")));
			String toQty = object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("Ŀ������")));
			if(itemBarcode.equals("") || toLocation.equals("") || toContainer.equals("")){
				return false;
			}
			if(Math_SAM.str2Double(toQty)==0){
				return false;
			}
		}
		return true;
	}
	
	public void detailTableEditSetup(boolean bool){
		detailTable.setColumnEditableAll(false);
//		detailTable.setColumnEditable(true, detailTable.getColumnModel().getColumnIndex("��Ʒ����"));
		detailTable.setColumnEditable(bool, detailTable.getColumnModel().getColumnIndex("Ŀ���λ"));
		detailTable.setColumnEditable(bool, detailTable.getColumnModel().getColumnIndex("Ŀ�����"));
		detailTable.setColumnEditable(bool, detailTable.getColumnModel().getColumnIndex("Ŀ������"));
		detailTable.setColumnEditable(bool, detailTable.getColumnModel().getColumnIndex("Ŀ����������1"));
		detailTable.setColumnEditable(bool, detailTable.getColumnModel().getColumnIndex("Ŀ����������2"));
		detailTable.setColumnEditable(bool, detailTable.getColumnModel().getColumnIndex("Ŀ����������3"));
		detailTable.setColumnEditable(bool, detailTable.getColumnModel().getColumnIndex("Ŀ����������4"));
		detailTable.setColumnEditable(bool, detailTable.getColumnModel().getColumnIndex("Ŀ����������5"));
		detailTable.setColumnEditable(bool, detailTable.getColumnModel().getColumnIndex("Ŀ����������6"));
		detailTable.setColumnEditable(bool, detailTable.getColumnModel().getColumnIndex("Ŀ����������7"));
		detailTable.setColumnEditable(bool, detailTable.getColumnModel().getColumnIndex("Ŀ����������8"));
		detailTable.setColumnEditable(bool, detailTable.getColumnModel().getColumnIndex("Ŀ����������9"));
		detailTable.setColumnEditable(bool, detailTable.getColumnModel().getColumnIndex("Ŀ����������10"));
		detailTable.editCellAt(detailTable.getRowCount() - 1, detailTable.getColumnModel().getColumnIndex("��Ʒ����"));
	}
	
	public String object2String(Object o){
		return o==null?"":o.toString();
	}

	private void setExtendedState(int maximizedBoth) {
		// TODO Auto-generated method stub

	}

}
