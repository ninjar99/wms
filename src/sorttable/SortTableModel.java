package sorttable;

import java.util.Vector;
import java.util.Hashtable;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

@SuppressWarnings("serial")
public class SortTableModel extends javax.swing.table.DefaultTableModel {
	int[] indexes;
	TableSorter sorter;
	int lastsortColumn = 0;
	boolean lastsortAscent = true;
	private boolean canEdit = false;
	private boolean edit[];
	private String[] columnTypeForSort;
	private Vector<?> cellEdit;
	private Hashtable<?, ?> spSort = new Hashtable<Object, Object>();// 用于指定列按特别内容排序，不一定对实际列内容排序

	public final static String sortStringType = "String";
	public final static String sortDateType = "Date";
	public final static String sortNumType = "Number";
	public final static String sortBooleanType = "Boolean";

	public void setColumnTypeForSort(String[] types) {
		this.columnTypeForSort = types;
	}

	public void setSpecialSort(Hashtable<?, ?> spSort) {
		this.spSort = spSort;
	}

	public Hashtable<?, ?> getSpecialSort() {
		return spSort;
	}

	public String[] getColumnTypeForSort() {
		return this.columnTypeForSort;
	}

	public String getColumnTypeForSortCol(int col) {
		return this.columnTypeForSort[col];
	}

	public SortTableModel() {
	}

	@SuppressWarnings({ "rawtypes" })
	public Class<?> getColumnClass(int col) {
		new PrintDebugInfo("getColumnClass");
		java.util.Vector v = (java.util.Vector) dataVector.elementAt(0);
		try {
			new PrintDebugInfo("return is :" + v.elementAt(col).getClass().toString());
			return v.elementAt(col).getClass();
		} catch (java.lang.NullPointerException e) {
			new PrintDebugInfo("return is :" + "Null");
			// System.out.println("Null class at " + col);
			return new String().getClass();
		}

		/*
		 * switch (col) { case 0: return String.class; case 1: return
		 * Double.class; case 2: return String.class; case 3: return
		 * Integer.class; case 4: return Boolean.class; default: return
		 * Object.class; }
		 */
	}

	public void setlastColumn(int lastcolumn) {
		lastsortColumn = lastcolumn;
	}

	public void setlastascent(boolean lastascent) {
		lastsortAscent = lastascent;
	}

	public int getLastSortColumn() {
		return lastsortColumn;
	}

	public boolean getLastAscent() {
		return lastsortAscent;
	}

	public Object getValueAt(int row, int col) {
		if (indexes == null || this.getRowCount() != indexes.length) {
			indexes = getIndexes();
		}
		new PrintDebugInfo("in sortTableModel--->getValueAt " + "Row " + row + "Col " + col);
		int rowIndex = row;
		if (indexes != null) {
			rowIndex = indexes[row];
		}
		new PrintDebugInfo("return is :" + super.getValueAt(rowIndex, col));
		return super.getValueAt(rowIndex, col);
	}

	public void setValueAt(Object value, int row, int col) {
		if (indexes == null || this.getRowCount() != indexes.length) {
			indexes = getIndexes();
		}

		new PrintDebugInfo("in sortTableModel--->setValueAt " + "Row " + row + "Col " + col);
		int rowIndex = row;
		try {
			if (indexes != null && row != -1) {
				try {
					rowIndex = indexes[row];
				} catch (Exception e) {
					rowIndex = indexes[0];
//					e.printStackTrace();
				}
			}
			if (row != -1)
				super.setValueAt(value, rowIndex, col);
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		// new PrintDebugInfo("set is :" + value.toString());
	}

	public void sortByColumn(int column, boolean isAscent) {
		new PrintDebugInfo("in SorttableModel--->sortByColumn ");
		if (sorter == null) {
			sorter = new TableSorter(this);
		}
		sorter.sort(column, isAscent);
		lastsortColumn = column;
		lastsortAscent = isAscent;
		fireTableDataChanged();
	}

	public int[] getIndexes() {
		new PrintDebugInfo("in SorttableModel--->getIndexes");
		int n = getRowCount();
		if (indexes != null) {
			if (indexes.length == n) {
				return indexes;
			}
		}
		indexes = new int[n];
		for (int i = 0; i < n; i++) {
			indexes[i] = i;
		}
		return indexes;
	}

	public int getSelectRowByIndex(int index) {
		for (int i = 0; i < indexes.length; i++) {
			if (index == indexes[i])
				return i;
		}
		return index;
	}

	public void setIndexes(int[] newindexes) {
		if (newindexes.length != this.getRowCount())
			return;
		for (int i = 0; i < newindexes.length; i++) {
			indexes[i] = newindexes[i];
		}
		System.out.println();
	}

	public void setEditStatus(boolean[] edit) {
		this.edit = edit;
	}

	public void setEditStatusCell(Vector<?> editCell) {
		this.cellEdit = editCell;
	}

	public boolean isCellEditable(int row, int col) {
		boolean canEdit = true;
		if (edit != null)
			canEdit = edit[col];

		if (cellEdit != null) {
			for (int i = 0; i < cellEdit.size(); i++) {
				CellID cellcan = (CellID) cellEdit.elementAt(i);
				if (cellcan.getRow() == row && cellcan.getCol() == col) {
					canEdit = canEdit && cellcan.isEditable();
				}
			}
		}

		return canEdit;
	}

	public static void main(String[] args) {
		SortTableModel sortTableModel1 = new SortTableModel();
	}

}
