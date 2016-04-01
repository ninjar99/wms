package sys;

import DBUtil.DBOperator;
import dmdata.DataManager;

public class test2 {

	public static void main(String[] args) {
		String sql = "select DATE_FORMAT(a.PO_CREATED_DATE,'%Y-%m') datetime,a.STORER_CODE,b.ITEM_CODE,"
				+ "sum(b.TOTAL_QTY) inb_qty,0 oub_qty "
				+ "from inb_po_header a  "
				+ "inner join inb_po_detail b on a.PO_NO=b.PO_NO "
				+ "where a.WAREHOUSE_CODE='SHJD' and a.`STATUS`='900' and DATE_FORMAT(a.PO_CREATED_DATE,'%Y-%m')>'0000-00' "
				+ "and DATE_FORMAT(a.PO_CREATED_DATE,'%Y-%m') = '2016-03' "
				+ "group by DATE_FORMAT(a.PO_CREATED_DATE,'%Y-%m'),a.STORER_CODE,b.ITEM_CODE";
		DataManager dm = DBOperator.DoSelect2DM(sql);
		for(int i=0;i<dm.getCurrentCount();i++){
			String datetime = dm.getString("datetime", i);
			String STORER_CODE = dm.getString("STORER_CODE", i);
			String ITEM_CODE = dm.getString("ITEM_CODE", i);
			String inb_qty = dm.getString("inb_qty", i);
			String sql2 = "select item_code from inv_month "
					+ "where STORER_CODE='"+STORER_CODE+"' and ITEM_CODE='"+ITEM_CODE+"'";
			DataManager dm2 = DBOperator.DoSelect2DM(sql2);
			if(dm2.getCurrentCount()>0){
				sql2 = "update inv_month set inb_qty=inb_qty+"+inb_qty+" "
					 + "where STORER_CODE='"+STORER_CODE+"' and ITEM_CODE='"+ITEM_CODE+"'";
				DBOperator.DoUpdate(sql2);
			}else{
				sql2 = "insert into inv_month(STORER_CODE,ITEM_CODE,inb_qty) "
					+ "select '"+STORER_CODE+"','"+ITEM_CODE+"',"+inb_qty+" from dual ";
				DBOperator.DoUpdate(sql2);
			}
		}
		//³ö¿â
		sql = "select datetime,storer_code,sku,sum(qty) qty "
			+ "from tmp0330 where datetime ='2016-03' group by storer_code,sku,datetime";
		dm = DBOperator.DoSelect2DM(sql);
		for(int i=0;i<dm.getCurrentCount();i++){
			String datetime = dm.getString("datetime", i);
			String STORER_CODE = dm.getString("STORER_CODE", i);
			String sku = dm.getString("sku", i);
			String qty = dm.getString("qty", i);
			String sql2 = "select item_code from inv_month "
					+ "where STORER_CODE='"+STORER_CODE+"' and ITEM_CODE='"+sku+"'";
			DataManager dm2 = DBOperator.DoSelect2DM(sql2);
			if(dm2.getCurrentCount()>0){
				sql2 = "update inv_month set oub_qty=oub_qty+"+qty+" "
						 + "where STORER_CODE='"+STORER_CODE+"' and ITEM_CODE='"+sku+"'";
					DBOperator.DoUpdate(sql2);
			}else{
				sql2 = "insert into inv_month(STORER_CODE,ITEM_CODE,oub_qty) "
						+ "select '"+STORER_CODE+"','"+sku+"',"+qty+" from dual ";
					DBOperator.DoUpdate(sql2);
			}
		}
		
	}
}
