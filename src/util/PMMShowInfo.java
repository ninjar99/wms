package util;

import java.awt.*;
import javax.swing.*;
import java.util.Vector;
import main.PBSUIBaseDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;

public class PMMShowInfo extends PBSUIBaseDialog {
  JPanel panel1 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JScrollPane jScrollPane1 = new JScrollPane();
  JTextArea txtInfo = new JTextArea();
  JPanel jPanel1 = new JPanel();
  JButton btnClose = new JButton();
  JButton btnCopy = new JButton();
  BorderLayout borderLayout2 = new BorderLayout();
  Vector infoNewData;
  Vector infoOldData;
  Vector infoOtherData;
  JTextPane lbInfo = new JTextPane();
  JPanel jPanel2 = new JPanel();
  BorderLayout borderLayout3 = new BorderLayout();
  String strinfo = "";

  public PMMShowInfo(Frame owner, String title,String info,boolean modal,Vector vecdata) {
    super(owner, title, modal);
    this.strinfo = info;
    this.infoOtherData = vecdata;
    try {
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      jbInit();
      setSize(350,300);
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      this.setLocation( (screenSize.width - this.getWidth()) / 2,
                        (screenSize.height - this.getHeight()) / 2);
      this.show();
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  public PMMShowInfo(Frame owner, String title,String info,boolean modal,Vector vecdata1,Vector vecdata2) {
    super(owner, title, modal);
    this.strinfo = info;
    this.infoNewData = vecdata1;
    this.infoOldData = vecdata2;
    try {
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      jbInit();
      setSize(300,260);
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      this.setLocation( (screenSize.width - this.getWidth()) / 2,
                        (screenSize.height - this.getHeight()) / 2);
      this.show();
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  public PMMShowInfo(String title,String info,Vector vecdata) {
    this(new Frame(),title,info,true,vecdata);
  }

  public PMMShowInfo(String title,String info,Vector vecdata1,Vector vecdata2) {
    this(new Frame(),title,info,true,vecdata1,vecdata2);
  }

  private void jbInit() throws Exception {
    panel1.setLayout(borderLayout1);
    lbInfo.setText(strinfo);
    txtInfo.setEditable(false);
    btnClose.setText("CLOSE");
    btnClose.addActionListener(new PMMShowInfo_btnClose_actionAdapter(this));
    btnCopy.setText("COPY");
    btnCopy.addActionListener(new PMMShowInfo_btnCopy_actionAdapter(this));
    jPanel1.setLayout(borderLayout2);
    lbInfo.setBackground(new Color(236, 233, 216));
    lbInfo.setEnabled(false);
    lbInfo.setPreferredSize(new Dimension(50, 50));
    lbInfo.setEditable(false);
    jPanel2.setLayout(borderLayout3);
    panel1.setPreferredSize(new Dimension(30, 31));
    panel1.setRequestFocusEnabled(true);
    panel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);
    panel1.add(jPanel1, java.awt.BorderLayout.SOUTH);
    jScrollPane1.getViewport().add(txtInfo);
    jPanel1.add(btnCopy, java.awt.BorderLayout.WEST);
    jPanel1.add(btnClose, java.awt.BorderLayout.EAST);
    jPanel2.add(lbInfo, BorderLayout.NORTH);
    jPanel2.add(panel1,  BorderLayout.CENTER);
    this.getContentPane().add(jPanel2, BorderLayout.CENTER);

    if (infoNewData!=null && infoNewData.size()>0){
      txtInfo.append("以下订单是新生成的订单"+"\n");
      for(int i=0;i<infoNewData.size();i++){
        txtInfo.append(infoNewData.elementAt(i).toString()+"\n");
      }
      txtInfo.append("\n");
    }
    if (infoOldData!=null && infoOldData.size()>0){
      txtInfo.append("以下订单是已经存在的订单，系统不需要保存"+"\n");
      for(int i=0;i<infoOldData.size();i++){
        txtInfo.append(infoOldData.elementAt(i).toString()+"\n");
      }
      txtInfo.append("\n");
    }
    if (infoOtherData!=null && infoOtherData.size()>0){
      for(int i=0;i<infoOtherData.size();i++){
        Object obj[] = (Object[])infoOtherData.get(i);
        for(int j=0;j<obj.length;j++){
          txtInfo.append(obj[j].toString()+"   ");
        }
        txtInfo.append("\n");
      }
      txtInfo.append("\n");
    }

  }

  public void btnClose_actionPerformed(ActionEvent e) {
    this.setVisible(false);
  }

  public void btnCopy_actionPerformed(ActionEvent e) {
    StringSelection str=new StringSelection(txtInfo.getText());
    Clipboard  clipboard  =  java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
    clipboard.setContents(str,str);
  }
}

class PMMShowInfo_btnCopy_actionAdapter
    implements ActionListener {
  private PMMShowInfo adaptee;
  PMMShowInfo_btnCopy_actionAdapter(PMMShowInfo adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.btnCopy_actionPerformed(e);
  }
}

class PMMShowInfo_btnClose_actionAdapter
    implements ActionListener {
  private PMMShowInfo adaptee;
  PMMShowInfo_btnClose_actionAdapter(PMMShowInfo adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.btnClose_actionPerformed(e);
  }
}
