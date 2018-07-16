package com.platform.apps.thirdpartymock.rule;

import org.dom4j.Document;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BK003Rule extends Rule{
	
	public static Logger logger = LoggerFactory.getLogger(BK003Rule.class);

	public BK003Rule() {
		this.name = "BK003";
	}
	
	@Override
	public String getRuleResponse(Document reqDocument) {
		Node reqMerchId = reqDocument.selectSingleNode("/ROOT/MSG_HEAD/merchId");
		Node resMerchId = this.docResponse.selectSingleNode("/resp/merchId");
		
		if("auto".equals(resMerchId.getText())) {
			resMerchId.setText(reqMerchId.getText());
		}
		
		Node reqTranCode = reqDocument.selectSingleNode("/ROOT/MSG_HEAD/tranCode");
		Node resTranCode = this.docResponse.selectSingleNode("/resp/tranCode");
		
		if("auto".equals(resTranCode.getText())) {
			resTranCode.setText(reqTranCode.getText());
		}
		
		Node reqSystemNo = reqDocument.selectSingleNode("/ROOT/MSG_HEAD/systemNo");
		Node resSystemNo = this.docResponse.selectSingleNode("/resp/systemNo");
		
		if("auto".equals(resSystemNo.getText())) {
			resSystemNo.setText(reqSystemNo.getText());
		}
		
		return this.docResponse.asXML();
	}
}
