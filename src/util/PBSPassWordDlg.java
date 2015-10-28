package util;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import com.borland.jbcl.layout.XYLayout;
import com.borland.jbcl.layout.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PBSPassWordDlg
    extends JDialog {
  JPanel panel1 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JLabel jLabel1 = new JLabel();
  XYLayout xYLayout1 = new XYLayout();
  JButton jBtnOK = new JButton();
  JButton jBtnCacel = new JButton();
  JPasswordField jPassWord = new JPasswordField();
  public String PassWord="";
  public PBSPassWordDlg(Frame owner, String title, boolean modal) {
    super(owner, title, modal);
    try {
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      jbInit();
      pack();
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  public PBSPassWordDlg(String Title) {
    this(new Frame(), Title, true);
  }

  private void jbInit() throws Exception {
    panel1.setLayout(borderLayout1);
    jLabel1.setText("PASSWORD");
    this.getContentPane().setLayout(xYLayout1);
    jPassWord.setMinimumSize(new Dimension(80, 23));
    jPassWord.setPreferredSize(new Dimension(120, 23));
    this.getContentPane().add(panel1, new XYConstraints(0, 0, -1, -1));
    jBtnCacel.setText("CANCEL");
    jBtnOK.setText("OK");
    this.getContentPane().add(jLabel1, new XYConstraints(70, 80, -1, -1));
    this.getContentPane().add(jBtnCacel, new XYConstraints(250, 140, -1, -1));
    this.getContentPane().add(jBtnOK, new XYConstraints(50, 140, -1, -1));
    this.getContentPane().add(jPassWord,
                              new XYConstraints(130, 79, 160, -1));

    jBtnOK.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jBtnOK_actionPerformed(e);
      }
    });

    jBtnCacel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jBtnCacel_actionPerformed(e);
      }
    });

    jPassWord.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e) {
        jBtnOK.doClick();
      }
    });

    xYLayout1.setWidth(350);
    xYLayout1.setHeight(200);
  }

  public void jBtnOK_actionPerformed(ActionEvent e) {
        PassWord=getPsw();
        this.setVisible(false);
    }

    public void jBtnCacel_actionPerformed(ActionEvent e) {
      PassWord = getPsw();
      this.setVisible(false);
    }

  public String getPsw(){ return String.copyValueOf(this.jPassWord.getPassword()); }

}
