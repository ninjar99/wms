package inventory;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import sys.InnerFrame;
import sys.JTableUtil;
import sys.MainFrm;
import sys.Message;
import sys.tableQueryDialog;
import util.JTNumEdit;
import util.Math_SAM;

import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import DBUtil.DBOperator;
import comUtil.comData;
import dmdata.DataManager;
import dmdata.xArrayList;
import main.PBSUIBaseGrid;

import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InvMoveFrm extends InnerFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6207820266990975561L;
	private JPanel contentPane;
	private static InvMoveFrm instance;
	private static boolean isOpen = false;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private PBSUIBaseGrid table1;
	private JTextField txt_warehouse_from;
	private JTextField txt_location_from;
	private JTextField txt_container_from;
	private JTextField txt_item_from;
	private JTextField txt_warehouse_to;
	private JTextField txt_location_to;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JButton btn_warehouse_from_query;
	private JButton btn_warehouse_to_query;
	private JButton btn_item_from_query;
	private JButton btn_container_to_query;
	private JTextField txt_container_to;
	private PBSUIBaseGrid table;
	private JTNumEdit txt_inv_qty;
	
	public static synchronized InvMoveFrm getInstance() {
		 if(instance == null) {    
	            instance = new InvMoveFrm();  
	        }  
	        return instance;
		 
	 }
	
	public static synchronized boolean getOpenStatus() {
		 if(instance == null) {    
	            instance = new InvMoveFrm();  
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
					InvMoveFrm frame = new InvMoveFrm();
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
	public InvMoveFrm() {
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.addVetoableChangeListener(new VetoableChangeListener() {

			public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException {
				String name = e.getPropertyName();
				Object value = e.getNewValue();
				if (name.equals("closed") && value.equals(Boolean.TRUE)) // 窗口被关闭
				{
					System.out.println("InvMoveFrm窗口被关闭");
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
		
		setBounds(100, 100, 854, 457);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("\u8DE8\u4ED3\u79FB\u5E93", null, panel_2, null);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JPanel headerPanel_2 = new JPanel();
		panel_2.add(headerPanel_2, BorderLayout.NORTH);
		headerPanel_2.setLayout(new GridLayout(3, 1, 0, 0));
		
		JPanel panel_2_type = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_2_type.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		headerPanel_2.add(panel_2_type);
		
		JRadioButton rb1 = new JRadioButton("\u4ED3\u5185\u5355\u4EF6\u79FB\u5E93  ");
		buttonGroup.add(rb1);
		rb1.setForeground(Color.BLUE);
		rb1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if(rb1.isSelected()){
					txt_warehouse_from.setText("");
					txt_warehouse_from.setEditable(false);
					txt_warehouse_to.setText("");
					txt_warehouse_to.setEditable(false);
					txt_location_from.requestFocus();
					btn_warehouse_from_query.setEnabled(false);
					btn_warehouse_to_query.setEnabled(false);
					txt_item_from.setEditable(true);
					btn_item_from_query.setEnabled(true);
					txt_inv_qty.setEditable(true);
					txt_container_to.setEditable(true);
				}else{
					txt_warehouse_from.setText("");
					txt_warehouse_from.setEditable(true);
					txt_warehouse_to.setText("");
					txt_warehouse_to.setEditable(true);
					txt_location_from.requestFocus();
					btn_warehouse_from_query.setEnabled(true);
					btn_warehouse_to_query.setEnabled(true);
				}
			}
		});
		panel_2_type.add(rb1);
		
		JRadioButton rb3 = new JRadioButton("\u8DE8\u4ED3\u6574\u7BB1\u79FB\u5E93  ");
		buttonGroup.add(rb3);
		rb3.setForeground(Color.MAGENTA);
		rb3.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(rb3.isSelected()){
					txt_warehouse_from.setText("");
					txt_warehouse_from.setEditable(true);
					txt_warehouse_to.setText("");
					txt_warehouse_to.setEditable(true);
					txt_location_from.requestFocus();
					btn_warehouse_from_query.setEnabled(true);
					btn_warehouse_to_query.setEnabled(true);
					txt_item_from.setEditable(false);
					btn_item_from_query.setEnabled(false);
					txt_container_to.setEditable(false);
					btn_container_to_query.setEnabled(false);
					txt_inv_qty.setEditable(false);
				}else{
//					txt_warehouse_from.setText("");
//					txt_warehouse_from.setEditable(true);
//					txt_warehouse_to.setText("");
//					txt_warehouse_to.setEditable(true);
//					txt_location_from.requestFocus();
//					btn_warehouse_from_query.setEnabled(true);
//					btn_warehouse_to_query.setEnabled(true);
				}
			}
		});
		
		JRadioButton rb2 = new JRadioButton("\u4ED3\u5185\u6574\u7BB1\u79FB\u5E93  ");
		rb2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(rb2.isSelected()){
					txt_warehouse_from.setText("");
					txt_warehouse_from.setEditable(false);
					txt_warehouse_to.setText("");
					txt_warehouse_to.setEditable(false);
					txt_location_from.requestFocus();
					btn_warehouse_from_query.setEnabled(false);
					btn_warehouse_to_query.setEnabled(false);
					txt_item_from.setEditable(false);
					btn_item_from_query.setEnabled(false);
					txt_container_to.setEditable(false);
					btn_container_to_query.setEnabled(false);
					txt_inv_qty.setEditable(false);
				}else{
					txt_warehouse_from.setText("");
					txt_warehouse_from.setEditable(true);
					txt_warehouse_to.setText("");
					txt_warehouse_to.setEditable(true);
					txt_location_from.requestFocus();
					btn_warehouse_from_query.setEnabled(true);
					btn_warehouse_to_query.setEnabled(true);
					
				}
			}
		});
		buttonGroup.add(rb2);
		rb2.setForeground(Color.RED);
		panel_2_type.add(rb2);
		panel_2_type.add(rb3);
		
		JPanel panel_2_from = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_2_from.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		headerPanel_2.add(panel_2_from);
		
		JLabel lblNewLabel_5 = new JLabel("\u539F\u4ED3\u5E93\u7F16\u7801\uFF1A");
		panel_2_from.add(lblNewLabel_5);
		
		txt_warehouse_from = new JTextField();
		txt_warehouse_from.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				if (arg0.getKeyChar() == '\n') {
					txt_location_from.requestFocus();
					txt_location_from.selectAll();
				}
			}
		});
		panel_2_from.add(txt_warehouse_from);
		txt_warehouse_from.setColumns(10);
		
	    btn_warehouse_from_query = new JButton("<");
		btn_warehouse_from_query.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String sql = "select distinct warehouse_code 仓库编码,warehouse_name 仓库名称 from bas_warehouse ";
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
				Object obj = dm.getObject("仓库编码", 0);
				if(obj==null || obj.equals("")){
					return;
				}else{
					txt_warehouse_from.setText((String) dm.getObject("仓库编码", 0));
					txt_warehouse_from.requestFocus();
					txt_warehouse_from.selectAll();
				}
			}
		});
		panel_2_from.add(btn_warehouse_from_query);
		
		JLabel lblNewLabel_6 = new JLabel("\u5E93\u4F4D\u53F7\uFF1A");
		panel_2_from.add(lblNewLabel_6);
		
		txt_location_from = new JTextField();
		txt_location_from.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyChar() == '\n') {
					txt_container_from.requestFocus();
					txt_container_from.selectAll();
				}
			}
		});
		txt_location_from.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				String locationCode = txt_location_from.getText().trim();
				if(!locationCode.equals("")){
					showInventory(" and ii.location_code='"+locationCode+"' ");
				}
			}
		});
		panel_2_from.add(txt_location_from);
		txt_location_from.setColumns(10);
		
		JButton btn_location_from_query = new JButton("<");
		btn_location_from_query.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = "select distinct bw.warehouse_code 仓库编码,bw.warehouse_name 仓库名称,bl.location_code 库位编码,bl.location_type_code 库位类型  "
						+"from inv_inventory ii inner join bas_location bl on ii.location_code=bl.location_code "
						+"inner join bas_warehouse bw on bl.warehouse_code=bw.warehouse_code "
						+" where bl.location_type_code <> 'Dock' ";
				String warehosueCode = txt_warehouse_from.getText().trim();
				if(!warehosueCode.equals("")){
					sql = sql + " and bw.warehouse_code= '"+warehosueCode+"' ";
				}else{
					sql = sql + " and bw.warehouse_code= '"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' ";
				}
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
				Object obj = dm.getObject("库位编码", 0);
				if(obj==null || obj.equals("")){
					return;
				}else{
					txt_location_from.setText((String) dm.getObject("库位编码", 0));
					txt_location_from.requestFocus();
					txt_location_from.selectAll();
				}
			}
		});
		panel_2_from.add(btn_location_from_query);
		
		JLabel lblNewLabel_7 = new JLabel("\u7BB1\u53F7\uFF1A");
		panel_2_from.add(lblNewLabel_7);
		
		txt_container_from = new JTextField();
		txt_container_from.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyChar() == '\n') {
					if(txt_item_from.isEditable()){
						txt_item_from.requestFocus();
						txt_item_from.selectAll();
					}else{
						if(txt_warehouse_to.isEditable()){
							txt_warehouse_to.requestFocus();
							txt_warehouse_to.selectAll();
						}else{
							txt_location_to.requestFocus();
							txt_location_to.selectAll();
						}
					}
				}
			}
		});
		txt_container_from.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				String locationCode = txt_location_from.getText().trim();
				String containerCode = txt_container_from.getText().trim();
				if(!containerCode.equals("")){
					showInventory(" and ii.location_code='"+locationCode+"' and ii.container_code='"+containerCode+"' ");
				}
			}
		});
		panel_2_from.add(txt_container_from);
		txt_container_from.setColumns(10);
		
		JButton btn_container_from_query = new JButton("<");
		btn_container_from_query.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = "select bw.warehouse_name 仓库名称,container_code 箱号 ,ii.item_code 商品编码,bi.item_bar_code 商品条码,bi.item_name 商品名称,sum(ON_HAND_QTY-ALLOCATED_QTY-PICKED_QTY) 可用库存数量  "
							+" from inv_inventory ii inner join bas_item bi on ii.storer_code=bi.storer_code and ii.item_code=bi.item_code "
						+"inner join bas_warehouse bw on ii.warehouse_code=bw.warehouse_code "
							+"where 1=1 ";
				String warehouseCode = txt_warehouse_from.getText().trim();
				String locationCode = txt_location_from.getText().trim();
				if(!warehouseCode.equals("")){
					sql = sql + " and ii.warehouse_code= '"+warehouseCode+"' ";
				}else{
					sql = sql + " and ii.warehouse_code= '"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' ";
				}
				if(!locationCode.equals("")){
					sql = sql + " and ii.location_code= '"+locationCode+"' ";
				}
				sql = sql + " group by container_code,ii.item_code,bi.item_bar_code,bi.item_name ";
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
				Object obj = dm.getObject("箱号", 0);
				if(obj==null || obj.equals("")){
					return;
				}else{
					txt_container_from.setText((String) dm.getObject("箱号", 0));
					txt_container_from.requestFocus();
					txt_container_from.selectAll();
				}
			}
		});
		panel_2_from.add(btn_container_from_query);
		
		JLabel lblNewLabel_8 = new JLabel("\u6761\u7801\uFF1A");
		panel_2_from.add(lblNewLabel_8);
		
		txt_item_from = new JTextField();
		txt_item_from.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyChar() == '\n') {
					if(txt_warehouse_to.isEditable()){
						txt_warehouse_to.requestFocus();
						txt_warehouse_to.selectAll();
					}else{
						txt_location_to.requestFocus();
						txt_location_to.selectAll();
					}
				}
			}
		});
		txt_item_from.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				String strwhere ="";
				String itemBarCode = txt_item_from.getText().trim();
				String locationCode = txt_location_from.getText().trim();
				String containerCode = txt_container_from.getText().trim();
				if(itemBarCode.equals("")){
					return;
				}
				if(!locationCode.equals("")){
					strwhere = " and ii.location_code='"+locationCode+"' ";
				}
				if(!containerCode.equals("")){
					strwhere = strwhere+ " and ii.container_code='"+containerCode+"' ";
				}else{
					return;
				}
				Object itemCode = txt_item_from.getToolTipText();
				if(itemCode==null){
					
				}else if(!itemCode.toString().equals("")){
					strwhere = strwhere + " and ii.item_code='"+itemCode+"' ";
					showInventory(strwhere);
				}
				txt_inv_qty.setText("1");
