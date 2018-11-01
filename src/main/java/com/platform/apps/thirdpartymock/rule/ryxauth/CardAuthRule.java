package com.platform.apps.thirdpartymock.rule.ryxauth;

import org.dom4j.Node;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONObject;

@Component("CardAuthRule")
public class CardAuthRule extends RyxAuthRule {
	
	public CardAuthRule() {
		// TODO Auto-generated constructor stub
		this.name = "CardAuth";
	}

	@Override
	public JSONObject getRuleResponse(JSONObject reqJson) {
		// TODO Auto-generated method stub
		super.init();
		
		JSONObject response = new JSONObject();
		
		Node responseCode = this.docResponse.selectSingleNode("/ROOT/ResponseCode");
		
		if(null != responseCode && null != responseCode.getText()){
			response.put("ResponseCode", responseCode.getText());
		}
		
		Node responseMsg = this.docResponse.selectSingleNode("/ROOT/ResponseMsg");
		
		if(null != responseMsg && null != responseMsg.getText()){
			response.put("ResponseMsg", responseMsg.getText());
		}
		
		Node result = this.docResponse.selectSingleNode("/ROOT/Result");
		
		if(null != result && null != result.getText()){
			response.put("Result", result.getText());
		}
		
		Node resultMsg = this.docResponse.selectSingleNode("/ROOT/ResultMsg");
		
		if(null != resultMsg && null != resultMsg.getText()){
			response.put("ResultMsg", resultMsg.getText());
		}
		
		return response;
	}
	
}
