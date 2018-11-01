package com.platform.apps.thirdpartymock.rule.jz;

import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONObject;

@Component
public class JZIDAuthRule extends JZAuthRule {
	
	public JZIDAuthRule() {
		// TODO Auto-generated constructor stub
		this.name = "JZIDAuth";
	}

	@Override
	public JSONObject getRuleResponse(JSONObject reqJson) {
		// TODO Auto-generated method stub
		return null;
	}

}
