package util;

import java.awt.*;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;

public class DateChooser extends JDialog {
  private static boolean isShow = false;

  private static Calendar showMonth = new GregorianCalendar();

  private int startYear = 1950;
  private int lastYear = 2050;

  JPanel rootPanel = new JPanel(new BorderLayout(), true);

  private TablePanel tablePanel = null;

  private ConfigLine configLine = null;

  public static final int width = 190;
  public static final int height = 170;

  private int local_X = 0;
  private int local_Y = 0;

  public DateChooser() {

    makeFace();
  }

  public DateChooser(Frame owner) {

    super(owner, "", true);
    makeFace();
  }

  /**
   * 构造方法3
   *
   * @param owner java.awt.Frame
   * @param showMonth java.util.Calendar
   * @param startYear int
   * @param lastYear int
   */
  public DateChooser(Frame owner, Calendar showMonth, int startYear,
                     int lastYear) {
    super(owner, "", true);

    this.showMonth = showMonth;
    this.startYear = startYear;
    this.lastYear = lastYear;

    makeFace();
  }

  public DateChooser(Calendar showMonth, int startYear, int lastYear) {

    super( (Frame)null, "", true);

    this.showMonth = showMonth;
    this.startYear = startYear;
    this.lastYear = lastYear;

    makeFace();
  }

  private void makeFace() {

    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

    setResizable(false);

    tablePanel = new TablePanel(this, showMonth);
    configLine = new ConfigLine(tablePanel, showMonth,
                                startYear, lastYear);

    setSize(width, height);

    rootPanel.setBorder(new LineBorder(Pallet.backGroundColor, 2));
    rootPanel.setBackground(Pallet.backGroundColor);

    rootPanel.add(tablePanel, BorderLayout.CENTER);
    rootPanel.add(configLine, BorderLayout.SOUTH);

    getContentPane().add(rootPanel, BorderLayout.CENTER);
  }

  public Date showChooser(JComponent invoker, int x, int y) {

    Point invokerOrigin;

    if (invoker != null) {

      if (isShow == true) {
        setVisible(false);

      }
      invokerOrigin = invoker.getLocationOnScreen();

      setLocation(invokerOrigin.x , invokerOrigin.y);

    }
    else {

      if (isShow == true) {
        setVisible(false);

      }
      setLocation(x, y);
    }

    setVisible(true);
    isShow = true;

    return tablePanel.getDate();
  }

  public void hideChooser() {
    setVisible(false);
  }

  public Date getDate() {
    return tablePanel.getDate();
  }
}
