package main;

import javax.swing.JTable;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;
import javax.swing.DefaultCellEditor;
import java.util.Vector;
import javax.swing.table.TableModel;
import java.util.Date;
import java.util.EventObject;

import javax.swing.event.TableModelEvent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.table.TableColumnModel;
import dmdata.DataManager;
import java.awt.Color;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class PBSUIBaseGrid extends JTable {
	private final static int SUCCESS = 0;
	private final static int NULL_TITLE = 1;
	private final static int NULL_CELLEDITOR = 2;
	private final static int INVALID_CELLEDITOR = 3;
	private final static int INVALID_CELLEDITOR_NUMBER = 4;
	private final static int INVALID_DATA = 5;
	private final static int NULL_STRUCTURE = 6;
	private final static int INVALID_ROW = 7;
	private final static int INVALID_COLUMN = 8;
	private final static int INVALID_COLUMN_WIDTH = 9;
	private final static int INVALID_ALIGNMENT = 10;
	private final static int INVALID_EDITSTATUS = 11;
	private int indexes[];
	private Vector sortingColumns = new Vector();
	private boolean ascending = true;
	private int compares;
	public String frameName = new String("GivemeAMoney");
	private boolean canSort = true;
	private boolean[] editable;
	private boolean isChanged = false;
	public int[] keyColumn;
	public int[] unableEditedColumn;
	public Vector dateColumn = new Vector();
	public Vector doubleColumn = new Vector();
	public int[] double4Column;
	public int[] double5Column;
	public int[] passwordColumn;
	public Vector integerColumn = new Vector();
	public int[] fixedColumn;
	public int lastUserColumn = -1;
	public int lastModiDateColumn = -1;
	public int showLineColumn = -1;
	public int Keylength = -1;
	public char[] keychar = null;
	public JPanel itemgridPanel;
	public String[] allNumber;
	public int startSelect = -1;
	public boolean isReadonly = false;
	public PBSUIBaseGrid stateGrid;
	public boolean isNeedScrol = false;
	public boolean canlowkey = false;

	public Vector allComps = new Vector();
	private RowRenderer cellRenderer[];

	public void setcanlowkey(boolean canlowkey) {
		this.canlowkey = canlowkey;
	}

	public void setisNeedScrol(boolean isneed) {
		this.isNeedScrol = isneed;
	}

	public void setisReadonly(boolean readonly) {
		this.isReadonly = readonly;
	}

	public void setdouble4Column(int[] double4column) {
		this.double4Column = double4column;
	}

	public void setdouble5Column(int[] double5column) {
		this.double5Column = double5column;
	}

	public void setpasswordColumn(int[] passwordColumn) {
		this.passwordColumn = passwordColumn;
	}

	public void setfixedColumn(int[] fixcolumn) {
		this.fixedColumn = fixcolumn;
	}

	public void setStateGrid(PBSUIBaseGrid statetable) {
		this.stateGrid = statetable;
	}

	public void setFrameName(String theframeName) {
		frameName = theframeName;
	}

	public void setitemGridPanel(JPanel givePanel) {
		this.itemgridPanel = givePanel;
	}

	public void setallNumber(String[] allNum) {
		this.allNumber = allNum;
	}

	public void setintegerColumn(Vector integercolumn) {
		this.integerColumn = integercolumn;
	}

	public void setkeyColumn(int[] keycolumn) {
		this.keyColumn = keycolumn;
		((thisModel) this.getModel()).setkeyCol(keycolumn);
	}

	public void setkeyChar(char[] keychar) {
		this.keychar = keychar;
	}

	public void setkeyLegth(int keyLegth) {
		this.Keylength = keyLegth;
	}

	public void setdoubleColumn(Vector doublecolumn) {
		this.doubleColumn = doublecolumn;
	}

	public void setallComps(Vector alComps) {
		this.allComps = alComps;
		System.out.println();
	}

	public void setunableEditedColumn(int[] unableedited) {
		this.unableEditedColumn = unableedited;
	}

	public void setdateColumn(Vector datecolumn) {
		this.dateColumn = datecolumn;
	}

	public PBSUIBaseGrid() {
		super(new thisModel());
		indexes = new int[0];
		reallocateIndexes();
		this.addMouseListenerToHeaderInTable(this);
		setTitleAlignment(SwingConstants.CENTER);
		setIsChanged(false);
	}

	public PBSUIBaseGrid(DefaultTableModel tablemodel) {
		super(tablemodel);
		indexes = new int[0];
		reallocateIndexes();
		this.addMouseListenerToHeaderInTable(this);
		setTitleAlignment(SwingConstants.CENTER);
		setIsChanged(false);
	}

	public void setSortable(boolean sort) {
		this.canSort = sort;
		if (!canSort) {
//			JTableHeader header = new JTableHeader();//this.getTableHeader();
//			DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
//			for (int i = 0; i < this.getColumnCount(); i++) {
//				this.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
//			}
		}

	}

	public int setColumn(Object[] TitleName) {
		if (TitleName == null || TitleName.length == 0)
			return NULL_TITLE;

		editable = new boolean[TitleName.length];
		cellRenderer = new RowRenderer[TitleName.length];
		DefaultTableModel tm = (DefaultTableModel) this.getModel();
		for (int i = 0; i < TitleName.length; i++) {
			tm.addColumn(TitleName[i]);
			editable[i] = true;
			cellRenderer[i] = new RowRenderer();
			cellRenderer[i].setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		}
		setColumnEditable(editable);
		return SUCCESS;
	}

	public int removeColumn() {
		DefaultTableModel tm = (DefaultTableModel) this.getModel();
		tm.setColumnCount(0);
		return SUCCESS;
	}

	public int setColumn(Object[] TitleName, int titleAlignment) {
		int rtn = setColumn(TitleName);
		if (rtn != SUCCESS)
			return rtn;

		setTitleAlignment(titleAlignment);

		return SUCCESS;
	}

	public int setColumn(Object[] TitleName, Component[] cellEditor) {
		int rtnT = setColumn(TitleName);
		if (rtnT != SUCCESS)
			return rtnT;

		int rtnC = setComponent(cellEditor);
		if (rtnC != SUCCESS)
			return rtnC;

		return SUCCESS;
	}

	public int setColumn(Object[] TitleName, Component[] cellEditor, int titleAlignment) {
		int rtnT = setColumn(TitleName);
		if (rtnT != SUCCESS)
			return rtnT;

		int rtnC = setComponent(cellEditor);
		if (rtnC != SUCCESS)
			return rtnC;

		setTitleAlignment(titleAlignment);

		return SUCCESS;
	}

	public int setComponent(Component[] cellEditor) {
		if (this.getColumnCount() == 0)
			return NULL_STRUCTURE;

		if (cellEditor == null || cellEditor.length != this.getColumnCount())
			return INVALID_DATA;

		for (int i = 0; i < cellEditor.length; i++) {
			boolean validCellEditor = false;

			if (cellEditor[i] == null)
				continue;

			if (cellEditor[i] instanceof JComboBox) {
				validCellEditor = true;
				JComboBox tmpCellEditor = (JComboBox) cellEditor[i];
				this.getColumnModel().getColumn(i).setCellEditor(new DefaultCellEditor(tmpCellEditor));
			}
			if (cellEditor[i] instanceof JCheckBox) {
				validCellEditor = true;
				JCheckBox tmpCellEditor = (JCheckBox) cellEditor[i];
				this.getColumnModel().getColumn(i).setCellEditor(new DefaultCellEditor(tmpCellEditor));
			}
			if (cellEditor[i] instanceof JTextField) {
				validCellEditor = true;
				JTextField tmpCellEditor = (JTextField) cellEditor[i];
				this.getColumnModel().getColumn(i).setCellEditor(new DefaultCellEditor(tmpCellEditor));
			}

			if (!validCellEditor)
				return INVALID_CELLEDITOR;
		}

		return SUCCESS;
	}

	public int setComponent(Component cellEditor, int column) {
		if (this.getColumnCount() == 0)
			return NULL_STRUCTURE;

		if (cellEditor == null)
			return INVALID_DATA;

		boolean validCellEditor = false;
		if (cellEditor instanceof JComboBox) {
			validCellEditor = true;
			JComboBox tmpCellEditor = (JComboBox) cellEditor;
			this.getColumnModel().getColumn(column).setCellEditor(new DefaultCellEditor(tmpCellEditor));
		}
		if (cellEditor instanceof JCheckBox) {
			validCellEditor = true;
			JCheckBox tmpCellEditor = (JCheckBox) cellEditor;
			this.getColumnModel().getColumn(column).setCellEditor(new DefaultCellEditor(tmpCellEditor));
		}
		if (cellEditor instanceof JTextField) {
			validCellEditor = true;
			JTextField tmpCellEditor = (JTextField) cellEditor;
			this.getColumnModel().getColumn(column).setCellEditor(new DefaultCellEditor(tmpCellEditor));
		}
		if (cellEditor instanceof JButton) {
			validCellEditor = true;
			JTextField tmpCellEditor = new JTextField();
			;
			tmpCellEditor.setText(((JButton) cellEditor).getText());
			this.getColumnModel().getColumn(column).setCellEditor(new ButtonEditor(tmpCellEditor));
		}

		if (!validCellEditor)
			return INVALID_CELLEDITOR;

		return SUCCESS;
	}

	class ButtonEditor extends DefaultCellEditor {

		protected JButton button;
		private String label;
		private boolean isPushed;

		public ButtonEditor(JTextField checkBox) {
			super(checkBox);
			this.setClickCountToStart(1);
			label = checkBox.getText().trim();
			button = new JButton();
			button.setOpaque(true);
			button.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					fireEditingStopped();
				}
			});

		}

		public Component getTableCellEditorComponent(final JTable table, Object value, boolean isSelected, int row,
				int column) {
			if (isSelected) {
				button.setForeground(table.getSelectionForeground());
				button.setBackground(table.getSelectionBackground());
			} else {
				button.setForeground(table.getForeground());
				button.setBackground(table.getBackground());
			}
			if (label == null) {
				label = (value == null) ? "" : value.toString();
			}
			button.setText(label);
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("row:" + table.getSelectedRow() + " col:" + table.getSelectedColumn());
				}
			});
			isPushed = true;
			return button;
		}

		public Object getCellEditorValue() {
			if (isPushed) {
				//
				//
				// JOptionPane.showMessageDialog(button, label + ": Ouch!");
				// System.out.println(label + ": Ouch!");
			}
			isPushed = false;
			return new String(label);
		}

		public boolean stopCellEditing() {
			isPushed = false;
			return super.stopCellEditing();
		}

		@Override
		public boolean shouldSelectCell(EventObject anEvent) {
			return super.shouldSelectCell(anEvent);
		}
	}

	@SuppressWarnings("serial")
	class ButtonRenderer extends JButton implements TableCellRenderer {

		public ButtonRenderer() {
			setOpaque(true);
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} else {
				setForeground(table.getForeground());
				setBackground(UIManager.getColor("UIManager"));
			}
			setText((value == null) ? "" : value.toString());
			return this;
		}
	}

	public int setTitleAlignment(int alignment) {
		int rtn = checkAlignment(alignment);
		if (rtn != SUCCESS)
			return rtn;

		JTableHeader header = this.getTableHeader();
		DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
		headerRenderer.setHorizontalAlignment(alignment);
		for (int i = 0; i < this.getColumnCount(); i++) {
			this.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
		}

		return SUCCESS;
	}

	public int setCellAlignment(int alignment, int column) {
		int rtn = checkAlignment(alignment);
		if (rtn != SUCCESS)
			return rtn;

		if (!(this.getColumnCount() > 0) || !(column < this.getColumnCount()) || !(column >= 0)) {
			return INVALID_COLUMN;
		}

		cellRenderer[column].setHorizontalAlignment(alignment);
		for (int i = 0; i < this.getColumnCount(); i++) {
			this.getColumnModel().getColumn(column).setCellRenderer(cellRenderer[column]);
		}

		return SUCCESS;
	}

	public int setCellAlignmentAll(int alignment) {
		int rtn = checkAlignment(alignment);
		if (rtn != SUCCESS)
			return rtn;

		for (int i = 0; i < this.getColumnCount(); i++) {
			cellRenderer[i].setHorizontalAlignment(alignment);
			this.getColumnModel().getColumn(i).setCellRenderer(cellRenderer[i]);
		}

		return SUCCESS;
	}

	public int setCellAlignment(int[] alignment) {
		for (int a = 0; a < alignment.length; a++) {
			int rtn = checkAlignment(alignment[a]);
			if (rtn != SUCCESS)
				return rtn;
		}
		if (alignment.length != this.getColumnCount())
			return INVALID_ALIGNMENT;

		for (int i = 0; i < this.getColumnCount(); i++) {
			// DefaultTableCellRenderer cellRenderer = new
			// DefaultTableCellRenderer();
			cellRenderer[i].setHorizontalAlignment(alignment[i]);
			this.getColumnModel().getColumn(i).setCellRenderer(cellRenderer[i]);
		}

		return SUCCESS;
	}

	public int addRow(Object[] data) {
		if (this.getColumnCount() == 0)
			return NULL_STRUCTURE;

		if (data == null || data.length != this.getColumnCount())
			return INVALID_DATA;

		DefaultTableModel tm = (DefaultTableModel) this.getModel();
		tm.addRow(data);

		return SUCCESS;
	}

	public int insertRow(int row, Object[] data) {
		if (this.getColumnCount() == 0)
			return NULL_STRUCTURE;

		if (data == null || data.length != this.getColumnCount())
			return INVALID_DATA;

		DefaultTableModel tm = (DefaultTableModel) this.getModel();
		tm.insertRow(row, data);

		return SUCCESS;
	}

	public void removeStructue() {
		DefaultTableModel tm = (DefaultTableModel) this.getModel();
		tm.setRowCount(0);
		tm.setColumnCount(0);
		editable = null;
		setColumnEditable(editable);
	}

	public void removeRowAll() {
		DefaultTableModel tm = (DefaultTableModel) this.getModel();
		tm.setRowCount(0);
	}

	public int removeRow(int row) {
		if (row >= this.getRowCount() || row < 0) {
			return INVALID_ROW;
		}
		DefaultTableModel tm = (DefaultTableModel) this.getModel();
		if (row >= tm.getDataVector().size() || row < 0) {
			return INVALID_ROW;
		}
		tm.removeRow(row);
		return SUCCESS;
	}

	public int setData(Object[][] data) {
		for (int i = 0; i < data.length; i++) {
			Object[] rowData = data[i];

			if (rowData.length != this.getColumnCount())
				return INVALID_DATA;
		}

		DefaultTableModel tm = (DefaultTableModel) this.getModel();
		for (int i = 0; i < data.length; i++) {
			Object[] rowData = data[i];
			tm.addRow(rowData);
		}

		return SUCCESS;
	}

	public int setData(Vector data) {
		for (int i = 0; i < data.size(); i++) {
			Object[] rowData = (Object[]) data.elementAt(i);

			if (rowData.length != this.getColumnCount())
				return INVALID_DATA;
		}

		DefaultTableModel tm = (DefaultTableModel) this.getModel();
		for (int i = 0; i < data.size(); i++) {
			Object[] rowData = (Object[]) data.elementAt(i);
			tm.addRow(rowData);
		}

		return SUCCESS;
	}

	public Vector getData() {
		Vector rtn = new Vector();

		for (int i = 0; i < this.getRowCount(); i++) {
			Object[] rowData = new Object[this.getColumnCount()];
			rowData = getRowData(i);
			rtn.addElement(rowData);
		}

		return rtn;
	}

	public boolean saveData(DataManager dmsav, DataManager dmdel) {
		// save your GridData To Database with EJB
		System.out.println("Please implements this mothed in your SetupFrame");
		return true;
	}

	public Object[] getRowData(int row, int[] cols) {
		Object[] rtn = null;
		Object[] tmp = null;
		if (this.getRowCount() > 0 && row < this.getRowCount() && row >= 0) {
			DefaultTableModel tm = (DefaultTableModel) this.getModel();
			tmp = new Object[this.getColumnCount()];
			rtn = new Object[cols.length];
			int tmpCount = 0;
			for (int col = 0; col < tmp.length; col++) {
				// if (PBSUIBaseSetupFrame.isIntInInteger(col,cols)){
				rtn[tmpCount] = tm.getValueAt(row, col);
				tmpCount++;
				// }
			}
		}
		return rtn;
	}

	public Object[] getRowData(int row) {
		Object[] rtn = null;

		if (this.getRowCount() > 0 && row < this.getRowCount() && row >= 0) {
			DefaultTableModel tm = (DefaultTableModel) this.getModel();

			rtn = new Object[this.getColumnCount()];
			for (int col = 0; col < this.getColumnCount(); col++) {
				rtn[col] = tm.getValueAt(row, col);
			}
		}
		return rtn;
	}

	// 根据列数，列内容，查询所在Table具体行号
	public int getRowIndexByColIndexAndColValue(int col, String colValue) {
		if (this.getRowCount() == 0) {
			return 0;
		}
		for (int i = 0; i < this.getRowCount(); i++) {
			String col_tmp = this.getValueAt(i, col).toString();
			if (col_tmp.equalsIgnoreCase(colValue)) {
				return i;
			}
		}
		return 0;
	}

	public int setRowData(Object[] data, int row) {
		if (!(this.getRowCount() > 0) || !(row < this.getRowCount()) || !(row >= 0)) {
			return INVALID_ROW;
		}
		if (data.length != this.getColumnCount())
			return INVALID_DATA;

		DefaultTableModel tm = (DefaultTableModel) this.getModel();
		for (int col = 0; col < this.getColumnCount(); col++) {
			tm.setValueAt(data[col], row, col);
		}

		return SUCCESS;
	}

	public Object[] getColumnData(int column) {
		Object[] rtn = null;

		if (this.getRowCount() > 0 && column >= 0 && column < this.getColumnCount()) {
			DefaultTableModel tm = (DefaultTableModel) this.getModel();

			rtn = new Object[this.getRowCount()];
			for (int row = 0; row < this.getRowCount(); row++) {
				rtn[row] = tm.getValueAt(row, column);
			}
		}

		return rtn;
	}

	public int setColumnData(Object[] data, int column) {
		if (!(this.getColumnCount() > 0) || !(column < this.getColumnCount()) || !(column >= 0)) {
			return INVALID_COLUMN;
		}
		if (data.length != this.getRowCount())
			return INVALID_DATA;

		DefaultTableModel tm = (DefaultTableModel) this.getModel();
		for (int row = 0; row < this.getRowCount(); row++) {
			tm.setValueAt(data[row], row, column);
		}

		return SUCCESS;
	}

	public int setColumnData(Object data, int column) {
		if (!(this.getColumnCount() > 0) || !(column < this.getColumnCount()) || !(column >= 0)) {
			return INVALID_COLUMN;
		}

		DefaultTableModel tm = (DefaultTableModel) this.getModel();
		for (int row = 0; row < this.getRowCount(); row++) {
			tm.setValueAt(data, row, column);
		}

		return SUCCESS;
	}

	public int setColumnVisible(int column, boolean show, int width) {
		if (!(this.getColumnCount() > 0) || !(column < this.getColumnCount()) || !(column >= 0))
			return INVALID_COLUMN;

		if (show) {
			this.getColumnModel().getColumn(column).setResizable(true);
			this.getColumnModel().getColumn(column).setMinWidth(10);
			this.getColumnModel().getColumn(column).setMaxWidth(800);
			this.getColumnModel().getColumn(column).setPreferredWidth(width);
		} else {
			this.getColumnModel().getColumn(column).setPreferredWidth(0);
			this.getColumnModel().getColumn(column).setMinWidth(0);
			this.getColumnModel().getColumn(column).setMaxWidth(0);
			this.getColumnModel().getColumn(column).setResizable(false);
		}

		return SUCCESS;
	}

	public int setWidth(int[] width) {
		if (width == null || width.length != this.getColumnCount())
			return INVALID_COLUMN_WIDTH;

		for (int i = 0; i < this.getColumnCount(); i++) {
			this.getColumnModel().getColumn(i).setPreferredWidth(width[i]);
		}

		return SUCCESS;
	}

	public int setWidth(int width, int column) {
		if (!(this.getColumnCount() > 0) || !(column < this.getColumnCount()) || (column < 0))
			return INVALID_COLUMN;

		this.getColumnModel().getColumn(column).setPreferredWidth(width);

		return SUCCESS;
	}

	private int checkAlignment(int alignment) {
		if (alignment != SwingConstants.RIGHT && alignment != SwingConstants.CENTER
				&& alignment != SwingConstants.LEADING && alignment != SwingConstants.LEFT
				&& alignment != SwingConstants.TRAILING)
			return INVALID_ALIGNMENT;

		return SUCCESS;
	}

	public void reallocateIndexes() {
		int rowCount = ((DefaultTableModel) this.getModel()).getRowCount();

		indexes = new int[rowCount];

		for (int row = 0; row < rowCount; row++) {
			indexes[row] = row;
		}
	}

	public int compareRowsByColumn(int row1, int row2, int column) {
		Class type = ((DefaultTableModel) this.getModel()).getColumnClass(column);
		TableModel data = ((TableModel) this.getModel());

		// Check for nulls.

		Object o1 = data.getValueAt(row1, column);
		Object o2 = data.getValueAt(row2, column);

		// If both values are null, return 0.
		if (o1 == null && o2 == null) {
			return 0;
		} else if (o1 == null) { // Define null less than everything.
			return -1;
		} else if (o2 == null) {
			return 1;
		}

		if (type.getSuperclass() == java.lang.Number.class) {
			Number n1 = (Number) data.getValueAt(row1, column);
			double d1 = n1.doubleValue();
			Number n2 = (Number) data.getValueAt(row2, column);
			double d2 = n2.doubleValue();

			if (d1 < d2) {
				return -1;
			} else if (d1 > d2) {
				return 1;
			} else {
				return 0;
			}
		} else if (type == java.util.Date.class) {
			Date d1 = (Date) data.getValueAt(row1, column);
			long n1 = d1.getTime();
			Date d2 = (Date) data.getValueAt(row2, column);
			long n2 = d2.getTime();

			if (n1 < n2) {
				return -1;
			} else if (n1 > n2) {
				return 1;
			} else {
				return 0;
			}
		} else if (type == String.class) {
			String s1 = (String) data.getValueAt(row1, column);
			String s2 = (String) data.getValueAt(row2, column);
			int result = s1.compareTo(s2);

			if (result < 0) {
				return -1;
			} else if (result > 0) {
				return 1;
			} else {
				return 0;
			}
		} else if (type == Boolean.class) {
			Boolean bool1 = (Boolean) data.getValueAt(row1, column);
			boolean b1 = bool1.booleanValue();
			Boolean bool2 = (Boolean) data.getValueAt(row2, column);
			boolean b2 = bool2.booleanValue();

			if (b1 == b2) {
				return 0;
			} else if (b1) { // Define false < true
				return 1;
			} else {
				return -1;
			}
		} else {
			Object v1 = data.getValueAt(row1, column);
			String s1 = v1.toString();
			Object v2 = data.getValueAt(row2, column);
			String s2 = v2.toString();
			int result = s1.compareTo(s2);

			if (result < 0) {
				return -1;
			} else if (result > 0) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	public int compare(int row1, int row2) {
		compares++;
		for (int level = 0; level < sortingColumns.size(); level++) {
			Integer column = (Integer) sortingColumns.elementAt(level);
			int result = compareRowsByColumn(row1, row2, column.intValue());
			if (result != 0) {
				return ascending ? result : -result;
			}
		}
		return 0;
	}

	public void tableChanged(TableModelEvent e) {
		// System.out.println("in PBSUIBaseGrid,
		// PBSUIBaseGrid.tableChanged....Type : " + e.getType() + " Column " +
		// e.getColumn() + " FirstRow" + e.getFirstRow() + " LastRow " +
		// e.getLastRow());
		reallocateIndexes();
		isChanged = true;
		super.tableChanged(e);
	}

	/*
	 * public void setValueAt(Object aValue, int row, int column) {
	 * nc.strAddComma((String)aValue); getModel().setValueAt(aValue, row,
	 * column); }
	 */

	public void checkModel() {
		if (indexes.length != ((DefaultTableModel) this.getModel()).getRowCount()) {
			System.err.println("Sorter not informed of a change in model.");
		}
	}

	private void sort(Object sender) {
		checkModel();

		compares = 0;
		shuttlesort((int[]) indexes.clone(), indexes, 0, indexes.length);
	}

	private void shuttlesort(int from[], int to[], int low, int high) {
		if (high - low < 2) {
			return;
		}
		int middle = (low + high) / 2;
		shuttlesort(to, from, low, middle);
		shuttlesort(to, from, middle, high);

		int p = low;
		int q = middle;

		if (high - low >= 4 && compare(from[middle - 1], from[middle]) <= 0) {
			for (int i = low; i < high; i++) {
				to[i] = from[i];
			}
			return;
		}

		// A normal merge.

		for (int i = low; i < high; i++) {
			if (q >= high || (p < middle && compare(from[p], from[q]) <= 0)) {
				to[i] = from[p++];
			} else {
				to[i] = from[q++];
			}
		}
	}

	public void sortByColumn(int column) {
		sortByColumn(column, true);
	}

	public void sortByColumn(int column, boolean ascending) {
		this.ascending = ascending;
		sortingColumns.removeAllElements();
		sortingColumns.addElement(new Integer(column));
		sort(this);
		// super.tableChanged(new TableModelEvent(this.getModel()));
	}

	public void addMouseListenerToHeaderInTable(JTable table) {
		final PBSUIBaseGrid sorter = this;
		final JTable tableView = table;
		tableView.setColumnSelectionAllowed(false);
		MouseAdapter listMouseListener = new MouseAdapter() {
			boolean as = true;
			int column = -1;

			public void mouseClicked(MouseEvent e) {
				if (sorter.isEditing())
					sorter.getCellEditor().stopCellEditing();

				TableColumnModel columnModel = tableView.getColumnModel();

				int viewColumn = columnModel.getColumnIndexAtX(e.getX());
				if (column != tableView.convertColumnIndexToModel(viewColumn)) {
					column = tableView.convertColumnIndexToModel(viewColumn);
					as = true;
				} else
					as = !as;

				if (e.getClickCount() == 1 && column != -1) {
					// int shiftPressed =
					// e.getModifiers()&InputEvent.SHIFT_MASK;
					// boolean ascending = (shiftPressed == 0);
					sorter.sortByColumn(column, as);
				}
			}
		};
		JTableHeader th = tableView.getTableHeader();
		th.addMouseListener(listMouseListener);
	}

	/*
	 * public Object getValueAt(int aRow, int aColumn) { checkModel(); return
	 * this.getModel().getValueAt(indexes[aRow], aColumn); }
	 */
	public int setColumnEditable(boolean[] editable) {
		if (this.getColumnCount() == 0)
			return NULL_STRUCTURE;

		if (editable == null || editable.length != this.getColumnCount())
			return INVALID_EDITSTATUS;

		sorttable.SortTableModel tm = (sorttable.SortTableModel) this.getModel();
		tm.setEditStatus(editable);

		return SUCCESS;
	}

	// Don't use this for those setup that extends from PBSUIBaseSetupFrame
	public void setSortEnable() {
		sorttable.SortButtonRenderer renderer = new sorttable.SortButtonRenderer();
		javax.swing.table.TableColumnModel model = this.getColumnModel();
		String[] columnNames = new String[this.getColumnCount()];
		int n = columnNames.length;
		for (int i = 0; i < n; i++) {
			model.getColumn(i).setHeaderRenderer(renderer);
		}
		// add MouseListener for header
		javax.swing.table.JTableHeader header = this.getTableHeader();
		header.addMouseListener(new sorttable.HeaderListener(header, renderer));
	}

	public int setColumnEditable(boolean colEditable, int column) {
		if (this.getColumnCount() == 0)
			return NULL_STRUCTURE;

		sorttable.SortTableModel tm = (sorttable.SortTableModel) this.getModel();
		editable[column] = colEditable;
		tm.setEditStatus(editable);

		return SUCCESS;
	}

	public int setCellEditable(Vector colEditable) {
		if (this.getColumnCount() == 0)
			return NULL_STRUCTURE;

		sorttable.SortTableModel tm = (sorttable.SortTableModel) this.getModel();
		tm.setEditStatusCell(colEditable);

		return SUCCESS;
	}

	public int setColumnEditableAll(boolean colEditable) {
		if (this.getColumnCount() == 0)
			return NULL_STRUCTURE;

		thisModel tm = (thisModel) this.getModel();
		for (int i = 0; i < editable.length; i++)
			editable[i] = colEditable;
		tm.setEditStatus(editable);

		return SUCCESS;
	}

	public void setIsChanged(boolean isChanged) {
		this.isChanged = isChanged;
	}

	public boolean getIsChanged() {
		return isChanged;
	}

	/**
	 * The Method will return some String which told the Caller to create a
	 * correct Object to editor the in A Panel
	 * <p>
	 * Now Support 3 Strings
	 * <p>
	 * -------javax.swing.JComboBox
	 * <p>
	 * -------javax.swing.JCheckBox
	 * <p>
	 * -------java.lang.String
	 * <p>
	 * the Returntype and celltype and editortype is these:
	 * <p>
	 * ReturnType ---------ColumnClassType---------ColumnEditorModelType
	 * <p>
	 * JTextfield ----------- String ---------------------- NULL
	 * <p>
	 * JCheckBox ------------ Boolean ---------------------- NULL
	 * <p>
	 * JNUMTextField ---------- Integer/double -------------------- NULL
	 * <p>
	 * JComboBox ------------- String ---------------------- JComboBox
	 * <p>
	 * Because when we use a param with "String" or "Boolean" ,it will throw a
	 * exception with Null Point, so in catch() , we return a JCheckBox with
	 * "Boolean" type param , and return "String" with other type param.
	 * 
	 * @param col
	 *            The Column Index.
	 * @return String Give a Object should be created by Caller.
	 */
	public String getColumnCellEditorComponentName(int col) {
		String columndatatype = this.getColumnClass(col).getName();
		String editortype;
		if (this.getCellEditor(0, col).getTableCellEditorComponent(this, null, false, 0, col) != null) {
			editortype = this.getCellEditor(0, col).getTableCellEditorComponent(this, null, false, 0, col).getClass()
					.getName();
		} else {
			editortype = "javax.swing.JTextField";
		}
		if ((columndatatype == "java.lang.String") && (editortype == "javax.swing.JTextField")) {
			return "StringEditor";
		}
		if ((columndatatype == "java.lang.String") && (editortype == "setup.PMMdmComboBox")) {
			return "ComboEditor";
		}
		if ((columndatatype == "java.lang.Boolean") && (editortype == "javax.swing.JCheckBox")) {
			return "CheckEditor";
		}
		if ((columndatatype == "java.lang.Double") && (editortype == "javax.swing.JTextField")) {
			return "DoubleNumEditor";
		}
		if ((columndatatype == "java.lang.Integer") && (editortype == "javax.swing.JTextField")) {
			return "IntNumEditor";
		}
		if ((columndatatype == "java.sql.Date") && (editortype == "javax.swing.JTextField")) {
			return "sqlDateEditor";
		}
		if (this.getCellEditor(0, col).getTableCellEditorComponent(this, null, false, 0, col) instanceof JComboBox)
			return "ComboEditor";
		return "NewType";
	}

	public void setColColor(Vector colID, Color color) {
		for (int i = 0; i < this.getColumnCount(); i++) {
			if (this.getRowCount() > 0) {
				if (this.getValueAt(0, i) instanceof java.lang.Boolean)
					continue;
			}
			cellRenderer[i].setModifyRowsID(colID, color);
			cellRenderer[i].setIsCol(true);
			this.getColumnModel().getColumn(i).setCellRenderer(cellRenderer[i]);
		}
	}

	public void setColColor(Vector colID, Vector color) {
		for (int i = 0; i < this.getColumnCount(); i++) {
			if (this.getRowCount() > 0) {
				if (this.getValueAt(0, i) instanceof java.lang.Boolean)
					continue;
			}
			cellRenderer[i].setModifyRowsID(colID, color);
			cellRenderer[i].setIsCol(true);
			this.getColumnModel().getColumn(i).setCellRenderer(cellRenderer[i]);
		}
	}

	public void setRowColor(Vector rowID, Color color) {
		for (int i = 0; i < this.getColumnCount(); i++) {
			if (this.getRowCount() > 0) {
				if (this.getValueAt(0, i) instanceof java.lang.Boolean)
					continue;
			}
			cellRenderer[i].setModifyRowsID(rowID, color);
			cellRenderer[i].setIsCol(false);
			this.getColumnModel().getColumn(i).setCellRenderer(cellRenderer[i]);
		}
	}

	public void setCellColor(Vector cellColor) {
		for (int i = 0; i < this.getColumnCount(); i++) {
			if (this.getRowCount() > 0) {
				if (this.getValueAt(0, i) instanceof java.lang.Boolean)
					continue;
			}
			cellRenderer[i].setCellColor(cellColor);
			this.getColumnModel().getColumn(i).setCellRenderer(cellRenderer[i]);
		}
	}

	public void setEachRowColor(Vector rowColor) {
		for (int i = 0; i < this.getColumnCount(); i++) {
			if (this.getRowCount() > 0) {
				if (this.getValueAt(0, i) instanceof java.lang.Boolean)
					continue;
			}
			cellRenderer[i].setModifyRowsIDEachColor(rowColor);
			cellRenderer[i].setIsCol(false);
			this.getColumnModel().getColumn(i).setCellRenderer(cellRenderer[i]);
		}
	}

	// no use for other class but only String
	public void setRowColorByCol(Vector rowID, int col, Color color) {
		cellRenderer[col].setModifyRowsID(rowID, color);
		cellRenderer[col].setIsCol(false);
		this.getColumnModel().getColumn(col).setCellRenderer(cellRenderer[col]);

	}

	/*
	 * public String getColumnCellEditorComponentName(int col){ String celltype
	 * = this.getColumnClass(col).getName(); // System.out.println(col +
	 * "The CellType of "+col+" is ----"+ celltype); try{ String modeltype =
	 * getColumnModel().getColumn(col).getCellEditor().
	 * getTableCellEditorComponent(this,null,true,0,col).getClass().getName();
	 * // System.out.println("The ModelType of "+col+" is ----"+ modeltype); if
	 * (modeltype == "javax.swing.JComboBox") return new
	 * javax.swing.JComboBox().getClass().getName(); // You can add others Type
	 * of class to return other ClassName // else if (type == "--------") //
	 * return -----------------; }catch(NullPointerException e){ //
	 * System.out.println("catch a NullPointerException ,so return a String");
	 * if (celltype == "java.lang.Boolean") return new
	 * javax.swing.JCheckBox().getClass().getName(); else return new
	 * java.lang.String().getClass().getName(); } return new
	 * java.lang.String().getClass().getName(); }
	 */
}
