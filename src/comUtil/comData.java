package comUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import DBUtil.DBConnectionManager;
import DBUtil.DBOperator;
import DBUtil.LogInfo;
import dmdata.DataManager;
import dmdata.xArrayList;
import main.PBSUIBaseGrid;
import sys.MainFrm;

public class comData {

	public synchronized static String getValueFromBasNumRule(String table,String fieldname){
		String retValue = "";
		String sql = "select current_id,rule_expr,now() strdate from bas_num_rule where table_name='"+table+"' and field_name='"+fieldname+"' ";
		System.out.println(sql);
		LogInfo.appendLog("sql",sql);
		java.sql.Connection con = DBConnectionManager.getInstance().getConnection("wms");
		try {
			java.sql.Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				StringBuffer sb = new StringBuffer();
				int current_id = rs.getInt("current_id");
				Date date = rs.getDate("strdate");
				String rule = rs.getString("rule_expr");
				String[] rules = rule.split("@");
				for(int i=0;i<rules.length;i++){
					if(rules[i].indexOf("DATE:")>0){
						sb.append(new SimpleDateFormat(rules[i].substring(rules[i].indexOf(":")+1, rules[i].length()-1)).format(date));
					}else if(rules[i].indexOf("SEQ:")>0){
						String tmp = "00000000000000000000"+String.valueOf(current_id);
						String seq = rules[i].substring(rules[i].indexOf(":")+1, rules[i].length()-1);
						sb.append((tmp.substring(tmp.length()-Integer.parseInt(seq),tmp.length()) ) );
					}else if(rules[i].length()>0){
						sb.append(rules[i]);
					}
				}
				sql = "update bas_num_rule set current_id=current_id+1 where table_name='"+table+"' and field_name='"+fieldname+"'";
				System.out.println(sql);
				LogInfo.appendLog("sql",sql);
				java.sql.Statement stmt2 = con.createStatement();
				int t = stmt2.executeUpdate(sql);
				if(t==1){
					retValue = sb.toString();
				}else{
					retValue = "";
				}
			}
			DBConnectionManager.getInstance().freeConnection("wms", con);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retValue = "";
		}
		
