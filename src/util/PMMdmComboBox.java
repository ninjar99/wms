package util;

import javax.swing.*;
import java.util.Vector;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PMMdmComboBox extends JComboBox<Object> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7465510028335876690L;
	String[] desc;
	boolean isNeedName = false;
	@SuppressWarnings("rawtypes")
	Vector[] defaultValue;

	public void setisNeedName(boolean isneed) {
		this.isNeedName = isneed;
	}

	public boolean getisNeedName() {
		return this.isNeedName;
	}

	public void setDesc(String[] descs) {
		if (descs != null) {
			this.desc = descs;
		}
	}

	public String[] getDesc() {
		return this.desc;
	}

	public boolean hasValue(Object value) {
		if (((DefaultComboBoxModel<?>) this.getModel()).getIndexOf(value) == -1) {
			return false;
		}
		return true;
	}

	public void insertItemWithDesc(Object insertedItem, String Inserteddesc, int position) {
		if (position >= this.getItemCount()) {
			return;
		}
		if (this.getItemCount() == 1 && this.getItemAt(0).equals(insertedItem)) {
			return;
		}
		try {
			String[] olddesc = this.getDesc();
			String[] newdesc = new String[olddesc.length + 1];
			System.arraycopy(olddesc, 0, newdesc, 0, position);
			newdesc[position] = Inserteddesc;
			System.arraycopy(olddesc, position, newdesc, position + 1, olddesc.length - position);
			this.insertItemAt(insertedItem, position);
			this.setDesc(newdesc);
		} catch (Exception ex) {
		}
	}

	// translate parm
	public PMMdmComboBox() {
		super();
		this.setRenderer(new MyCellRenderer(this));
		System.out.println();
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PMMdmComboBox(Object[][] alldata, int col) {
		for (int i = 0; i < alldata.length; i++) {
			this.addItem(alldata[i][col]);
		}
		this.setRenderer(new MyCellRenderer(this));
	}

	public static void main(String[] args) {
		JFrame testFrame = new JFrame();
		BorderLayout borderLayout1 = new BorderLayout();
		testFrame.setSize(new Dimension(300, 200));
		PMMdmComboBox dmComboBox1 = new PMMdmComboBox();
		// testFrame.setLayout(borderLayout1);
		JButton b = new JButton("veiw Value-Name");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(dmComboBox1.desc[dmComboBox1.getSelectedIndex()]);
				System.out.println(dmComboBox1.getDescByValue(dmComboBox1.getSelectedItem()));
			}
		});
		
		// dmComboBox1.setRenderer(new DefaultListCellRenderer());
		dmComboBox1.desc = new String[3];
		dmComboBox1.addItem("1");
		dmComboBox1.desc[0] = "name-1";
		dmComboBox1.addItem("2");
		dmComboBox1.desc[1] = "name-2";
		dmComboBox1.addItem("3");
		dmComboBox1.desc[2] = "name-3";
		dmComboBox1.setSelectedItem("2");// Not name-2
		testFrame.getContentPane().add(b, BorderLayout.SOUTH);
		testFrame.getContentPane().add(dmComboBox1, BorderLayout.NORTH);
		testFrame.show();
	}

	public void setDefaultValue(Vector[] vec) {
		this.defaultValue = vec;
	}

	public Vector[] getAllDefaultValue() {
		if (defaultValue != null) {
			return defaultValue;
		}
		return null;
	}

	public String[] getAllItems() {
		if (this.getItemCount() == 0) {
			return null;
		}
		String[] allitems = new String[getItemCount()];
		for (int i = 0; i < (getItemCount()); i++) {
			allitems[i] = getItemAt(i).toString();
		}
		return allitems;
	}

	// change by steven change between somestore
	public String[] getAllItemssteven() {
		if (this.getItemCount() == 0) {
			return null;
		}
		String[] allitems = new String[getItemCount()];
		for (int i = 0; i <= (getItemCount() - 1); i++) {
			allitems[i] = getItemAt(i).toString();
		}
		return allitems;
	}

	private String[] getAlphabet() {
		return new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
				"S", "T", "U", "V", "W", "X", "Y", "Z" };
	}

	Vector<?> getDefaultValueByValue(Object value) {
		String[] all = this.getAllItems();
		try {
			for (int i = 0; i < all.length; i++) {
				if (value.equals(all[i]) && this.defaultValue != null) {

					// if desc or name is blank , then it return the id , but
					// not desc
					return this.defaultValue[i];
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
		return null;
	}

	public String getDescByValue(Object value) {
		String[] all = this.getAllItems();
		try {
			for (int i = 0; i < all.length; i++) {
				if (value.equals(all[i]) && this.desc != null && this.desc.length > 0) {

					// if desc or name is blank , then it return the id , but
					// not desc
					if (this.desc[i] != "") {
						return this.desc[i];
					}
				}
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	private void jbInit() throws Exception {
	}

}
