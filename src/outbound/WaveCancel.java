package outbound;

import DBUtil.DBOperator;
import comUtil.comData;
import dmdata.DataManager;
import sys.MainFrm;
import sys.Message;
import util.Math_SAM;

public class WaveCancel {
	
	public static void main(String[] args){
//		String str = waveCancelNew("W000000028");
//		System.out.println(str);
		invAllocateTryAgain();
	}
	
	public static String waveCancelNew(String waveNo,String shipmentNo){
		StringBuffer sbf = new StringBuffer();
		String sql = "select opd.SHIPMENT_NO,opd.SHIPMENT_LINE_NO,opd.STORER_CODE,opd.WAREHOUSE_CODE,opd.LOT_NO,opd.FROM_LOCATION_CODE,opd.FROM_CONTAINER_CODE,opd.ITEM_CODE,opd.PICKED_QTY "
				+"from oub_pick_detail opd where opd.SHIPMENT_NO in "
				+"(select SHIPMENT_NO from oub_wave_detail where WAVE_NO='"+waveNo+"') "
				+"and opd.PICKED_QTY>0 and opd.status<>'999' "
				+"and opd.SHIPMENT_NO='"+shipmentNo+"' ";
		DataManager dm = DBOperator.DoSelect2DM(sql);
		if(dm==null || dm.getCurrentCount()==0){
			return "ERR-波次号不正确";
		}else{
			for(int i=0;i<dm.getCurrentCount();i++){
				String SHIPMENT_NO = dm.getString("SHIPMENT_NO", i);
				String SHIPMENT_LINE_NO = dm.getString("SHIPMENT_LINE_NO", i);
				String STORER_CODE = dm.getString("STORER_CODE", i);
				String WAREHOUSE_CODE = dm.getString("WAREHOUSE_CODE", i);
				String LOT_NO = dm.getString("LOT_NO", i);
				String FROM_LOCATION_CODE = dm.getString("FROM_LOCATION_CODE", i);
				String FROM_CONTAINER_CODE = dm.getString("FROM_CONTAINER_CODE", i);
				String ITEM_CODE = dm.getString("ITEM_CODE", i);
				String PICKED_QTY = dm.getString("PICKED_QTY", i);
				sql = "update inv_inventory ii set ii.ALLOCATED_QTY=ii.ALLOCATED_QTY - ("+PICKED_QTY+") "
						+"where ii.STORER_CODE='"+STORER_CODE+"' and ii.WAREHOUSE_CODE='"+WAREHOUSE_CODE+"' and ii.LOT_NO='"+LOT_NO+"' "
						+" and ii.LOCATION_CODE='"+FROM_LOCATION_CODE+"' "
						+" and ii.CONTAINER_CODE='"+FROM_CONTAINER_CODE+"' and ii.ITEM_CODE='"+ITEM_CODE+"' "
						+"";
				int t = DBOperator.DoUpdate(sql);
				if(t==1){
					sql = "delete from oub_pick_detail where SHIPMENT_NO ='"+SHIPMENT_NO+"' and SHIPMENT_LINE_NO='"+SHIPMENT_LINE_NO+"' ";
					t = DBOperator.DoUpdate(sql);
					sql = "update oub_shipment_detail osd set osd.`STATUS`='100',osd.ALLOCATED_QTY=osd.ALLOCATED_QTY - ("+PICKED_QTY+") "
							+"where osd.SHIPMENT_NO='"+SHIPMENT_NO+"' and osd.SHIPMENT_LINE_NO='"+SHIPMENT_LINE_NO+"' and osd.ITEM_CODE='"+ITEM_CODE+"' ";
					t = DBOperator.DoUpdate(sql);
					sql = "update oub_shipment_header osh set osh.`STATUS`='100',osh.WAVE_NO='' "
							+"where not exists(select osd.SHIPMENT_NO from oub_shipment_detail osd where osh.SHIPMENT_NO=osd.SHIPMENT_NO and osd.`STATUS`>'100') "
							+"and osh.SHIPMENT_NO = '"+SHIPMENT_NO+"' ";
					t = DBOperator.DoUpdate(sql);
					sql = "delete from oub_pick_header where SHIPMENT_NO='"+SHIPMENT_NO+"' ";
					t = DBOperator.DoUpdate(sql);
					
				}else{
					sbf.append("库存还原失败\n"+"SHIPMENT_NO:"+SHIPMENT_NO+"  STORER_CODE:"+STORER_CODE+"  WAREHOUSE_CODE:"+WAREHOUSE_CODE+
							"  LOT_NO:"+LOT_NO+"  LOCATION_CODE:"+FROM_LOCATION_CODE+
							"  CONTAINER_CODE:"+FROM_CONTAINER_CODE+"  ITEM_CODE:"+ITEM_CODE+"  Qty:"+PICKED_QTY);
				}
			}
			if(sbf.toString().length()>1){
				return "ERR-波次取消失败\n"+sbf.toString();
			}else{
				return "OK-波次取消成功";
			}
			
		}
	}
	
