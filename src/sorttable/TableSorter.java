package sorttable;

import java.awt.*;
import java.sql.Date;
import javax.swing.*;
import javax.swing.table.*;

/**
 * @version 1.0 02/25/99
 */
public class TableSorter {
  SortTableModel model;

  public TableSorter(SortTableModel model) {
    this.model = model;
  }

  //n2 selection
  public void sort(int column, boolean isAscent) {
    new PrintDebugInfo("in TableSorter---->Sort");
    int n;
    if (model instanceof main.thisModel && ((main.thisModel)model).keyCol !=null){
      System.out.println("it's common setup frame sort");
      n = model.getRowCount() - 1;
    }else
      n = model.getRowCount();
    int[] indexes = model.getIndexes();

    for (int i=0; i<n-1; i++) {
      int k = i;
      for (int j=i+1; j<n; j++) {
        if (isAscent) {
          if (compare(column, j, k) < 0) {
            k = j;
          }
        } else {
          if (compare(column, j, k) > 0) {
            k = j;
          }
        }
      }
      int tmp = indexes[i];
      indexes[i] = indexes[k];
      indexes[k] = tmp;
    }
  }


  // comparaters

  public int compare(int column, int row1, int row2) {
    new PrintDebugInfo("in TableSorter--->compare");
    Object o1 = model.getValueAt(row1, column);
    Object o2 = model.getValueAt(row2, column);
    if (o1 == null && o2 == null) {
      return  0;
    } else if (o1 == null) {
      return -1;
    } else if (o2 == null) {
      return  1;
    } else {
      if (model.getColumnTypeForSort() == null) {
        Class type = model.getColumnClass(column);
        if (type.getSuperclass() == Number.class) {
          return compare( (Number) o1, (Number) o2);
        }
        else if (type == String.class) {
          return compare( (String) o1, (String) o2);
        }
        else if (type == Date.class) {
          return compare( (Date) o1, (Date) o2);
        }
        else if (type == Boolean.class) {
          return compare( (Boolean) o1, (Boolean) o2);
        }
        else {
          return ( (String) o1).compareTo( (String) o2);
        }
      }else{
        String type = model.getColumnTypeForSortCol(column);
        if (type.equals(SortTableModel.sortStringType)) {
          return ( (String) o1).compareTo( (String) o2);
        }
        else if (type.equals(SortTableModel.sortDateType)) {
          try{
            Date de1 = getDateFromString(o1.toString());
            Date de2 = getDateFromString(o2.toString());
            return compare( (Date) de1, (Date) de2);
          }catch(Exception ex){
            return 0;
          }
        }
        else if (type.equals(SortTableModel.sortNumType)) {
          try{
            Double d1 = new Double(myStringToConvert(o1.toString()));
            Double d2 = new Double(myStringToConvert(o2.toString()));
            return compare( (Number) d1, (Number) d2);
          }catch(Exception ex){
            return 0;
          }
        }
        else if (type.equals(SortTableModel.sortBooleanType)) {
          return compare( (Boolean) o1, (Boolean) o2);
        }
        else {
          return ( (String) o1).compareTo( (String) o2);
        }
      }
    }
  }
  public static java.sql.Date getDateFromString(String date){
	   java.text.SimpleDateFormat formatter
	     = new java.text.SimpleDateFormat ("yyyy-MM-DD");
	   java.text.ParsePosition pos = new java.text.ParsePosition(0);
	   java.util.Date dt = formatter.parse(date,pos);

	   if ( dt == null ) return null;

	   java.sql.Date currentTime = new java.sql.Date(dt.getTime());

	   if ( getDate2String (currentTime).compareTo(date) != 0 )
	    currentTime = null;

	   return currentTime;
	  }
  
  public static String getDate2String(java.sql.Date date){
	    if ( date == null ) return "";
	   // Format the current time.
	   java.text.SimpleDateFormat formatter
	     = new java.text.SimpleDateFormat ("yyyy-MM-DD");
	   String dateString = formatter.format(date);
	   return dateString;
	  }

  public double myStringToConvert(String TextString)
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


  public int compare(String o1, String o2) {
    if (o1.toUpperCase().compareTo(o2.toUpperCase()) < 0) {
      return -1;
    } else if (o1.toUpperCase().compareTo(o2.toUpperCase()) > 0) {
      return 1;
    } else {
      return 0;
    }
  }


  public int compare(Number o1, Number o2) {
    new PrintDebugInfo("in TableSorter--->compare Number");
    double n1 = o1.doubleValue();
    double n2 = o2.doubleValue();
    if (n1 < n2) {
      return -1;
    } else if (n1 > n2) {
      return 1;
    } else {
      return 0;
    }
  }

  public int compare(java.sql.Date o1, java.sql.Date o2) {
    new PrintDebugInfo("in TableSorter--->compare Date");
    long n1 = o1.getTime();
    long n2 = o2.getTime();
    if (n1 < n2) {
      return -1;
    } else if (n1 > n2) {
      return 1;
    } else {
      return 0;
    }
  }

  public int compare(Boolean o1, Boolean o2) {
    new PrintDebugInfo("in TableSorter--->compare Boolean");
    boolean b1 = o1.booleanValue();
    boolean b2 = o2.booleanValue();
    if (b1 == b2) {
      return 0;
    } else if (b1) {
      return 1;
    } else {
      return -1;
    }
  }
}
