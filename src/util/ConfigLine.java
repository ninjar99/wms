package util;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.text.SimpleDateFormat;

class ConfigLine
    extends JPanel {

  private TablePanel tablePanel = null;

  private Calendar showMonth = null;
  private int startYear = 0;
  private int lastYear = 0;

  private int nowYear = 0;
  private int nowMonth = 0;

  Timer timer = new Timer(true);

  private RoundBox yearBox = null;
  private RoundBox monthBox = null;

  private JLabel txtYear = new JLabel("年");
  private JLabel txtMonth = new JLabel("月");

  ConfigLine(TablePanel tablePanel, Calendar showMonth,
             int startYear, int lastYear) {

    this.tablePanel = tablePanel;

    this.showMonth = showMonth;
    this.startYear = startYear;
    this.lastYear = lastYear;

    nowYear = Integer.valueOf(new SimpleDateFormat("yyyy")
                              .format(showMonth.getTime())).intValue();
    nowMonth = Integer.valueOf(new SimpleDateFormat("MM")
                               .format(showMonth.getTime())).intValue();

    yearBox = new RoundBox(nowYear, startYear, lastYear);
    monthBox = new RoundBox(nowMonth, 1, 12);

    makeFace();
    addListener();
  }

  private void makeFace() {

    Font txtFont = new Font("宋体", Font.PLAIN, 12);

    this.setBorder(null);
    this.setBackground(Pallet.configLineColor);
    this.setLayout(new FlowLayout(1, 7, 1));
    this.setPreferredSize(new Dimension(50, 19));

    txtYear.setForeground(Pallet.cfgTextColor);
    txtYear.setPreferredSize(new Dimension(14, 14));
    txtYear.setFont(txtFont);

    txtMonth.setForeground(Pallet.cfgTextColor);
    txtMonth.setPreferredSize(new Dimension(14, 14));
    txtMonth.setFont(txtFont);

    monthBox.setShowWidth(17);

    add(yearBox);
    add(txtYear);
    add(monthBox);
    add(txtMonth);
  }

  private void addListener() {
    yearBox.bt_UP.addMouseListener(new MouseAdapter() {

      public void mousePressed(MouseEvent e) {
        btPressed(yearBox, 1);
      }

      public void mouseReleased(MouseEvent e) {
        btReleased(yearBox, 1);
        nowYear = yearBox.showNow;

        tablePanel.setMonth(nowYear, nowMonth);
      }
    });

    yearBox.bt_DOWN.addMouseListener(new MouseAdapter() {

      public void mousePressed(MouseEvent e) {
        btPressed(yearBox, 2);
      }

      public void mouseReleased(MouseEvent e) {
        btReleased(yearBox, 2);
        nowYear = yearBox.showNow;

        tablePanel.setMonth(nowYear, nowMonth);
      }
    });

    monthBox.bt_UP.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        btPressed(monthBox, 1);
      }

      public void mouseReleased(MouseEvent e) {
        btReleased(monthBox, 1);
        nowMonth = monthBox.showNow;

        tablePanel.setMonth(nowYear, nowMonth);
      }
    });

    monthBox.bt_DOWN.addMouseListener(new MouseAdapter() {

      //monthBox.bt_DOWN 按下
      public void mousePressed(MouseEvent e) {
        btPressed(monthBox, 2);
      }

      //monthBox.bt_DOWN 弹起
      public void mouseReleased(MouseEvent e) {
        btReleased(monthBox, 2);
        nowMonth = monthBox.showNow;

        tablePanel.setMonth(nowYear, nowMonth);
      }
    });
  }

  /**
   * RoundBox 统一按钮按下事务
   *
   * @param box RoundBox
   * @param theBT int
   */
  private void btPressed(RoundBox box, int theBT) {
    final RoundBox theBox = box;

    if (theBT == 1) { //"+"按钮
      timer = new Timer(true);
      timer.schedule(new TimerTask() {
        public void run() {
          if (theBox.showNow < theBox.showMax) {
            theBox.showing.setText("" + (theBox.showNow + 1));

            theBox.showNow++;
          }
        }
      }

      , 500, 100);
    }
    else if (theBT == 2) { //"-"按钮
      timer = new Timer(true);
      timer.schedule(new TimerTask() {
        public void run() {
          if (theBox.showNow > theBox.showMin) {
            theBox.showing.setText("" + (theBox.showNow - 1));

            theBox.showNow--;
          }
        }
      }

      , 500, 100);
    }
  }

  /**
   * RoundBox 统一按钮弹起事务
   *
   * @param box RoundBox
   * @param theBT int
   */
  private void btReleased(RoundBox box, int theBT) {

    final RoundBox theBox = box;

    timer.cancel();

    if (theBT == 1) { //"+"按钮
      if (theBox.showNow < theBox.showMax) {
        theBox.showing.setText("" + (theBox.showNow + 1));

        theBox.showNow++;
      }
    }

    else if (theBT == 2) { //"-"按钮
      if (theBox.showNow > theBox.showMin) {
        theBox.showing.setText("" + (theBox.showNow - 1));

        theBox.showNow--;
      }
    }
  }
}
