package main;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.*;
import java.awt.*;
import java.util.Vector;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class RowRenderer extends DefaultTableCellRenderer {
  private Vector modifyRows;
  private Color color = Color.WHITE;
  private Vector colorMore;
  private Vector cellColor;
  private boolean isCol=false;
  public RowRenderer(Vector rows) {
    setModifyRowsID(rows,color);
  }
  public RowRenderer() {

  }
  public Component getTableCellRendererComponent(JTable table,
                                    Object value,boolean isSelected,
                                    boolean hasFoucs,int row,int column){
    setBackground(Color.WHITE);
    setForeground(Color.BLACK);
    if (modifyRows!=null){
      for(int i=0;i<modifyRows.size();i++){
        int mRow=-1;
        int mCol=-1;
        Object[] rowColor=null;
        if (modifyRows.elementAt(i) instanceof java.lang.Integer){
          mRow = ((Integer)modifyRows.elementAt(i)).intValue();
          mCol = ((Integer)modifyRows.elementAt(i)).intValue();
        }
        if (modifyRows.elementAt(i) instanceof java.lang.Object[]){
          rowColor = (Object[])modifyRows.elementAt(i);
          mRow = ((Integer)rowColor[0]).intValue();
          mCol = ((Integer)rowColor[0]).intValue();
          color = (Color)rowColor[1];
        }

        if (!isCol){
          if (row==mRow){
            setBackground(color);
            setForeground(getTextColor(color));
          }
        }else{
          if (column==mRow){
            try{
              setBackground((Color)colorMore.elementAt(i));
              setForeground(getTextColor((Color)colorMore.elementAt(i)));
            }catch(Exception ee){
              setBackground(color);
              setForeground(getTextColor(color));
            }
          }
        }
      }
    }
    if (cellColor!=null){
      for(int i=0;i<cellColor.size();i++){
        Object[] CellColor=(Object[])cellColor.elementAt(i);

        int mRow = ((Integer)CellColor[0]).intValue();
        int mCol = ((Integer)CellColor[1]).intValue();
        Color color = (Color)CellColor[2];
        if (row==mRow && column==mCol){
          setBackground(color);
          setForeground(getTextColor(color));
        }
      }
    }

    return super.getTableCellRendererComponent(table,value,isSelected,hasFoucs,row,column);
  }

  private Color getTextColor(Color bColor) {
    int r = bColor.getRed();
    int g = bColor.getGreen();
    int b = bColor.getAlpha();
    int v=0;
    if (r < 10)
      v++;
    if (g < 10)
      v++;
    if (b < 10)
      v++;

    //if (r < 90 && g < 90 && b < 90) {
    if (v>1){
      return Color.white;
    }
    else {
      return Color.BLACK;
    }
  }

  public void setModifyRowsID(Vector rows,Color color){
    setNewBackgroundColor(color);
    this.modifyRows = rows;
  }
  public void setModifyRowsID(Vector rows,Vector color){
    colorMore=color;
    this.modifyRows = rows;
  }

  public void setModifyRowsIDEachColor(Vector rowsColor){
    this.modifyRows = rowsColor;
  }

  public void setCellColor(Vector cellColor){
    this.cellColor = cellColor;
  }

  public void setIsCol(boolean iscol){
    this.isCol=iscol;
  }

  public void setNewBackgroundColor(Color color){
    this.color = color;
  }
}
