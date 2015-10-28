package api;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import comUtil.MD5;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
//对接口进行测试
public class TestMain {
	private String url = "http://api.kjt.com/open.api";
	private String charset = "utf-8";
	private HttpClientUtil httpClientUtil = null;
	
	public TestMain(){
		httpClientUtil = new HttpClientUtil();
	}
	
	public void test(){
		String httpOrgCreateTest = url;
		Map<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("appid", "seller1280");
		hashMap.put("format", "json");
		hashMap.put("method", "Inventory.ChannelQ4SBatchGet");
		hashMap.put("timestamp", getDateTimeString());
		hashMap.put("data", "{\"ProductIDs\":\"A21ITA163940002,A21USC125490003,A21USC125490002,A21USC125490001,\",\"SaleChannelSysNo\":\"44\"}");
		hashMap.put("nonce",(new Random().nextInt(100000000)) + "");
		hashMap.put("version", "1.0");
		String salt = createLinkString(hashMap) + "appsecret=0bae030e6a964214aca12698b4a52d5c";
		String mysign = MD5.GetMD5Code(salt.toLowerCase());
		hashMap.put("sign", mysign);
		String httpOrgCreateTestRtn = httpClientUtil.doPost(httpOrgCreateTest,hashMap,charset);
		System.out.println("result:"+httpOrgCreateTestRtn);
		
		JSONObject jo = new JSONObject();
		JSONObject dataJson = jo.fromObject(httpOrgCreateTestRtn);
		JSONArray data=dataJson.getJSONArray("Data");
		for(int i=0;i<data.size();i++){
			JSONObject info=data.getJSONObject(i);
			String ProductID = info.getString("ProductID");
			String OnlineQty = info.getString("OnlineQty");
			String WareHouseID = info.getString("WareHouseID");
			System.out.println(WareHouseID+" / "+ProductID +" : "+ OnlineQty);
		}
		
	}
	
	public String createLinkString(Map<String, String> params) {

		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < keys.size(); i++) {
			sb.append(keys.get(i)).append("=").append(params.get(keys.get(i))).append("&");
		}
		return sb.toString();
	}
	
	public static  String getDateTimeString(){
        Date now = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String result = dateFormat.format(now);
        return  result;
    }
	
	public static void main(String[] args){
		TestMain main = new TestMain();
		main.test();
	}
}
