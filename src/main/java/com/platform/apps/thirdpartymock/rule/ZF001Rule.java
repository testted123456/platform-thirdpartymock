package com.platform.apps.thirdpartymock.rule;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.dom4j.Document;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.platform.apps.thirdpartymock.service.FastPayOrderService;

@Component
public class ZF001Rule extends Rule{
	
	public static Logger logger = LoggerFactory.getLogger(ZF001Rule.class);

	@Autowired
	FastPayOrderService fastPayOrderService;
	
	public ZF001Rule() {
		this.name = "ZF001";
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
		
		Node resOrderNo = this.docResponse.selectSingleNode("/resp/orderNo");
		
		if("auto".equals(resOrderNo.getText())) {
			String randomNo = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
			resOrderNo.setText(randomNo);
		}
		
		Node cardNo = reqDocument.selectSingleNode("/ROOT/MSG_BODY/cardNo");
		
		fastPayOrderService.add(reqSystemNo.getText(), resOrderNo.getText(), cardNo.getText(), (short)0);
		
		return this.docResponse.asXML();
	}
}
