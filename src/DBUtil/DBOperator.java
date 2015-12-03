package DBUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import dmdata.DataManager;
import dmdata.ListData;
import dmdata.xArrayList;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DBOperator {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Vector DoSelect(String sql){
		java.util.Vector alldata = new java.util.Vector();
		java.sql.Connection con = DBConnectionManager.getInstance().getConnection("wms");
		java.sql.Statement stmt;
		try {
			stmt = con.createStatement();
			System.out.println(sql);
			LogInfo.appendLog("sql",sql);
			ResultSet rs = stmt.executeQuery(sql);
			java.sql.ResultSetMetaData rsmd = rs.getMetaData();
	        String[] colnames = new String[rsmd.getColumnCount()];
	        for (int i = 1 ; i <= rsmd.getColumnCount() ; i++){
	          colnames[i-1] = rsmd.getColumnName(i);
	        }
	        while(rs.next()){
	          Object[] rowdata = new Object[rsmd.getColumnCount()];
	          for ( int i = 1;i <= rsmd.getColumnCount() ;i++) {
	            rowdata[i-1] = (rs.getObject(i));
	          }
	          alldata.addElement(rowdata);
	        }
	        rs.close();
	        stmt.close();
	        System.out.println("column:"+colnames.length+" ; row:"+alldata.size());
	        LogInfo.appendLog("sql","column:"+colnames.length+" ; row:"+alldata.size());
//	        DBConnectionManager.getInstance().freeConnection("wms", con);
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return alldata;
	}
	
	public static DataManager DoSelect2DM(String sql){
		DataManager dm = new DataManager();
		java.sql.Connection con = DBConnectionManager.getInstance().getConnection("wms");
		Statement stmt;
		try {
			System.out.println(sql);
			LogInfo.appendLog("sql",sql);
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			java.sql.ResultSetMetaData rsmd = rs.getMetaData();
			if (rsmd.getColumnCount()==0){
				return null;
			}
	        String[] colnames = new String[rsmd.getColumnCount()];
	        for (int i = 1 ; i <= rsmd.getColumnCount() ; i++){
	          colnames[i-1] = rsmd.getColumnName(i);
	          dm.setCol(i-1,rsmd.getColumnLabel(i));
	        }
	        
	        while(rs.next()){
	          Object[] rowdata = new Object[rsmd.getColumnCount()];
	          for ( int i = 1;i <= rsmd.getColumnCount() ;i++) {
	            rowdata[i-1] = ChangeTimeStamp2Date(rs.getObject(i));
	          }
	          dm.AddNewRows(rowdata);
	        }
	        System.out.println("column:"+colnames.length+" ; row:"+dm.getCurrentCount());
	        LogInfo.appendLog("sql","column:"+colnames.length+" ; row:"+dm.getCurrentCount());
	        rs.close();
	        stmt.close();
//	        DBConnectionManager.getInstance().freeConnection("wms", con);
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return dm;
	}
	
	/*
	 * DoSelectWithSequenceRow 该方法在sql查询出的数据前面增加一列序号列.
	 * 所以会多出一列序号列
	 */
	public static Vector DoSelectAddSequenceRow(String sql){
		java.util.Vector alldata = new java.util.Vector();
		java.sql.Connection con = DBConnectionManager.getInstance().getConnection("wms");
		java.sql.Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			System.out.println(sql);
			LogInfo.appendLog("sql",sql);
			java.sql.ResultSetMetaData rsmd = rs.getMetaData();
	        String[] colnames = new String[rsmd.getColumnCount()];
	        for (int i = 1 ; i <= rsmd.getColumnCount() ; i++){
	          colnames[i-1] = rsmd.getColumnName(i);
	        }
	        System.out.println(colnames);
	        int seq = 0;
	        while(rs.next()){
	          seq++;
	          Object[] rowdata = new Object[rsmd.getColumnCount()+1];
	          rowdata[0] = seq;
	          for ( int i = 1;i <= rsmd.getColumnCount() ;i++) {
	            rowdata[i] = (rs.getObject(i));
	          }
	          alldata.addElement(rowdata);
	        }
	        System.out.println("column:"+colnames.length+" ; row:"+alldata.size());
	        LogInfo.appendLog("sql","column:"+colnames.length+" ; row:"+alldata.size());
	        rs.close();
	        stmt.close();
//	        DBConnectionManager.getInstance().freeConnection("wms", con);
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return alldata;
	}
	
	public static int DoUpdate(String sql){
		int ret = 0;
		java.util.Vector alldata = new java.util.Vector();
		java.sql.Connection con = DBConnectionManager.getInstance().getConnection("wms");
		java.sql.Statement stmt;
		try {
			System.out.println(sql);
			LogInfo.appendLog("sql",sql);
			stmt = con.createStatement();
			ret = stmt.executeUpdate(sql);
	        stmt.close();
	        System.out.println("updated row:"+ret);
	        LogInfo.appendLog("sql","updated row:"+ret);
//	        DBConnectionManager.getInstance().freeConnection("wms", con);
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	public static String DataManager2JSONString(DataManager dm,String jsonRoot){
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonAllArray = new JSONArray();
		if(dm==null || dm.getCurrentCount()==0){
			return "";
		}
		for(int i=0;i<dm.getCurrentCount();i++){
			JSONObject jsonArray = new JSONObject();
			for(int j=0;j<dm.getColCount();j++){
				jsonArray.put(dm.getCol(j), dm.getObject(j, i).toString());
			}
			jsonAllArray.add(jsonArray);
		}
		jsonObject.put(jsonRoot, jsonAllArray);
		LogInfo.appendLog(jsonObject.toString());
		return jsonObject.toString();
	}
	
	private static Object ChangeTimeStamp2Date(Object obj) {
		if (obj == null)
			return "";
		if (obj != null) {
			if (obj.getClass().getName().compareToIgnoreCase("java.sql.Timestamp") == 0) {
				obj = Timestamp2Date((Timestamp) obj);
			}
		}
		return obj;
	}

	private java.sql.Timestamp Date2Timestamp(java.sql.Date date) {
		return date == null ? null : Timestamp.valueOf(date.toString() + " 00:00:00");
	}

	private static java.sql.Date Timestamp2Date(java.sql.Timestamp timestamp) {
		return timestamp == null ? null : new java.sql.Date(timestamp.getTime());
	}
	
	public static String refFormatNowDate() {
		Date nowTime = new Date(System.currentTimeMillis());
		SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String retStrFormatNowDate = sdFormatter.format(nowTime);
		return retStrFormatNowDate;
	}
	
	public static DataManager getTableHeader2DataManager(String table){
		DataManager dm = new DataManager();
		if(table.equals("")){
			return dm;
		}
		try{
		String sql = "select * from "+table+" where 1<>1";
		dm = DBOperator.DoSelect2DM(sql);
		String[] rowdata = new String[dm.getColCount()];
		dm.AddNewRow(rowdata);
		}catch(Exception e){
			return dm;
		}
		return dm;
	}
	
	public static boolean saveTableRow(String table,String keyName,DataManager dm){
		StringBuffer sbf = new StringBuffer();
		String sql = "select "+keyName+" from "+table+" where 1=1 ";
		for(int k=0;k<dm.getCurrentCount();k++){
			String keyValue = dm.getString(keyName, k);
			sql = sql +" and "+keyName+" = '"+keyValue+"'";
			Vector vec = DBOperator.DoSelect(sql);
			if(vec.size()==0){
				//insert into
				sbf.append("insert into "+table+"(");
				for(int i=0;i<dm.getColCount();i++){
					if(dm.getString(i, 0).trim().equals("")) {
						continue;
					}
					sbf.append(dm.getCol(i));
					if(i<dm.getColCount()-1){
						sbf.append(",");
					}
				}
				//删除最后一个,字符
				if(sbf.toString().endsWith(",")){
					sbf.delete(sbf.lastIndexOf(","), sbf.length());
				}
				sbf.append(") ");

				sbf.append("\n select ");
				for(int i=0;i<dm.getColCount();i++){
					if(dm.getString(i, 0).trim().equals("")) {
						continue;
					}
					//如果是null或者日期函数，需要去掉 单引号
					if (dm.getString(i, k).equalsIgnoreCase("null")
							|| dm.getString(i, k).equalsIgnoreCase("now()")
							|| dm.getString(i, k).equalsIgnoreCase("UNIX_TIMESTAMP()")) {
						//主键ID插入NULL
						if(dm.getCol(i).equalsIgnoreCase(keyName)){
							sbf.append("null");
						}else{
							sbf.append(dm.getString(i, k).equals("")?"null":dm.getString(i, k));
						}
					} else {
						//主键ID插入NULL
						if(dm.getCol(i).equalsIgnoreCase(keyName)){
							sbf.append("null");
						}else{
							sbf.append(dm.getString(i, k).equals("")?"null":"'"+dm.getString(i, k)+"'");
						}
					}
					if(i<dm.getColCount()-1){
						sbf.append(",");
					}
				}
				//删除最后一个,字符
				if(sbf.toString().endsWith(",")){
					sbf.delete(sbf.lastIndexOf(","), sbf.length());
				}
				int t = DBOperator.DoUpdate(sbf.toString());
				if(t>0){
					continue;
				}else{
					return false;
				}
			}else{
				//update
				int cycle = 0;
				sbf.append("update "+table+" set ");
				for(int i=0;i<dm.getColCount();i++){
					if(dm.getString(i, k).equals("")) continue;
					if(dm.getCol(i).equalsIgnoreCase(keyName)) continue;
					//如果是null或者日期函数，需要去掉 单引号
					cycle++;
					if(i<dm.getColCount() && cycle>1){
						sbf.append(",");
						cycle --;
					}
					if(dm.getString(i, k).equalsIgnoreCase("null") || dm.getString(i, k).equalsIgnoreCase("now()")){
						sbf.append(dm.getCol(i)+" = "+dm.getString(i, k));
					}else{
						sbf.append(dm.getCol(i)+" = '"+dm.getString(i, k)+"'");
					}
					
				}
				sbf.append(" where "+keyName+" ='"+dm.getString(keyName, k)+"'");
				int t = DBOperator.DoUpdate(sbf.toString());
				if(t>0){
					continue;
				}else{
					return false;
				}
			}
		}
		return true;
	}
	
	public static boolean saveTableData(String tableName,String primaryColName,String[] colNames,String[] colValues){
		DataManager dmSaveTable = DBOperator.getTableHeader2DataManager(tableName);
		dmdata.xArrayList list = (xArrayList) dmSaveTable.getRow(0);
		for(int i=0;i<colNames.length;i++){
			list.set(dmSaveTable.getCol(colNames[i]), colValues[i]);
		}
		dmSaveTable.RemoveRow(0);
		dmSaveTable.AddNewRow(list);
		boolean bool = DBOperator.saveTableRow(tableName,primaryColName, dmSaveTable);
		return bool;
	}
	
	public static void main(String[] args){
		
//		saveTableData("bas_item_bak","STORER_CODE",
//				new String[]{"STORER_CODE","MATRRIAL_NUM","OLD_MATRRIAL_NUM"},
//				new String[]{"11","22","33"});
		
//		for (int k = 0; k < 1000; k++) {
//			DataManager dm = new DataManager();
//			dm = DoSelect2DM("select * from inb_receipt_header limit 10 ");
//			for (int i = 0; i < dm.getCurrentCount(); i++) {
//				Object[] row = new Object[dm.getColCount()];
//				row[0] = dm.getString("receipt_no", i);
//				row[0] = dm.getString("po_no", i);
//			}
//			System.out.println(refFormatNowDate());
//		}
		
//		DataManager dm = new DataManager();
//	    dm.addCols(new String[] {
//	        "CompanyID",
//	        "EntityID"
//	    });
//	    for(int i=0;i<data.size();i++){
//	      Object[] row = (Object[])data.elementAt(i);
//	      dm.AddNewRow(row);
//	    }
	}
}
