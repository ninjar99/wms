package comUtil;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import DBUtil.DBConnectionManager;

@SuppressWarnings("rawtypes")
public class WMSCombobox extends JComboBox {

	private static final long serialVersionUID = -4691802828891246384L;

	private boolean flag;
	public ConcurrentHashMap<String, String> hm_name_OID;
	public ConcurrentHashMap<String, String> hm_OID_name;
	private Vector<String> items;
	public WMSCombobox self;
	private String sql;
	private static WMSCombobox cmb;

	public WMSCombobox(String sql, boolean flag) {
		super();
		this.flag = flag;
		this.sql = sql;
		hm_name_OID = new ConcurrentHashMap<String, String>();
		hm_OID_name = new ConcurrentHashMap<String, String>();
		items = new Vector<String>();
		self = new WMSCombobox();
		init();
	}

	public WMSCombobox() {
		super();
	}

	@SuppressWarnings("unchecked")
	void init() {
		if (flag) {
			this.addItem("");
		}
		try {
			System.out.println(sql);
			java.sql.Connection con = DBConnectionManager.getInstance().getConnection("wms");
			java.sql.Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				items.add(NulltoSpace(rs.getString(1)));
				hm_name_OID.put(NulltoSpace(rs.getString(2)), NulltoSpace(rs.getString(1)));
				hm_OID_name.put(NulltoSpace(rs.getString(1)), NulltoSpace(rs.getString(2)));
				this.addItem(NulltoSpace(rs.getString(2)));
				self.addItem(NulltoSpace(rs.getString(2)));
			}
			self = this;
			DBConnectionManager.getInstance().freeConnection("wms", con);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.updateUI();
//		this.getEditor().getEditorComponent().addFocusListener(new FocusAdapter() {
//			@Override
//			public void focusLost(FocusEvent arg0) {
//				boolean find = false;
//	            String s = self.getEditor().getItem().toString();  
//	            ComboBoxModel showModel = self.getModel();
//	            for (int i = 0; i < showModel.getSize(); i++) {  
//                    Object element = showModel.getElementAt(i);  
//                    if (element.toString().indexOf(s)>=0) { 
//                    	find = true;
//                    	showModel.setSelectedItem(element);
//                    	self.showPopup();
//                        break;  
//                    }  
//                } 
//	            if(!find){
//	            	showModel.setSelectedItem("");
//	            }
//			}
//		});
		this.getEditor().getEditorComponent().addKeyListener(new KeyAdapter(){  
		    public void keyPressed(KeyEvent e) {
		        if(e.getKeyCode() != KeyEvent.VK_BACK_SPACE){ 
		        	boolean find = false;
		            String s = self.getEditor().getItem().toString();  
		            ComboBoxModel showModel = self.getModel();
		            for (int i = 0; i < showModel.getSize(); i++) {  
	                    Object element = showModel.getElementAt(i);  
	                    if (element.toString().indexOf(s)>=0) { 
	                    	find = true;
	                    	showModel.setSelectedItem(element);
	                    	self.showPopup();
	                        break;  
	                    }  
	                } 
		            if(!find){
		            	showModel.setSelectedItem("");
		            }
		        }
		    }  
		}); 
	}

	@SuppressWarnings("unchecked")
	public void refresh() {
		self.removeAllItems();
		hm_name_OID.clear();
		hm_OID_name.clear();
		if (flag) {
			self.addItem("");
		}
		try {
			System.out.println(sql);
			java.sql.Connection con = DBConnectionManager.getInstance().getConnection("wms");
			java.sql.Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				hm_name_OID.put(NulltoSpace(rs.getString(2)), NulltoSpace(rs.getString(1)));
				hm_OID_name.put(NulltoSpace(rs.getString(1)), NulltoSpace(rs.getString(2)));
				this.addItem(NulltoSpace(rs.getString(2)));

			}
			DBConnectionManager.getInstance().freeConnection("wms", con);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.updateUI();
		self.updateUI();
		cmb.updateUI();
	}

	public String getSelectedOID() {
		String displayOID = NulltoSpace(this.getSelectedItem());
		if (displayOID != null && !displayOID.equals("")) {
			return (String) hm_name_OID.get(displayOID);
		} else {
			return "";
		}
	}

	public void setSelectedDisplayName(String OID) {
		if (OID != null && !OID.equals("")) {
			this.setSelectedItem(hm_OID_name.get(OID));
		} else {
			this.setSelectedItem("");
		}
	}

	public Object[] getAllItems() {
		Object[] s = items.toArray();
		return s;
	}

	public static String NulltoSpace(Object o) {
		if (o == null)
			return "";
		else if (o.equals("null")) {
			return "";
		} else
			return o.toString().trim();
	}
	
	public static void main(String[] args) {
	    JFrame frame = new JFrame();
	    JPanel contentPane;
	    cmb = new WMSCombobox("SELECT COUNTRY_CODE,country_name FROM bas_country ",true);
	    cmb.setEditable(true);
	    cmb.setSelectedIndex(-1);
	    Object[] obj = cmb.getAllItems();
		System.out.println(obj.length);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		frame.setContentPane(contentPane);
		
		JButton btnNewButton = new JButton("New button");
		contentPane.add(btnNewButton, BorderLayout.NORTH);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmb.removeAllItems();
				cmb = new WMSCombobox("SELECT COUNTRY_CODE,country_name FROM bas_country where country_name like '%жа%' ",true);
				Object[] obj = cmb.getAllItems();
				System.out.println(obj.length);
				cmb.updateUI();
				cmb.repaint();
				contentPane.remove(cmb);
//				contentPane.updateUI();
//				contentPane.repaint();
				frame.repaint();
				frame.pack();

				contentPane.add(cmb.self, BorderLayout.SOUTH);
//				contentPane.updateUI();
//				contentPane.repaint();
				frame.pack();
			}
		});
		contentPane.add(cmb, BorderLayout.SOUTH);
		frame.show();
	  }
}
