package com.platform.apps.thirdpartymock.controller.upzf;

import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.nonobank.apps.HttpClient;
import com.platform.apps.thirdpartymock.util.SMCryptUtil;

/**
 * 扫码支付
 * @author xinxiang
 *
 */
//@RequestMapping(value="ydzf")
//@Controller
//@CrossOrigin(origins = "*", maxAge = 3600)
public class SMZFController {

	public static Logger logger = LoggerFactory.getLogger(SMZFController.class);
	
	private static String smzfPubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsPNh/QHhmdxxtTtnwtM0" +
							"Nkkod+JgOK3p5mMaABHOadcNjPuXb+Cnx+Fb1nPDrH/lg7h8owF1U686+TX2ocJ0" +
							"oug1bGsDkIYgLKzh+Y03NOo+4XkhCIEpByXUkAaiZo11Dta/NYFrJwhZXL96i0IR" +
							"1ELuhWaQXFQo+9NgRb3OuDpgjD45mBQoV+Pbmrl5oUVOYhCV8qfTfvD6ktc/nMc1" +
							"sj7ERkn39iCI/71rAAKJuu28woXTHA3nIibVYVBxw3nblKWXyQ6VKsTiwEm2+UR2" +
							"75n263YjT44JUjVXRxoqlhrn7bpSc9xcOaiV51h98MZiC/+GZf8QuKLEwjG5Xcj3" +
							"YQIDAQAB";
	
	private static String smzfPriKey = 
			"MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCw82H9AeGZ3HG1" +
			"O2fC0zQ2SSh34mA4renmYxoAEc5p1w2M+5dv4KfH4VvWc8Osf+WDuHyjAXVTrzr5" +
			"NfahwnSi6DVsawOQhiAsrOH5jTc06j7heSEIgSkHJdSQBqJmjXUO1r81gWsnCFlc" +
			"v3qLQhHUQu6FZpBcVCj702BFvc64OmCMPjmYFChX49uauXmhRU5iEJXyp9N+8PqS" +
			"1z+cxzWyPsRGSff2IIj/vWsAAom67bzChdMcDeciJtVhUHHDeduUpZfJDpUqxOLA" +
			"Sbb5RHbvmfbrdiNPjglSNVdHGiqWGuftulJz3Fw5qJXnWH3wxmIL/4Zl/xC4osTC" +
			"MbldyPdhAgMBAAECggEAf1MRfhE9eQ3TT/nn5ps8pZcspF7ip8YUWO+URNqLMUNJ" +
			"/9kFFb6MW9n/0CbKdLQuvQIhxNUGzGl5QNsglTKr5JC76EB0dyjbu+nGTF/dipjH" +
			"GEylhU4S7I6/DceIUcyVn1DMkz6FkLeEPaWWEuA+ngJvSNQNKjJwrwtg6a3zMkG0" +
			"TBIeqhd5rpEuLH63erahmD/2tQmMHcyxpK7icmSVf4FuuqW0ZGYRTE+AAY2LxrFj" +
			"d5oPpr5YqYMeYyty0b71JoVXPwF/2pQZ0oOITOiXkgi10VXufXauu+G3R0zuaZeA" +
			"64PSmYIDf6giqei/VQKxVnhCbRzuuS5QKYMTvC7oAQKBgQDi6/LGd06FWFvT7F9v" +
			"TSApUeLxPimk1GJ3vzV8HKEZEpRsnHm2avVNGt/Y99i8AsWdqm3lW5TzOW9utqOi" +
			"/FgsdTLPzgDUDy6sWaiIJczTTuUC3ZUDVJPOdmr4cJyp53EiNkh9996v4RAgUkxX" +
			"P+NFzvNwVXxyJhHb7sRZwc5UQQKBgQDHoCmEOilCcYuN+5jP738zCuz89B2WzBRs" +
			"VWmPVb7OArTeC1e720WbrOyS1cV2r4ClX1eKYYqPqfZNuzIEP86J/7yfjpAkGAJv" +
			"tqg/IwZwFwdZvnACMk+d0Ja119dBqdOhS6YbhxFIFIst1MiGWzu/cJg7wPZARpz/" +
			"TTJQRv1bIQKBgFg0In/mbA0E1VzAqi/XKMMm4zGmWgWJ0GR/XEjL0Aqw4CR/hgfT" +
			"91lrMsthO0uJQEsSrfjS6kjIcHn2YBDASX2uaHvBtSy4Ygn4J7wUBVO1fOrqOju7" +
			"KmC8QsQi8aJRJfaIZxEfwd1XfC80Dhxz5uGBmbp2YBL2nUaBvqys2oiBAoGBAI5r" +
			"+VLKQAeUhNFekG7B2wlqoRvzemzM20FTSsWlkkTeb3X/t69iSG5B2GMGOyRrIChc" +
			"NlOXNck7pGtew9d+Qnf4SfW4O6RUsOBM/ApXjc9Izsi7cqROirj4SZ4hCQVjCYcJ" +
			"NtBq8+xuI8ImDo605pK13Ra8m7PN7VLEG8Jkc3uhAoGBAJ4a62oaI8y/tWDUGn4g" +
			"FQlO99birtj1RJLLt+7RCwyoaQNFPLeH8fOMZDyzfA5v3yBI6BtSEgRSlGbGpyCo" +
			"Z5d+8HEIpsMb8hUS/uIFAuiuuDYIS20V+Ws7D6k8uH7yT89d5vFNgsyaMn05JERk" +
			"HHTmCsY2bqPWjrJvBw3C7CXW";
	
	
	private static String globalReqMsgId;
	
