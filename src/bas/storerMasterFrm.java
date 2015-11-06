package bas;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;

import DBUtil.DBConnectionManager;
import DBUtil.DBOperator;
import dmdata.DataManager;
import inbound.poImportFrm;
import sys.InnerFrame;
import sys.JTableUtil;
import sys.Message;
import sys.QueryDialog;
import sys.QueryFrm;
import sys.testFrm;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.DefaultListSelectionModel;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.JScrollPane;

public class storerMasterFrm extends InnerFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8176509107719881599L;
	private JPanel contentPane;
	private static volatile storerMasterFrm instance;
	private static boolean isOpen = false;
	private JTextField storer_code_txt;
	private JTextField storer_name_txt;
	private JTable storer_table;
	private JButton btnAdd;
	private JButton btnModify;
	private JButton btnDelete;
	private JButton btnQuery;
	private JButton btnSave;
	private JButton btnCancel;
	private JButton btnClose;
	static JCheckBox storer_is_active;
	
	public static storerMasterFrm getInstance() {
		if(instance == null) { 
			 synchronized(storerMasterFrm.class){
				 if(instance == null) {
					 instance = new storerMasterFrm();
				 }
			 }
	        }  
	        return instance;
		 
	 }
	
	public static synchronized boolean getOpenStatus() {
		 if(instance == null) {    
	            instance = new storerMasterFrm();  
	        }  
	        return isOpen;
		 
	 }

	/**
	 * Create the frame.
	 */
	public storerMasterFrm() {
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//		BasicInternalFrameUI ui = (BasicInternalFrameUI)this.getUI(); //去除标题栏
//		ui.getNorthPane().setVisible(false); 
		// 向侦听器列表添加一个 VetoableChangeListener。为所有属性注册该侦听器
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.addVetoableChangeListener(new VetoableChangeListener() {

			public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException {
				JInternalFrame frame = (JInternalFrame) e.getSource();
				String name = e.getPropertyName();
				Object value = e.getNewValue();
				if (name.equals("closed") && value.equals(Boolean.TRUE)) // 窗口被关闭
				{
					System.out.println("storerMasterFrm窗口被关闭");
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
		
		setBounds(100, 100, 564, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		maximizable = true;
		closable = true;
		setContentPane(contentPane);
		
		JPanel topPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) topPanel.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		flowLayout.setAlignment(FlowLayout.LEFT);
		flowLayout.setAlignOnBaseline(true);
		contentPane.add(topPanel, BorderLayout.NORTH);
		
		btnAdd = new JButton("\u589E\u52A0");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				storer_code_txt.setText("");
				storer_name_txt.setText("");
				storer_is_active.setSelected(true);
				storer_code_txt.setEditable(true);
				storer_name_txt.setEditable(true);
				btnAdd.setEnabled(false);
				btnModify.setEnabled(false);
				btnDelete.setEnabled(false);
				btnQuery.setEnabled(false);
				btnSave.setEnabled(true);
				btnCancel.setEnabled(true);
				storer_is_active.setEnabled(true);
				storer_table.setEnabled(false);
			}
		});
		topPanel.add(btnAdd);
		
		btnModify = new JButton("\u4FEE\u6539");
		btnModify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(storer_code_txt.getText().length()<=0){
					JOptionPane.showMessageDialog(null, "请选择一行数据！");
				}else{
					storer_code_txt.setEditable(false);
					storer_name_txt.setEditable(true);
					storer_is_active.setEnabled(true);
					btnAdd.setEnabled(false);
					btnModify.setEnabled(false);
					btnDelete.setEnabled(false);
					btnQuery.setEnabled(false);
					btnSave.setEnabled(true);
					btnCancel.setEnabled(true);
					
					storer_table.setEnabled(false);
				}
			}
		});
		topPanel.add(btnModify);
		
		btnDelete = new JButton("\u5220\u9664");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(storer_code_txt.getText().length()<=0){
					JOptionPane.showMessageDialog(null, "请选择一行数据！");
				}else{
					int res = JOptionPane.showConfirmDialog(null, "是否删除【"+storer_code_txt.getText()+"】这行记录？");
					if (res == JOptionPane.YES_OPTION)
						System.out.println("Result=Yes");
					else if (res == JOptionPane.NO_OPTION){
						return;
					}
					else if (res == JOptionPane.CANCEL_OPTION){
						return;
					}
					else if (res == JOptionPane.CLOSED_OPTION){
						return;
					}
					else{
						return;
					}
					try{
						String sql_delete = "select bs.STORER_CODE from bas_storer bs "
								+"where (exists(select ii.STORER_CODE from inv_inventory ii where ii.STORER_CODE=bs.STORER_CODE) "
								+"or exists(select osh.STORER_CODE from oub_shipment_header osh where osh.STORER_CODE=bs.STORER_CODE and osh.status<'900') "
								+"or exists(select * from inb_receipt_header irh where irh.STORER_CODE=bs.STORER_CODE and irh.status<'900') "
								+" ) and bs.storer_code='"+storer_code_txt.getText().trim()+"' ";
						DataManager dm = DBOperator.DoSelect2DM(sql_delete);
						if(dm==null || dm.getCurrentCount()==0){
							
						}else{
							Message.showWarningMessage("此货主信息已经在系统使用，不能删除");
							return;
						}
						
						String sql = "delete from bas_storer where storer_code='"+storer_code_txt.getText().trim()+"'";
						java.sql.Connection con = DBConnectionManager.getInstance().getConnection("wms");
						java.sql.Statement stmt = con.createStatement();
						int rst = stmt.executeUpdate(sql);
						if(rst==1){
							JOptionPane.showMessageDialog(null, "删除【"+storer_code_txt.getText()+"】成功！");
							initFormData("");
						}
						stmt.close();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		topPanel.add(btnDelete);
		
		btnQuery = new JButton("\u67E5\u8BE2");
		btnQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ArrayList fieldList = new ArrayList();
				fieldList.add("storer_code:货主编码");
				fieldList.add("storer_name:货主名称");
				fieldList.add("is_active:是否启用");
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
				initFormData(retWhere);
				
//				
//				QueryFrm query = QueryFrm.getInstance(fieldList);
////				query.setLocationRelativeTo(null);
//				query.setSize(850,400);
//				Toolkit toolkit = Toolkit.getDefaultToolkit();
//				int x = (int)(toolkit.getScreenSize().getWidth()-query.getWidth())/2;
//				int y = (int)(toolkit.getScreenSize().getHeight()-query.getHeight())/2;
//				query.setLocation(x, y);
//				query.setVisible(true);
//				query.setAlwaysOnTop(true);
			}
		});
		topPanel.add(btnQuery);
		