	public static String waveCancelNew(String waveNo){
		StringBuffer sbf = new StringBuffer();
		String sql = "select opd.SHIPMENT_NO,opd.SHIPMENT_LINE_NO,opd.STORER_CODE,opd.WAREHOUSE_CODE,opd.LOT_NO,opd.FROM_LOCATION_CODE,opd.FROM_CONTAINER_CODE,opd.ITEM_CODE,opd.PICKED_QTY "
				+"from oub_pick_detail opd where opd.SHIPMENT_NO in "
				+"(select SHIPMENT_NO from oub_wave_detail where WAVE_NO='"+waveNo+"') "
				+"and opd.PICKED_QTY>0 and opd.status<>'999' ";
		DataManager dm = DBOperator.DoSelect2DM(sql);
		if(dm==null || dm.getCurrentCount()==0){
			return "ERR-波次号不正确";
		}else{
			for(int i=0;i<dm.getCurrentCount();i++){
				String SHIPMENT_NO = dm.getString("SHIPMENT_NO", i);
				String SHIPMENT_LINE_NO = dm.getString("SHIPMENT_LINE_NO", i);
				String STORER_CODE = dm.getString("STORER_CODE", i);
				String WAREHOUSE_CODE = dm.getString("WAREHOUSE_CODE", i);
				String LOT_NO = dm.getString("LOT_NO", i);
				String FROM_LOCATION_CODE = dm.getString("FROM_LOCATION_CODE", i);
				String FROM_CONTAINER_CODE = dm.getString("FROM_CONTAINER_CODE", i);
				String ITEM_CODE = dm.getString("ITEM_CODE", i);
				String PICKED_QTY = dm.getString("PICKED_QTY", i);
				sql = "update inv_inventory ii set ii.ALLOCATED_QTY=ii.ALLOCATED_QTY - ("+PICKED_QTY+") "
						+"where ii.STORER_CODE='"+STORER_CODE+"' and ii.WAREHOUSE_CODE='"+WAREHOUSE_CODE+"' and ii.LOT_NO='"+LOT_NO+"' "
						+" and ii.LOCATION_CODE='"+FROM_LOCATION_CODE+"' "
						+" and ii.CONTAINER_CODE='"+FROM_CONTAINER_CODE+"' and ii.ITEM_CODE='"+ITEM_CODE+"' "
						+"";
				int t = DBOperator.DoUpdate(sql);
				if(t==1){
					sql = "update oub_pick_detail opd set opd.`STATUS`='999' where opd.SHIPMENT_NO ='"+SHIPMENT_NO+"' and opd.SHIPMENT_LINE_NO='"+SHIPMENT_LINE_NO+"' ";
					t = DBOperator.DoUpdate(sql);
					sql = "update oub_shipment_detail osd set osd.`STATUS`='0',osd.ALLOCATED_QTY=osd.ALLOCATED_QTY - ("+PICKED_QTY+") "
							+"where osd.SHIPMENT_NO='"+SHIPMENT_NO+"' and osd.SHIPMENT_LINE_NO='"+SHIPMENT_LINE_NO+"' and osd.ITEM_CODE='"+ITEM_CODE+"' ";
					t = DBOperator.DoUpdate(sql);
					sql = "update oub_shipment_header osh set osh.`STATUS`='100',osh.WAVE_NO='' "
							+"where not exists(select osd.SHIPMENT_NO from oub_shipment_detail osd where osh.SHIPMENT_NO=osd.SHIPMENT_NO and osd.`STATUS`>'100') "
							+"and osh.SHIPMENT_NO = '"+SHIPMENT_NO+"' ";
					t = DBOperator.DoUpdate(sql);
					sql = "update oub_pick_header oph set oph.`STATUS`='999' "
							+"where not exists(select opd.SHIPMENT_NO from oub_pick_detail opd where opd.status<>'999') "
							+" and oph.SHIPMENT_NO='"+SHIPMENT_NO+"' ";
					t = DBOperator.DoUpdate(sql);
					
				}else{
					sbf.append("库存还原失败\n"+"SHIPMENT_NO:"+SHIPMENT_NO+"  STORER_CODE:"+STORER_CODE+"  WAREHOUSE_CODE:"+WAREHOUSE_CODE+
							"  LOT_NO:"+LOT_NO+"  LOCATION_CODE:"+FROM_LOCATION_CODE+
							"  CONTAINER_CODE:"+FROM_CONTAINER_CODE+"  ITEM_CODE:"+ITEM_CODE+"  Qty:"+PICKED_QTY);
				}
			}
			if(sbf.toString().length()>1){
				return "ERR-波次取消失败\n"+sbf.toString();
			}else{
				return "OK-波次取消成功";
			}
			
		}
	}
	
