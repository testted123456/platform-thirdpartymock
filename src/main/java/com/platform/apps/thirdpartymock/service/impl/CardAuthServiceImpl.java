package com.platform.apps.thirdpartymock.service.impl;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.platform.apps.thirdpartymock.rule.ryxauth.CardAuthRule;
import com.platform.apps.thirdpartymock.service.RyxAuthService;

@Service
public class CardAuthServiceImpl implements RyxAuthService {
	
	@Autowired
	CardAuthRule cardAuthRule;
	
	/**
	 * 卡鉴权响应恢复默认
	 */
	@Override
	public boolean reset() {
		return cardAuthRule.setDefault("cardauth");
	}

	/**
	 * 获取响应xml
	 */
	@Override
	public String getContent() {
		// TODO Auto-generated method stub
		Map<String, String> map = cardAuthRule.getInfos();
		
		if(null != map){
			return map.get("response");
		}
		
		return null;
	}

	@Override
	public String getResponseData() {
		// TODO Auto-generated method stub
		JSONObject reqJson = new JSONObject();
		JSONObject resJson = cardAuthRule.getRuleResponse(reqJson);
		
		if(null != resJson){
			String responseData = resJson.toJSONString().replace("\r\n", "").replace(" ", "");
			return responseData;
		}
		
		return null;
	}

}
