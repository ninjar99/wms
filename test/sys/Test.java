package sys;

import DBUtil.DBOperator;
import DBUtil.LogInfo;
import dmdata.DataManager;

public class Test {

	public static void main(String[] args){
		//临时用来 计算库存   锁库 - 出库 = 当前剩余库存
//		String sql = "select t.sku,sum(t.outbound_qty) qty from tmp3 t where t.inv_qty=0 and t.inbound_qty=0 group by t.sku";
//		DataManager dm = DBOperator.DoSelect2DM(sql);
//		for(int i=0;i<dm.getCurrentCount();i++){
//			String sku = dm.getString("sku", i);
//			String qty = dm.getString("qty", i);
//			sql = "update tmp3 set tmp_qty = tmp_qty+("+qty+") where sku='"+sku+"' "
//				+ "and storer_code=(select storer_code from bas_item where item_code='"+sku+"' limit 1)";
//			int t = DBOperator.DoUpdate(sql);
//			if(t==0){
//				System.out.println("更新失败："+sql);
//			}
//		}
		
		//汇总统计三明治出库数据，然后根据WMS PO导入数据，按照出库数据 更新 出库数量
		String sql = "select t.storer_code,t.sku,sum(t.qty) qty from tmp1 t group by t.storer_code,t.sku";
		DataManager dm = DBOperator.DoSelect2DM(sql);
		for(int i=0;i<dm.getCurrentCount();i++){
			String storer_code = dm.getString("storer_code", i);
			String sku = dm.getString("sku", i);
			String qty = dm.getString("qty", i);
			sql = "update tmp2 set outbound_qty = ifnull(outbound_qty,0)+("+qty+") where item_code='"+sku+"' "
				+ "and storer_code='"+storer_code+"'";
			int t = DBOperator.DoUpdate(sql);
			if(t==0){
				sql = "update tmp2 set outbound_qty = ifnull(outbound_qty,0)+("+qty+") where item_code='"+sku+"' "
					+ "and storer_code=(select storer_code from (select storer_code from tmp2 where item_code='"+sku+"' limit 1) t)";
				t = DBOperator.DoUpdate(sql);
				if(t==0){
					System.out.println("更新失败："+sql);
					LogInfo.appendLog("tmp2",sql);
				}
				
			}
		}
		
	}
}