	public static String waveCancel(String waveNo){
		StringBuffer sbf = new StringBuffer();
		String sql = "select opd.SHIPMENT_NO,opd.SHIPMENT_LINE_NO,opd.STORER_CODE,opd.WAREHOUSE_CODE,opd.LOT_NO,opd.FROM_LOCATION_CODE,opd.FROM_CONTAINER_CODE,opd.ITEM_CODE,opd.PICKED_QTY "
				+"from oub_pick_detail opd where opd.SHIPMENT_NO in "
				+"(select SHIPMENT_NO from oub_wave_detail where WAVE_NO='"+waveNo+"') "
				+"and opd.PICKED_QTY>0 and opd.status<>'999' ";
		DataManager dm = DBOperator.DoSelect2DM(sql);
		if(dm==null || dm.getCurrentCount()==0){
			return "ERR-波次号不正确";
		}else{
			for(int i=0;i<dm.getCurrentCount();i++){
				String SHIPMENT_NO = dm.getString("SHIPMENT_NO", i);
				String SHIPMENT_LINE_NO = dm.getString("SHIPMENT_LINE_NO", i);
				String STORER_CODE = dm.getString("STORER_CODE", i);
				String WAREHOUSE_CODE = dm.getString("WAREHOUSE_CODE", i);
				String LOT_NO = dm.getString("LOT_NO", i);
				String FROM_LOCATION_CODE = dm.getString("FROM_LOCATION_CODE", i);
				String FROM_CONTAINER_CODE = dm.getString("FROM_CONTAINER_CODE", i);
				String ITEM_CODE = dm.getString("ITEM_CODE", i);
				String PICKED_QTY = dm.getString("PICKED_QTY", i);
				sql = "update inv_inventory ii set ii.ALLOCATED_QTY=ii.ALLOCATED_QTY - ("+PICKED_QTY+") "
						+"where ii.STORER_CODE='"+STORER_CODE+"' and ii.WAREHOUSE_CODE='"+WAREHOUSE_CODE+"' and ii.LOT_NO='"+LOT_NO+"' "
						+" and ii.LOCATION_CODE='"+FROM_LOCATION_CODE+"' "
						+" and ii.CONTAINER_CODE='"+FROM_CONTAINER_CODE+"' and ii.ITEM_CODE='"+ITEM_CODE+"' "
						+"";
				int t = DBOperator.DoUpdate(sql);
				if(t==1){
					sql = "update oub_shipment_detail osd set osd.`STATUS`='0',osd.ALLOCATED_QTY=osd.ALLOCATED_QTY - ("+PICKED_QTY+") "
							+"where osd.SHIPMENT_NO='"+SHIPMENT_NO+"' and osd.SHIPMENT_LINE_NO='"+SHIPMENT_LINE_NO+"' and osd.ITEM_CODE='"+ITEM_CODE+"' ";
					t = DBOperator.DoUpdate(sql);
					sql = "update oub_shipment_header osh set osh.`STATUS`='100',osh.WAVE_NO='' "
							+"where not exists(select osd.SHIPMENT_NO from oub_shipment_detail osd where osh.SHIPMENT_NO=osd.SHIPMENT_NO and osd.`STATUS`>'100') "
							+"and osh.SHIPMENT_NO = '"+SHIPMENT_NO+"' ";
					t = DBOperator.DoUpdate(sql);
					if(t>0){
						sql = "update oub_pick_detail opd set opd.`STATUS`='999' where opd.SHIPMENT_NO ='"+SHIPMENT_NO+"' and opd.SHIPMENT_LINE_NO='"+SHIPMENT_LINE_NO+"' ";
						t = DBOperator.DoUpdate(sql);
						if(t>0){
							sql = "update oub_pick_header oph set oph.`STATUS`='999' "
									+"where not exists(select opd.SHIPMENT_NO from oub_pick_detail opd where opd.status<>'999') "
									+" and oph.SHIPMENT_NO='"+SHIPMENT_NO+"' ";
						}
					}
					
				}else{
					sbf.append("库存还原失败\n"+"SHIPMENT_NO:"+SHIPMENT_NO+"  STORER_CODE:"+STORER_CODE+"  WAREHOUSE_CODE:"+WAREHOUSE_CODE+
							"  LOT_NO:"+LOT_NO+"  LOCATION_CODE:"+FROM_LOCATION_CODE+
							"  CONTAINER_CODE:"+FROM_CONTAINER_CODE+"  ITEM_CODE:"+ITEM_CODE+"  Qty:"+PICKED_QTY);
				}
			}
			if(sbf.toString().length()>1){
				return "ERR-波次取消失败\n"+sbf.toString();
			}else{
				return "OK-波次取消成功";
			}
			
		}
	}
	
