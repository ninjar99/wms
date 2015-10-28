package comUtil;

import DBUtil.DBOperator;
import dmdata.DataManager;

public class BasItemCompare {
	
	public static void main(String[] args) {
		String sql = "select * from sandwich.ag_product where ifnull(part_number,'')<>''";
		DataManager dm = DBOperator.DoSelect2DM(sql);

		for (int j = 0; j < dm.getColCount(); j++) {
			System.out.print(dm.getCol(j) + " ");
		}
		System.out.println();
		for (int i = 0; i < dm.getCurrentCount(); i++) {
			for (int j = 0; j < dm.getColCount(); j++) {
				System.out.print(dm.getString(j, i) + " ");
			}
			System.out.println();
		}
		
		for (int i = 0; i < dm.getCurrentCount(); i++) {
			String itemCode = dm.getString("part_number", i);
			String weight = dm.getString("product_weight", i);
			String taxNumber = dm.getString("tax_no", i);
			String price = dm.getString("product_price", i);
			String country = dm.getString("producing_country", i);
			if(isExists(itemCode)){
				sql = "update bas_item set weight='"+weight+"',TAX_NUMBER='"+taxNumber+"',LIST_PRICE='"+price+"',COUNTRY_CODE='"+country+"' "
						+"where item_code='"+itemCode+"'";
				int t = DBOperator.DoUpdate(sql);
			}
		}
	}
	
	public static boolean isExists(String storer_code,String item_code){
		String sql = "select storer_code,item_code from bas_item where storer_code='"+storer_code+"' and item_code='"+item_code+"' ";
		DataManager dm = DBOperator.DoSelect2DM(sql);
		if(dm==null || dm.getCurrentCount()==0){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean isExists(String item_code){
		String sql = "select storer_code,item_code from bas_item where item_code='"+item_code+"' ";
		DataManager dm = DBOperator.DoSelect2DM(sql);
		if(dm==null || dm.getCurrentCount()==0){
			return false;
		}else{
			return true;
		}
	}

}
