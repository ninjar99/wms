package sys;

import DBUtil.DBOperator;
import DBUtil.LogInfo;
import dmdata.DataManager;

public class Test {

	public static void main(String[] args){
		//��ʱ���� ������   ���� - ���� = ��ǰʣ����
//		String sql = "select t.sku,sum(t.outbound_qty) qty from tmp3 t where t.inv_qty=0 and t.inbound_qty=0 group by t.sku";
//		DataManager dm = DBOperator.DoSelect2DM(sql);
//		for(int i=0;i<dm.getCurrentCount();i++){
//			String sku = dm.getString("sku", i);
//			String qty = dm.getString("qty", i);
//			sql = "update tmp3 set tmp_qty = tmp_qty+("+qty+") where sku='"+sku+"' "
//				+ "and storer_code=(select storer_code from bas_item where item_code='"+sku+"' limit 1)";
//			int t = DBOperator.DoUpdate(sql);
//			if(t==0){
//				System.out.println("����ʧ�ܣ�"+sql);
//			}
//		}
		
		//����ͳ�������γ������ݣ�Ȼ�����WMS PO�������ݣ����ճ������� ���� ��������
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
					System.out.println("����ʧ�ܣ�"+sql);
					LogInfo.appendLog("tmp2",sql);
				}
				
			}
		}
		
	}
}