//		Component verticalStrut = Box.createVerticalStrut(20);
//		verticalStrut.setBackground(Color.BLUE);
//		topPanel.add(verticalStrut);
		
		btnSave = new JButton("\u4FDD\u5B58");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(storer_code_txt.getText().length()<=0 || storer_name_txt.getText().length()<=0){
					JOptionPane.showMessageDialog(null, "输入不正确！");
				}else{
					try {
						String is_active = storer_is_active.isSelected()?"1":"0";
						java.sql.Connection con = DBConnectionManager.getInstance().getConnection("wms");
						java.sql.Statement stmt = con.createStatement();
						String sql = "select storer_code from bas_storer where storer_code='"+storer_code_txt.getText().trim()+"'";
						ResultSet rs = stmt.executeQuery(sql);
						if (rs.next()) {
							sql = "update bas_storer set storer_name='" + storer_name_txt.getText().trim()
									+ "',UPDATED_DTM_LOC=now(),UPDATED_BY_USER='sys' " + "where storer_code='"
									+ storer_code_txt.getText().trim() + "'";
							int rst = stmt.executeUpdate(sql);
							if (rst == 1) {
								JOptionPane.showMessageDialog(null, "更新【" + storer_code_txt.getText() + "】成功！");
								initFormData("");
								storer_code_txt.setText("");
								storer_name_txt.setText("");
								storer_is_active.setSelected(false);
								storer_code_txt.setEditable(false);
								storer_name_txt.setEditable(false);
								btnAdd.setEnabled(true);
								btnModify.setEnabled(true);
								btnDelete.setEnabled(true);
								btnQuery.setEnabled(true);
								btnSave.setEnabled(false);
								btnCancel.setEnabled(false);
								storer_is_active.setEnabled(false);
								storer_table.setEnabled(true);
							}
						} else {
							sql = "insert into bas_storer(STORER_CODE,STORER_SHORT_NAME,STORER_NAME,IS_ACTIVE,CREATED_DTM_LOC,CREATED_BY_USER,UPDATED_DTM_LOC,UPDATED_BY_USER) "
									+ "select '" + storer_code_txt.getText().trim() + "','"
									+ storer_name_txt.getText().trim() + "'," + "'" + storer_name_txt.getText().trim() + "',"+is_active+",now(),'sys',now(),'sys' ";
							int rst = stmt.executeUpdate(sql);
							if (rst == 1) {
								JOptionPane.showMessageDialog(null, "新增【" + storer_code_txt.getText() + "】成功！");
								initFormData("");
								storer_code_txt.setText("");
								storer_name_txt.setText("");
								storer_is_active.setSelected(false);
								storer_code_txt.setEditable(false);
								storer_name_txt.setEditable(false);
								btnAdd.setEnabled(true);
								btnModify.setEnabled(true);
								btnDelete.setEnabled(true);
								btnQuery.setEnabled(true);
								btnSave.setEnabled(false);
								btnCancel.setEnabled(false);
								storer_is_active.setEnabled(false);
								storer_table.setEnabled(true);
							}
						}
						stmt.close();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		topPanel.add(btnSave);
		
		btnCancel = new JButton("\u53D6\u6D88");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				storer_code_txt.setText("");
				storer_name_txt.setText("");
				storer_is_active.setSelected(false);
				storer_code_txt.setEditable(false);
				storer_name_txt.setEditable(false);
				btnAdd.setEnabled(true);
				btnModify.setEnabled(true);
				btnDelete.setEnabled(true);
				btnQuery.setEnabled(true);
				btnSave.setEnabled(false);
				btnCancel.setEnabled(false);
				storer_is_active.setEnabled(false);
				storer_table.setEnabled(true);
			}
		});
		topPanel.add(btnCancel);
		
		btnClose = new JButton("\u5173\u95ED");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					instance.setClosed(true);
				} catch (PropertyVetoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		verticalStrut_1.setBackground(Color.BLUE);
		topPanel.add(verticalStrut_1);
		topPanel.add(btnClose);
		
		JPanel centerPanel = new JPanel();
		contentPane.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel editPanel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) editPanel.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		flowLayout_1.setAlignOnBaseline(true);
		centerPanel.add(editPanel, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("\u8D27\u4E3B\u7F16\u7801\uFF1A");
		editPanel.add(lblNewLabel);
		
		storer_code_txt = new JTextField();
		editPanel.add(storer_code_txt);
		storer_code_txt.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("\u8D27\u4E3B\u540D\u79F0\uFF1A");
		editPanel.add(lblNewLabel_1);
		
		storer_name_txt = new JTextField();
		editPanel.add(storer_name_txt);
		storer_name_txt.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("\u662F\u5426\u542F\u7528\uFF1A");
		editPanel.add(lblNewLabel_2);
		
		storer_is_active = new JCheckBox("");
		editPanel.add(storer_is_active);
		
		JPanel tablePanel = new JPanel();
		centerPanel.add(tablePanel, BorderLayout.SOUTH);
		
		JScrollPane scrollPane = new JScrollPane();
		centerPanel.add(scrollPane, BorderLayout.CENTER);
		
		storer_table = new JTable(new DefaultTableModel()){
			private static final long serialVersionUID = 1L;
			//Table不能编辑，只能选中
			public boolean isCellEditable(int row, int column) {
                return false;
            }
		};
		scrollPane.setViewportView(storer_table);
		//storer_table
        DefaultTableModel dtm = (DefaultTableModel) storer_table.getModel();
        dtm.addColumn("序号");
        dtm.addColumn("货主编码");
        dtm.addColumn("货主名称");
        dtm.addColumn("是否启用");
        //单选
        DefaultListSelectionModel tableSelectionModel8 = (DefaultListSelectionModel) storer_table.getSelectionModel();
        tableSelectionModel8.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        storer_table.setRowHeight(0, 20);//20
        //横向滚动条显示
//        storer_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JTableUtil.fitTableColumns(storer_table);
        storer_table.getTableHeader().setReorderingAllowed(false);
        initComponents();//初始化控件
        initFormData("");//初始化界面数据
        
	}

	private void setExtendedState(int maximizedBoth) {
		// TODO Auto-generated method stub
		
	}
	
	private void initComponents(){
		btnSave.setEnabled(false);
		btnCancel.setEnabled(false);
		storer_is_active.setEnabled(false);
		storer_code_txt.setEditable(false);
		storer_name_txt.setEditable(false);
		storer_table.addMouseListener(new java.awt.event.MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()>=2){
					storer_table.setColumnSelectionAllowed(true);
				}
	               int r= storer_table.getSelectedRow();
	               int c= storer_table.getSelectedColumn();
	               String storer_code = storer_table.getValueAt(r, 1).toString();
	               String storer_name = storer_table.getValueAt(r, 2).toString();
	               String storer_is_active_txt = storer_table.getValueAt(r, 3).toString();
	               storer_code_txt.setText(storer_code);
	               storer_name_txt.setText(storer_name);
	               storer_is_active.setSelected(storer_is_active_txt.equals("1")?true:false);
			}
		});
	}
	private void initFormData(String strWhere){
		DefaultTableModel dtm = (DefaultTableModel) storer_table.getModel();
        dtm.getDataVector().removeAllElements();
        dtm.setRowCount(0);
		String sql = "select STORER_CODE,STORER_NAME,IS_ACTIVE from bas_storer where 1=1 ";
		if(!strWhere.equals("")){
			sql = sql + strWhere;
		}
		System.out.println(sql);
		try{
			java.sql.Connection con = DBConnectionManager.getInstance().getConnection("wms");
			java.sql.Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			int seq = 0;
			while (rs.next()) {
				seq++;
				Vector rowdata = new Vector();
				rowdata.add(String.valueOf(seq));
				rowdata.add(rs.getString("STORER_CODE"));
				rowdata.add(rs.getString("STORER_NAME"));
				rowdata.add(rs.getString("IS_ACTIVE"));
				dtm.addRow(rowdata);
			}
			DBConnectionManager.getInstance().freeConnection("wms", con);
			JTableUtil.fitTableColumns(storer_table);
			storer_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
