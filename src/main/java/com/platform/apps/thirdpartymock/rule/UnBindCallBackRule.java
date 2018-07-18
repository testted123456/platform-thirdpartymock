package com.platform.apps.thirdpartymock.rule;

import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.nonobank.apps.HttpClient;
import com.platform.apps.thirdpartymock.util.FastPayUtil;
import com.platform.apps.thirdpartymock.util.SignatureUtilQuickPay;

@Component
public class UnBindCallBackRule extends CallBackRule {
	
	public static Logger logger = LoggerFactory.getLogger(UnBindCallBackRule.class);
	
	public UnBindCallBackRule() {
		this.name = "UnBindCallBack";
	}

	@Override
	public boolean callBack(Map<String, String> requestMap) {
		// TODO Auto-generated method stub
		String url = requestMap.get("url");
		String unBindOrder = requestMap.get("unBindOrder");
		JSONObject jsonObj = JSONObject.parseObject(unBindOrder);
		String sign = jsonObj.getString("sign");
		String card_no = jsonObj.getString("card_no");
		String mer_id = jsonObj.getString("mer_id");
		
		String str = card_no + mer_id;
		byte[] plainBytes = str.getBytes();

		
		if(null == sign || "".equals(sign)) {
			SignatureUtilQuickPay singUtil = new SignatureUtilQuickPay();
			String publicKey=  FastPayUtil.localPublicKey;
			String privateKey = FastPayUtil.localPrivateKey;
			
			try {
				singUtil.initKey(privateKey, publicKey, 2048);
				byte[] signBytes = singUtil.signRSA(plainBytes, true, "UTF-8");
				sign = new String(signBytes, "UTF-8");
				jsonObj.put("sign", sign);
				
				HttpClient httpClient = new HttpClient();
				CloseableHttpClient client = httpClient.getHttpClient();
				CloseableHttpResponse response = httpClient.doPostSendJson(client, null, url, jsonObj.toJSONString());
				System.out.println(httpClient.getResBody(response));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return false;
	}

}
