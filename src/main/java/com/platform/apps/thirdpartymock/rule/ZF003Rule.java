package com.platform.apps.thirdpartymock.rule;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.dom4j.Document;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.platform.apps.thirdpartymock.entity.FastPayOrder;
import com.platform.apps.thirdpartymock.service.FastPayOrderService;

@Component
public class ZF003Rule extends Rule{
	
	public static Logger logger = LoggerFactory.getLogger(ZF003Rule.class);

	@Autowired
	FastPayOrderService fastPayOrderService;
	
	public ZF003Rule() {
		this.name = "ZF003";
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
		
		Node settleDate = this.docResponse.selectSingleNode("/resp/settleDate");
		
		if("auto".equals(settleDate.getText())) {
			String sdate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			settleDate.setText(sdate);
		}
		
		Node resOrderNo = this.docResponse.selectSingleNode("/resp/orderNo");
		
		if("auto".equals(resOrderNo.getText())) {
			
			FastPayOrder fastPayOrder =	fastPayOrderService.getBySystemNo(reqSystemNo.getText());
			
			if(null != fastPayOrder) {
				String randomNo = fastPayOrder.getOrderNo();
				resOrderNo.setText(randomNo);
			}else {
				logger.error("快捷支付订单不存在， systemNo:{}", reqSystemNo);
			}
		}
		
		return this.docResponse.asXML();
	}
}
