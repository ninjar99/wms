package comUtil;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;

import DBUtil.DBConnectionManager;
import util.JAutoCompleteComboBox;

public class WMSCombobox extends JComboBox {

	private static final long serialVersionUID = -4691802828891246384L;

	private boolean flag;
	private ConcurrentHashMap<String, String> hm_name_OID;
	private ConcurrentHashMap<String, String> hm_OID_name;
	private Vector<String> items;
	WMSCombobox self;
	private String sql;

	public WMSCombobox(String sql, boolean flag) {
		super();
		this.flag = flag;
		this.sql = sql;
		hm_name_OID = new ConcurrentHashMap<String, String>();
		hm_OID_name = new ConcurrentHashMap<String, String>();
		items = new Vector<String>();
		init();
	}

	void init() {
		if (flag) {
			this.addItem("");
		}
		try {
			java.sql.Connection con = DBConnectionManager.getInstance().getConnection("wms");
			java.sql.Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				items.add(NulltoSpace(rs.getString(1)));
				hm_name_OID.put(NulltoSpace(rs.getString(2)), NulltoSpace(rs.getString(1)));
				hm_OID_name.put(NulltoSpace(rs.getString(1)), NulltoSpace(rs.getString(2)));
				this.addItem(NulltoSpace(rs.getString(2)));
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

	public void refresh() {
		self.removeAllItems();
		hm_name_OID.clear();
		hm_OID_name.clear();
		if (flag) {
			self.addItem("");
		}
		try {
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
	    WMSCombobox cmb = new WMSCombobox("SELECT COUNTRY_CODE,country_name FROM bas_country",true);
	    cmb.setEditable(true);
	    cmb.setSelectedIndex(-1);
	    JTextField text = new JTextField();
	    frame.getContentPane().add(cmb);
	    frame.getContentPane().add(text);
	    frame.setSize(400, 80);
	    frame.setVisible(true);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  }
}
