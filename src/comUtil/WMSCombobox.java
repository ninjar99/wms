package comUtil;

import java.sql.ResultSet;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JComboBox;

import DBUtil.DBConnectionManager;

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
			DBConnectionManager.getInstance().freeConnection("wms", con);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.updateUI();

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
}