//				btn_item_from_query.doClick();
			}
		});
		panel_2_from.add(txt_item_from);
		txt_item_from.setColumns(10);
		
		btn_item_from_query = new JButton("<");
		btn_item_from_query.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = "select ii.INV_INVENTORY_ID 库存ID,ii.item_code 商品编码,bi.item_bar_code 商品条码,bi.item_name 商品名称,item_spec 商品规格,"
							+"ifnull(ii.ON_HAND_QTY,0)+(ifnull(IN_TRANSIT_QTY,0))-(ifnull(ii.ALLOCATED_QTY,0))-(ifnull(ii.PICKED_QTY,0))-(ifnull(INACTIVE_QTY,0)) 可用数量 "
							+",ii.LOT_NO,il.LOTTABLE01,il.LOTTABLE02,il.LOTTABLE03,il.LOTTABLE04,il.LOTTABLE05,il.LOTTABLE06,il.LOTTABLE07,il.LOTTABLE08,il.LOTTABLE09,il.LOTTABLE10 "
							+"from inv_inventory ii inner join bas_item bi on ii.storer_code=bi.storer_code and ii.item_code=bi.item_code "
							+"inner join inv_lot il on ii.LOT_NO=il.LOT_NO ";
				String locationCode = txt_location_from.getText().trim();
				if(!locationCode.equals("")){
					sql = sql + " and ii.location_code='"+locationCode+"' ";
				}
				String containerCode = txt_container_from.getText().trim();
				if(!containerCode.equals("")){
					sql = sql + " and ii.container_code= '"+containerCode+"' ";
				}
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
				Object obj = dm.getObject("商品条码", 0);
				if(obj==null || obj.equals("")){
					return;
				}else{
					txt_item_from.setText((String) dm.getObject("商品条码", 0));
					txt_item_from.setToolTipText((String) dm.getObject("商品编码", 0));
					txt_inv_qty.setText("1");
//					txt_item_from.requestFocus();
					txt_item_from.selectAll();
					txt_item_from.requestFocus();
				}
			}
		});
		panel_2_from.add(btn_item_from_query);
		
		JLabel lblNewLabel_12 = new JLabel("\u6570\u91CF\uFF1A");
		panel_2_from.add(lblNewLabel_12);
		
		txt_inv_qty = new JTNumEdit(10, "#####",true);
		panel_2_from.add(txt_inv_qty);
		txt_inv_qty.setColumns(3);
		
		JPanel panel_2_to = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_2_to.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		headerPanel_2.add(panel_2_to);
		
		JLabel lblNewLabel_9 = new JLabel("\u76EE\u6807\u4ED3\u5E93\u53F7\uFF1A");
		panel_2_to.add(lblNewLabel_9);
		
		txt_warehouse_to = new JTextField();
		txt_warehouse_to.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyChar() == '\n') {
					txt_location_to.requestFocus();
					txt_location_to.selectAll();
				}
			}
		});
		panel_2_to.add(txt_warehouse_to);
		txt_warehouse_to.setColumns(10);
		
		btn_warehouse_to_query = new JButton("<");
		btn_warehouse_to_query.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = "select distinct warehouse_code 仓库编码,warehouse_name 仓库名称 from bas_warehouse ";
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
				Object obj = dm.getObject("仓库编码", 0);
				if(obj==null || obj.equals("")){
					return;
				}else{
					txt_warehouse_to.setText((String) dm.getObject("仓库编码", 0));
					txt_warehouse_to.requestFocus();
					txt_warehouse_to.selectAll();
				}
			}
		});
		panel_2_to.add(btn_warehouse_to_query);
		
		JLabel lblNewLabel_10 = new JLabel("\u5E93\u4F4D\u53F7\uFF1A");
		panel_2_to.add(lblNewLabel_10);
		
		txt_location_to = new JTextField();
		txt_location_to.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyChar() == '\n') {
					txt_container_to.requestFocus();
					txt_container_to.selectAll();
				}
			}
		});
		panel_2_to.add(txt_location_to);
		txt_location_to.setColumns(10);
		
		JButton btn_location_to_query = new JButton("<");
		btn_location_to_query.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = "select distinct bw.warehouse_code 仓库编码,bw.warehouse_name 仓库名称,bl.location_code 库位编码,bl.location_type_code 库位类型  "
						+"from bas_location bl inner join bas_warehouse bw on bl.warehouse_code=bw.warehouse_code "
						+" where bl.location_type_code <> 'Dock' ";
				String warehosueCode = txt_warehouse_to.getText().trim();
				if(!warehosueCode.equals("")){
					sql = sql + " and bw.warehouse_code= '"+warehosueCode+"' ";
				}else{
					sql = sql + " and bw.warehouse_code= '"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' ";
				}
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
				Object obj = dm.getObject("库位编码", 0);
				if(obj==null || obj.equals("")){
					return;
				}else{
					txt_location_to.setText((String) dm.getObject("库位编码", 0));
					txt_location_to.requestFocus();
					txt_location_to.selectAll();
				}
			}
		});
		panel_2_to.add(btn_location_to_query);
		
		JLabel lblNewLabel_11 = new JLabel("\u7BB1\u53F7\uFF1A");
		panel_2_to.add(lblNewLabel_11);
		
		txt_container_to = new JTextField();
		txt_container_to.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyChar() == '\n') {
					table.requestFocus();
					table.selectAll();
				}
			}
		});
		panel_2_to.add(txt_container_to);
		txt_container_to.setColumns(10);
		
		btn_container_to_query = new JButton("<");
		btn_container_to_query.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = "select bw.WAREHOUSE_NAME 仓库名称,bc.CONTAINER_CODE 箱号,(case bc.`STATUS` when '0' then '空箱' when '1' then '收货中' when '2' then '收货完成' else bc.`STATUS` end) 状态 "
						+",case bc.USE_TYPE when 'normal' then '固定' when 'temp' then '临时' else bc.USE_TYPE end 用途类型 "
						+"from bas_container bc inner join bas_warehouse bw on bc.WAREHOUSE_CODE=bw.WAREHOUSE_CODE "
						+"where bc.warehouse_code='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' and bc.status in ('0','1') "
						+"and bc.USE_TYPE='normal' "
						+"order by bc.status,bc.container_code ";
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
				Object obj = dm.getObject("箱号", 0);
				if(obj==null || obj.equals("")){
					return;
				}else{
					txt_container_to.setText((String) dm.getObject("箱号", 0));
					txt_container_to.requestFocus();
					txt_container_to.selectAll();
				}
			}
		});
		panel_2_to.add(btn_container_to_query);
		
		JButton btnOK = new JButton("\u79FB\u5E93\u786E\u8BA4");
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean isDamage = false;
				String fromInvID = "";
				String fromQty = "";
				String lotNo = "";
				String warehousrFrom = txt_warehouse_from.getText().trim();
				String locationFrom = txt_location_from.getText().trim();
				String containerFrom = txt_container_from.getText().trim();
				String itemFrom = txt_item_from.getText().trim();
				Object itemCodeFrom = "";
				String warehouseTo = txt_warehouse_to.getText().trim();
				String locationTo = txt_location_to.getText().trim();
				String containerTo = txt_container_to.getText().trim();
				String inputInvQty = txt_inv_qty.getText();
				if(Math_SAM.str2Double(inputInvQty)<=0){
					Message.showWarningMessage("移库数量不正确，不能移库");
					txt_inv_qty.requestFocus();
					return;
				}
				if(table.getRowCount()==0){
					Message.showWarningMessage("表格无明细数据，不能移库");
					return;
				}
				if(table.getSelectedRow()<0){
					Message.showWarningMessage("请选择一行明细进行移库");
					return;
				}
				if(table.getSelectedRowCount()>1){
					Message.showWarningMessage("请选择一行明细进行移库");
					return;
				}
				if(rb3.isSelected() && warehousrFrom.equalsIgnoreCase(warehouseTo)){
					Message.showWarningMessage("跨仓移库，原仓库和目标仓库不能相同");
					return;
				}
				if(rb1.isSelected() && containerFrom.equalsIgnoreCase(containerTo)){
					if(containerFrom.equals("*") && containerTo.equals("*")){
						
					}else{
						Message.showWarningMessage("仓内单件移库，原箱号和目标箱号不能相同");
						return;
					}
					
				}
				if(rb2.isSelected() && containerFrom.equalsIgnoreCase(containerTo)){
					Message.showWarningMessage("仓内整箱移库，原箱号和目标箱号不能相同");
					return;
				}
				if(table.getRowCount()<=0){
					Message.showWarningMessage("未查找到库存，无法移库！");
					return;
				}
				lotNo = (String) table.getValueAt(table.getSelectedRow(), table.getColumnModel().getColumnIndex("批次号"));
				fromInvID = (String) table.getValueAt(table.getSelectedRow(), table.getColumnModel().getColumnIndex("库存ID"));
				itemCodeFrom = (String) table.getValueAt(table.getSelectedRow(), table.getColumnModel().getColumnIndex("商品编码"));
				fromQty = (String) table.getValueAt(table.getSelectedRow(), table.getColumnModel().getColumnIndex("可用库存"));
				String sql = "";
				//检查是否目的库位为残次库位
				sql = "select location_code,location_type_code from bas_location "
					 +"where warehouse_code='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' and location_code='"+locationTo+"' and location_type_code='Damage' ";
				DataManager dm = DBOperator.DoSelect2DM(sql);
				if(dm==null || dm.getCurrentCount()==0){
					
				}else{
					boolean b_confirm = Message.showOKorCancelMessage("目标库位为残次库位，是否将该商品转残次?\n残次库位的库存系统自动冻结，不能分配出库");
					if(b_confirm){
						isDamage = true;
					}else{
						return;
					}
				}
				
				if (rb3.isSelected() && !warehousrFrom.equals("") && !warehouseTo.equals("")
						&& !locationTo.equals("")) {
					if(isDamage){						
						sql = "update inv_inventory set warehouse_code='" + warehouseTo + "',location_code='" + locationTo
								+ "' " + ",INACTIVE_QTY=ifnull(ii.ON_HAND_QTY,0)-(ifnull(ii.ALLOCATED_QTY,0))-(ifnull(ii.PICKED_QTY,0))-(ifnull(INACTIVE_QTY,0)) "
								+ "where warehouse_code='" + warehousrFrom + "' and location_code='" + locationFrom
								+ "' and container_code='" + containerFrom + "' ";
					}else{
						sql = "update inv_inventory set warehouse_code='" + warehouseTo + "',location_code='" + locationTo
								+ "' " + "where warehouse_code='" + warehousrFrom + "' and location_code='" + locationFrom
								+ "' and container_code='" + containerFrom + "' ";
					}
					
				} else if (rb1.isSelected() && !containerFrom.equals("") && !itemCodeFrom.toString().trim().equals("")
						&& !locationTo.equals("") && !containerTo.equals("")) {
					if(isDamage){
						//先查询目标库位是否存在记录
						sql = "select ii.INV_INVENTORY_ID,ii.ON_HAND_QTY+ii.IN_TRANSIT_QTY-(ii.ALLOCATED_QTY)-(ii.PICKED_QTY) availableQty "
								+" from inv_inventory ii "
								+ "where ii.warehouse_code='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' and ii.location_code='" + locationTo + "' and ii.container_code='" + containerTo
								+ "' and ii.item_code='" + itemCodeFrom.toString().trim() + "' and LOT_NO = '"+lotNo+"' ";
						DataManager dm2 = DBOperator.DoSelect2DM(sql);
						if(Math_SAM.str2Double(inputInvQty)>Math_SAM.str2Double(fromQty)){
							Message.showWarningMessage("数量超出原库存数量，不能操作");
							return;
						}
						//不存在就插入目标库位新记录   冻结数量
						if(dm2==null || dm2.getCurrentCount()==0){
							sql = "insert into inv_inventory(WAREHOUSE_CODE,STORER_CODE,ITEM_CODE,ITEM_NAME,INV_LOT_ID,LOT_NO,LOCATION_CODE"
									+ ",CONTAINER_CODE,INACTIVE_QTY,INB_TOTAL_QTY,CREATED_BY_USER,CREATED_DTM_LOC) "
									+"select WAREHOUSE_CODE,STORER_CODE,ITEM_CODE,ITEM_NAME,INV_LOT_ID,LOT_NO,'"+locationTo+"',"
									+"'"+containerTo+"',"+inputInvQty+","+inputInvQty+",'"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"',now() "
									+" from inv_inventory where warehouse_code='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' and location_code='" + locationFrom + "' and container_code='" + containerFrom
									+ "' and item_code='" + itemCodeFrom.toString().trim() + "' and LOT_NO = '"+lotNo+"' ";
							int insertCount = DBOperator.DoUpdate(sql);
							if(insertCount==0){
								Message.showErrorMessage("数据插入目标库位失败");
								return;
							}else{
								//存在就扣减原库位数量
								sql = "update inv_inventory set ON_HAND_QTY=ON_HAND_QTY-("+inputInvQty+"),"
										+ "OUB_TOTAL_QTY=OUB_TOTAL_QTY+("+inputInvQty+") "
										+ "where INV_INVENTORY_ID='"+fromInvID+"' and warehouse_code='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' and location_code='" + locationFrom + "' and container_code='" + containerFrom
										+ "' and item_code='" + itemCodeFrom.toString().trim() + "' and LOT_NO = '"+lotNo+"' ";
								DBOperator.DoUpdate(sql);
							}
						}else{
							//存在 更新目标库位库存数量    冻结数量
							String toInvID = dm2.getString("INV_INVENTORY_ID", 0);
							sql = "update inv_inventory set INACTIVE_QTY=INACTIVE_QTY+("+inputInvQty+"),"
									+ "INB_TOTAL_QTY=INB_TOTAL_QTY+("+inputInvQty+") "
									+ "where INV_INVENTORY_ID='"+toInvID+"' and warehouse_code='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' and location_code='" + locationTo + "' and container_code='" + containerTo
									+ "' and item_code='" + itemCodeFrom.toString().trim() + "' and LOT_NO = '"+lotNo+"' ";
							int insertCount = DBOperator.DoUpdate(sql);
							if(insertCount==0){
								Message.showErrorMessage("数据更新到目标库位失败");
								return;
							}else{
								//存在就扣减原库位数量
								sql = "update inv_inventory set ON_HAND_QTY=ON_HAND_QTY-("+inputInvQty+"),"
										+ "OUB_TOTAL_QTY=OUB_TOTAL_QTY+("+inputInvQty+") "
										+ "where INV_INVENTORY_ID='"+fromInvID+"' and warehouse_code='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' and location_code='" + locationFrom + "' and container_code='" + containerFrom
										+ "' and item_code='" + itemCodeFrom.toString().trim() + "' and LOT_NO = '"+lotNo+"' ";
								//放到后面执行
							}
						}
					}else{
						//先查询目标库位是否存在记录
						sql = "select ii.INV_INVENTORY_ID,ii.ON_HAND_QTY+ii.IN_TRANSIT_QTY-(ii.ALLOCATED_QTY)-(ii.PICKED_QTY) availableQty"
								+" from inv_inventory ii "
								+ "where ii.warehouse_code='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' and ii.location_code='" + locationTo + "' and ii.container_code='" + containerTo
								+ "' and ii.item_code='" + itemCodeFrom.toString().trim() + "' and ii.LOT_NO = '"+lotNo+"' ";
						DataManager dm2 = DBOperator.DoSelect2DM(sql);
						if(Math_SAM.str2Double(inputInvQty)>Math_SAM.str2Double(fromQty)){
							Message.showWarningMessage("数量超出原库存数量，不能操作");
							return;
						}
						//不存在就插入目标库位新记录
						if(dm2==null || dm2.getCurrentCount()==0){
							sql = "insert into inv_inventory(WAREHOUSE_CODE,STORER_CODE,ITEM_CODE,ITEM_NAME,INV_LOT_ID,LOT_NO,LOCATION_CODE"
									+ ",CONTAINER_CODE,ON_HAND_QTY,INB_TOTAL_QTY,CREATED_BY_USER,CREATED_DTM_LOC) "
									+"select WAREHOUSE_CODE,STORER_CODE,ITEM_CODE,ITEM_NAME,INV_LOT_ID,LOT_NO,'"+locationTo+"',"
									+"'"+containerTo+"',"+inputInvQty+","+inputInvQty+",'"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"',now() "
									+" from inv_inventory where warehouse_code='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' and location_code='" + locationFrom + "' and container_code='" + containerFrom
									+ "' and item_code='" + itemCodeFrom.toString().trim() + "' and LOT_NO = '"+lotNo+"' ";
							int insertCount = DBOperator.DoUpdate(sql);
							if(insertCount==0){
								Message.showErrorMessage("数据写入目标库位失败");
								return;
							}else{
								//存在就扣减原库位数量
								sql = "update inv_inventory set ON_HAND_QTY=ON_HAND_QTY-("+inputInvQty+"),"
										+ "OUB_TOTAL_QTY=OUB_TOTAL_QTY+("+inputInvQty+") "
										+ "where INV_INVENTORY_ID='"+fromInvID+"' and warehouse_code='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' and location_code='" + locationFrom + "' and container_code='" + containerFrom
										+ "' and item_code='" + itemCodeFrom.toString().trim() + "' and LOT_NO = '"+lotNo+"' ";
								//放到后面执行
							}
						}else{
							//存在 更新目标库位库存数量
							String toInvID = dm2.getString("INV_INVENTORY_ID", 0);
							sql = "update inv_inventory set ON_HAND_QTY=ON_HAND_QTY+("+inputInvQty+"),"
									+ "INB_TOTAL_QTY=INB_TOTAL_QTY+("+inputInvQty+") "
									+ "where INV_INVENTORY_ID='"+toInvID+"' and warehouse_code='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' and location_code='" + locationTo + "' and container_code='" + containerTo
									+ "' and item_code='" + itemCodeFrom.toString().trim() + "' and LOT_NO = '"+lotNo+"' ";
							int insertCount = DBOperator.DoUpdate(sql);
							if(insertCount==0){
								Message.showErrorMessage("数据更新到目标库位失败");
								return;
							}else{
								//存在就扣减原库位数量
								sql = "update inv_inventory set ON_HAND_QTY=ON_HAND_QTY-("+inputInvQty+"),"
										+ "OUB_TOTAL_QTY=OUB_TOTAL_QTY+("+inputInvQty+") "
										+ "where INV_INVENTORY_ID='"+fromInvID+"' and warehouse_code='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' and location_code='" + locationFrom + "' and container_code='" + containerFrom
										+ "' and item_code='" + itemCodeFrom.toString().trim() + "' and LOT_NO = '"+lotNo+"' ";
								//放到后面执行
							}
						}
					}
					
				} else if (rb2.isSelected() && !containerFrom.equals("") && !locationTo.equals("")) {
					if(isDamage){
						sql = "update inv_inventory set location_code='" + locationTo + "' " + ",INACTIVE_QTY=ifnull(ii.ON_HAND_QTY,0)-(ifnull(ii.ALLOCATED_QTY,0))-(ifnull(ii.PICKED_QTY,0))-(ifnull(INACTIVE_QTY,0)) "
								+" where warehouse_code='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' and location_code='"
								+ locationFrom + "' and container_code='" + containerFrom + "' ";
						//放到后面执行
					}else{
						sql = "update inv_inventory set location_code='" + locationTo + "' " + "where warehouse_code='"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"' and location_code='"
								+ locationFrom + "' and container_code='" + containerFrom + "' ";
						//放到后面执行
					}
					
				}else{
					return;
				}
				if(sql.equals("")){
					Message.showErrorMessage("请输入完整信息！");
					return;
				}
				//执行扣原库位库存
				int t = DBOperator.DoUpdate(sql);
				if(t>0){
					Message.showInfomationMessage("移库操作成功");
					//更新目标箱号状态status='2'
					if(!containerTo.equals("")){
						sql = "update bas_container set status='2' where container_code='"+containerTo+"' ";
						DBOperator.DoUpdate(sql);
					}
					//显示移库后的库存信息
					String strwhere ="";

					if(txt_warehouse_to.isEditable() && !warehouseTo.equals("")){
						strwhere = strwhere + " and ii.warehouse_code='"+warehouseTo+"' ";
					}
					if(txt_location_to.isEditable() && !locationTo.equals("")){
						strwhere = strwhere + " and ii.location_code='"+locationTo+"' ";
					}
					if(txt_container_to.isEditable() && !containerTo.equals("")){
						strwhere = strwhere + " and ii.container_code='"+containerTo+"' ";
					}

					if(txt_item_from.isEditable() && !itemCodeFrom.toString().equals("")){
						strwhere = strwhere + " and ii.item_code='"+itemCodeFrom.toString().trim()+"' ";
					}
					showInventory(strwhere);
					txt_warehouse_from.setText("");
					txt_location_from.setText("");
					txt_container_from.setText("");
					txt_item_from.setText("");
					txt_item_from.setToolTipText("");
					txt_warehouse_to.setText("");
					txt_location_to.setText("");
					txt_container_to.setText("");
					txt_inv_qty.setText("");
					//记录操作日志
					DataManager dmProcess = comData.getSysProcessHistoryDataModel("sys_process_history");
					if (dmProcess != null) {
						dmdata.xArrayList list = (xArrayList) dmProcess.getRow(0);
						list.set(dmProcess.getCol("SYS_PROCESS_HISTORY_ID"), "null");
						list.set(dmProcess.getCol("PROCESS_CODE"), "INV_MOVE_PC");
						list.set(dmProcess.getCol("PROCESS_NAME"), "移库");
						list.set(dmProcess.getCol("STORER_CODE"), "");
						list.set(dmProcess.getCol("WAREHOUSE_CODE"),txt_warehouse_from.getText());
						list.set(dmProcess.getCol("TO_WAREHOUSE_CODE"),txt_warehouse_to.getText());
						list.set(dmProcess.getCol("FROM_LOCATION_CODE"), txt_location_from.getText());
						list.set(dmProcess.getCol("TO_LOCATION_CODE"), txt_location_to.getText());
						list.set(dmProcess.getCol("FROM_CONTAINER_CODE"), txt_container_from.getText());
						list.set(dmProcess.getCol("TO_CONTAINER_CODE"), txt_container_to.getText());
						list.set(dmProcess.getCol("ITEM_CODE"), itemCodeFrom);
						list.set(dmProcess.getCol("QTY"), txt_item_from.getText());
						list.set(dmProcess.getCol("REFERENCE_NO"), "");
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
				}else{
					Message.showErrorMessage("移库操作失败，请输入完整信息！");
				}
			}
		});
		panel_2_to.add(btnOK);
		
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
		panel_2_to.add(btnClose);
		
		JPanel detailPanel_2 = new JPanel();
		panel_2.add(detailPanel_2, BorderLayout.CENTER);
		detailPanel_2.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		detailPanel_2.add(scrollPane_1, BorderLayout.CENTER);
		
		table = new PBSUIBaseGrid();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()>=2){
					table.setColumnSelectionAllowed(true);
				}
			}
		});
		scrollPane_1.setViewportView(table);
		
		JPanel panel_1 = new JPanel();
		//隐藏这个TAB
