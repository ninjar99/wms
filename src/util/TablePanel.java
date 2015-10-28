package util;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

class TablePanel
    extends JPanel {

  DateChooser dateChooser = null;

  private Calendar showMonth = null;
  private Date selectedDate = null;
  private String[] colname = {
      "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
  private String[][] date = new String[7][7];


  private DefaultTableModel model;
  private JTable table;

  /**
   * 构造方法
   *
   * @param dateChooser DateChooser
   * @param showMonth java.util.Calendar
   */
  TablePanel(DateChooser dateChooser, Calendar showMonth) {

    this.dateChooser = dateChooser;
    this.showMonth = showMonth;

    makeFace();
    addListener();
  }

  private void makeFace() {
    date[0][0] = "日";
    date[0][1] = "一";
    date[0][2] = "二";
    date[0][3] = "三";
    date[0][4] = "四";
    date[0][5] = "五";
    date[0][6] = "六";

    table = new JTable(model = new DefaultTableModel(date, colname) {
      public boolean isCellEditable(int rowIndex, int mColIndex) {
        return false;
      }
    });

    DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() {
      public Component getTableCellRendererComponent(JTable table,
          Object value, boolean isSelected, boolean hasFocus,
          int row, int column) {

        setHorizontalAlignment(JLabel.RIGHT);

        if (row == 0) {
          setBackground(Pallet.backGroundColor);
        }
        else if ( ("" + new GregorianCalendar().get(Calendar.DAY_OF_MONTH))
                 .equals(date[row][column])) {
          setBackground(Pallet.todayBackColor);
        }
        else {
          setBackground(Pallet.palletTableColor);
        }
        if ( (column == 0 && row != 0) || (column == 6 && row != 0)) {
          setForeground(Pallet.weekendFontColor);
        }
        else if (row != 0 && column != 0 && column != 6) {
          setForeground(Pallet.dateFontColor);
        }
        else {
          setForeground(Pallet.weekFontColor);
        }
        return super.getTableCellRendererComponent(table, value,
            isSelected, hasFocus, row, column);

      }
    };

    for (int i = 0; i < colname.length; i++) {
      table.getColumn(colname[i]).setCellRenderer(tcr);
    }

    table.setShowHorizontalLines(false);
    table.setShowVerticalLines(false);

    table.setRowSelectionAllowed(false);
    table.setColumnSelectionAllowed(false);

    table.setIntercellSpacing(new Dimension(0, 0));

    setMonth(showMonth);

    setLayout(new BorderLayout());
    add(table, BorderLayout.CENTER);
  }

  private void addListener() {
    table.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {

        if (e.getClickCount() == 2) {
          int selectedRow = table.getSelectedRow();
          int selectedColumn = table.getSelectedColumn();

          if (selectedRow > 0 &&
              !date[selectedRow][selectedColumn].equals("")) {

            showMonth.set(Calendar.DAY_OF_MONTH, Integer.valueOf(
                date[selectedRow][selectedColumn]).intValue());

            selectedDate = showMonth.getTime();

            dateChooser.hideChooser();
          }
        } //end if
      }
    });
  }

  private void setMonth(Calendar showMonth) {

    this.showMonth = showMonth;

    String[][] tmpDate = MonthMaker.makeMonth(showMonth);

    for (int i = 1; i < 7; i++) {
      for (int j = 0; j < 7; j++) {
        date[i][j] = tmpDate[i - 1][j];
        table.setValueAt("" + tmpDate[i - 1][j], i, j);
      }
    }
  }

  public void setMonth(int year, int month) {

    showMonth.set(year, month - 1, 1);

    setMonth(showMonth);
  }

  public Date getDate() {
    return selectedDate;
  }
}
