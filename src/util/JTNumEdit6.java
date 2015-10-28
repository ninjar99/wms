package util;

import javax.swing.text.PlainDocument;
import javax.swing.text.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JTNumEdit6 extends JTextField
{
  private boolean initialized = true;

  // Assign defalut value
  private int nMaxStringLen = 14;
  private String Formatter = "#,##0.00";
  public JTNumEdit6()
  {
    super();
    init();
  }

  // The maximal string length of TextFiled is given by caller

  public JTNumEdit6(int MaxStringLen)
  {
    super();
    init();
    this.nMaxStringLen = MaxStringLen;
  }

 // The maximal string length of TextFiled and decimal formatter is given by caller

  public JTNumEdit6(int MaxStringLen, String Formatter)
  {
    super();
    init();
    this.nMaxStringLen = MaxStringLen;
    this.Formatter = Formatter;
  }
  // initialize

  private void init()
  {
//    this.setText(this.getText()) ;
    this.setHorizontalAlignment(SwingConstants.RIGHT);
    this.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e)
      {
        NumEdit_FocusLost(e);
      }
    });

    this.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e)
      {
        NumEdit_FocusGained(e);
      }
    });
  }

// After the focus is lost, the string is formatted

  void NumEdit_FocusLost(FocusEvent e)
  {
    if (!this.getText().trim().equals(""))
      setValueToTextField(getValueFromTextField());
  }

// After the focus is gained, the string is selected

  void NumEdit_FocusGained(FocusEvent e)
  {
    this.selectAll();
  }
// override the setText function of TextField

  public void setText(String text)
  {
    setValueToTextField(Convert(text));
  }

// Get a double value from TextField

  public double getValueFromTextField()
  {
    return Convert(this.getText());
  }

  public void setValueToTextField(double d)
  {
     String ss = this.getText();
     java.text.DecimalFormat df = new java.text.DecimalFormat(Formatter);
     String tt = df.format(d);
     if (!ss.equals(tt))
       super.setText(tt);
     System.out.println();
  }

  public void setValueToTextField(String s)
  {
     String ss = this.getText();
     java.text.DecimalFormat df = new java.text.DecimalFormat(Formatter);
     double dd = Convert(s);
     String tt = df.format(dd);
     if (!ss.equals(tt))
       this.setText(tt);
  }
// After the focus is lost, the string is formatted
  public double Convert(String TextString)
  {
     double d = 0;
     TextString = TextString.replaceAll(",","");
     String TempString = "";
     TempString = TextString.replaceAll("-","");
     if (!TempString.trim().equals(""))
       try{
         d = Double.parseDouble(TextString);
       }catch(Exception ex){
         d = 0;
       }
     return d;
  }



  // Format the string according to decimal formatter
}
