package inbound;

import java.util.Vector;

import DBUtil.DBOperator;
import comUtil.comData;
import dmdata.DataManager;

public class GenerateINVFromStockTakeDetail {
	
	public static void main(String[] args){
		GenerateINVFromStockTakeDetail generateInv = new GenerateINVFromStockTakeDetail();
		//注意。。。。。。。。。。。。。。。。执行前需要再次确认
		//generateInv.generateInventory("ST00000001");
		generateInv.generateInventoryByTmpInv();
	}
	
	public static boolean generateInventory(String stockTakeNo,String INV_STOCKTAKE_DETAIL_ID){
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
		String sql = "select isd.STOCKTAKE_NO,isd.WAREHOUSE_CODE,isd.STORER_CODE,isd.ITEM_CODE,isd.CONTAINER_CODE,isd.LOCATION_CODE  "
				+",'' LOTTABLE01,'' LOTTABLE02,'' LOTTABLE03,'' LOTTABLE04,'' LOTTABLE05,'' LOTTABLE06,'' LOTTABLE07,'' LOTTABLE08,'' LOTTABLE09,'' LOTTABLE10 "
				+",isd.CONF_QTY,isd.CONF_UOM "
				+"from inv_stocktake_detail isd where isd.STOCKTAKE_NO='"+stockTakeNo+"' ";
		if(!INV_STOCKTAKE_DETAIL_ID.equals("")){
			sql = sql + " and isd.INV_STOCKTAKE_DETAIL_ID='"+INV_STOCKTAKE_DETAIL_ID+"' ";
		}
		DataManager dm = DBOperator.DoSelect2DM(sql);
		for(int i=0;i<dm.getCurrentCount();i++){
			warehouseCode = dm.getString("WAREHOUSE_CODE", i);
			storerCode = dm.getString("STORER_CODE", i);
			itemCode = dm.getString("ITEM_CODE", i);
			locationCode = dm.getString("LOCATION_CODE", i);
			containCode = dm.getString("CONTAINER_CODE", i);
			receivedQty = dm.getString("CONF_QTY", i);
			receivedUOM = dm.getString("CONF_UOM", i);
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
			lotNo = comData.getInventoryLotNo(storerCode,itemCode,lottable01,lottable02,lottable03,lottable04,lottable05,lottable06,lottable07,lottable08,lottable09,lottable10);
			if(!lotNo.equals("")){
				//插入库存表
				inventoryID = comData.getInventoryID(warehouseCode,storerCode,itemCode,lotNo,locationCode,containCode,receivedQty,"sys");
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
	
	//根据盘点数据导入库存（嘉定盘点）
	public static boolean generateInventoryByTmpInv(){
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
		String locationCode = "J1-01-01-01";
		String containCode = "*";
		String receivedQty = "";
		String receivedUOM = "007";
		String sql = "select 'SHJD' 仓库编码,storer_code 货主,sku 料号,inv_qty 理论入库数据 "
				+"from tmp5 ";//tmp_inv
		DataManager dm = DBOperator.DoSelect2DM(sql);
		for(int i=0;i<dm.getCurrentCount();i++){
			warehouseCode = dm.getString("仓库编码", i);
			storerCode = dm.getString("货主", i);
			itemCode = dm.getString("料号", i);
			receivedQty = dm.getString("理论入库数据", i);
			//先生成库存批次号
			lotNo = comData.getInventoryLotNo(storerCode,itemCode,lottable01,lottable02,lottable03,lottable04,lottable05,lottable06,lottable07,lottable08,lottable09,lottable10);
			if(!lotNo.equals("")){
				//插入库存表
				inventoryID = comData.getInventoryID(warehouseCode,storerCode,itemCode,lotNo,locationCode,containCode,receivedQty,"sys");
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

}
