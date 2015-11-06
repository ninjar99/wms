package bas;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;

import DBUtil.DBOperator;
import comUtil.StringUtil;
import dmdata.DataManager;
import main.PBSUIBaseGrid;
import sys.InnerFrame;
import sys.JTableUtil;
import sys.Message;
import sys.QueryDialog;
import sys.ToolBarItem;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class BasBrandFrm extends InnerFrame{
	private JPanel contentPane;
	private static volatile BasBrandFrm instance;
	private static boolean isOpen = false;
	private JButton btnAdd;
	private JButton btnModify;
	private JButton btnDelete;
	private JButton btnQuery;
	private JButton btnSave;
	private JButton btnCancel;
	private JButton btnClose;
	private JPanel centerPanel;
	private JPanel editPanel;
	private PBSUIBaseGrid table_brand;
	
	int lastStatus;
	private JTextField textField_brand_code;
	private JLabel lblNewLabel_1;
	private JTextField textField_brand_name;
	
	public BasBrandFrm() {
		BasicInternalFrameUI ui = (BasicInternalFrameUI)this.getUI(); //去除标题栏
		ui.getNorthPane().setVisible(false); 
		this.addVetoableChangeListener(new VetoableChangeListener() {
			public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException {
				String name = e.getPropertyName();
				Object value = e.getNewValue();
				if (name.equals("closed") && value.equals(Boolean.TRUE)) // 窗口被关闭
				{
					System.out.println("UserFrm窗口被关闭");
					instance = null;
					isOpen = false;
				}
			}
		});
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				isOpen = true;
			}
		});
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 796, 447);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel topPanel = new JPanel();
		contentPane.add(topPanel, BorderLayout.NORTH);
		topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		
		btnAdd = new JButton("\u65B0\u589E");
		btnAdd.addActionListener(addListener);
		topPanel.add(btnAdd);
		
		btnModify = new JButton("\u4FEE\u6539");
		btnModify.addActionListener(modifyListener);
		topPanel.add(btnModify);
		
		btnDelete = new JButton("\u5220\u9664");
		btnDelete.addActionListener(deleteListener);
		topPanel.add(btnDelete);
		
		btnQuery = new JButton("\u67E5\u8BE2");
		btnQuery.addActionListener(queryListener);
		topPanel.add(btnQuery);
		
		btnSave = new JButton("\u4FDD\u5B58");
		btnSave.addActionListener(saveListener);
		topPanel.add(btnSave);
		
		btnCancel = new JButton("\u53D6\u6D88");
		btnCancel.addActionListener(cancelListener);
		topPanel.add(btnCancel);
		
		btnClose = new JButton("\u5173\u95ED");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					instance.setClosed(true);
				} catch (PropertyVetoException e) {
					e.printStackTrace();
				}
			}
		});
		topPanel.add(btnClose);
		
		centerPanel = new JPanel();
		contentPane.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new BorderLayout(0, 0));
		
		editPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) editPanel.getLayout();
		flowLayout.setAlignOnBaseline(true);
		flowLayout.setAlignment(FlowLayout.LEFT);
		flowLayout.setVgap(10);
		centerPanel.add(editPanel, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("*\u54C1\u724C\u7F16\u7801:");
		editPanel.add(lblNewLabel);
		
		textField_brand_code = new JTextField();
		editPanel.add(textField_brand_code);
		textField_brand_code.setColumns(10);
		
		lblNewLabel_1 = new JLabel("*\u54C1\u724C\u540D\u79F0:");
		editPanel.add(lblNewLabel_1);
		
		textField_brand_name = new JTextField();
		editPanel.add(textField_brand_name);
		textField_brand_name.setColumns(25);
		
		JScrollPane scrollPane = new JScrollPane();
		centerPanel.add(scrollPane, BorderLayout.CENTER);
		
		table_brand = new PBSUIBaseGrid();
		table_brand.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_brand.setColumnEditableAll(false);
		table_brand.setSortEnable();
		scrollPane.setViewportView(table_brand);
		String[] RHColumnNames ={"序号","品牌编码","品牌名称"};
		table_brand.setColumn(RHColumnNames);
		table_brand.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JTableUtil.fitTableColumns(table_brand);
		init();
		
	}
	private void init(){
		enableEditComp(false);
		enableSaveCancelButton(false);
		initTableData("");
		
		table_brand.addMouseListener(mouseAdapter);
	}
	MouseAdapter mouseAdapter = new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount()>=2){
				table_brand.setColumnSelectionAllowed(true);
			}
			int r = table_brand.getSelectedRow();
			textField_brand_code.setText(StringUtil.NulltoSpace(table_brand.getValueAt(r, table_brand.getColumnModel().getColumnIndex("品牌编码"))));
			textField_brand_name.setText(StringUtil.NulltoSpace(table_brand.getValueAt(r, table_brand.getColumnModel().getColumnIndex("品牌名称"))));
		}
	};
	private void enableEditComp(boolean b){
		textField_brand_code.setEditable(b);
		textField_brand_name.setEditable(b);
		
	}
	private void initTableData(String strWhere){
		String sql = "select BRAND_CODE,BRAND_NAME from bas_brand where 1=1 ";
		if(!strWhere.equals("")){
			sql = sql + strWhere;
		}
		table_brand.removeRowAll();
        Vector tableData = DBOperator.DoSelectAddSequenceRow(sql);
        table_brand.setData(tableData);
        JTableUtil.fitTableColumns(table_brand);
        table_brand.setColumnEditableAll(false);
	}
	ActionListener saveListener = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			String brand_code = textField_brand_code.getText().trim();
			String brand_name = textField_brand_name.getText().trim();
			if(brand_code.equals("")||brand_name.equals("")){
				Message.showInfomationMessage("* 为必选项!");
			}else if (lastStatus==ToolBarItem.Create) {
				String validateSql = "SELECT brand_code FROM bas_brand WHERE brand_code = '"+brand_code+"'";
				DataManager dmtmp2 = DBOperator.DoSelect2DM(validateSql);
				if(dmtmp2.getCurrentCount()>0){
					Message.showInfomationMessage("品牌编码: "+brand_code+" 已经存在不能添加!");
				}else{
					String sql = "insert into bas_brand(BRAND_CODE,BRAND_SHORT_NAME,BRAND_NAME,CREATED_BY_USER,CREATED_DTM_LOC)"+
									"value('"+brand_code+"','"+brand_name+"','"+brand_name+"' , 'sys',now())";
					System.out.println("sql = "+sql);
					int t = DBOperator.DoUpdate(sql);
					if(t>0){
						Message.showInfomationMessage("保存成功!");
						enableAll(true);
						initTableData("");
					}else{
						Message.showInfomationMessage("出错了,保存失败!");
					}
				}
			}else if(lastStatus == ToolBarItem.Modify){
				String sql = "update bas_brand set BRAND_NAME = '"+brand_name+"',BRAND_SHORT_NAME='"+brand_name+
						"',UPDATED_BY_USER ='sys',UPDATED_DTM_LOC = now() where BRAND_CODE = '"+brand_code+"'";
				System.out.println("update sql = "+sql);
				int t = DBOperator.DoUpdate(sql);
				if(t>0){
					Message.showInfomationMessage("更新成功!");
					enableAll(true);
					initTableData("");
				}else{
					Message.showInfomationMessage("出错了,更新失败!");
				}
				
			}
			//滚动 定位Table保存数据行
			Rectangle rect = new Rectangle(0, table_brand.getHeight(), 20, 20);
			table_brand.scrollRectToVisible(rect);
			table_brand.setRowSelectionInterval(table_brand.getRowCount() - 1, table_brand.getRowCount() - 1);
			table_brand.grabFocus();
            table_brand.changeSelection(table_brand.getRowIndexByColIndexAndColValue(1,brand_code), 0, false, true);
			//定位Table保存数据行
			table_brand.setRowSelectionInterval(0, table_brand.getRowIndexByColIndexAndColValue(1,brand_code));
		}
	};
	
	ActionListener cancelListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			lastStatus = ToolBarItem.Cancel;
			enableAll(true);
		}
	};
	ActionListener modifyListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(table_brand.getModel().getRowCount()==0){
				Message.showInfomationMessage("不能进行修改操作还没有数据,请先新增");
				return;
			}
			if(textField_brand_code.getText().trim().length()<=0){
				Message.showInfomationMessage("请先点击下面表格选择一行要修改的数据");
				return;
			}
			lastStatus = ToolBarItem.Modify;
			enableCRUDbutton(false);
			enableEditComp(true);
			textField_brand_code.setEditable(false);
			enableSaveCancelButton(true);
			table_brand.setEnabled(false);
		}
	};
	ActionListener deleteListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			lastStatus = ToolBarItem.Delete;
			if(table_brand.getModel().getRowCount()==0){
				Message.showInfomationMessage("不能进行删除操作还没有数据,请先新增");
				return;
			}
			if(textField_brand_code.getText().length()<=0){
				Message.showInfomationMessage("请先点击下面表格选择一行要删除的数据");
				return;
			}
			boolean b_confirm = Message.showOKorCancelMessage("是否删除所选行?");
			if(b_confirm){
				String brand_code = textField_brand_code.getText().trim();
				try {
					
					String sql_delete = "select bb.BRAND_CODE from bas_brand bb "
							+"where exists(select bi.BRAND_CODE from bas_item bi where bi.BRAND_CODE=bb.BRAND_CODE) "
							+"and bb.BRAND_CODE= '"+brand_code+"' ";
					DataManager dm = DBOperator.DoSelect2DM(sql_delete);
					if(dm==null || dm.getCurrentCount()==0){
						
					}else{
						Message.showWarningMessage("此品牌信息已经在系统使用，不能删除");
						return;
					}
					
					
					sql_delete = " delete from bas_brand where BRAND_CODE = '"+brand_code+"'";
					int t = DBOperator.DoUpdate(sql_delete);
					if(t>0){
						int selectedRow = table_brand.getSelectedRow();
						((DefaultTableModel) table_brand.getModel()).removeRow(selectedRow);
						Message.showInfomationMessage("删除成功!");
						enableAll(true);
						initTableData("");
					}
					
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	};
	ActionListener queryListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			ArrayList fieldList = new ArrayList();
			fieldList.add("BRAND_CODE:品牌编码");
			fieldList.add("BRAND_NAME:品牌名称");
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
			initTableData(retWhere);
			
			
		}
	};
	ActionListener addListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			lastStatus = ToolBarItem.Create;
			enableCRUDbutton(false);
			enableEditComp(true);
			enableSaveCancelButton(true);
			clearEditUI();
			table_brand.setEnabled(false);
			table_brand.removeMouseListener(mouseAdapter);
		}
	};
	private void enableAll(boolean b){
		enableCRUDbutton(b);
		enableEditComp(!b);
		enableSaveCancelButton(!b);
		clearEditUI();
		table_brand.setEnabled(b);
		table_brand.addMouseListener(mouseAdapter);
		
	}
	private void enableCRUDbutton(boolean b){
		btnAdd.setEnabled(b);
		btnModify.setEnabled(b);
		btnDelete.setEnabled(b);
		btnQuery.setEnabled(b);
		
	}
	private void enableSaveCancelButton(boolean b){
		btnSave.setEnabled(b);
		btnCancel.setEnabled(b);
	}
	private void clearEditUI(){
		textField_brand_code.setText("");
		textField_brand_name.setText("");
		table_brand.clearSelection();
	}
	public static BasBrandFrm getInstance() {
		if(instance == null) { 
			 synchronized(BasBrandFrm.class){
				 if(instance == null) {
					 instance = new BasBrandFrm();
				 }
			 }
	        }  
	        return instance;
	 }
	public static synchronized boolean getOpenStatus() {
		 if(instance == null) {    
	            instance = new BasBrandFrm();  
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
					BasBrandFrm frame = new BasBrandFrm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