//		tabbedPane.addTab("\u4ED3\u5185\u79FB\u5E93", null, panel_1, null);
//		panel_1.setLayout(new BorderLayout(0, 0));
		
		JPanel headerPanel_1 = new JPanel();
		panel_1.add(headerPanel_1, BorderLayout.NORTH);
		headerPanel_1.setLayout(new GridLayout(2, 1, 0, 0));
		
		JPanel panel_1_from = new JPanel();
		FlowLayout fl_panel_1_from = (FlowLayout) panel_1_from.getLayout();
		fl_panel_1_from.setAlignment(FlowLayout.LEFT);
		headerPanel_1.add(panel_1_from);
		
		JLabel lblNewLabel = new JLabel("\u539F\u5E93\u4F4D\u53F7\uFF1A");
		panel_1_from.add(lblNewLabel);
		
		textField = new JTextField();
		panel_1_from.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("\u7BB1\u53F7\uFF1A");
		panel_1_from.add(lblNewLabel_1);
		
		textField_1 = new JTextField();
		panel_1_from.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("\u6761\u7801\uFF1A");
		panel_1_from.add(lblNewLabel_2);
		
		textField_2 = new JTextField();
		panel_1_from.add(textField_2);
		textField_2.setColumns(10);
		
		JPanel panel_1_to = new JPanel();
		FlowLayout fl_panel_1_to = (FlowLayout) panel_1_to.getLayout();
		fl_panel_1_to.setAlignment(FlowLayout.LEFT);
		headerPanel_1.add(panel_1_to);
		
		JLabel lblNewLabel_3 = new JLabel("\u76EE\u6807\u5E93\u4F4D\uFF1A");
		panel_1_to.add(lblNewLabel_3);
		
		textField_3 = new JTextField();
		panel_1_to.add(textField_3);
		textField_3.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel("\u7BB1\u53F7\uFF1A");
		panel_1_to.add(lblNewLabel_4);
		
		textField_4 = new JTextField();
		panel_1_to.add(textField_4);
		textField_4.setColumns(10);
		
		JPanel detailPanel_1 = new JPanel();
		panel_1.add(detailPanel_1, BorderLayout.CENTER);
		detailPanel_1.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		detailPanel_1.add(scrollPane, BorderLayout.CENTER);
		
		table1 = new PBSUIBaseGrid();
		scrollPane.setViewportView(table1);
		
		rb1.setSelected(true);
	}
	
	private void showInventory(String strwhere){
		String sql = "select ii.WAREHOUSE_CODE 仓库编码,bw.WAREHOUSE_NAME 仓库名称,ii.STORER_CODE 货主编码,bs.STORER_NAME 货主名称,ii.LOCATION_CODE 库位编码,ii.CONTAINER_CODE 箱号,"
				+"ii.ITEM_CODE 商品编码,bi.ITEM_BAR_CODE 商品条码,bi.ITEM_NAME 商品名称,ii.ON_HAND_QTY+ii.IN_TRANSIT_QTY-(ii.ALLOCATED_QTY)-(ii.PICKED_QTY) 可用库存,ii.ON_HAND_QTY 库存总数,ii.ALLOCATED_QTY 分配数量,ii.IN_TRANSIT_QTY 冻结数量,biu.unit_name 单位"
				+",ii.lot_no 批次号,il.LOTTABLE01 批次属性1,il.LOTTABLE02 批次属性2,il.LOTTABLE03 批次属性3,il.LOTTABLE04 批次属性4,il.LOTTABLE05 批次属性5,"
				+"il.LOTTABLE06 批次属性6,il.LOTTABLE07 批次属性7,il.LOTTABLE08 批次属性8,il.LOTTABLE09 批次属性9,il.LOTTABLE10 批次属性10 "
				+",ii.`STATUS` 库存状态,ii.QUALITY 库存质量,ii.INV_INVENTORY_ID 库存ID "
				+"from inv_inventory ii "
				+"inner join bas_location bl on ii.LOCATION_CODE=bl.LOCATION_CODE and ii.WAREHOUSE_CODE=bl.WAREHOUSE_CODE "
				+"inner join bas_item bi on ii.ITEM_CODE=bi.ITEM_CODE and ii.STORER_CODE=bi.STORER_CODE "
				+"left join bas_item_unit biu on bi.UNIT_CODE = biu.unit_code "
				+"inner join inv_lot il on ii.LOT_NO=il.LOT_NO "
				+"inner join bas_storer bs on ii.STORER_CODE=bs.STORER_CODE "
				+"inner join bas_warehouse bw on ii.WAREHOUSE_CODE=bw.WAREHOUSE_CODE "
				+"where 1=1 ";
		if(!strwhere.equals("")){
			sql = sql + strwhere;
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
		table.updateUI();
	}

	private void setExtendedState(int maximizedBoth) {
		// TODO Auto-generated method stub
		
	}

}
