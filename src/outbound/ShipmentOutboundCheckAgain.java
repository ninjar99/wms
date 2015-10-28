package outbound;

import DBUtil.DBOperator;
import dmdata.DataManager;
import sys.MainFrm;

public class ShipmentOutboundCheckAgain {
	public static void main(String[] args){
		StringBuffer sb = new StringBuffer();
		String sql = "select TRANSFER_ORDER_NO from oub_shipment_header where status='800' ";
		DataManager dm = DBOperator.DoSelect2DM(sql);
		for(int i=0;i<dm.getCurrentCount();i++){
			String TRANSFER_ORDER_NO = dm.getString("TRANSFER_ORDER_NO", i);
			//更新订单明细状态
			sql = "update oub_shipment_detail set `STATUS`='800' "
					+" where SHIPMENT_NO=(select SHIPMENT_NO from oub_shipment_header where TRANSFER_ORDER_NO='"+TRANSFER_ORDER_NO+"') "
					+" and status<'800' ";
			int t = DBOperator.DoUpdate(sql);
			if(t==0){
				sb.append("运单号："+TRANSFER_ORDER_NO+" 明细状态更新失败，此行数据忽略\n");
				continue;
			}
			//更新库存分配数量和拣货数量
			sql = "update inv_inventory ii "
				+"inner join oub_pick_detail opd on ii.INV_INVENTORY_ID=opd.INV_INVENTORY_ID  "
				+"inner join oub_shipment_header osh on osh.WAREHOUSE_CODE=opd.WAREHOUSE_CODE and osh.SHIPMENT_NO=opd.SHIPMENT_NO "
				+" set ii.ON_HAND_QTY=ii.ON_HAND_QTY-(opd.PICKED_QTY),ii.PICKED_QTY=ii.PICKED_QTY-(opd.PICKED_QTY),ii.OUB_TOTAL_QTY=ii.OUB_TOTAL_QTY+opd.PICKED_QTY "
				+",ii.UPDATED_DTM_LOC=now(),ii.UPDATED_BY_USER='sys' "
				+"where osh.TRANSFER_ORDER_NO='"+TRANSFER_ORDER_NO+"' "
				+"";
			t = DBOperator.DoUpdate(sql);
			if(t==0){
				sb.append("运单号："+TRANSFER_ORDER_NO+" 扣减库存失败，此行数据忽略\n");
				//扣除库存失败    订单状态更新为700
				sql = "update oub_shipment_header set status='700',CHECK_BY_USER='"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"'"
						+",CHECK_DTM_LOC=now(),UPDATED_BY_USER='"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"',UPDATED_DTM_LOC=now() "
						+" where TRANSFER_ORDER_NO='"+TRANSFER_ORDER_NO+"' ";
				t = DBOperator.DoUpdate(sql);
				continue;
			}
		}
	}

}
