package util;

import java.io.File;
import java.util.Date;
import jxl.*;
import jxl.write.*;
import java.util.Vector;
import java.awt.Color;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class ExcelOutput {
  private int freezeRow = 0;
  private int freezeCol = 0;
  private int HeaderOrientation = -1;
  private boolean autoAdjustColWidth = false;
  private String fileName="output.xls";
  private String sheetName="Sheet1";
  private Vector data = new Vector();
  private String dateFormat = "yyyy-MM-dd";
  private String doubleFormat = "#,##0.0";
  private String integerFormat = "#,##0";
  private Vector colWidth = new Vector();
  private Vector rowColor = new Vector();
  private Vector rowAlingment = new Vector();

  public ExcelOutput() {

  }

  public ExcelOutput(String fileName,Vector data) {
    this.data=data;
    this.fileName = fileName;
  }

  public void setData(Vector data){
    this.data = data;
  }

  public void setSheetName(String sName){
    if (sName==null || sName.trim().equals(""))
      return;
    this.sheetName = sName;
  }

  public void setDateFormat(String dateFormat){
    this.dateFormat = dateFormat;
  }

  public void setRowAlignment(Vector rowAlign){
    if (rowAlign==null)
      return;

    this.rowAlingment.removeAllElements();
    this.rowAlingment.addElement(rowAlign.elementAt(0));
    this.rowAlingment.addElement((getAlignment( ((Integer)rowAlign.elementAt(1)).intValue() )));
  }

  public void setDoubleFormat(String doubleFormat){
    this.doubleFormat = doubleFormat;
  }

  public void setIntegerFormat(String integerFormat){
    this.integerFormat = integerFormat;
  }
  //自动列宽
  public void setAutoAdjustColWidth(boolean b){
    this.autoAdjustColWidth = b;
  }
  //冻结行
  public void setFreezeRow(int row){
    this.freezeRow = row;
  }
  //冻结列
  public void setFreezeCol(int col){
    this.freezeCol = col;
  }
  //设置Excel表头字体水平
  public void setHeaderHORIZONTAL(){
    this.HeaderOrientation = 0;
  }
  //设置Excel表头字体旋转90度
  public void setHeaderPlus90(){
    this.HeaderOrientation = 90;
  }
  //设置Excel表头字体旋转45度
  public void setHeaderPlus45(){
    this.HeaderOrientation = 45;
  }
  //设置Excel表头字体旋转-90度
  public void setHeaderMinus90(){
    this.HeaderOrientation = -90;
  }
  //设置Excel表头字体旋转-45度
  public void setHeaderMinus45(){
    this.HeaderOrientation = -45;
  }

  public void setColumnWidth(Vector colWidth){
    this.colWidth = colWidth;
  }

  public void setColumnColor(Vector rColor){
    if (rColor==null)
      return;
    rowColor.removeAllElements();
    for(int i=0;i<rColor.size();i++){
      rowColor.addElement(getColourFromColor((Color)rColor.elementAt(i)));
    }
  }

  private jxl.format.Colour getColourFromColor(Color color){
    if (color==Color.red){
      return jxl.format.Colour.RED;
    }
    if (color==Color.BLACK){
      return jxl.format.Colour.BLACK;
    }
    if (color==Color.YELLOW){
      return jxl.format.Colour.YELLOW;
    }
    if (color==Color.GREEN){
      return jxl.format.Colour.GREEN;
    }
    if (color==Color.ORANGE){
      return jxl.format.Colour.ORANGE;
    }
    if (color==Color.PINK){
      return jxl.format.Colour.PINK;
    }
    if (color==Color.CYAN){
      return jxl.format.Colour.SKY_BLUE;
    }
    return jxl.format.Colour.WHITE;
  }

  private jxl.format.Alignment getAlignment(int align){
    if (align==javax.swing.SwingConstants.CENTER){
      return jxl.format.Alignment.CENTRE;
    }
    if (align==javax.swing.SwingConstants.LEFT){
      return jxl.format.Alignment.LEFT;
    }

    return jxl.format.Alignment.RIGHT;
  }

  public void setFileName(String fileName){
    this.fileName = fileName;
  }

  public boolean generateFile() {
    if (fileName==null || fileName.trim().equals("")){
      return false;
    }
    if (data==null || data.size()==0){
      return false;
    }

    WritableWorkbook workbook=null;
    try{
      workbook = Workbook.createWorkbook(new File(fileName));
      WritableSheet sheet = workbook.createSheet(sheetName, 0);
      if(freezeRow>0) sheet.getSettings().setVerticalFreeze(freezeRow);
      if(freezeCol>0) sheet.getSettings().setHorizontalFreeze(freezeCol);
      if (colWidth!=null && colWidth.size()>0){
        for(int i=0;i<colWidth.size();i++){
          sheet.setColumnView(i,((Integer)colWidth.elementAt(i)).intValue());
        }
      }
      int adjustColWidth[] = new int[((Object[])data.elementAt(0)).length];
      for(int i=0;i<adjustColWidth.length;i++){
        adjustColWidth[i] = 0;
      }

      for(int i=0;i<data.size();i++){
        Object[] row = (Object[])data.elementAt(i);
        jxl.format.Alignment align = null;
        if (((Integer)rowAlingment.elementAt(0)).intValue()==i)
          align = ((jxl.format.Alignment)rowAlingment.elementAt(1));
        else
          align = null;

        for(int col = 0;col<row.length;col++){
          //auto adjust colwidth
          if(this.HeaderOrientation!=-1){
            if(i>0){
              if (row[col].toString().length() > (int) adjustColWidth[col])
                adjustColWidth[col] = row[col].toString().length();
            }
          }else{
            if(row[col].toString().length()>(int)adjustColWidth[col])
              adjustColWidth[col] = row[col].toString().length();
          }

          if ((row[col] instanceof java.lang.String) || (row[col]==null) ){
            WritableCellFormat cellFormat = new WritableCellFormat();
            cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            //set HeaderOrientation
            if(this.HeaderOrientation==90&&i==0)
              cellFormat.setOrientation(cellFormat.getOrientation().PLUS_90);
            else if(this.HeaderOrientation==45&&i==0)
              cellFormat.setOrientation(cellFormat.getOrientation().PLUS_45);
            else if(this.HeaderOrientation==-90&&i==0)
              cellFormat.setOrientation(cellFormat.getOrientation().MINUS_90);
            else if(this.HeaderOrientation==-45&&i==0)
              cellFormat.setOrientation(cellFormat.getOrientation().MINUS_45);
            else if(this.HeaderOrientation==0&&i==0)
              cellFormat.setOrientation(cellFormat.getOrientation().HORIZONTAL);
            if (align!=null)
              cellFormat.setAlignment(align);
            if (rowColor!=null && rowColor.size()>i){
              if (rowColor.elementAt(i) instanceof jxl.format.Colour)
                cellFormat.setBackground((jxl.format.Colour)rowColor.elementAt(i));
            }
            if(row[col]==null)
              row[col]="";
            jxl.write.Label label = new jxl.write.Label(col, i, row[col].toString(),cellFormat);
            sheet.addCell(label);
            cellFormat = null;
            label = null;
          }

          if (row[col] instanceof java.lang.Integer ){
            NumberFormat fivedps = new NumberFormat(integerFormat);
            WritableCellFormat integerFormat = new WritableCellFormat(fivedps);
            integerFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            if (align!=null)
              integerFormat.setAlignment(align);
            if (rowColor!=null && rowColor.size()>i){
              if (rowColor.elementAt(i) instanceof jxl.format.Colour)
                integerFormat.setBackground((jxl.format.Colour)rowColor.elementAt(i));
            }
            jxl.write.Number number = new jxl.write.Number(col, i, ((Integer)row[col]).intValue(),integerFormat);
            sheet.addCell(number);
            integerFormat = null;
            number = null;
          }

          if (row[col] instanceof java.lang.Double ){
            NumberFormat fivedps = new NumberFormat(doubleFormat);
            WritableCellFormat doubleFormat = new WritableCellFormat(fivedps);
            doubleFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            if (align!=null)
              doubleFormat.setAlignment(align);
            if (rowColor!=null && rowColor.size()>i){
              if (rowColor.elementAt(i) instanceof jxl.format.Colour)
                doubleFormat.setBackground((jxl.format.Colour)rowColor.elementAt(i));
            }
            jxl.write.Number number = new jxl.write.Number(col, i, ((Double)row[col]).doubleValue(),doubleFormat);
            sheet.addCell(number);
            doubleFormat = null;
            number = null;
          }

          if (row[col] instanceof java.sql.Date ){
            DateFormat customDateFormat = new DateFormat (dateFormat);
            WritableCellFormat dateFormat = new WritableCellFormat (customDateFormat);
            dateFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            if (align!=null)
              dateFormat.setAlignment(align);
            if (rowColor!=null && rowColor.size()>i){
              if (rowColor.elementAt(i) instanceof jxl.format.Colour)
                dateFormat.setBackground((jxl.format.Colour)rowColor.elementAt(i));
            }
            jxl.write.DateTime dateCell = new jxl.write.DateTime(col, i, new java.util.Date(((java.sql.Date)row[col]).getTime()), dateFormat);
            sheet.addCell(dateCell);
            dateCell = null;
          }

          if (row[col] instanceof java.lang.Boolean ){
            WritableCellFormat cellFormat = new WritableCellFormat();
            cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            if (align!=null)
              cellFormat.setAlignment(align);
            if (rowColor!=null && rowColor.size()>i){
              if (rowColor.elementAt(i) instanceof jxl.format.Colour)
                cellFormat.setBackground((jxl.format.Colour)rowColor.elementAt(i));
            }
            jxl.write.Label label = new jxl.write.Label(col, i, row[col].toString(),cellFormat);
            sheet.addCell(label);
            cellFormat = null;
            label = null;
          }
        }
        row = null;
        if(i%1000==0)System.gc();
      }
      if(this.autoAdjustColWidth){
        for (int i = 0; i < adjustColWidth.length; i++) {
          if(((int)adjustColWidth[i])==0){
            sheet.setColumnView(i,3);
          }else{
            sheet.setColumnView(i, ((int) adjustColWidth[i])+2);
          }
        }
      }
      workbook.write();
    }catch(Exception e){
      e.printStackTrace();
      return false;
    }finally{
      try{
        workbook.close();
      }catch(Exception e){}
    };

    return true;
  }
  public static void main(String[] args) {
    Vector vec = new Vector();
    vec.insertElementAt(new Object[]{"Col 1","Col 2","Col 3","Col 4","Col 5"},0);
    for(int i=0;i<20;i++){
      Object obj[] = new Object[5];
      obj[0] = "("+String.valueOf(i+1)+",1)";
      obj[1] = "("+String.valueOf(i+1)+",2)";
      obj[2] = "("+String.valueOf(i+1)+",3)";
      obj[3] = "("+String.valueOf(i+1)+",4)";
      obj[4] = "("+String.valueOf(i+1)+",5)";
      vec.add(obj);
    }
    ExcelOutput ot = new ExcelOutput("c:/test.xls", vec);
    Vector w = new Vector();
    for(int j=0;j<5;j++){
      w.addElement(new Integer(((Object[])vec.get(0))[j].toString().length()*1+3));
    }
    ot.setColumnWidth(w);
    Vector color =new Vector();
    color.addElement(Color.CYAN);
    for(int j=0;j<vec.size()-1;j++){
     color.addElement(Color.WHITE);
    }
    ot.setColumnColor(color);
    ot.setDateFormat("yyyy-MM-dd");
    Vector headAlign = new Vector();
    headAlign.addElement(new Integer(0));
    headAlign.addElement(new Integer(javax.swing.SwingConstants.CENTER));
    ot.setRowAlignment(headAlign);
    ot.setFreezeRow(1);
    ot.setSheetName("Sheet1");
    ot.generateFile();
  }
}