		return retValue;
	}
	
	public static boolean getUserMenuPower(String menuItem){
		DataManager dm = MainFrm.getUserInfo();
		String role = dm.getString("ROLE_NAME", 0);
		String[] roles = role.split(",");
		StringBuffer menuID = new StringBuffer(" in(");
		for(int i=0;i<roles.length;i++){
			menuID.append(roles[i].toString());
			if(i==roles.length-1){
				menuID.append(")");
			}else{
				menuID.append(",");
			}
		}
		if(menuID.toString().equals(" in()")){
			menuID = new StringBuffer("in('')");
		}
		String sql = "select menu_item from sys_user_role_menu_util where id "+menuID +" and menu_item='"+menuItem+"' ";
		Vector vec = DBOperator.DoSelect(sql);
		if(vec==null || vec.size()==0){
			return false;
		}else{
			return true;
		}
	}
	
	public static void updateUserInfo(){
		//用户权限更新
		String userCode = MainFrm.getUserInfo().getString("USER_CODE", 0);
		String sql = "select WAREHOUSE_CODE,USER_CODE,USER_NAME,ROLE_NAME from sys_user where user_code= '"+userCode+"' ";
		DataManager dm = DBOperator.DoSelect2DM(sql);
		if(dm==null || dm.getCurrentCount()==0){
		}else{
			MainFrm.setUserInfo(dm);
		}
	}
	
	public static boolean generateInventory(String receiptNo,String userCode){
		String warehouseCode ="";
		String storerCode = "";
		String itemCode = "";
		String lotNo = "";
		String inventoryID = "";
		String lottable01 = "";
		String lottable02 = "";
		String lottable03 = "";
		String lottable04 = "";
		String lottable05 = "";
		String lottable06 = "";
		String lottable07 = "";
		String lottable08 = "";
		String lottable09 = "";
		String lottable10 = "";
		String locationCode = "";
		String containCode = "";
		String receivedQty = "";
		String receivedUOM = "";
		String sql = "select RECEIPT_NO,WAREHOUSE_CODE,STORER_CODE,ITEM_CODE,CONTAINER_CODE,LOCATION_CODE,"
				+"LOTTABLE01,LOTTABLE02,LOTTABLE03,LOTTABLE04,LOTTABLE05,LOTTABLE06,LOTTABLE07,LOTTABLE08,LOTTABLE09,LOTTABLE10,"
				+"RECEIVED_QTY,RECEIVED_UOM "
		+"from inb_receipt_detail "
		+"where receipt_no='"+receiptNo+"' ";
		DataManager dm = DBOperator.DoSelect2DM(sql);
		for(int i=0;i<dm.getCurrentCount();i++){
			warehouseCode = dm.getString("WAREHOUSE_CODE", i);
			storerCode = dm.getString("STORER_CODE", i);
			itemCode = dm.getString("ITEM_CODE", i);
			locationCode = dm.getString("LOCATION_CODE", i);
			containCode = dm.getString("CONTAINER_CODE", i);
			receivedQty = dm.getString("RECEIVED_QTY", i);
			receivedUOM = dm.getString("RECEIVED_UOM", i);
			lottable01 = dm.getString("LOTTABLE01", i);
			lottable02 = dm.getString("LOTTABLE02", i);
			lottable03 = dm.getString("LOTTABLE03", i);
			lottable04 = dm.getString("LOTTABLE04", i);
			lottable05 = dm.getString("LOTTABLE05", i);
			lottable06 = dm.getString("LOTTABLE06", i);
			lottable07 = dm.getString("LOTTABLE07", i);
			lottable08 = dm.getString("LOTTABLE08", i);
			lottable09 = dm.getString("LOTTABLE09", i);
			lottable10 = dm.getString("LOTTABLE10", i);
			//先生成库存批次号
			lotNo = getInventoryLotNo(storerCode,itemCode,lottable01,lottable02,lottable03,lottable04,lottable05,lottable06,lottable07,lottable08,lottable09,lottable10);
			if(!lotNo.equals("")){
				//插入库存表
				inventoryID = getInventoryID(warehouseCode,storerCode,itemCode,lotNo,locationCode,containCode,receivedQty,userCode);
				if(!inventoryID.equals("")){
					//库存表写入成功
					continue;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}
		return true;
	}
	
	public static String getInventoryLotNo(String storerCode,String itemCode,String lot1,String lot2,String lot3,String lot4,String lot5,String lot6,String lot7,String lot8,String lot9,String lot10){
		String lotNo = "";
		String sql = "select LOT_NO from inv_lot where STORER_CODE='"+storerCode+"' and ITEM_CODE='"+itemCode+"' "
				+" and LOTTABLE01='"+lot1+"' and LOTTABLE02='"+lot2+"' and LOTTABLE03='"+lot3+"' and LOTTABLE04='"+lot4+"' "
				+" and LOTTABLE05='"+lot5+"' and LOTTABLE06='"+lot6+"' and LOTTABLE07='"+lot7+"' and LOTTABLE08='"+lot8+"' "
				+" and LOTTABLE09='"+lot9+"' and LOTTABLE10='"+lot10+"'";
		Vector vec = DBOperator.DoSelect(sql);
		if(vec==null || vec.size()==0){
			lotNo = comData.getValueFromBasNumRule("inv_lot", "lot_no");
			sql = "insert into inv_lot(LOT_NO,STORER_CODE,ITEM_CODE,LOTTABLE01,LOTTABLE02,LOTTABLE03,LOTTABLE04,LOTTABLE05,LOTTABLE06,LOTTABLE07,LOTTABLE08,LOTTABLE09,LOTTABLE10) "
					+"select '"+lotNo+"','"+storerCode+"','"+itemCode+"','"+lot1+"','"+lot2+"','"+lot3+"','"+lot4+"','"+lot5+"' "
					+",'"+lot6+"','"+lot7+"','"+lot8+"','"+lot9+"','"+lot10+"' ";
			int t = DBOperator.DoUpdate(sql);
			if(t==1){
				return lotNo;
			}else{
				return "";
			}
		}else{
			Object[] obj = (Object[]) vec.get(0);
			lotNo = obj[0].toString();
		}
		return lotNo;
	}
	
	@SuppressWarnings("rawtypes")
	public static String getInventoryID(String warehouseCode,String storerCode,String itemCode,String lotNo,
			String locationCode,String containerCode,String onHandQty,String userCode){
		String INV_INVENTORY_ID = "";
		String sql = "select INV_INVENTORY_ID from inv_inventory "
				+"where WAREHOUSE_CODE='"+warehouseCode+"' and STORER_CODE='"+storerCode+"' and ITEM_CODE='"+itemCode+"' and LOT_NO='"+lotNo+"' "
				+" and LOCATION_CODE='"+locationCode+"' and CONTAINER_CODE='"+containerCode+"' ";
		Vector vec = DBOperator.DoSelect(sql);
		if(vec==null || vec.size()==0){
			//插入新的库存行记录
			sql = "insert into inv_inventory(WAREHOUSE_CODE,STORER_CODE,ITEM_CODE,ITEM_NAME,INV_LOT_ID,LOT_NO,LOCATION_CODE"
					+ ",CONTAINER_CODE,ON_HAND_QTY,CREATED_BY_USER,CREATED_DTM_LOC) " 
					+ "select '"+warehouseCode+"','"+storerCode+"','"+itemCode+"',(select ITEM_NAME from bas_item where storer_code='"+storerCode+"' and item_code='"+itemCode+"') "
					+",(select INV_LOT_ID from inv_lot where LOT_NO='"+lotNo+"' and STORER_CODE='"+storerCode+"' and ITEM_CODE='"+itemCode+"'),"
					+"'"+lotNo+"','"+locationCode+"','"+containerCode+"',"+onHandQty+",'"+userCode+"',now() ";
			int t = DBOperator.DoUpdate(sql);
			if(t==1){
				sql = "select INV_INVENTORY_ID from inv_inventory "
						+"where WAREHOUSE_CODE='"+warehouseCode+"' and STORER_CODE='"+storerCode+"' and ITEM_CODE='"+itemCode+"' and LOT_NO='"+lotNo+"' "
						+" and LOCATION_CODE='"+locationCode+"' and CONTAINER_CODE='"+containerCode+"' ";
				vec = DBOperator.DoSelect(sql);
				if(vec==null || vec.size()==0){
					return "";
				}else{
					Object[] obj = (Object[]) vec.get(0);
					INV_INVENTORY_ID = obj[0].toString();
					return INV_INVENTORY_ID;
				}
			}else{
				return "";
			}
		}else{
			//增加库存数量
			Object[] obj = (Object[]) vec.get(0);
			INV_INVENTORY_ID = obj[0].toString();
			sql = "update inv_inventory set ON_HAND_QTY=ON_HAND_QTY+("+onHandQty+") "
					+"where INV_INVENTORY_ID="+INV_INVENTORY_ID;
			int t = DBOperator.DoUpdate(sql);
			if(t==1){
				return INV_INVENTORY_ID;
			}else{
				return "";
			}
		}
	}
	
	public static String getLotID(String storerCode,String itemCode,String lot01,String lot02,String lot03,String lot04,String lot05,
			String lot06,String lot07,String lot08,String lot09,String lot10){
		String sql = "select LOT_NO from inv_lot where STORER_CODE='"+storerCode+"' and ITEM_CODE='"+itemCode+"' "
				+" and ifnull(LOTTABLE01,'')='"+lot01+"' and ifnull(LOTTABLE02,'')='"+lot02+"' and ifnull(LOTTABLE03,'')='"+lot03+"' "
				+" and ifnull(LOTTABLE04,'')='"+lot04+"' and ifnull(LOTTABLE05,'')='"+lot05+"' and ifnull(LOTTABLE06,'')='"+lot06+"' "
				+" and ifnull(LOTTABLE07,'')='"+lot07+"' and ifnull(LOTTABLE08,'')='"+lot08+"' and ifnull(LOTTABLE09,'')='"+lot09+"' "
				+" and ifnull(LOTTABLE10,'')='"+lot10+"' ";
		DataManager dm = DBOperator.DoSelect2DM(sql);
		if(dm==null || dm.getCurrentCount()==0){
			return "";
		}else{
			return dm.getString("LOT_NO", 0);
		}
	}
	
	public static DataManager list2DataManager(ArrayList list){
		DataManager dm = new DataManager();
		if(list.size()==0){
			return dm;
		}
		String[] rowheader = (String[]) list.get(0);
		for(int i=0;i<rowheader.length;i++){
			dm.setCol(i,rowheader[i]);
		}
		for(int i=1;i<list.size();i++){
			String[] dataRow = (String[]) list.get(i);
			dm.AddNewRow(dataRow);
		}
		System.out.println("column:"+dm.getColCount()+" ; row:"+dm.getCurrentCount());
		return dm;
	}
	
	public static DataManager jtable2DataManager(PBSUIBaseGrid table){
		DataManager dm = new DataManager();
		if(table.getRowCount()==0){
			return dm;
		}
		String[] cols = new String[table.getColumnCount()];
		String[] rowLine = new String[table.getColumnCount()];
		for(int i=0;i<table.getColumnCount();i++){
			cols[i] = table.getColumnName(i);
		}
		dm.setCols(cols);
		for(int i=0;i<table.getRowCount();i++){
			dm.AddNewRow(table.getRowData(i));
		}
		return dm;
	}
	
	public static String getTableOneColValue(String table,String returnField,String queryField,String param){
		String sql = "select "+returnField + " from "+table+" where "+queryField+" = '"+param+"' limit 1";
		DataManager dm = DBOperator.DoSelect2DM(sql);
		if(dm==null || dm.getCurrentCount()==0){
			return "";
		}else{
			return dm.getString(returnField, 0);
		}
	}
	
	//效验SQL
    public static boolean sqlValidate(String str) {
        str = str.toLowerCase();//统一转为小写
        String badStr = "'|and|exec|execute|insert|select|delete|update|count|drop|*|%|chr|mid|master|truncate|" +
                "char|declare|sitename|net user|xp_cmdshell|;|or|-|+|,|like'|and|exec|execute|insert|create|drop|" +
                "table|from|grant|use|group_concat|column_name|" +
                "information_schema.columns|table_schema|union|where|select|delete|update|order|by|count|*|" +
                "chr|mid|master|truncate|char|declare|or|;|-|--|+|,|like|//|/|%|#";//过滤掉的sql关键字，可以手动添加
        String[] badStrs = badStr.split("\\|");
        for (int i = 0; i < badStrs.length; i++) {
            if (str.indexOf(badStrs[i]) >= 0) {
                return true;
            }
        }
        return false;
    }
    
    public static DataManager getSysProcessHistoryDataModel(String table){
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
    
    public static String multipleSpaces(int n){
    	StringBuffer sb = new StringBuffer();
    	for(int i=0;i<n;i++){
    		sb.append(" ");
    	}
    	return sb.toString();
    }
	
	public static boolean addSysProcessHistory(String table,DataManager dm){
		StringBuffer sbf = new StringBuffer();
		sbf = new StringBuffer();
		sbf.append("insert into "+table+"(");
		for(int i=0;i<dm.getColCount();i++){
			sbf.append(dm.getCol(i));
			if(i<dm.getColCount()-1){
				sbf.append(",");
			}
		}
		sbf.append(") ");
		for(int k=0;k<dm.getCurrentCount();k++){
			if(k==0){
				sbf.append("\n select ");
			}else{
				sbf.append(" \n union all \n select ");
			}
			for(int i=0;i<dm.getColCount();i++){
				if(dm.getString(i, k).equalsIgnoreCase("null") || dm.getString(i, k).equalsIgnoreCase("now()")){
					sbf.append(dm.getString(i, k));
				}else{
					sbf.append("'"+dm.getString(i, k)+"'");
				}
				if(i<dm.getColCount()-1){
					sbf.append(",");
				}
			}
		}
		int t = DBOperator.DoUpdate(sbf.toString());
		if(t>0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean addTempTable(String table,DataManager dm){
		StringBuffer sbf = new StringBuffer();
		//先查找表是否存在，存在先删除表 然后创建表
		String sql = "show tables like '"+table+"'";
		DataManager dmtmp = DBOperator.DoSelect2DM(sql);
		if(dmtmp==null || dmtmp.getCurrentCount()==0){
			sbf.append("create table "+table+"( select ");
			for(int i=0;i<dm.getColCount();i++){
				sbf.append("'"+dm.getCol(i)+multipleSpaces(200)+"' "+dm.getCol(i));
				if(i<dm.getColCount()-1){
					sbf.append(",");
				}
			}
			sbf.append(") ");
			DBOperator.DoUpdate(sbf.toString());
		}else{
			sql = "drop table "+table;
			DBOperator.DoUpdate(sql);
			sbf.append("create table "+table+"( select ");
			for(int i=0;i<dm.getColCount();i++){
				sbf.append("'"+dm.getCol(i)+multipleSpaces(200)+"' "+dm.getCol(i));
				if(i<dm.getColCount()-1){
					sbf.append(",");
				}
			}
			sbf.append(") ");
			DBOperator.DoUpdate(sbf.toString());
		}
		sql = "delete from "+table;
		DBOperator.DoUpdate(sql);
		
		sbf = new StringBuffer();
		sbf.append("insert into "+table+"(");
		for(int i=0;i<dm.getColCount();i++){
			sbf.append(dm.getCol(i));
			if(i<dm.getColCount()-1){
				sbf.append(",");
			}
		}
		sbf.append(") ");
		for(int k=0;k<dm.getCurrentCount();k++){
			if(k==0){
				sbf.append("\n select ");
			}else{
				sbf.append(" \n union all \n select ");
			}
			for(int i=0;i<dm.getColCount();i++){
				if(dm.getString(i, k).equalsIgnoreCase("null") || dm.getString(i, k).equalsIgnoreCase("now()")){
					sbf.append(dm.getString(i, k));
				}else{
					sbf.append("'"+dm.getString(i, k)+"'");
				}
				if(i<dm.getColCount()-1){
					sbf.append(",");
				}
			}
		}
		int t = DBOperator.DoUpdate(sbf.toString());
		if(t>0){
			return true;
		}else{
			return false;
		}
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
	
	public static boolean saveTableData(String table,String keyName,DataManager dm){
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
					sbf.append(dm.getCol(i));
					if(i<dm.getColCount()-1){
						sbf.append(",");
					}
				}
				sbf.append(") ");

				sbf.append("\n select ");
				for(int i=0;i<dm.getColCount();i++){
					//如果是null或者日期函数，需要去掉 单引号
					if (dm.getString(i, k).equalsIgnoreCase("null")
							|| dm.getString(i, k).equalsIgnoreCase("now()")) {
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
					if(i<dm.getColCount()-1 && cycle>1){
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
	
	public static void main(String[] args){
//		DataManager dmSaveTable = getTableHeader2DataManager("bas_warehouse");
//		dmdata.xArrayList list = (xArrayList) dmSaveTable.getRow(0);
//		list.set(dmSaveTable.getCol("BAS_WAREHOUSE_ID"), "4");
//		list.set(dmSaveTable.getCol("WAREHOUSE_CODE"), "test");
//		list.set(dmSaveTable.getCol("WAREHOUSE_NAME"), "测试仓库");
//		list.set(dmSaveTable.getCol("WAREHOUSE_SHORT_NAME"), "测试仓库");
//		list.set(dmSaveTable.getCol("WAREHOUSE_TYPE"), "0");
//		list.set(dmSaveTable.getCol("PORT_NO"), "101");
//		list.set(dmSaveTable.getCol("IS_ACTIVE"), "1");
//		list.set(dmSaveTable.getCol("CREATED_BY_USER"), "sys");
//		list.set(dmSaveTable.getCol("CREATED_DTM_LOC"), "now()");
//		dmSaveTable.RemoveRow(0);
//		dmSaveTable.AddNewRow(list);
//		boolean bool = saveTableData("bas_warehouse","BAS_WAREHOUSE_ID", dmSaveTable);
		
//		//记录操作日志
//		DataManager dmProcess = getSysProcessHistoryDataModel("sys_process_history");
//		if (dmProcess!=null) {
//			dmdata.xArrayList list = (xArrayList) dmProcess.getRow(0);
//			list.set(dmProcess.getCol("SYS_PROCESS_HISTORY_ID"), "null");
//			list.set(dmProcess.getCol("QTY"), "0");
//			list.set(dmProcess.getCol("STORER_CODE"), "1117");
//			list.set(dmProcess.getCol("WAREHOUSE_CODE"), "HZ");
//			list.set(dmProcess.getCol("PROCESS_TIME"), "now()");
//			list.set(dmProcess.getCol("CREATED_DTM_LOC"), "now()");
//			list.set(dmProcess.getCol("UPDATED_DTM_LOC"), "now()");
//			dmProcess.RemoveRow(0);
//			dmProcess.AddNewRow(list);
//			boolean bool = addSysProcessHistory("sys_process_history", dmProcess);
//			System.out.println(bool);
//		}
		
//		String tmp = comData.getValueFromBasNumRule("sys_user", "user_code");
//		System.out.println(tmp);
		
//		for(int i=0;i<=100;i++){
//			String tmp = comData.getValueFromBasNumRule("bas_container", "container_code");
//			String sql = "insert into bas_container(CONTAINER_CODE,WAREHOUSE_CODE,CONTAINER_TYPE_CODE,USE_TYPE,STATUS) "+
//			"select '"+tmp+"','shysg','inb','normal','0' ";
//			DBOperator.DoUpdate(sql);
//		}
		
	}
}
