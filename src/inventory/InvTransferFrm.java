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
				if (name.equals("closed") && value.equals(Boolean.TRUE)) // 窗口被关闭
				{
					System.out.println("InvQueryFrm窗口被关闭");
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

				// 增加一行空行
				String[] addRowValues = new String[detailTable.getColumnCount()];
				addRowValues[detailTable.getColumnModel().getColumnIndex("目标数量")]="0";
				detailTable.addRow(addRowValues);
				detailTable.setComponent(new JTNumEdit(15, "#####", true), detailTable.getColumnModel().getColumnIndex("目标数量"));

				detailTableEditSetup(true);
				tableRowColorSetup(detailTable);

				// detailTable.getColumn("商品条码").setCellRenderer(new
				// MyTableCellRenderrer());
				// detailTable.getColumn("数量").setCellRenderer(new
				// MyTableCellRenderrer());
				// detailTable.getColumn("批次属性1").setCellRenderer(new
				// MyTableCellRenderrer());
				// detailTable.getColumn("批次属性2").setCellRenderer(new
				// MyTableCellRenderrer());
				// detailTable.getColumn("批次属性3").setCellRenderer(new
				// MyTableCellRenderrer());
				// detailTable.getColumn("批次属性4").setCellRenderer(new
				// MyTableCellRenderrer());
				// detailTable.getColumn("批次属性5").setCellRenderer(new
				// MyTableCellRenderrer());
				// detailTable.getColumn("批次属性6").setCellRenderer(new
				// MyTableCellRenderrer());
				// detailTable.getColumn("批次属性7").setCellRenderer(new
				// MyTableCellRenderrer());
				// detailTable.getColumn("批次属性8").setCellRenderer(new
				// MyTableCellRenderrer());
				// detailTable.getColumn("批次属性9").setCellRenderer(new
				// MyTableCellRenderrer());
				// detailTable.getColumn("批次属性10").setCellRenderer(new
				// MyTableCellRenderrer());
				detailTable.updateUI();
				// makeFace(detailTable);
				detailTable.requestFocus();
				detailTable.changeSelection(detailTable.getRowCount() - 1, detailTable.getColumnModel().getColumnIndex("商品条码"), false, false);
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
					JOptionPane.showMessageDialog(null, "请选择一行数据！");
					return;
				}
				String transferNoStatus = checkTransferNoStatus(txt_transfer_no.getText().trim());
				if (transferNoStatus.equals("")) {
					Message.showErrorMessage("变更单号不存在");
					return;
				} else if (!transferNoStatus.equals("100")) {
					Message.showWarningMessage("变更单非初始状态，不能进行修改");
					return;
				}
				if (detailTable.getRowCount() == 0) {
					// 增加一行空行
					Object[] addRowValues = new Object[detailTable.getColumnCount()];
					detailTable.addRow(addRowValues);
					detailTable.editCellAt(detailTable.getRowCount() - 1, detailTable.getColumnModel().getColumnIndex("商品条码"));
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
					JOptionPane.showMessageDialog(null, "请选择一行数据！");
					return;
				}
				String transferNo = checkTransferNoStatus(txt_transfer_no.getText().trim());
				if(transferNo.equals("")){
					Message.showErrorMessage("单号不存在");
					return;
				}else if(!transferNo.equals("100")){
					Message.showWarningMessage("非初始创建状态，不能删除");
					return;
				}
				int t = JOptionPane.showConfirmDialog(null, "是否删除该单号【"+txt_transfer_no.getText().trim()+"]？");
				if(t==0){
					//删除PO
					String sql = "delete from inv_transfer_header where TRANSFER_NO='"+txt_transfer_no.getText().trim()+"' and status='100' ";
					int delrow = DBOperator.DoUpdate(sql);
					if(delrow==1){
						sql = "delete from inv_transfer_detail where TRANSFER_NO='"+txt_transfer_no.getText().trim()+"' ";
						DBOperator.DoUpdate(sql);
						JOptionPane.showMessageDialog(null, "删除数据成功","提示",JOptionPane.INFORMATION_MESSAGE);
						getHeaderTableData(" and ith.status='100' ");
					}else{
						JOptionPane.showMessageDialog(null, "删除数据失败","提示",JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		topPanel.add(btnDelete);

		btnQuery = new JButton("\u67E5\u8BE2");
		btnQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ArrayList<String> fieldList = new ArrayList<String>();
				fieldList.add("bs.STORER_NAME:原货主名称");
				fieldList.add("ith.STORER_CODE:原货主编码");
				fieldList.add("bs2.STORER_NAME:目标货主名称");
				fieldList.add("ith.TO_STORER_CODE:目标货主编码");
				fieldList.add("bw2.WAREHOUSE_NAME:目标仓库名称");
				fieldList.add("ith.TO_WAREHOUSE_CODE:目标仓库编码");
				fieldList.add("su.USER_NAME:创建人");
				fieldList.add("ith.CREATED_DTM_LOC:创建日期");
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
					int row = headerTable.getRowIndexByColIndexAndColValue(headerTable.getColumnModel().getColumnIndex("单号"), txt_transfer_no.getText().trim());
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
				if(!comData.getUserMenuPower("库存属性变更-审核")){
					Message.showWarningMessage("无此功能权限");
					return;
				}
				String transferStatus = txt_status.getText().trim();
				if(transferStatus.equals("完成")){
					Message.showWarningMessage("已完成状态不能审核");
					return;
				}
				
				int t = JOptionPane.showConfirmDialog(null, "是否审核该单号【"+txt_transfer_no.getText().trim()+"]？\n审核之后库存的货主或批次属性会发生相应变化，请慎重！");
				if(t==0){
					new SwingWorker<String, Void>() {
						WaitingSplash splash = new WaitingSplash();

			            @Override
			            protected String doInBackground() throws Exception {
			            	//审核
			            	btnAudit.setEnabled(false);
			            	headerTable.setEnabled(false);
			            	headerTable.setColumnEditableAll(false);
			            	splash.start(); // 运行启动界面
			            	try{
			            		auditData();
			            		//记录操作日志
								DataManager dmProcess = comData.getSysProcessHistoryDataModel("sys_process_history");
								if (dmProcess != null) {
									dmdata.xArrayList list = (xArrayList) dmProcess.getRow(0);
									list.set(dmProcess.getCol("SYS_PROCESS_HISTORY_ID"), "null");
									list.set(dmProcess.getCol("PROCESS_CODE"), "INV_TRANSFER");
									list.set(dmProcess.getCol("PROCESS_NAME"), "货主属性变更");
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
									System.out.println("写入操作日志：" + bool);
								}
			            	}catch(Exception e){
			            		Message.showWarningMessage(e.getMessage());
			            	}
			                return "";
			            }

			            @Override
			            protected void done() {
			            	splash.stop(); // 运行启动界面
			                System.out.println("数据审核结束");
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
		txt_transfer_no.setFont(new Font("宋体", Font.BOLD, 14));
		txt_transfer_no.setEditable(false);
		txt_transfer_no.setColumns(12);
		panel.add(txt_transfer_no);
				
				lblNewLabel_5 = new JLabel("\u72B6\u6001\uFF1A");
				panel.add(lblNewLabel_5);
				
				txt_status = new JTextField();
				txt_status.setFont(new Font("宋体", Font.BOLD, 12));
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
						JOptionPane.showMessageDialog(null, "请输入原始货主");
						return;
					}
					if(to_storerCode.equals("")){
						JOptionPane.showMessageDialog(null, "请输入目标货主");
						return;
					}
					if(to_warrehouseCode.equals("")){
						JOptionPane.showMessageDialog(null, "请输入目标仓库");
						return;
					}
					if(column==detailTable.getColumnModel().getColumnIndex("商品条码")){
						String sql = "select ii.INV_INVENTORY_ID 库存ID,bi.ITEM_BAR_CODE 商品条码,ii.ITEM_CODE 商品编码,bi.ITEM_NAME 商品名称,ii.ON_HAND_QTY+ii.IN_TRANSIT_QTY-(ii.ALLOCATED_QTY)-(ii.PICKED_QTY) 库存数量, "
								+"biu.unit_name 单位,ii.LOCATION_CODE 库位,ii.CONTAINER_CODE 箱号,ii.LOT_NO 批次"
								+",il.LOTTABLE01 批次属性1,il.LOTTABLE02 批次属性2,il.LOTTABLE03 批次属性3,il.LOTTABLE04 批次属性4,il.LOTTABLE05 批次属性5"
								+",il.LOTTABLE06 批次属性6,il.LOTTABLE07 批次属性7,il.LOTTABLE08 批次属性8,il.LOTTABLE09 批次属性9,il.LOTTABLE10 批次属性10 "
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
						detailTable.setValueAt(cb_from_storer.getSelectedItem().toString(), row, detailTable.getColumnModel().getColumnIndex("货主"));
						detailTable.setValueAt(cb_from_warehouse.getSelectedItem().toString(), row, detailTable.getColumnModel().getColumnIndex("仓库"));
						detailTable.setValueAt(cb_to_storer.getSelectedItem().toString(), row, detailTable.getColumnModel().getColumnIndex("目标货主"));
						detailTable.setValueAt(cb_to_warehouse.getSelectedItem().toString(), row, detailTable.getColumnModel().getColumnIndex("目标仓库"));
						detailTable.setValueAt(dm.getString("商品条码", 0), row, detailTable.getColumnModel().getColumnIndex("商品条码"));
						detailTable.setValueAt(dm.getString("商品编码", 0), row, detailTable.getColumnModel().getColumnIndex("商品编码"));
						detailTable.setValueAt(dm.getString("商品名称", 0), row, detailTable.getColumnModel().getColumnIndex("商品名称"));
						detailTable.setValueAt(dm.getString("库存数量", 0), row, detailTable.getColumnModel().getColumnIndex("原库存数量"));
						detailTable.setValueAt(dm.getString("单位", 0), row, detailTable.getColumnModel().getColumnIndex("单位"));
						detailTable.setValueAt(dm.getString("库位", 0), row, detailTable.getColumnModel().getColumnIndex("原库位"));
						detailTable.setValueAt(dm.getString("箱号", 0), row, detailTable.getColumnModel().getColumnIndex("原箱号"));
						detailTable.setValueAt(dm.getString("批次", 0), row, detailTable.getColumnModel().getColumnIndex("原批次号"));
						detailTable.setValueAt(dm.getString("批次属性1", 0), row, detailTable.getColumnModel().getColumnIndex("原批次属性1"));
						detailTable.setValueAt(dm.getString("批次属性2", 0), row, detailTable.getColumnModel().getColumnIndex("原批次属性2"));
						detailTable.setValueAt(dm.getString("批次属性3", 0), row, detailTable.getColumnModel().getColumnIndex("原批次属性3"));
						detailTable.setValueAt(dm.getString("批次属性4", 0), row, detailTable.getColumnModel().getColumnIndex("原批次属性4"));
						detailTable.setValueAt(dm.getString("批次属性5", 0), row, detailTable.getColumnModel().getColumnIndex("原批次属性5"));
						detailTable.setValueAt(dm.getString("批次属性6", 0), row, detailTable.getColumnModel().getColumnIndex("原批次属性6"));
						detailTable.setValueAt(dm.getString("批次属性7", 0), row, detailTable.getColumnModel().getColumnIndex("原批次属性7"));
						detailTable.setValueAt(dm.getString("批次属性8", 0), row, detailTable.getColumnModel().getColumnIndex("原批次属性8"));
						detailTable.setValueAt(dm.getString("批次属性9", 0), row, detailTable.getColumnModel().getColumnIndex("原批次属性9"));
						detailTable.setValueAt(dm.getString("批次属性10", 0), row, detailTable.getColumnModel().getColumnIndex("原批次属性10"));
						//默认目标批次属性和原批次属性一致
						detailTable.setValueAt(dm.getString("批次属性1", 0), row, detailTable.getColumnModel().getColumnIndex("目标批次属性1"));
						detailTable.setValueAt(dm.getString("批次属性2", 0), row, detailTable.getColumnModel().getColumnIndex("目标批次属性2"));
						detailTable.setValueAt(dm.getString("批次属性3", 0), row, detailTable.getColumnModel().getColumnIndex("目标批次属性3"));
						detailTable.setValueAt(dm.getString("批次属性4", 0), row, detailTable.getColumnModel().getColumnIndex("目标批次属性4"));
						detailTable.setValueAt(dm.getString("批次属性5", 0), row, detailTable.getColumnModel().getColumnIndex("目标批次属性5"));
						detailTable.setValueAt(dm.getString("批次属性6", 0), row, detailTable.getColumnModel().getColumnIndex("目标批次属性6"));
						detailTable.setValueAt(dm.getString("批次属性7", 0), row, detailTable.getColumnModel().getColumnIndex("目标批次属性7"));
						detailTable.setValueAt(dm.getString("批次属性8", 0), row, detailTable.getColumnModel().getColumnIndex("目标批次属性8"));
						detailTable.setValueAt(dm.getString("批次属性9", 0), row, detailTable.getColumnModel().getColumnIndex("目标批次属性9"));
						detailTable.setValueAt(dm.getString("批次属性10", 0), row, detailTable.getColumnModel().getColumnIndex("目标批次属性10"));
						detailTable.setValueAt(dm.getString("库存ID", 0), row, detailTable.getColumnModel().getColumnIndex("原库存ID"));
						//如果原仓库和原货主 与 目标仓库 目标货主一致，说明这个时候用户只是想更改批次属性，
						if(cb_from_warehouse.getSelectedOID().equals(cb_to_warehouse.getSelectedOID()) 
								&& cb_from_storer.getSelectedOID().equals(cb_to_storer.getSelectedOID())){
							detailTable.setValueAt(dm.getString("库位", 0), row, detailTable.getColumnModel().getColumnIndex("目标库位"));
							detailTable.setValueAt(dm.getString("箱号", 0), row, detailTable.getColumnModel().getColumnIndex("目标箱号"));
						}
						
						
						JTableUtil.fitTableColumns(detailTable);
//						detailTable.editCellAt(detailTable.getSelectedRow(), detailTable.getColumnModel().getColumnIndex("目标库位"));
					}else if(column==detailTable.getColumnModel().getColumnIndex("目标库位")){
						String sql = "select bl.location_code 库位 from bas_location bl "
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
						detailTable.setValueAt(dm.getString("库位", 0), row, detailTable.getColumnModel().getColumnIndex("目标库位"));
						JTableUtil.fitTableColumns(detailTable);
//						detailTable.editCellAt(detailTable.getSelectedRow(), detailTable.getColumnModel().getColumnIndex("目标箱号"));
					}else if(column==detailTable.getColumnModel().getColumnIndex("目标箱号")){
						String sql = "select '*' 箱号  union all select bc.container_code 箱号 from bas_container bc "
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
						detailTable.setValueAt(dm.getObject("箱号", 0), row, detailTable.getColumnModel().getColumnIndex("目标箱号"));
						JTableUtil.fitTableColumns(detailTable);
//						detailTable.editCellAt(detailTable.getSelectedRow(), detailTable.getColumnModel().getColumnIndex("目标数量"));
					}
				}
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					JPopupMenu popupmenu = new JPopupMenu();
					JMenuItem menuItem1 = new JMenuItem();
					menuItem1.setLabel("新增一行");
					menuItem1.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(ActionEvent e) {
							// 增加一行空行
							Object[] addRowValues = new Object[detailTable.getColumnCount()];
							addRowValues[detailTable.getColumnModel().getColumnIndex("目标数量")]="0";
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
//		String sql = "select bs.STORER_NAME 货主,bw.WAREHOUSE_NAME 仓库,bi.ITEM_BAR_CODE 商品条码,bi.ITEM_CODE 商品编码,bi.ITEM_NAME 商品名称,"
//				+ "ii.ON_HAND_QTY+ii.IN_TRANSIT_QTY-(ii.ALLOCATED_QTY)-(ii.PICKED_QTY) 原库存数量,biu.UNIT_NAME 单位,ii.LOCATION_CODE 原库位,ii.CONTAINER_CODE 原箱号,ii.LOT_NO 原批次号"
//				+ ",il.LOTTABLE01 原批次属性1,il.LOTTABLE02 原批次属性2,il.LOTTABLE03 原批次属性3,il.LOTTABLE04 原批次属性4,il.LOTTABLE05 原批次属性5,il.LOTTABLE06 原批次属性6,il.LOTTABLE07 原批次属性7,il.LOTTABLE08 原批次属性8,il.LOTTABLE09 原批次属性9,il.LOTTABLE10 原批次属性10"
//				+ ",'' 目标货主,'' 目标仓库,'' 目标库位,'' 目标箱号,'' 目标数量"
//				+ ",'' 目标批次属性1,'' 目标批次属性2,'' 目标批次属性3,'' 目标批次属性4,'' 目标批次属性5,'' 目标批次属性6,'' 目标批次属性7,'' 目标批次属性8,'' 目标批次属性9,'' 目标批次属性10,ii.INV_INVENTORY_ID 原库存ID "
//				+ "from inv_inventory ii " + "inner join inv_lot il on ii.LOT_NO=il.LOT_NO "
//				+ "inner join bas_storer bs on ii.STORER_CODE=bs.STORER_CODE "
//				+ "inner join bas_warehouse bw on ii.WAREHOUSE_CODE=bw.WAREHOUSE_CODE "
//				+ "inner join bas_item bi on ii.STORER_CODE=bi.STORER_CODE and ii.ITEM_CODE=bi.ITEM_CODE "
//				+ "inner join bas_item_unit biu on bi.UNIT_CODE=biu.unit_code " + "where 1=1 ";
		String sql = "select case itd.STATUS when '100' then '新建' when '900' then '完成' else itd.STATUS end 状态,"
				+ "bs.STORER_NAME 货主,bw.WAREHOUSE_NAME 仓库,bi.ITEM_BAR_CODE 商品条码,bi.ITEM_CODE 商品编码,bi.ITEM_NAME 商品名称, "
        		+"itd.FROM_QTY 原库存数量,itd.FROM_UOM 单位,itd.FROM_LOCATION_CODE 原库位,"
        		+"itd.FROM_CONTAINER_CODE 原箱号,itd.FROM_LOT_NO 原批次号,itd.LOTTABLE01 原批次属性1,itd.LOTTABLE02 原批次属性2,itd.LOTTABLE03 原批次属性3,itd.LOTTABLE04 原批次属性4,"
        		+"itd.LOTTABLE05 原批次属性5,itd.LOTTABLE06 原批次属性6,itd.LOTTABLE07 原批次属性7,itd.LOTTABLE08 原批次属性8,itd.LOTTABLE09 原批次属性9,itd.LOTTABLE10 原批次属性10,"
        		+"bs2.STORER_NAME 目标货主,bw2.WAREHOUSE_NAME 目标仓库,itd.TO_LOCATION_CODE 目标库位,itd.TO_CONTAINER_CODE 目标箱号,itd.REQ_QTY 目标数量,"
        		+"itd.TO_LOTTABLE01 目标批次属性1,itd.TO_LOTTABLE02 目标批次属性2,itd.TO_LOTTABLE03 目标批次属性3,itd.TO_LOTTABLE04 目标批次属性4,itd.TO_LOTTABLE05 目标批次属性5,"
        		+"itd.TO_LOTTABLE06 目标批次属性6,itd.TO_LOTTABLE07 目标批次属性7,itd.TO_LOTTABLE08 目标批次属性8,itd.TO_LOTTABLE09 目标批次属性9,itd.TO_LOTTABLE10 目标批次属性10,"
        		+"itd.INV_INVENTORY_ID 原库存ID,itd.TO_INV_INVENTORY_ID 目标库存ID "
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
			if (column == detailTable.getColumnModel().getColumnIndex("目标数量")) {
				String fromQty = object2String(detailTable.getValueAt(selectRow, detailTable.getColumnModel().getColumnIndex("原库存数量")));
				String toQty = object2String(detailTable.getValueAt(selectRow, detailTable.getColumnModel().getColumnIndex("目标数量")));
				if (detailTable.getValueAt(selectRow, column) != null
						&& !detailTable.getValueAt(selectRow, column).equals("")) {
					if(Math_SAM.str2Double(fromQty)<Math_SAM.str2Double(toQty)){
						Message.showWarningMessage("输入的库存数量不能大于原库存数量【"+fromQty+"】");
						detailTable.setValueAt("0", selectRow, column);
					}
				}
			}else if (column == detailTable.getColumnModel().getColumnIndex("目标库位")) {
				String value = object2String(detailTable.getValueAt(selectRow, column));
				if(!value.equals("")){
					String sql = "select location_code from bas_location where warehouse_code='"+cb_to_warehouse.getSelectedOID()+"' and location_code='"+value+"'";
					DataManager dm = DBOperator.DoSelect2DM(sql);
					if(dm==null || dm.getCurrentCount()==0){
						Message.showWarningMessage("输入的库位不正确");
						detailTable.setValueAt("", selectRow, column);
					}
				}
			}else if (column == detailTable.getColumnModel().getColumnIndex("目标箱号")) {
				String value = object2String(detailTable.getValueAt(selectRow, column));
				if(!value.equals("") && !value.equals("*")){
					String sql = "select container_code from bas_container where warehouse_code='"+cb_to_warehouse.getSelectedOID()+"' and container_code='"+value+"' ";
					DataManager dm = DBOperator.DoSelect2DM(sql);
					if(dm==null || dm.getCurrentCount()==0){
						Message.showWarningMessage("输入的箱号不正确");
						detailTable.setValueAt("", selectRow, column);
					}
				}
			}
			trigTable = true;
			break;
	    }
	  }
	
	private void getHeaderTableData(String strWhere){
		String sql = "select ith.TRANSFER_NO 单号,ith.WAREHOUSE_CODE 原仓库编码,bw.WAREHOUSE_NAME 原仓库名称,ith.TO_WAREHOUSE_CODE 目标仓库编码,bw2.WAREHOUSE_NAME 目标仓库名称 " 
				+",ith.STORER_CODE 原货主编码,bs.STORER_NAME 原货主名称,ith.TO_STORER_CODE 目标货主编码,bs2.STORER_NAME 目标货主名称  "
				+",case ith.STATUS when '100' then '新建' when '900' then '完成' else ith.STATUS end 状态 "
				+",ith.CREATED_DTM_LOC 创建日期,su.USER_NAME 创建人 "
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
		//表头隐藏其他列
		for(int i=0;i<headerTable.getColumnCount();i++){
			if(i==headerTable.getColumnModel().getColumnIndex("单号")) continue;
			headerTable.setColumnVisible(i, false, 0);
		}
		headerTable.updateUI();
		
		if(headerTable.getRowCount()>0){
			int row = headerTable.getSelectedRow();
			if(row<=0){
				row = headerTable.getRowIndexByColIndexAndColValue(headerTable.getColumnModel().getColumnIndex("单号"), txt_transfer_no.getText().trim());
				headerTable.setRowSelectionInterval(row,row);
			}
		}
		headerTableClick();
	}
	
	private void headerTableClick(){
		int r= headerTable.getSelectedRow();
        if(headerTable.getRowCount()>0){
	        String transferNo = headerTable.getValueAt(r, headerTable.getColumnModel().getColumnIndex("单号")).toString();
	        initDetailTable(" and itd.TRANSFER_NO='"+transferNo+"' ");
	        String sql = "select ith.TRANSFER_NO,case ith.STATUS when '100' then '新建' when '900' then '完成' else ith.STATUS end STATUS,"
	        		+ "ith.WAREHOUSE_CODE,ith.TO_WAREHOUSE_CODE,ith.STORER_CODE,ith.TO_STORER_CODE,ith.REMARK,ith.CREATED_DTM_LOC,su.USER_NAME  "
	        		+"from inv_transfer_header ith "
	        		+"inner join sys_user su on ith.CREATED_BY_USER=su.USER_CODE "
	        		+"where ith.TRANSFER_NO='"+transferNo+"'" ;
	        DataManager dm = DBOperator.DoSelect2DM(sql);
	        if(dm!=null){
	        	txt_status.setText(dm.getString("STATUS", 0));
	        	if(txt_status.getText().trim().equals("完成")){
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
//			String totalQty = (String) tab.getValueAt(i, tab.getColumnModel().getColumnIndex("数量"));
//			String receiveQty = (String) tab.getValueAt(i, tab.getColumnModel().getColumnIndex("已收数量"));
			// if(Math_SAM.str2Double(totalQty)-Math_SAM.str2Double(receiveQty)==0){
			
			Object[] rc1Cell = new Object[3];
			rc1Cell[0] = new Integer(i);
			rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("状态"));
			rc1Cell[2] = Color.RED;
			String lineStatus = object2String(tab.getValueAt(i, tab.getColumnModel().getColumnIndex("状态")));
			if(lineStatus.equals("完成")){
				cellColor.addElement(rc1Cell);
			}
			

			rc1Cell = new Object[3];
			rc1Cell[0] = new Integer(i);
			rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("商品条码"));
			rc1Cell[2] = Color.ORANGE;
			cellColor.addElement(rc1Cell);
			
			rc1Cell = new Object[3];
			rc1Cell[0] = new Integer(i);
			rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("原库存数量"));
			rc1Cell[2] = Color.PINK;
			cellColor.addElement(rc1Cell);
			
			rc1Cell = new Object[3];
			rc1Cell[0] = new Integer(i);
			rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("原库位"));
			rc1Cell[2] = Color.PINK;
			cellColor.addElement(rc1Cell);

			rc1Cell = new Object[3];
			rc1Cell[0] = new Integer(i);
			rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("目标库位"));
			rc1Cell[2] = Color.ORANGE;
			cellColor.addElement(rc1Cell);

			rc1Cell = new Object[3];
			rc1Cell[0] = new Integer(i);
			rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("目标箱号"));
			rc1Cell[2] = Color.ORANGE;
			cellColor.addElement(rc1Cell);

			rc1Cell = new Object[3];
			rc1Cell[0] = new Integer(i);
			rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("目标数量"));
			rc1Cell[2] = Color.ORANGE;
			cellColor.addElement(rc1Cell);

			rc1Cell = new Object[3];
			rc1Cell[0] = new Integer(i);
			rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("目标批次属性1"));
			rc1Cell[2] = Color.ORANGE;
			cellColor.addElement(rc1Cell);

			rc1Cell = new Object[3];
			rc1Cell[0] = new Integer(i);
			rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("目标批次属性2"));
			rc1Cell[2] = Color.ORANGE;
			cellColor.addElement(rc1Cell);

			rc1Cell = new Object[3];
			rc1Cell[0] = new Integer(i);
			rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("目标批次属性3"));
			rc1Cell[2] = Color.ORANGE;
			cellColor.addElement(rc1Cell);

			rc1Cell = new Object[3];
			rc1Cell[0] = new Integer(i);
			rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("目标批次属性4"));
			rc1Cell[2] = Color.ORANGE;
			cellColor.addElement(rc1Cell);

			rc1Cell = new Object[3];
			rc1Cell[0] = new Integer(i);
			rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("目标批次属性5"));
			rc1Cell[2] = Color.ORANGE;
			cellColor.addElement(rc1Cell);

			rc1Cell = new Object[3];
			rc1Cell[0] = new Integer(i);
			rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("目标批次属性6"));
			rc1Cell[2] = Color.ORANGE;
			cellColor.addElement(rc1Cell);

			rc1Cell = new Object[3];
			rc1Cell[0] = new Integer(i);
			rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("目标批次属性7"));
			rc1Cell[2] = Color.ORANGE;
			cellColor.addElement(rc1Cell);

			rc1Cell = new Object[3];
			rc1Cell[0] = new Integer(i);
			rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("目标批次属性8"));
			rc1Cell[2] = Color.ORANGE;
			cellColor.addElement(rc1Cell);

			rc1Cell = new Object[3];
			rc1Cell[0] = new Integer(i);
			rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("目标批次属性9"));
			rc1Cell[2] = Color.ORANGE;
			cellColor.addElement(rc1Cell);

			rc1Cell = new Object[3];
			rc1Cell[0] = new Integer(i);
			rc1Cell[1] = new Integer(tab.getColumnModel().getColumnIndex("目标批次属性10"));
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
				Message.showWarningMessage("明细输入不完整，不能保存");
				return false;
			}
			String transferNo = "";
			if(txt_transfer_no.getText().trim().equals("")){
				transferNo = comData.getValueFromBasNumRule("inv_transfer_header", "transfer_no");
				if(transferNo.equals("")){
					Message.showWarningMessage("生成单号出错，请联系系统管理员");
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
			//保存表头
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
				//保存明细 先删除再插入
				if(detailTable.isEditing()){
					detailTable.getCellEditor().stopCellEditing();
				}
				sql = "delete from inv_transfer_detail where TRANSFER_NO='"+transferNo+"' ";
				DBOperator.DoUpdate(sql);
				for(int i=0;i<detailTable.getRowCount();i++){
					String INV_INVENTORY_ID=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("原库存ID")));
					String TO_LOCATION_CODE=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("目标库位"))).toString();
					String TO_CONTAINER_CODE=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("目标箱号"))).toString();
					String FROM_QTY=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("原库存数量"))).toString();
					String REQ_QTY=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("目标数量"))).toString();
					String REQ_UOM=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("单位"))).toString();
					String LOTTABLE01=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("原批次属性1"))).toString();
					String LOTTABLE02=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("原批次属性2"))).toString();
					String LOTTABLE03=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("原批次属性3"))).toString();
					String LOTTABLE04=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("原批次属性4"))).toString();
					String LOTTABLE05=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("原批次属性5"))).toString();
					String LOTTABLE06=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("原批次属性6"))).toString();
					String LOTTABLE07=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("原批次属性7"))).toString();
					String LOTTABLE08=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("原批次属性8"))).toString();
					String LOTTABLE09=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("原批次属性9"))).toString();
					String LOTTABLE10=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("原批次属性10"))).toString();
					String TO_LOTTABLE01=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("目标批次属性1"))).toString();
					String TO_LOTTABLE02=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("目标批次属性2"))).toString();
					String TO_LOTTABLE03=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("目标批次属性3"))).toString();
					String TO_LOTTABLE04=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("目标批次属性4"))).toString();
					String TO_LOTTABLE05=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("目标批次属性5"))).toString();
					String TO_LOTTABLE06=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("目标批次属性6"))).toString();
					String TO_LOTTABLE07=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("目标批次属性7"))).toString();
					String TO_LOTTABLE08=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("目标批次属性8"))).toString();
					String TO_LOTTABLE09=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("目标批次属性9"))).toString();
					String TO_LOTTABLE10=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("目标批次属性10"))).toString();
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
				Message.showErrorMessage("表头数据保存失败");
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
			lineStatus = object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("状态")));
			if(lineStatus.equals("完成")){
				continue;
			}
			//检查目标货主的商品编码是否存在，不存在需要新增
			itemCode = object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("商品编码")));
			invID = object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("原库存ID")));
			REQ_QTY=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("目标数量"))).toString();
			String sql = "select item_code from bas_item where storer_code='"+to_storerCode+"' and item_code='"+itemCode+"' ";
			DataManager dm = DBOperator.DoSelect2DM(sql);
			if(dm==null || dm.getCurrentCount()==0){
				sql = "INSERT INTO `bas_item` (`BAS_ITEM_ID`, `STORER_CODE`, `WAREHOUSE_CODE`, `BRAND_CODE`, `ITEM_CODE`, `ITEM_NAME`, `DESCRIPTION`, `ITEM_BAR_CODE`, `PORT_CODE`, `PLATFORM_CODE`, `VENDOR_CODE`, `UNIT_CODE`, `ITEM_SPEC`, `HSCODE`, `HSCODE_DESC`, `TAX_NUMBER`, `ITEM_CLASS_CODE`, `COUNTRY_CODE`, `VALIDITY`, `VALIDITY_UOM`, `DIVISION`, `DEPARTMENT`, `COST`, `ITEM_STYLE`, `ITEM_COLOR`, `ITEM_SIZE`, `IS_LOT_CONTROLLED`, `IS_CATCH_WEIGHT_REQD`, `IS_FRANGIBLE`, `IS_VALUABLES`, `IS_CONTAIN_FITTING`, `IS_TRACKING_SERIAL_NUM`, `PROCESS_STAMP`, `LIST_PRICE`, `NET_PRICE`, `PACKING_CLASS`, `WEIGHT`, `VOLUME`, `VOLUME_UOM`, `LENGTH`, `WIDTH`, `HEIGHT`, `DIMENSION_UOM`, `PIC_URL`, `ITEM_CATEGORY1`, `ITEM_CATEGORY2`, `ITEM_CATEGORY3`, `ITEM_CATEGORY4`, `ITEM_CATEGORY5`, `ITEM_CATEGORY6`, `ITEM_CATEGORY7`, `ITEM_CATEGORY8`, `ITEM_CATEGORY9`, `ITEM_CATEGORY10`, `NEED_QC`, `INBOUND_QC_UM`, `ALTERNATE_ITEM`, `WEB_THUMBNAILIMG`, `INBOUND_QC_AMOUNT`, `INBOUND_QC_AMOUNT_TYPE`, `WEB_IMG`, `NEED_PC`, `REMARK`, `TEMPERATURE`, `IS_LARGE`, `TRANSFER_DATE`, `IS_AIRLINE_BAN_PRODUCTS`, `WEIGHT_UOM`, `USER_DEF1`, `USER_DEF2`, `USER_DEF3`, `USER_DEF4`, `USER_DEF5`, `USER_DEF6`, `USER_DEF7`, `USER_DEF8`, `USER_DEF9`, `USER_DEF10`, `CREATED_BY_USER`, `CREATED_OFFICE`, `CREATED_DTM_LOC`, `CREATED_TIME_ZONE`, `UPDATED_BY_USER`, `UPDATED_OFFICE`, `UPDATED_DTM_LOC`, `UPDATED_TIME_ZONE`, `RECORD_VERSION`, `ITEM_CODE_CAPITAL`, `SHELFLIFE`, `INSURANCE_LIFE`, `IS_SHELFLIFE_CONTROL`, `SHELFLIFE_TYPE`, `INB_LIFE`, `OUB_LIFE`, `ONLINE_LIFE`, `VALUE_LEVEL`, `GROSS_WEIGHT`, `NET_WEIGHT`, `TARE_WEIGHT`, `TI`, `HI`, `IS_QC`, `REC_QC_LOC`, `REC_LOC`, `PICKTO_LOC`, `RETURN_LOC`, `TURNOVER_LOC`, `PUTAWAY_ZONE`, `PUTAWAY_LOC`, `PUTAWAY_RULE`, `PUTAWAY_CALCULATE`, `REC_VERIFY`, `ADDINVENTORY_STRATEGY`, `USER_ANNOTATION1`, `USER_ANNOTATION2`, `pack_spec`, `invoice_company_code`, `invoice_item_code`, `oub_invoice_template_id`, `vendor_item`, `brand_code_id`, `IS_SORTABLE`, `PACK_CODE`, `LABEL_LANG`, `COUNTRY_OF_ORIGIN`, `BRAND_NAME`)  "
						+"select null, '"+to_storerCode+"', `WAREHOUSE_CODE`, `BRAND_CODE`, `ITEM_CODE`, `ITEM_NAME`, `DESCRIPTION`, `ITEM_BAR_CODE`, `PORT_CODE`, `PLATFORM_CODE`, `VENDOR_CODE`, `UNIT_CODE`, `ITEM_SPEC`, `HSCODE`, `HSCODE_DESC`, `TAX_NUMBER`, `ITEM_CLASS_CODE`, `COUNTRY_CODE`, `VALIDITY`, `VALIDITY_UOM`, `DIVISION`, `DEPARTMENT`, `COST`, `ITEM_STYLE`, `ITEM_COLOR`, `ITEM_SIZE`, `IS_LOT_CONTROLLED`, `IS_CATCH_WEIGHT_REQD`, `IS_FRANGIBLE`, `IS_VALUABLES`, `IS_CONTAIN_FITTING`, `IS_TRACKING_SERIAL_NUM`, `PROCESS_STAMP`, `LIST_PRICE`, `NET_PRICE`, `PACKING_CLASS`, `WEIGHT`, `VOLUME`, `VOLUME_UOM`, `LENGTH`, `WIDTH`, `HEIGHT`, `DIMENSION_UOM`, `PIC_URL`, `ITEM_CATEGORY1`, `ITEM_CATEGORY2`, `ITEM_CATEGORY3`, `ITEM_CATEGORY4`, `ITEM_CATEGORY5`, `ITEM_CATEGORY6`, `ITEM_CATEGORY7`, `ITEM_CATEGORY8`, `ITEM_CATEGORY9`, `ITEM_CATEGORY10`, `NEED_QC`, `INBOUND_QC_UM`, `ALTERNATE_ITEM`, `WEB_THUMBNAILIMG`, `INBOUND_QC_AMOUNT`, `INBOUND_QC_AMOUNT_TYPE`, `WEB_IMG`, `NEED_PC`, `REMARK`, `TEMPERATURE`, `IS_LARGE`, `TRANSFER_DATE`, `IS_AIRLINE_BAN_PRODUCTS`, `WEIGHT_UOM`, `USER_DEF1`, `USER_DEF2`, `USER_DEF3`, `USER_DEF4`, `USER_DEF5`, `USER_DEF6`, `USER_DEF7`, `USER_DEF8`, `USER_DEF9`, `USER_DEF10`, `CREATED_BY_USER`, `CREATED_OFFICE`, `CREATED_DTM_LOC`, `CREATED_TIME_ZONE`, `UPDATED_BY_USER`, `UPDATED_OFFICE`, `UPDATED_DTM_LOC`, `UPDATED_TIME_ZONE`, `RECORD_VERSION`, `ITEM_CODE_CAPITAL`, `SHELFLIFE`, `INSURANCE_LIFE`, `IS_SHELFLIFE_CONTROL`, `SHELFLIFE_TYPE`, `INB_LIFE`, `OUB_LIFE`, `ONLINE_LIFE`, `VALUE_LEVEL`, `GROSS_WEIGHT`, `NET_WEIGHT`, `TARE_WEIGHT`, `TI`, `HI`, `IS_QC`, `REC_QC_LOC`, `REC_LOC`, `PICKTO_LOC`, `RETURN_LOC`, `TURNOVER_LOC`, `PUTAWAY_ZONE`, `PUTAWAY_LOC`, `PUTAWAY_RULE`, `PUTAWAY_CALCULATE`, `REC_VERIFY`, `ADDINVENTORY_STRATEGY`, `USER_ANNOTATION1`, `USER_ANNOTATION2`, `pack_spec`, `invoice_company_code`, `invoice_item_code`, `oub_invoice_template_id`, `vendor_item`, `brand_code_id`, `IS_SORTABLE`, `PACK_CODE`, `LABEL_LANG`, `COUNTRY_OF_ORIGIN`, `BRAND_NAME`"
						+" from bas_item bi "
						+"where bi.storer_code='"+storerCode+"' and bi.item_code='"+itemCode+"'";
				int updateCount = DBOperator.DoUpdate(sql);
				if(updateCount==0){
					Message.showErrorMessage("商品信息移入到目标货主失败！\n"+"目标货主："+to_storerCode+"\n商品编码："+itemCode);
					return false;
				}
			}
			sql = "select ON_HAND_QTY+IN_TRANSIT_QTY-(ALLOCATED_QTY)-(PICKED_QTY) availabledQty from inv_inventory where INV_INVENTORY_ID='"+invID+"' ";
			dm = DBOperator.DoSelect2DM(sql);
			if(dm==null || dm.getCurrentCount()==0){
				Message.showErrorMessage("原库存信息不存在！\n"+"货主："+storerName+"\n商品编码："+itemCode);
				return false;
			}else{
				if(Math_SAM.str2Double(REQ_QTY)>Math_SAM.str2Double(dm.getString("availabledQty", 0))){
					Message.showErrorMessage("原库存数量小于变更单需求数量！\n"+"货主："+storerName+"\n商品编码："+itemCode);
					return false;
				}
			}
		}
		//库存属性转移
		for(int i=0;i<detailTable.getRowCount();i++){
			lineStatus = object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("状态")));
			if(lineStatus.equals("完成")){
				continue;
			}
			itemCode = object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("商品编码")));
			String INV_INVENTORY_ID=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("原库存ID")));
			String FROM_LOCATION_CODE=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("原库位"))).toString();
			String TO_LOCATION_CODE=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("目标库位"))).toString();
			String FROM_CONTAINER_CODE=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("原箱号"))).toString();
			String TO_CONTAINER_CODE=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("目标箱号"))).toString();
			REQ_QTY=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("目标数量"))).toString();
			String TO_LOTTABLE01=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("目标批次属性1"))).toString();
			String TO_LOTTABLE02=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("目标批次属性2"))).toString();
			String TO_LOTTABLE03=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("目标批次属性3"))).toString();
			String TO_LOTTABLE04=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("目标批次属性4"))).toString();
			String TO_LOTTABLE05=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("目标批次属性5"))).toString();
			String TO_LOTTABLE06=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("目标批次属性6"))).toString();
			String TO_LOTTABLE07=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("目标批次属性7"))).toString();
			String TO_LOTTABLE08=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("目标批次属性8"))).toString();
			String TO_LOTTABLE09=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("目标批次属性9"))).toString();
			String TO_LOTTABLE10=object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("目标批次属性10"))).toString();
			//先生成库存批次号
			String lotNo = comData.getInventoryLotNo(to_storerCode,itemCode,TO_LOTTABLE01,TO_LOTTABLE02,TO_LOTTABLE03,TO_LOTTABLE04,TO_LOTTABLE05,TO_LOTTABLE06,TO_LOTTABLE07,TO_LOTTABLE08,TO_LOTTABLE09,TO_LOTTABLE10);
			if(!lotNo.equals("")){
				//插入库存表
				String inventoryID = comData.getInventoryID(to_warrehouseCode,to_storerCode,itemCode,lotNo,TO_LOCATION_CODE,TO_CONTAINER_CODE,REQ_QTY,"sys");
				if(!inventoryID.equals("")){
					//库存表写入成功
					//扣减原库存
					String sql = "update inv_inventory ii set ii.ON_HAND_QTY=ii.ON_HAND_QTY - ("+REQ_QTY+") "
							+"where ii.INV_INVENTORY_ID='"+INV_INVENTORY_ID+"' "
							+"";
					int t = DBOperator.DoUpdate(sql);
					if(t==0){
						Message.showErrorMessage("扣减原库存失败！\n"+"货主："+storerName+"\n商品编码："+itemCode);
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
		//更新表头状态 status=900
		String sql = "update inv_transfer_header set STATUS='900',"
				+ "UPDATED_BY_USER='"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"',UPDATED_DTM_LOC=now() "
				+ "where TRANSFER_NO='"+transferNo+"' ";
		int t = DBOperator.DoUpdate(sql);
		Message.showInfomationMessage("操作成功");
		getHeaderTableData(" and (ith.status='100' or TRANSFER_NO='"+txt_transfer_no.getText().trim()+"') ");
		int row = headerTable.getRowIndexByColIndexAndColValue(headerTable.getColumnModel().getColumnIndex("单号"), txt_transfer_no.getText().trim());
		headerTable.setRowSelectionInterval(row,row);
		return true;
	}
	
	public boolean checkDetailTable(){
		for(int i=0;i<detailTable.getRowCount();i++){
			String itemBarcode = object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("商品条码")));
			String toLocation = object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("目标库位")));
			String toContainer = object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("目标箱号")));
			String toQty = object2String(detailTable.getValueAt(i, detailTable.getColumnModel().getColumnIndex("目标数量")));
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
//		detailTable.setColumnEditable(true, detailTable.getColumnModel().getColumnIndex("商品条码"));
		detailTable.setColumnEditable(bool, detailTable.getColumnModel().getColumnIndex("目标库位"));
		detailTable.setColumnEditable(bool, detailTable.getColumnModel().getColumnIndex("目标箱号"));
		detailTable.setColumnEditable(bool, detailTable.getColumnModel().getColumnIndex("目标数量"));
		detailTable.setColumnEditable(bool, detailTable.getColumnModel().getColumnIndex("目标批次属性1"));
		detailTable.setColumnEditable(bool, detailTable.getColumnModel().getColumnIndex("目标批次属性2"));
		detailTable.setColumnEditable(bool, detailTable.getColumnModel().getColumnIndex("目标批次属性3"));
		detailTable.setColumnEditable(bool, detailTable.getColumnModel().getColumnIndex("目标批次属性4"));
		detailTable.setColumnEditable(bool, detailTable.getColumnModel().getColumnIndex("目标批次属性5"));
		detailTable.setColumnEditable(bool, detailTable.getColumnModel().getColumnIndex("目标批次属性6"));
		detailTable.setColumnEditable(bool, detailTable.getColumnModel().getColumnIndex("目标批次属性7"));
		detailTable.setColumnEditable(bool, detailTable.getColumnModel().getColumnIndex("目标批次属性8"));
		detailTable.setColumnEditable(bool, detailTable.getColumnModel().getColumnIndex("目标批次属性9"));
		detailTable.setColumnEditable(bool, detailTable.getColumnModel().getColumnIndex("目标批次属性10"));
		detailTable.editCellAt(detailTable.getRowCount() - 1, detailTable.getColumnModel().getColumnIndex("商品条码"));
	}
	
	public String object2String(Object o){
		return o==null?"":o.toString();
	}

	private void setExtendedState(int maximizedBoth) {
		// TODO Auto-generated method stub

	}

}
