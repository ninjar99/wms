package sys;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class QueryDialog extends JDialog {

	private static QueryDialog instance;
	private final JPanel contentPanel = new JPanel();
	private JTextField txt_query_value;
	private JTextArea query_list;
	private JComboBox cb_left;
	private JComboBox cb_query_condition;
	private JComboBox cb_query_field;
	private JComboBox cb_query_operate;
	private JComboBox cb_right;
	private JButton btnNewButton;
	public static String queryValueResult="";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ArrayList fieldList = new ArrayList();
			fieldList.add("storer_code:货主编码");
			fieldList.add("storer_name:货主名称");
			fieldList.add("is_active:是否启用");
			QueryDialog dialog = QueryDialog.getInstance(fieldList);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static synchronized QueryDialog getInstance(ArrayList fieldList) {
//		 if(instance == null) {    
//	            instance = new QueryDialog(fieldList);
//	            instance.setModal(true);
//	        } 
		instance = new QueryDialog(fieldList);
        instance.setModal(true);
	        return instance;
		 
	 }

	/**
	 * Create the dialog.
	 */
	public QueryDialog(ArrayList fieldList) {
		setBounds(100, 100, 682, 314);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panel.getLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			contentPanel.add(panel, BorderLayout.NORTH);
			{
				cb_left = new JComboBox();
				cb_left.setModel(new DefaultComboBoxModel(new String[] {"", "("}));
				panel.add(cb_left);
			}
			{
				JLabel label = new JLabel("\u6761\u4EF6\u8FDE\u63A5\uFF1A");
				panel.add(label);
			}
			{
				cb_query_condition = new JComboBox();
				cb_query_condition.setModel(new DefaultComboBoxModel(new String[] {"", "\u5E76\u4E14", "\u6216\u8005"}));
				panel.add(cb_query_condition);
			}
			{
				JLabel label = new JLabel("\u67E5\u8BE2\u5B57\u6BB5\uFF1A");
				panel.add(label);
			}
			{
				cb_query_field = new JComboBox();
				cb_query_field.removeAllItems();
				for(int i=0;i<fieldList.size();i++){
					cb_query_field.addItem(fieldList.get(i).toString().split(":")[1]);
				}
				panel.add(cb_query_field);
			}
			{
				cb_query_operate = new JComboBox();
				cb_query_operate.setModel(new DefaultComboBoxModel(new String[] {"\u7B49\u4E8E", "\u5C0F\u4E8E", "\u5C0F\u4E8E\u7B49\u4E8E", "\u5927\u4E8E", "\u5927\u4E8E\u7B49\u4E8E", "\u5305\u542B", "\u4E0D\u5305\u542B"}));
				panel.add(cb_query_operate);
			}
			{
				JLabel label = new JLabel("\u67E5\u8BE2\u503C\uFF1A");
				panel.add(label);
			}
			{
				txt_query_value = new JTextField();
				txt_query_value.addFocusListener(new FocusAdapter() {
					@Override
					public void focusLost(FocusEvent arg0) {
						if(txt_query_value.getText().trim().length()>1){
							btnNewButton.doClick();
						}
					}
				});
				txt_query_value.addKeyListener(new KeyAdapter() {
					@Override
					public void keyReleased(KeyEvent e) {
						if (e.getKeyChar() == '\n') {
							btnNewButton.doClick();
						}
					}
				});
				txt_query_value.setColumns(10);
				panel.add(txt_query_value);
			}
			{
				cb_right = new JComboBox();
				cb_right.setModel(new DefaultComboBoxModel(new String[] {"", ")"}));
				panel.add(cb_right);
			}
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.CENTER);
			panel.setLayout(new BorderLayout(0, 0));
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1, BorderLayout.CENTER);
				panel_1.setLayout(new BorderLayout(0, 0));
				{
					JScrollPane scrollPane = new JScrollPane();
					panel_1.add(scrollPane, BorderLayout.CENTER);
					{
						query_list = new JTextArea();
						scrollPane.setViewportView(query_list);
					}
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1, BorderLayout.EAST);
				panel_1.setLayout(new GridLayout(0, 1, 0, 0));
				{
					btnNewButton = new JButton("\u6DFB\u52A0");
					btnNewButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							if(query_list.getText().length()>0 && cb_query_condition.getSelectedItem().toString().equals("")){
								cb_query_condition.setSelectedIndex(2);
//								if(cb_query_condition.getSelectedItem().toString().equals("")){
//									JOptionPane.showMessageDialog(null, "请选择连接条件");
//									cb_query_condition.setFocusable(true);
//									return;
//								}
							}
							//拼接SQL where 查询条件
							query_list.setText((query_list.getText().equals("")?"":query_list.getText()+"\n")+cb_left.getSelectedItem().toString() + ""
									+ convert_query_condition(cb_query_condition.getSelectedItem().toString()) +" "
									+ fieldList.get(cb_query_field.getSelectedIndex()).toString().split(":")[0] + " "
									+ convert_query_field(cb_query_operate.getSelectedItem().toString()) + " "
									+ (convert_query_field(cb_query_operate.getSelectedItem().toString()).endsWith("like")?" '%" + txt_query_value.getText() + "%' ":" '" + txt_query_value.getText() + "' ")+" "
									+ cb_right.getSelectedItem().toString());
							cb_left.setSelectedIndex(0);
							cb_right.setSelectedIndex(0);
							txt_query_value.setText("");
						}
					});
					panel_1.add(btnNewButton);
				}
				{
					JButton btnClearButton = new JButton("\u91CD\u7F6E");
					btnClearButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							queryValueResult = "";
							query_list.setText("");
						}
					});
					panel_1.add(btnClearButton);
				}
				{
					JButton btnQueryButton = new JButton("\u67E5\u8BE2");
					btnQueryButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							queryValueResult = query_list.getText();
							query_list.setText("");
							txt_query_value.setText("");
							instance.dispose();
							
						}
					});
					panel_1.add(btnQueryButton);
				}
				{
					JButton btnCloseButton = new JButton("\u53D6\u6D88");
					btnCloseButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							queryValueResult = "";
							query_list.setText("");
							txt_query_value.setText("");
							instance.dispose();
						}
					});
					panel_1.add(btnCloseButton);
				}
			}
		}
	}
	
	private String convert_query_field(String field){
		String ret = "";
		if(field.equals("等于")){
			ret = "=";
		}else if(field.equals("小于")){
			ret = "<";
		}else if(field.equals("小于等于")){
			ret = "<=";
		}else if(field.equals("大于")){
			ret = ">";
		}else if(field.equals("大于等于")){
			ret = ">=";
		}else if(field.equals("包含")){
			ret = "like";
		}else if(field.equals("不包含")){
			ret = "not like";
		}
		return ret;
	}
	
	private String convert_query_condition(String field){
		String ret = "";
		if(field.equals("并且")){
			ret = "and";
		}else if(field.equals("或者")){
			ret = "or";
		}
		return ret;
	}

}