	@ResponseBody
	@PostMapping(value="/ydzf-smdf")
	public String smzf(@RequestParam String encryptData,
			@RequestParam String encryptKey,
			@RequestParam String cooperator,
			@RequestParam String signData,
			@RequestParam String tranCode,
			@RequestParam String callBack,
			@RequestParam String reqMsgId){
		
		logger.info("扫码支付请求字段：");
		logger.info("encryptData:" + encryptData);
		logger.info("encryptKey:" + encryptKey);
		logger.info("cooperator:" + cooperator);
		logger.info("signData:" + signData);
		logger.info("tranCode:" + tranCode);
		logger.info("callBack:" + callBack);
		logger.info("reqMsgId:" + reqMsgId);
		
		if(tranCode.equals("SMZF002")){
			globalReqMsgId = reqMsgId;
		}
		
		 /** 编码公私钥对象*/
        Map<String, Object> keyMaps = new HashMap<String, Object>();
        keyMaps.put("publickKey", smzfPubKey);
        keyMaps.put("privateKey", smzfPriKey);
        
        Map<String, Object> keys = null;
		try {
			keys = SMCryptUtil.getKeys(keyMaps);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        PrivateKey hzfPriKey = (PrivateKey) keys.get("hzfPriKey");
        PublicKey yhPubKey = (PublicKey) keys.get("yhPubKey");
        
		try {
			 byte[] decodeBase64KeyBytes = Base64.decodeBase64(encryptKey.getBytes("UTF-8"));
			// 解密encryptKey得到merchantAESKey
	        byte[] merchantAESKeyBytes = SMCryptUtil.RSADecrypt(decodeBase64KeyBytes, hzfPriKey, 2048, 11, "RSA/ECB/PKCS1Padding");

	        // 使用base64解码商户请求报文
	        byte[] decodeBase64DataBytes = Base64.decodeBase64(encryptData.getBytes("UTF-8"));
	        
	        // 用解密得到的merchantAESKey解密encryptData
	        byte[] merchantXmlDataBytes = SMCryptUtil.AESDecrypt(decodeBase64DataBytes, merchantAESKeyBytes, "AES", "AES/ECB/PKCS5Padding", null);
	        String resXml = new String(merchantXmlDataBytes, "UTF-8");
	        logger.info("发送瑞银信返回报文[微信支付〉明文]：" + resXml);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LocalDateTime now = LocalDateTime.now();
		String nowOfStr = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<6;i++){
			sb.append(random.nextInt(10));
		}
		
		JSONObject resData = new JSONObject();
		resData.put("respCode", "000000");
		resData.put("respDate", nowOfStr);
		resData.put("respMsg", "处理成功");
//		reqData.put("smzfMsgId", "B20180904000003478130");
		resData.put("smzfMsgId", "b" + nowOfStr + sb.toString());
		resData.put("respType", "S");
		resData.put("msgType", "02");
		resData.put("reqDate", nowOfStr);
		resData.put("version", "1.0.0");
		JSONObject data = new JSONObject();
		data.put("qrCode", "https://qr.95516.com/00010000/62028489073949969691921655610979");
		resData.put("totalAmount", "2.00");
		resData.put("data", data.toJSONString());
		
		if(tranCode.equals("SMZF006")){
			data.put("oriRespType", "S");
			data.put("oriReqMsgId", globalReqMsgId);
			data.put("oriRespCode", "000000");
			data.put("oriRespMsg", "处理成功");
			data.put("oriRespMsg", "处理成功");
			resData.put("data", data);
		}
		
		 try {
			byte[] reqDataByte = resData.toJSONString().getBytes("UTF-8");
			 /** 生成AES密钥key */
            String keyStr = SMCryptUtil.getAESKey();
            /** 生成AES密钥key字节流 */
            byte[] keyBytes = keyStr.getBytes("UTF-8");
            /** 对请求数据进行AES加密，并进行base64编码 */
            String encryptReqData = new String(Base64.encodeBase64(SMCryptUtil.AESEncrypt(reqDataByte, keyBytes, "AES", "AES/ECB/PKCS5Padding", null)), "UTF-8");
            /** 对请求数据进行哈希签名RSA加密，并进行base64编码 */
            String signReqData = new String(Base64.encodeBase64(SMCryptUtil.digitalSign(reqDataByte, hzfPriKey, "SHA1WithRSA")), "UTF-8");
            /** 对AES进行RSA加密，并进行base64编码 */
            String encrtptKey = new String(Base64.encodeBase64(SMCryptUtil.RSAEncrypt(keyBytes, yhPubKey, 2048, 11, "RSA/ECB/PKCS1Padding")), "UTF-8");
            
            JSONObject response = new JSONObject();
    		response.put("encryptData", encryptReqData);
    		response.put("encryptKey", encrtptKey);
    		response.put("tranCode", tranCode);
    		response.put("reqMsgId", reqMsgId);
    		response.put("signData", signReqData);
    		
    		return response.toJSONString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	@ResponseBody
	@PostMapping(value="/callback")
	public String callBack(){
		Map<String, String> map = new HashMap<>();
		
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("respCode", "000000");
		jsonObj.put("totalAmount", "2.00");
		LocalDateTime now = LocalDateTime.now();
		String nowOfStr = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		jsonObj.put("settleDate", nowOfStr);
		jsonObj.put("respType", "S");
		JSONObject jsonData = new JSONObject();
		jsonData.put("totalAmount", "2.00");
		jsonObj.put("data", jsonData);
		
		 Map<String, Object> keyMaps = new HashMap<String, Object>();
	        keyMaps.put("publickKey", smzfPubKey);
	        keyMaps.put("privateKey", smzfPriKey);
		
		  Map<String, Object> keys = null;
		try {
			keys = SMCryptUtil.getKeys(keyMaps);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 PrivateKey hzfPriKey = (PrivateKey) keys.get("hzfPriKey");
	     PublicKey yhPubKey = (PublicKey) keys.get("yhPubKey");
		
		 try {
				byte[] reqDataByte = jsonObj.toJSONString().getBytes("UTF-8");
				 /** 生成AES密钥key */
	            String keyStr = SMCryptUtil.getAESKey();
	            /** 生成AES密钥key字节流 */
	            byte[] keyBytes = keyStr.getBytes("UTF-8");
	            /** 对请求数据进行AES加密，并进行base64编码 */
	            String encryptReqData = new String(Base64.encodeBase64(SMCryptUtil.AESEncrypt(reqDataByte, keyBytes, "AES", "AES/ECB/PKCS5Padding", null)), "UTF-8");
	            /** 对请求数据进行哈希签名RSA加密，并进行base64编码 */
	            String signReqData = new String(Base64.encodeBase64(SMCryptUtil.digitalSign(reqDataByte, hzfPriKey, "SHA1WithRSA")), "UTF-8");
	            /** 对AES进行RSA加密，并进行base64编码 */
	            String encrtptKey = new String(Base64.encodeBase64(SMCryptUtil.RSAEncrypt(keyBytes, yhPubKey, 2048, 11, "RSA/ECB/PKCS1Padding")), "UTF-8");
	            
	            HttpClient httpClient = new HttpClient();
	    		CloseableHttpClient client = httpClient.getHttpClient();
	    		CloseableHttpResponse response = null;
	    		
	    		map.put("encryptData", encryptReqData);
	    		map.put("encryptKey", encrtptKey);
	    		map.put("tranCode", "SMZF002");
	    		map.put("reqMsgId", globalReqMsgId);
	    		
	    		response = httpClient.doPostSendForm(client, null, 
						"http://192.168.18.140:8282/PayServer/CallBack.do?ScanCodeType=01", map);
				System.out.println(httpClient.getResBody(response));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		return "000000";
		
	}
	
}
