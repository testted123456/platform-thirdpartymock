package com.platform.apps.thirdpartymock.rule.upzf;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONObject;
import com.platform.apps.thirdpartymock.entity.UPzf;
import com.platform.apps.thirdpartymock.service.UPzfService;
import com.platform.apps.thirdpartymock.util.SMCryptUtil;

/**
 * 扫码支付回调接口
 * @author xinxiang
 *
 */
@Component
public class SMZF008Rule extends UPZFRule {
	
	public static Logger logger = LoggerFactory.getLogger(SMZF008Rule.class);
	
	@Autowired
	UPzfService upzfService;
	
	public SMZF008Rule() {
		// TODO Auto-generated constructor stub
		this.name = "SMZF008";
	}

	@Override
	public JSONObject getRuleResponse(JSONObject reqJson) {
		super.init();
		
		JSONObject response = new JSONObject();
		
		String reqMsgId = reqJson.getString("reqMsgId");
		
		UPzf upzf = upzfService.findByReqMsgId(reqMsgId);
		
		response.put("reqMsgId", reqMsgId);
	
		Node reqTranCode = this.docRequest.selectSingleNode("/ROOT/tranCode");
		
		response.put("tranCode", reqTranCode.getText());
		
		String totalAmount = null;
		Node reqTotalAmount = this.docRequest.selectSingleNode("/ROOT/encryptData/data/totalAmount");
		
		if("auto".equals(reqTotalAmount.getText())){
			totalAmount = upzf.getTotalAmount();
		}
		
		String settleDate = null;
		Node reqSettleDate = this.docRequest.selectSingleNode("/ROOT/encryptData/data/settleDate");
		
		LocalDateTime now = LocalDateTime.now();
		String nowOfStr = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		
		if("auto".equals(reqSettleDate.getText())){
			settleDate = nowOfStr;
		}
		
		Node reqRespType = this.docRequest.selectSingleNode("/ROOT/encryptData/respType");
		String respType = reqRespType.getText();
		
		Node reqRespCode = this.docRequest.selectSingleNode("/ROOT/encryptData/respCode");
		String respCode = reqRespCode.getText();
		
		//封装请求
		JSONObject requestOfJson = new JSONObject();
		requestOfJson.put("respCode", respCode);
		requestOfJson.put("totalAmount", totalAmount);
		requestOfJson.put("settleDate", settleDate);
		requestOfJson.put("respType", respType);
		JSONObject jsonData = new JSONObject();
		jsonData.put("totalAmount", totalAmount);
		jsonData.put("settleDate", settleDate);
		jsonData.put("respType", respType);
		requestOfJson.put("data", jsonData);
		
		Map<String, String> map = new HashMap<>();
		//加密请求
		try {
			map = SMCryptUtil.encryptData(requestOfJson);
			response.put("encryptData", map.get("encryptData"));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Node reqEncryptKey = this.docRequest.selectSingleNode("/ROOT/encryptKey");
		
		if("auto".equals(reqEncryptKey.getText())){
			try {
				response.put("encryptKey", map.get("encryptKey"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return response;
	}
}
