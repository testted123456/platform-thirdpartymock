package com.platform.apps.thirdpartymock.rule;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.nonobank.apps.HttpClient;
import com.platform.apps.thirdpartymock.entity.FastPayOrder;
import com.platform.apps.thirdpartymock.service.FastPayOrderService;
import com.platform.apps.thirdpartymock.util.FastPayUtil;
import com.platform.apps.thirdpartymock.util.SignatureUtil;
import com.platform.apps.thirdpartymock.util.SignatureUtilQuickPay;

@Component
public class FastPayCallBackRule extends CallBackRule {
	
	public static Logger logger = LoggerFactory.getLogger(FastPayCallBackRule.class);
	
	@Autowired
	FastPayOrderService fastPayOrderService;
	
	public FastPayCallBackRule() {
		this.name = "FastPayCallBack";
	}

	@Override
	public boolean callBack(Map<String, String> requestMap) {
		// TODO Auto-generated method stub
		String url = requestMap.get("url");
		String strOfFastPayOrder = requestMap.get("fastPayOrder");
		FastPayOrder fastPayOrder = JSONObject.parseObject(strOfFastPayOrder, FastPayOrder.class);
		
		String serial = fastPayOrder.getSystemNo();
		String orderId = fastPayOrder.getOrderNo();
		String cardNo = fastPayOrder.getCardNo();
		String merchId = "1028";
		
		String str = serial + orderId + cardNo + merchId;
		try {
			byte[] plainBytes = str.getBytes("UTF-8");
			
			SignatureUtilQuickPay singUtil = new SignatureUtilQuickPay();
			String publicKey=  FastPayUtil.localPublicKey;
			String privateKey = FastPayUtil.localPrivateKey;
			
			singUtil.initKey(privateKey, publicKey, 2048);
			byte[] signBytes = singUtil.signRSA(plainBytes, true, "UTF-8");
			String sign = new String(signBytes, "UTF-8");
			
			JSONObject jsonObj = JSONObject.parseObject(strOfFastPayOrder);
			jsonObj.put("sign", sign);
			
			HttpClient httpClient = new HttpClient();
			CloseableHttpClient client = httpClient.getHttpClient();
			CloseableHttpResponse response = httpClient.doPostSendJson(client, null, url, jsonObj.toJSONString());
			System.out.println(httpClient.getResBody(response));
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}

}
