package sys;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import DBUtil.DBConnectionManager;
import inbound.POFrm;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.WindowConstants;

import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;

public class testFrm extends InnerFrame {

	private JPanel contentPane;
	private JTable table;
	private static volatile testFrm instance = null;
	private static boolean isOpen = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					testFrm frame = new testFrm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	 public static testFrm getInstance() {
		 if(instance == null) { 
			 synchronized(testFrm.class){
				 if(instance == null) {
					 instance = new testFrm();
				 }
			 }
	            try {
					instance.setMaximum(true);
				} catch (PropertyVetoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
	        }  
	        return instance;
		 
	 }
	 
	 public static synchronized boolean getOpenStatus() {
		 if(instance == null) {    
	            instance = new testFrm();  
	        }  
	        return isOpen;
		 
	 }
	 

	/**
	 * Create the frame.
	 */
	public testFrm() {
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
					System.out.println("testFrm窗口被关闭");
					instance = null;
					isOpen = false;
				}
			}
		});
		
		this.addComponentListener(new ComponentAdapter(){
			@Override public void componentResized(ComponentEvent e){
			    // write you code here
				isOpen = true;
			}});
					
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		maximizable = true;
		closable = true;
		setContentPane(contentPane);
		
		JPanel topPanel = new JPanel();
		contentPane.add(topPanel, BorderLayout.CENTER);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "test");
			}
		});
		topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		topPanel.add(btnNewButton);
		
		JSpinner spinner = new JSpinner();
		topPanel.add(spinner);
		
		table = new JTable();
		topPanel.add(table);
		
		JButton btnDbTest = new JButton("DB TEST");
		btnDbTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String sql = "select * from inb_po_header";
				try{
                java.sql.Connection con = DBConnectionManager.getInstance().getConnection("wms");
                java.sql.Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                	System.out.println(rs.getString(1));
                	JOptionPane.showMessageDialog(null, "test db successful!");
                }
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		topPanel.add(btnDbTest);
		
		JButton btnNewButton_1 = new JButton("close");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					instance.setClosed(true);
				} catch (PropertyVetoException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		topPanel.add(btnNewButton_1);
	}

	private void setExtendedState(int maximizedBoth) {
		// TODO Auto-generated method stub
		
	}
}