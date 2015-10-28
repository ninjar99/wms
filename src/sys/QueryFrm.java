package sys;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import bas.storerMasterFrm;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.FlowLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import javax.swing.JList;
import javax.swing.JScrollPane;

public class QueryFrm extends JFrame {

	private static QueryFrm instance;
	private static boolean isOpen = false;
	private JPanel contentPane;
	private JTextField txt_query_value;
	public static String queryValueResult="";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ArrayList fieldList = new ArrayList();
					fieldList.add("storer_code:货主编码");
					fieldList.add("storer_name:货主名称");
					fieldList.add("is_active:是否启用");
					QueryFrm frame = new QueryFrm(fieldList);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static synchronized QueryFrm getInstance(ArrayList fieldList) {
		 if(instance == null) {    
	            instance = new QueryFrm(fieldList);  
	        }  
	        return instance;
		 
	 }
	
	public static synchronized boolean getOpenStatus(ArrayList fieldList) {
		 if(instance == null) {    
	            instance = new QueryFrm(fieldList);  
	        }  
	        return isOpen;
		 
	 }

	/**
	 * Create the frame.
	 */
	public QueryFrm(ArrayList fieldList) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 630, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel, BorderLayout.NORTH);
		
		JComboBox cb_left = new JComboBox();
		cb_left.setModel(new DefaultComboBoxModel(new String[] {"", "("}));
		panel.add(cb_left);
		
		JLabel lblNewLabel = new JLabel("\u6761\u4EF6\u8FDE\u63A5\uFF1A");
		panel.add(lblNewLabel);
		
		JComboBox cb_query_condition = new JComboBox();
		cb_query_condition.setModel(new DefaultComboBoxModel(new String[] {"", "\u5E76\u4E14", "\u6216\u8005"}));
		panel.add(cb_query_condition);
		
		JLabel lblNewLabel_1 = new JLabel("\u67E5\u8BE2\u5B57\u6BB5\uFF1A");
		panel.add(lblNewLabel_1);
		
		JComboBox cb_query_field = new JComboBox();
		cb_query_field.removeAllItems();
		for(int i=0;i<fieldList.size();i++){
			cb_query_field.addItem(fieldList.get(i).toString().split(":")[1]);
		}
		panel.add(cb_query_field);
		
		JComboBox cb_query_operate = new JComboBox();
		cb_query_operate.setModel(new DefaultComboBoxModel(new String[] {"\u7B49\u4E8E", "\u5C0F\u4E8E", "\u5C0F\u4E8E\u7B49\u4E8E", "\u5927\u4E8E", "\u5927\u4E8E\u7B49\u4E8E", "\u5305\u542B", "\u4E0D\u5305\u542B"}));
		panel.add(cb_query_operate);
		
		JLabel lblNewLabel_2 = new JLabel("\u67E5\u8BE2\u503C\uFF1A");
		panel.add(lblNewLabel_2);
		
		txt_query_value = new JTextField();
		panel.add(txt_query_value);
		txt_query_value.setColumns(10);
		
		JComboBox cb_right = new JComboBox();
		cb_right.setModel(new DefaultComboBoxModel(new String[] {"", ")"}));
		panel.add(cb_right);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel_2.add(scrollPane, BorderLayout.CENTER);
		
		JTextArea query_list = new JTextArea();
		scrollPane.setViewportView(query_list);
		
		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3, BorderLayout.EAST);
		
		JButton btnNewButton = new JButton("\u6DFB\u52A0");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				query_list.setText((query_list.getText().equals("")?"":query_list.getText()+"\n")+cb_left.getSelectedItem().toString() + ""
						+ convert_query_condition(cb_query_condition.getSelectedItem().toString()) +" "
						+ fieldList.get(cb_query_field.getSelectedIndex()).toString().split(":")[0] + " "
						+ convert_query_field(cb_query_operate.getSelectedItem().toString()) + " " + txt_query_value.getText() + " "
						+ cb_right.getSelectedItem().toString());
				cb_left.setSelectedIndex(0);
				cb_right.setSelectedIndex(0);
			}
		});
		panel_3.setLayout(new GridLayout(0, 1, 0, 0));
		panel_3.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("\u5220\u9664");
		panel_3.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("\u91CD\u7F6E");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				query_list.setText("");
			}
		});
		panel_3.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("\u67E5\u8BE2");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				queryValueResult = query_list.getText();
				instance.dispose();
			}
		});
		panel_3.add(btnNewButton_3);
		
		JButton btnNewButton_4 = new JButton("\u53D6\u6D88");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//
				instance.dispose();
			}
		});
		panel_3.add(btnNewButton_4);
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