	private static void invAllocateTryAgain(){
		String waveNo = comData.getValueFromBasNumRule("oub_wave_header", "wave_no");
		String sql = "insert into oub_wave_header(WAVE_NO,WAVE_NAME,WAREHOUSE_CODE,CREATED_BY_USER,CREATED_DTM_LOC) "
				+"select '"+waveNo+"','Normal Wave','"+MainFrm.getUserInfo().getString("CUR_WAREHOUSE_CODE", 0)+"','"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"',now()";
		int t = DBOperator.DoUpdate(sql);
		if(t==1){
			sql = "select warehouse_code,shipment_no,TRANSFER_ORDER_NO,wave_no from oub_shipment_header";
			DataManager dmtmp = DBOperator.DoSelect2DM(sql);
			
			for(int i=0;i<dmtmp.getCurrentCount();i++){
				String shipmentWarehouseCode = dmtmp.getString("warehouse_code", i);
				String shipmentNo = dmtmp.getString("shipment_no", i);
				String transportNo = dmtmp.getString("TRANSFER_ORDER_NO", i);
				String shipmentWaveNo = dmtmp.getString("wave_no", i);
				//如果订单已经产生波次号，忽略此行，循环到下一行
				if(!shipmentWaveNo.trim().equals("")){
					continue;
				}
				sql = "insert into oub_wave_detail(OUB_WAVE_HEADER_ID,WAVE_NO,WAREHOUSE_CODE,SHIPMENT_NO,CREATED_BY_USER,CREATED_DTM_LOC) "
						+"select (select OUB_WAVE_HEADER_ID from oub_wave_header where WAVE_NO='"+waveNo+"'),'"
						+waveNo+"','"+shipmentWarehouseCode+"','"+shipmentNo+"','"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"',now() ";
				t = DBOperator.DoUpdate(sql);
				if(t==1){
					//更新订单表头波次号
					sql = "update oub_shipment_header set status='150', wave_no='"+waveNo+"' where shipment_no='"+shipmentNo+"' ";
					DBOperator.DoUpdate(sql);
//					headerTable.setValueAt(waveNo, i, headerTable.getColumnModel().getColumnIndex("波次号"));
				}else{
					Message.showWarningMessage("订单："+transportNo+" 创建波次明细表失败,系统自动忽略此订单");
					continue;
				}
				//开始分配库存
				String pickNo = comData.getValueFromBasNumRule("oub_pick_header", "pick_no");
				sql = "insert into oub_pick_header(WAREHOUSE_CODE,STORER_CODE,WAVE_NO,PICK_NO,OUB_SHIPMENT_HEADER_ID,SHIPMENT_NO,CREATED_BY_USER,CREATED_DTM_LOC) "
						+"select osh.WAREHOUSE_CODE,osh.STORER_CODE,'"+waveNo+"','"+pickNo+"',osh.OUB_SHIPMENT_HEADER_ID,osh.SHIPMENT_NO,'"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"',now() "
						+" from oub_shipment_header osh where osh.SHIPMENT_NO='"+shipmentNo+"' ";
				t = DBOperator.DoUpdate(sql);
				if(t==0){
					Message.showWarningMessage("创建订单【"+shipmentNo+"】拣货表头失败,系统自动忽略此订单!");
					continue;
				}else{
					sql = "select WAREHOUSE_CODE,STORER_CODE,SHIPMENT_LINE_NO,ITEM_CODE,REQ_QTY,LOTTABLE01,LOTTABLE02"
							+",LOTTABLE03,LOTTABLE04,LOTTABLE05,LOTTABLE06,LOTTABLE07,LOTTABLE08,LOTTABLE09,LOTTABLE10 "
							+"from oub_shipment_detail where shipment_no='"+shipmentNo+"' order by SHIPMENT_LINE_NO ";
					DataManager dm = DBOperator.DoSelect2DM(sql);
					if(dm==null || dm.getCurrentCount()==0){
						
					}else{
						for(int k=0;k<dm.getCurrentCount();k++){
							shipmentWarehouseCode = dm.getString("WAREHOUSE_CODE", k);
							String shipmentStorerCode = dm.getString("STORER_CODE", k);
							String shipmentItemCode = dm.getString("ITEM_CODE", k);
							String shipmentLineNo = dm.getString("SHIPMENT_LINE_NO", k);
							String shipmemtReqQty = dm.getString("REQ_QTY", k);
							String shipmentLot01 = dm.getString("LOTTABLE01", k);
							String shipmentLot02 = dm.getString("LOTTABLE02", k);
							String shipmentLot03 = dm.getString("LOTTABLE03", k);
							String shipmentLot04 = dm.getString("LOTTABLE04", k);
							String shipmentLot05 = dm.getString("LOTTABLE05", k);
							String shipmentLot06 = dm.getString("LOTTABLE06", k);
							String shipmentLot07 = dm.getString("LOTTABLE07", k);
							String shipmentLot08 = dm.getString("LOTTABLE08", k);
							String shipmentLot09 = dm.getString("LOTTABLE09", k);
							String shipmentLot10 = dm.getString("LOTTABLE10", k);
							//获取分配的库存信息
							System.out.println("库存查找情况：");
							DataManager dmAllocated = getInventoryRowsByQtyASC(shipmentWarehouseCode,shipmentStorerCode,shipmentItemCode,shipmemtReqQty,
									shipmentLot01,shipmentLot02,shipmentLot03,shipmentLot04,shipmentLot05
									,shipmentLot06,shipmentLot07,shipmentLot08,shipmentLot09,shipmentLot10);
							//未找到库存
							if(dmAllocated==null || dmAllocated.getCurrentCount()==0){
								
								continue;
							}
							for(int r=0;r<dmAllocated.getCurrentCount();r++){
								double AllocatedQty = Math_SAM.str2Double(dmAllocated.getString("AllocatedQty", r));
								if(AllocatedQty<=0){
									continue;
								}
								if(dmAllocated.getString("INV_INVENTORY_ID", r).equals("")){
									continue;
								}
								sql = "insert into oub_pick_detail(OUB_PICK_HEADER_ID,WAREHOUSE_CODE,STORER_CODE,WAVE_NO,PICK_NO,OUB_SHIPMENT_HEADER_ID,SHIPMENT_NO,SHIPMENT_LINE_NO,"
										+"LOT_NO,ITEM_CODE,REQ_QTY,PICKED_QTY,FROM_LOCATION_CODE,FROM_CONTAINER_CODE,INV_INVENTORY_ID,CREATED_BY_USER,CREATED_DTM_LOC) "
										+"select (select OUB_PICK_HEADER_ID from oub_pick_header where pick_no='"+pickNo+"'),osd.WAREHOUSE_CODE,osd.STORER_CODE,osh.WAVE_NO,'"+pickNo+"',osh.OUB_SHIPMENT_HEADER_ID,osh.SHIPMENT_NO,osd.SHIPMENT_LINE_NO, "
										+"'"+dmAllocated.getString("LOT_NO", r)+"',osd.ITEM_CODE,osd.REQ_QTY,'"+(dmAllocated.getString("AllocatedQty", r).equals("")?"0":dmAllocated.getString("AllocatedQty", r))+"', "
										+"'"+dmAllocated.getString("LOCATION_CODE", r)+"','"+dmAllocated.getString("CONTAINER_CODE", r)+"','"+dmAllocated.getString("INV_INVENTORY_ID", r)+"', "
										+"'"+MainFrm.getUserInfo().getString("USER_CODE", 0)+"',now() "
										+" from oub_shipment_header osh "
										+"inner join oub_shipment_detail osd on osh.SHIPMENT_NO=osd.SHIPMENT_NO "
										+"where osh.SHIPMENT_NO='"+shipmentNo+"' and osd.SHIPMENT_LINE_NO= "+shipmentLineNo+" ";
								t = DBOperator.DoUpdate(sql);
								sql = "update oub_shipment_detail osd set osd.ALLOCATED_QTY=ifnull((select sum(PICKED_QTY) from oub_pick_detail opd "
									 +"where opd.SHIPMENT_NO='"+shipmentNo+"' and opd.SHIPMENT_LINE_NO="+shipmentLineNo+" and opd.ITEM_CODE='"+shipmentItemCode+"'),0) "
									 +" where osd.SHIPMENT_NO='"+shipmentNo+"' and osd.SHIPMENT_LINE_NO="+shipmentLineNo+" and osd.ITEM_CODE='"+shipmentItemCode+"' ";
								t = DBOperator.DoUpdate(sql);
							}
						}
					}
				}
			}
		}
		
		String messageStr = "";
		//检查订单库存分配情况
		sql = "select warehouse_code,shipment_no,TRANSFER_ORDER_NO,wave_no from oub_shipment_header";
		DataManager dmtmp = DBOperator.DoSelect2DM(sql);
		for(int i=0;i<dmtmp.getCurrentCount();i++){
			String shipmentWarehouseCode = dmtmp.getString("warehouse_code", i);
			String shipmentNo = dmtmp.getString("shipment_no", i);
			String transportNo = dmtmp.getString("TRANSFER_ORDER_NO", i);
			String shipmentWaveNo = dmtmp.getString("wave_no", i);
			sql = "select PICK_NO from oub_pick_detail where SHIPMENT_NO='"+shipmentNo+"' and WAREHOUSE_CODE='"+shipmentWarehouseCode+"' ";
			DataManager dm = DBOperator.DoSelect2DM(sql);
			if(dm==null || dm.getCurrentCount()==0){
				//分配失败，清除订单表头的波次号
				sql = "update oub_shipment_header set WAVE_NO='',status='100' where SHIPMENT_NO='"+shipmentNo+"' and WAREHOUSE_CODE='"+shipmentWarehouseCode+"' ";
				DBOperator.DoUpdate(sql);
				//分配失败，清除拣货单表头
				sql = "delete from oub_pick_header where SHIPMENT_NO='"+shipmentNo+"' and WAREHOUSE_CODE='"+shipmentWarehouseCode+"' ";
				DBOperator.DoUpdate(sql);
				//分配失败，清除波次明细表的订单行信息
				sql = "delete from oub_wave_detail where SHIPMENT_NO='"+shipmentNo+"' and WAREHOUSE_CODE='"+shipmentWarehouseCode+"' ";
				DBOperator.DoUpdate(sql);
				//分配失败，清除波次表头（如果该波次号没有明细）
				sql = "delete from oub_wave_header where wave_no not in (select wave_no from oub_wave_detail where oub_wave_header.WAVE_NO=oub_wave_detail.WAVE_NO)";
				DBOperator.DoUpdate(sql);
//				headerTable.setValueAt("", i, headerTable.getColumnModel().getColumnIndex("波次号"));
				messageStr = messageStr + "订单号："+transportNo+" 库存分配失败\n";
			}else{
				//分配成功，订单状态status='200'
				sql = "update oub_shipment_header set status='200' where SHIPMENT_NO='"+shipmentNo+"' and WAREHOUSE_CODE='"+shipmentWarehouseCode+"' ";
				DBOperator.DoUpdate(sql);
				sql = "update oub_shipment_detail set status='200' where SHIPMENT_NO='"+shipmentNo+"' and WAREHOUSE_CODE='"+shipmentWarehouseCode+"' ";
				DBOperator.DoUpdate(sql);
//				refreshShowData();
			}
		}
		if(!messageStr.equals("")){
			Message.showWarningMessage(messageStr);
		}else{
			Message.showInfomationMessage("订单库存分配完成，请打印拣货单进行拣货");
//			btnPrint.requestFocus();
		}
		
//		tableRowColorSetup(headerTable);//设置有波次号的行此列的背景颜色
	}
	@SuppressWarnings("unused")
	private static DataManager getInventoryRowsByQtyASC(String warehouseCode,String storerCode,String itemCode,String qty,
			String lot01,String lot02,String lot03,String lot04,String lot05,
			String lot06,String lot07,String lot08,String lot09,String lot10){
		String sql = "";
		String lotNo = "";
		double shipmentQty = Math_SAM.str2Double(qty);
		DataManager dm = new DataManager();
		if(lot01.equals("") && lot02.equals("") && lot03.equals("") && lot04.equals("") && lot05.equals("") && 
				lot06.equals("") && lot07.equals("") && lot08.equals("") && lot09.equals("") && lot10.equals("")){
			//不需要按照批次信息分配库存
			sql = "select ii.INV_INVENTORY_ID,ii.WAREHOUSE_CODE,ii.STORER_CODE,ii.ITEM_CODE,ii.INV_LOT_ID,ii.LOT_NO,ii.LOCATION_CODE,ii.CONTAINER_CODE,"
					+"ii.ON_HAND_QTY+(ii.IN_TRANSIT_QTY)-(ii.ALLOCATED_QTY)-(ii.PICKED_QTY)-(ii.INACTIVE_QTY) QTY,0 AllocatedQty "
					+" from inv_inventory ii "
					+" inner join bas_location bl on ii.WAREHOUSE_CODE=bl.WAREHOUSE_CODE and ii.LOCATION_CODE=bl.LOCATION_CODE "
					+" where ii.WAREHOUSE_CODE='"+warehouseCode+"' and ii.STORER_CODE='"+storerCode+"' and ii.ITEM_CODE='"+itemCode+"' "
					+" and bl.LOCATION_TYPE_CODE not in ('Dock','Damage') "
					+" order by ii.ON_HAND_QTY+(ii.IN_TRANSIT_QTY)-(ii.ALLOCATED_QTY)-(ii.PICKED_QTY)-(ii.INACTIVE_QTY) asc";
			dm = DBOperator.DoSelect2DM(sql);
			for(int i=0;i<dm.getCurrentCount();i++){
				String INV_INVENTORY_ID = dm.getString("INV_INVENTORY_ID", i);
				double invQty = Math_SAM.str2Double(dm.getString("QTY", i));
				//循环订单数量，如果>0继续分配，否则退出分配
				if (shipmentQty > 0) {
					if ((shipmentQty - invQty) >= 0) {
						dm.setObject(dm.getCol("AllocatedQty"), i, invQty);
						//更新库存分配数量
						sql = "update inv_inventory set ALLOCATED_QTY=ALLOCATED_QTY+("+invQty+") "
							 +"where INV_INVENTORY_ID= '"+INV_INVENTORY_ID+"' and WAREHOUSE_CODE='"+warehouseCode+"' and STORER_CODE='"+storerCode+"' and ITEM_CODE='"+itemCode+"' "
							 +" and LOCATION_CODE='"+dm.getString("LOCATION_CODE", i)+"' and CONTAINER_CODE='"+dm.getString("CONTAINER_CODE", i)+"' ";
						DBOperator.DoUpdate(sql);
					} else {
						dm.setObject(dm.getCol("AllocatedQty"), i, shipmentQty);
						//更新库存分配数量
						sql = "update inv_inventory set ALLOCATED_QTY=ifnull(ALLOCATED_QTY,0)+("+shipmentQty+") "
							 +"where INV_INVENTORY_ID= '"+INV_INVENTORY_ID+"' and WAREHOUSE_CODE='"+warehouseCode+"' and STORER_CODE='"+storerCode+"' and ITEM_CODE='"+itemCode+"' "
							 +" and LOCATION_CODE='"+dm.getString("LOCATION_CODE", i)+"' and CONTAINER_CODE='"+dm.getString("CONTAINER_CODE", i)+"' ";
						DBOperator.DoUpdate(sql);
					}
					shipmentQty = shipmentQty - invQty;
				}else{
					return dm;
				}
			}
			
		}else{
			//先查找批次ID，根据批次ID分配库存
			lotNo = comData.getLotID(storerCode,itemCode,lot01,lot02,lot03,lot04,lot05,lot06,lot07,lot08,lot09,lot10);
			sql = "select INV_INVENTORY_ID,WAREHOUSE_CODE,STORER_CODE,ITEM_CODE,INV_LOT_ID,LOT_NO,LOCATION_CODE,CONTAINER_CODE,"
					+"ON_HAND_QTY+(IN_TRANSIT_QTY)-(ALLOCATED_QTY)-(PICKED_QTY)-(INACTIVE_QTY) QTY,0 AllocatedQty "
					+" from inv_inventory "
					+" where WAREHOUSE_CODE='"+warehouseCode+"' and STORER_CODE='"+storerCode+"' and LOT_NO='"+lotNo+"' and ITEM_CODE='"+itemCode+"' "
					+" order by ON_HAND_QTY+(IN_TRANSIT_QTY)-(ALLOCATED_QTY)-(PICKED_QTY)-(INACTIVE_QTY) asc";
			dm = DBOperator.DoSelect2DM(sql);
			for(int i=0;i<dm.getCurrentCount();i++){
				String INV_INVENTORY_ID = dm.getString("INV_INVENTORY_ID", i);
				double invQty = Math_SAM.str2Double(dm.getString("QTY", i));
				//循环订单数量，如果>0继续分配，否则退出分配
				if (shipmentQty > 0) {
					if ((shipmentQty - invQty) >= 0) {
						dm.setObject(dm.getCol("AllocatedQty"), i, invQty);
						//更新库存分配数量
						sql = "update inv_inventory set ALLOCATED_QTY=ALLOCATED_QTY+("+invQty+") "
							 +"where INV_INVENTORY_ID= '"+INV_INVENTORY_ID+"' and WAREHOUSE_CODE='"+warehouseCode+"' and STORER_CODE='"+storerCode+"' and LOT_NO='"+lotNo+"' and ITEM_CODE='"+itemCode+"'"
							 +" and LOCATION_CODE='"+dm.getString("LOCATION_CODE", i)+"' and CONTAINER_CODE='"+dm.getString("CONTAINER_CODE", i)+"' ";
						DBOperator.DoUpdate(sql);
					} else {
						dm.setObject(dm.getCol("AllocatedQty"), i, shipmentQty);
						//更新库存分配数量
						sql = "update inv_inventory set ALLOCATED_QTY=ALLOCATED_QTY+("+shipmentQty+") "
							 +"where INV_INVENTORY_ID= '"+INV_INVENTORY_ID+"' and WAREHOUSE_CODE='"+warehouseCode+"' and STORER_CODE='"+storerCode+"' and LOT_NO='"+lotNo+"' and ITEM_CODE='"+itemCode+"'"
							 +" and LOCATION_CODE='"+dm.getString("LOCATION_CODE", i)+"' and CONTAINER_CODE='"+dm.getString("CONTAINER_CODE", i)+"' ";
						DBOperator.DoUpdate(sql);
					}
					shipmentQty = shipmentQty - invQty;
				}else{
					return dm;
				}
			}
		}
		
		return dm;
	}

}
