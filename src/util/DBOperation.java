package util;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import dmdata.DataManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Vector;
import java.sql.ResultSetMetaData;

/***
 * Database operation Class
 * Author: KasonHe SamZheng
 */
public class DBOperation {

  private static Connection conn = null;
  private static Statement stmt = null;
  private static int cycle = 0;
  private static String server = "192.168.99.7";
  private static String port = "1433";
  private static String db = "pmmsysdb";
  private static String DBSURL = "jdbc:sqlserver://" + server + ":" + port +
      ";DatabaseName =" + db;
  private static String DBUSER = "sa";
  private static String DBPASS = "tcl96khz";
  final static String drivername = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

  public DBOperation() {
  }

  public DBOperation(String server,String port,String dbName,String user,String psd) {
    this.server = server;
    this.port = port;
    this.db = dbName;
    this.DBUSER = user;
    this.DBPASS = psd;
    DBSURL = "jdbc:sqlserver://" + server + ":" + port +
      ";DatabaseName =" + db;
    if (conn == null)
      open(DBSURL, DBUSER, DBPASS);
  }

  public static void open(String ConnectString, String user, String pass) {
    try {
      //Class.forName("com.mysql.jdbc.Driver").newInstance();
      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
      conn = DriverManager.getConnection(ConnectString, user, pass);
      stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                  ResultSet.CONCUR_READ_ONLY);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public synchronized static Vector executeSql2Vector(String sql) {
    int colNum = 0;
    ResultSet ret = null;
    Vector vec = new Vector();
    ResultSetMetaData rsm = null;
    try {
      if (conn == null) {
        System.out.println("Connection is closed!  system will open again!");
        open(DBSURL, DBUSER, DBPASS);
      }
      if (stmt == null) {
        System.out.println("Statement is closed!  system will open again!");
        open(DBSURL, DBUSER, DBPASS);
      }
      cycle++;
      System.out.println(cycle + ":" + sql);
      ret = stmt.executeQuery(sql);
      rsm = ret.getMetaData();
      colNum = rsm.getColumnCount();
      Object objName[] = new Object[colNum];
      for (int i = 0; i < colNum; i++) {
        objName[i] = rsm.getColumnName(i + 1);
      }
      vec.add(objName);
      while (ret.next()) {
        Object objValue[] = new Object[colNum];
        for (int i = 0; i < colNum; i++) {
          objValue[i] = ret.getObject(i + 1) == null ? "" : ret.getObject(i + 1);
        }
        vec.add(objValue);
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return vec;
  }

  public static DataManager executeSql2DataManager(String sql) {
    DataManager dm = new DataManager();
    Vector data = executeSql2Vector(sql);
    Object[] objHead = (Object[]) data.elementAt(0);
    for (int i = 0; i < objHead.length; i++) {
      dm.setCol(i, objHead[i].toString());
    }
    for (int i = 1; i < data.size(); i++) {
      Object[] row = (Object[]) data.elementAt(i);
      dm.AddNewRow(row);
    }
    return dm;
  }

  public synchronized static boolean updateSql(String sql) {
    boolean ret = false;
    try {
      if (conn == null) {
        System.out.println("Connection is closed!  system will open again!");
        open(DBSURL, DBUSER, DBPASS);
      }
      if (stmt == null) {
        System.out.println("Statement is closed!  system will open again!");
        open(DBSURL, DBUSER, DBPASS);
      }
      stmt.execute(sql);
      ret = true;
    }
    catch (Exception e) {
      ret = false;
      e.printStackTrace();
    }
    return ret;
  }

  public static void close() {
    try {
      if (conn != null) {
        conn.close();
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  public static void main(String[] args) {
    DBOperation dboper = new DBOperation("192.168.99.13","1433","UFDATA_998_2007","pmmteam1","123");
    DataManager dm = DBOperation.executeSql2DataManager("select * from pmm_edisource");
//    CMainApp.ShowDM(dm.getLD());
  }

}
