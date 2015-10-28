package inbound;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import DBUtil.DBOperator;
import comUtil.comData;
import dmdata.DataManager;
import main.PBSUIBaseGrid;
import sys.InnerFrame;
import sys.JTableUtil;
import sys.MainFrm;
import sys.Message;
import sys.basFrm;
import sys.tableQueryDialog;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class putawayFrm extends InnerFrame {

	private JPanel contentPane;
	private static putawayFrm instance;
	private static boolean isOpen = false;
	private JTextField txt_container_code;
	private JTextField txt_location_code;
	private PBSUIBaseGrid table;
	private JTextField txt_sysLocationCode;
	
	public static synchronized putawayFrm getInstance() {
		 if(instance == null) {    
	            instance = new putawayFrm();  
	        }  
	        return instance;
		 
	 }
	
	public static synchronized boolean getOpenStatus() {
		 if(instance == null) {    
	            instance = new putawayFrm();  
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
					putawayFrm frame = new putawayFrm();
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
	public putawayFrm() {
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.addVetoableChangeListener(new VetoableChangeListener() {

			public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException {
				JInternalFrame frame = (JInternalFrame) e.getSource();
				String name = e.getPropertyName();
				Object value = e.getNewValue();
				if (name.equals("closed") && value.equals(Boolean.TRUE)) // 窗口被关闭
				{
					System.out.println("putawayFrm窗口被关闭");
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
		
		setBounds(100, 100, 899, 458);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("\u6536\u8D27\u5468\u88C5\u7BB1\u53F7\uFF1A");
		panel.add(lblNewLabel);
		
		txt_container_code = new JTextField();
		txt_container_code.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyChar() == '\n') {
					txt_location_code.requestFocus();
					txt_location_code.selectAll();
				}
			}
		});
		txt_container_code.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				String containerCode = txt_container_code.getText().trim();
				if(containerCode.equals("")){
					return;
				}else{
					showInventoryTable(containerCode,"");
					//显示系统推荐库位
					txt_sysLocationCode.setText(getSysSuggestLocation(containerCode));
				}
				
			}
		});
		panel.add(txt_container_code);
		txt_container_code.setColumns(10);
		
		JButton btnContainerQuery = new JButton("\u6536\u8D27\u5468\u88C5\u7BB1\u67E5\u8BE2");
		btnContainerQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = "select distinct ii.container_code,ii.item_code,bi.item_bar_code,bi.item_name,ii.on_hand_qty "
						+"from inv_inventory ii inner join bas_item bi on ii.storer_code=bi.storer_code and ii.item_code=bi.item_code "
						+"inner join bas_location bl on ii.warehouse_code=bl.warehouse_code and ii.location_code=bl.location_code "
						+"inner join bas_container bc on ii.warehouse_code=bc.warehouse_code and ii.container_code=bc.container_code "
						+" where bl.location_type_code='Dock' and bc.status='2' and ii.warehouse_code='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' ";
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
				Object obj = dm.getObject("CONTAINER_CODE", 0);
				if(obj==null || obj.equals("")){
					return;
				}else{
					txt_container_code.setText((String) dm.getObject("CONTAINER_CODE", 0));
					txt_container_code.requestFocus();
					txt_container_code.selectAll();
				}
			}
		});
		panel.add(btnContainerQuery);
		
		JLabel lblNewLabel_2 = new JLabel("\u7CFB\u7EDF\u63A8\u8350\u5E93\u4F4D\uFF1A");
		panel.add(lblNewLabel_2);
		
		txt_sysLocationCode = new JTextField();
		txt_sysLocationCode.setEditable(false);
		panel.add(txt_sysLocationCode);
		txt_sysLocationCode.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("\u4E0A\u67B6\u5E93\u4F4D\u7F16\u7801\uFF1A");
		panel.add(lblNewLabel_1);
		
		txt_location_code = new JTextField();
		txt_location_code.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				String locationCode = txt_location_code.getText().trim();
				if(locationCode.equals("")){
					return;
				}
				String sql = "select location_code from bas_location where warehouse_code='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' and location_type_code='Normal' and location_code = '"+locationCode+"' ";
				Vector vec = DBOperator.DoSelect(sql);
				if(vec==null || vec.size()==0){
					JOptionPane.showMessageDialog(null, "库位号不正确","提示",JOptionPane.WARNING_MESSAGE);
					txt_location_code.setText("");
					txt_location_code.requestFocus();
					txt_location_code.selectAll();
					return;
				}
			}
		});
		panel.add(txt_location_code);
		txt_location_code.setColumns(10);
		
		JButton btnMove = new JButton("\u4E0A\u67B6\u786E\u8BA4");
		btnMove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = "";
				String locationCode = txt_location_code.getText().trim();
				String containerCode = txt_container_code.getText().trim();
				if(locationCode.equals("")){
					JOptionPane.showMessageDialog(null, "请输入正确库位","提示",JOptionPane.WARNING_MESSAGE);
					return;
				}
				sql = "select location_code from bas_location where warehouse_code='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' and location_type_code='Normal' and location_code = '"+locationCode+"' ";
				Vector vec = DBOperator.DoSelect(sql);
				if(vec==null || vec.size()==0){
					JOptionPane.showMessageDialog(null, "库位号不正确","提示",JOptionPane.WARNING_MESSAGE);
					txt_location_code.setText("");
					txt_location_code.requestFocus();
					txt_location_code.selectAll();
					return;
				}
				if(table.getRowCount()==0){
					Message.showWarningMessage("无明细记录，不能上架");
					return;
				}
				
				String tmp = putawayConfirm(locationCode,containerCode,MainFrm.getUserInfo().getString("USER_CODE", 0));
				if(tmp.substring(0, 3).equals("ERR")){
					Message.showErrorMessage(tmp);
				}else{
					Message.showInfomationMessage(tmp);
					sql = "select USE_TYPE from bas_container where CONTAINER_CODE='"+containerCode+"' and warehouse_code='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' ";
					DataManager dm = DBOperator.DoSelect2DM(sql);
					if(dm.getString("USE_TYPE", 0).equalsIgnoreCase("temp")){
						showInventoryTable("*"," and ii.location_code='"+locationCode+"' ");
					}else{
						showInventoryTable(containerCode," and ii.location_code='"+locationCode+"' ");
					}
					
					txt_container_code.setText("");
					txt_location_code.setText("");
					txt_sysLocationCode.setText("");
					txt_container_code.requestFocus();
				}
			}
		});
		
		JButton btnLocationQuery = new JButton("\u5E93\u4F4D\u67E5\u8BE2");
		btnLocationQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = "select bw.warehouse_code,bw.warehouse_name,bl.location_code,STATUS from bas_location bl inner join bas_warehouse bw on bl.warehouse_code=bw.warehouse_code "
						+" where bl.warehouse_code='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' and bl.location_type_code='Normal' ";
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
				Object obj = dm.getObject("location_code", 0);
				if(obj==null || obj.equals("")){
					return;
				}else{
					txt_location_code.setText((String) dm.getObject("location_code", 0));
					txt_location_code.requestFocus();
					txt_location_code.selectAll();
				}
			}
		});
		panel.add(btnLocationQuery);
		panel.add(btnMove);
		
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
		panel.add(btnClose);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane, BorderLayout.CENTER);
		
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
	}
	
	public String putawayConfirm(String LOCATION_CODE,String CONTAINER_CODE, String userCode) {
		String sql = "select container_code from inv_inventory where container_code='"+CONTAINER_CODE+"' ";
		DataManager tmpDM = DBOperator.DoSelect2DM(sql);
		if(tmpDM==null || tmpDM.getCurrentCount()==0){
			return "ERR-箱号未收货，不能上架";
		}
		if(tmpDM.getCurrentCount()>1){
			return "ERR-该箱号之前有收货完成未上架的任务，不能重复使用该箱号上架";
		}
		sql = "update inv_inventory set location_code='"+LOCATION_CODE+"' where container_code='"+CONTAINER_CODE+"' and warehouse_code='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' ";
		int t = DBOperator.DoUpdate(sql);
		if(t==0){
			return "ERR-上架失败，未找到数据,库位："+LOCATION_CODE+"，箱号："+CONTAINER_CODE+"，请联系系统管理员";
		}else{
			//如果周装箱 use_type=temp (临时)，上架后更新库存Container_code = '*'，同时收货周装箱  status='0'
			sql = "select ii.WAREHOUSE_CODE,ii.STORER_CODE,ii.ITEM_CODE,ii.LOT_NO,ii.LOCATION_CODE,bc.USE_TYPE,ii.CONTAINER_CODE,ii.ON_HAND_QTY "
					+"from inv_inventory ii inner join bas_container bc on ii.WAREHOUSE_CODE=bc.WAREHOUSE_CODE and ii.CONTAINER_CODE=bc.CONTAINER_CODE "
					+"where ii.LOCATION_CODE='"+LOCATION_CODE+"' and ii.CONTAINER_CODE='"+CONTAINER_CODE+"' ";
			DataManager dm = DBOperator.DoSelect2DM(sql);
			if(dm==null || dm.getCurrentCount()==0){
				return "ERR-上架失败，更新到上架库位失败，请联系系统管理员";
			}
			if(dm.getCurrentCount()==1){
				String USE_TYPE = dm.getString("USE_TYPE", 0);
				String WAREHOUSE_CODE = dm.getString("WAREHOUSE_CODE", 0);
				String STORER_CODE = dm.getString("STORER_CODE", 0);
				String ITEM_CODE = dm.getString("ITEM_CODE", 0);
				String LOT_NO = dm.getString("LOT_NO", 0);
				String ON_HAND_QTY = dm.getString("ON_HAND_QTY", 0);
				
				//先判断当前库存的container_code=* 是否有库存，如果有，就数量累加，否则就直接更新库存的container_code=*
				String tmp = "";
				if(USE_TYPE.equalsIgnoreCase("temp")){
					tmp = comData.getInventoryID(WAREHOUSE_CODE,STORER_CODE,ITEM_CODE,LOT_NO,LOCATION_CODE,"*",ON_HAND_QTY,userCode);
					if(!tmp.equals("")){
						//释放箱号状态
						sql = "update bas_container set status='0',UPDATED_BY_USER='"+userCode+"',UPDATED_DTM_LOC=now(),USER_DEF1='入库上架成功' where container_code='"+CONTAINER_CODE+"' ";
						t = DBOperator.DoUpdate(sql);
						if(t>0){
							//上架成功，原先库存行记录(container_code <> *)
							sql = "delete from inv_inventory where LOCATION_CODE='"+LOCATION_CODE+"' and CONTAINER_CODE='"+CONTAINER_CODE+"' ";
							t = DBOperator.DoUpdate(sql);
							//更改库位为已使用状态
							sql = "update bas_location set STATUS='storage',UPDATED_BY_USER='"+userCode+"',UPDATED_DTM_LOC=now(),USER_DEF1='入库上架' "
									+"where status='empty' and LOCATION_CODE='"+LOCATION_CODE+"' and WAREHOUSE_CODE in "
									+"(select WAREHOUSE_CODE from inv_inventory where container_code='"+CONTAINER_CODE+"') ";
							DBOperator.DoUpdate(sql);
							return "OK-上架成功";
						}else{
							return "ERR-上架失败，释放箱号："+CONTAINER_CODE+" 失败，请联系系统管理员";
						}
					}else{
						return "ERR-上架失败，请联系系统管理员";
					}
				}else{
					//更新箱号
					sql = "update bas_container set UPDATED_DTM_LOC=now(),USER_DEF1='入库上架成功' where container_code='"+CONTAINER_CODE+"' ";
					t = DBOperator.DoUpdate(sql);
					if(t>0){
						//更改库位为已使用状态
						sql = "update bas_location set STATUS='storage',UPDATED_BY_USER='"+userCode+"',UPDATED_DTM_LOC=now(),USER_DEF1='入库上架' "
								+"where status='empty' and LOCATION_CODE='"+LOCATION_CODE+"' and WAREHOUSE_CODE in "
								+"(select WAREHOUSE_CODE from inv_inventory where container_code='"+CONTAINER_CODE+"') ";
						DBOperator.DoUpdate(sql);
						return "OK-上架成功";
					}else{
						return "ERR-上架失败，释放箱号："+CONTAINER_CODE+" 失败，请联系系统管理员";
					}
				}
				
			}else{
				return "ERR-上架失败\n当前库位："+LOCATION_CODE+"，箱号："+CONTAINER_CODE+" \n之前有未完成的上架任务，请联系系统管理员";
			}

		}
	}
	
	private boolean showInventoryTable(String containerCode,String strWhere){
		if(containerCode.equals("")){
			return false;
		}else{
			String sql = "select ii.WAREHOUSE_CODE,bw.WAREHOUSE_NAME,ii.STORER_CODE,bs.STORER_NAME,ii.LOCATION_CODE,ii.CONTAINER_CODE,"
					+"ii.ITEM_CODE,bi.ITEM_BAR_CODE,bi.ITEM_NAME,bi.ITEM_SPEC,ii.ON_HAND_QTY,biu.unit_name"
					+",il.LOTTABLE01,il.LOTTABLE02,il.LOTTABLE03,il.LOTTABLE04,il.LOTTABLE05,"
					+"il.LOTTABLE06,il.LOTTABLE07,il.LOTTABLE08,il.LOTTABLE09,il.LOTTABLE10"
					+",ii.`STATUS`,ii.QUALITY "
					+"from inv_inventory ii "
					+"inner join bas_location bl on ii.LOCATION_CODE=bl.LOCATION_CODE and ii.WAREHOUSE_CODE=bl.WAREHOUSE_CODE "
					+"inner join bas_item bi on ii.ITEM_CODE=bi.ITEM_CODE and ii.STORER_CODE=bi.STORER_CODE "
					+"inner join bas_item_unit biu on bi.UNIT_CODE = biu.unit_code "
					+"inner join inv_lot il on ii.LOT_NO=il.LOT_NO "
					+"inner join bas_storer bs on ii.STORER_CODE=bs.STORER_CODE "
					+"inner join bas_warehouse bw on ii.WAREHOUSE_CODE=bw.WAREHOUSE_CODE "
					+"where ii.CONTAINER_CODE='"+containerCode+"' and ii.warehouse_code='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' ";
			if(!strWhere.trim().equals("")){
				sql = sql + strWhere;
			}
			DataManager dm = DBOperator.DoSelect2DM(sql);
			if (table.getColumnCount() == 0) {
				table.setColumn(dm.getCols());
			}
			table.removeRowAll();
			table.setData(dm.getDataStrings());
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			table.setColumnEditableAll(false);
			JTableUtil.fitTableColumns(table);
		}
		return true;
	}
	
	private String getSysSuggestLocation(String containerCode){
		String sql = "select ii.LOCATION_CODE from inb_receipt_detail ird "
				+"left outer join inv_inventory ii on ird.WAREHOUSE_CODE=ii.WAREHOUSE_CODE and ii.STORER_CODE=ird.STORER_CODE and ii.ITEM_CODE=ird.ITEM_CODE "
				+"left outer join bas_location bl on bl.location_code=ii.location_code and bl.warehouse_code=ii.warehouse_code "
				+"where bl.location_type_code='Normal' and ird.CONTAINER_CODE='"+containerCode+"' order by ii.ON_HAND_QTY limit 1 ";
		Vector vec = DBOperator.DoSelect(sql);
		if(vec==null || vec.size()==0){
			sql = "select bl.location_code from bas_location bl "
					+"where bl.warehouse_code='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' and bl.location_type_code='Normal' and bl.status='empty' order by bl.location_code limit 1 ";
			vec = DBOperator.DoSelect(sql);
			Object[] obj = (Object[]) vec.get(0);
			return obj[0].toString();
		}else{
			Object[] obj = (Object[]) vec.get(0);
			return obj[0].toString();
		}
	}

	private void setExtendedState(int maximizedBoth) {
		// TODO Auto-generated method stub
		
	}

}
