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
import com.platform.apps.thirdpartymock.service.UPzfService;
import com.platform.apps.thirdpartymock.util.SMCryptUtil;

/**
 * 银联主扫，支付成功后调用瑞银信的查询接口
 * @author xinxiang
 *
 */
@Component
public class SMZF006Rule extends UPZFRule {
	
	public static Logger logger = LoggerFactory.getLogger(SMZF006Rule.class);
	
	@Autowired
	UPzfService upzfService;
	
	public SMZF006Rule() {
		// TODO Auto-generated constructor stub
		this.name = "SMZF006";
	}

	@Override
	public JSONObject getRuleResponse(JSONObject reqJson) {
		// TODO Auto-generated method stub
		super.init();
		
		String reqMsgId = reqJson.getString("reqMsgId");
		
		//从request中取原始订单号
		String oriReqMsgId = reqJson.getJSONObject("encryptData").getJSONObject("data").getString("oriReqMsgId");
		
		JSONObject response = new JSONObject();
		
		Node resVersion = this.docResponse.selectSingleNode("/ROOT/encryptData/version");
		
		Node resMsgType = this.docResponse.selectSingleNode("/ROOT/encryptData/msgType");
		
		Node resSmzfMsgId = this.docResponse.selectSingleNode("/ROOT/encryptData/smzfMsgId");
		
		if("auto".equals(resSmzfMsgId.getText())){//设置响应流水号
			resSmzfMsgId.setText(upzfService.findByReqMsgId(oriReqMsgId).getSmzfMsgId());
		}
		
		response.put("reqMsgId", resSmzfMsgId.getText());
		
		Node resReqDate = this.docResponse.selectSingleNode("/ROOT/encryptData/reqDate");
		
		if("auto".equals(resReqDate.getText())){//设置请求日期
			String reqDate = reqJson.getJSONObject("encryptData").getString("reqDate");
			resSmzfMsgId.setText(reqDate);
		}
		
		Node resRespDate = this.docResponse.selectSingleNode("/ROOT/encryptData/respDate");
		
		LocalDateTime now = LocalDateTime.now();
		String nowOfStr = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		
		if("auto".equals(resRespDate.getText())){//设置应答日期
			resSmzfMsgId.setText(nowOfStr);
		}
		
		Node resRespType = this.docResponse.selectSingleNode("/ROOT/encryptData/respType");
		
		Node resRespCode = this.docResponse.selectSingleNode("/ROOT/encryptData/respCode");
		
		Node resRespMsg = this.docResponse.selectSingleNode("/ROOT/encryptData/respMsg");
		
		Node resSettleDate = this.docResponse.selectSingleNode("/ROOT/encryptData/settleDate");
		
		if("auto".equals(resSettleDate.getText())){
			resSettleDate.setText(nowOfStr);
		}
		
		Node resTotalAmount = this.docResponse.selectSingleNode("/ROOT/encryptData/totalAmount");
		
		if("auto".equals(resTotalAmount.getText())){
			resTotalAmount.setText(upzfService.findByReqMsgId(oriReqMsgId).getTotalAmount());
		}
		
		Node resOriRespType = this.docResponse.selectSingleNode("/ROOT/encryptData/data/oriRespType");
		
		Node resOriRespCode = this.docResponse.selectSingleNode("/ROOT/encryptData/data/oriRespCode");
		
		Node resOriRespMsg = this.docResponse.selectSingleNode("/ROOT/encryptData/data/oriRespMsg");
		
		Node resTotalFee = this.docResponse.selectSingleNode("/ROOT/encryptData/data/totalFee");
		
		//拼装请求字段
		JSONObject resData = new JSONObject();
		
		resData.put("respCode", resRespCode.getText());
		resData.put("respDate", resRespDate.getText());
		resData.put("respMsg", resRespMsg.getText());
		resData.put("smzfMsgId", resSmzfMsgId.getText());
		resData.put("respType", resRespType.getText());
		resData.put("settleDate", resSettleDate.getText());
		resData.put("totalAmount", resTotalAmount.getText());
		resData.put("msgType", resMsgType.getText());
		resData.put("reqDate", resReqDate.getText());
		resData.put("version", resVersion.getText());
		
		JSONObject jsonData = new JSONObject();
		jsonData.put("oriRespType", resOriRespType.getText());
		jsonData.put("oriRespCode", resOriRespCode.getText());
		jsonData.put("oriRespMsg", resOriRespMsg.getText());
		jsonData.put("totalFee", resTotalFee.getText());
		
		resData.put("data", jsonData);
		
		Map<String, String> map = new HashMap<>();
		//加密请求
		try {
			map = SMCryptUtil.encryptData(resData);
			response.put("encryptData", map.get("encryptData"));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Node resEncryptKey = this.docResponse.selectSingleNode("/ROOT/encryptKey");
		
		if("auto".equals(resEncryptKey.getText())){
			try {
				resEncryptKey.setText(map.get("encryptKey"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		response.put("encryptKey", resEncryptKey.getText());
		
		//请求加签
		Node resSignData = this.docResponse.selectSingleNode("/ROOT/signData");
		
		if("auto".equals(resSignData.getText())){
			try {
				resSignData.setText(map.get("signData"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		response.put("signData", resSignData.getText());
		
		Node resTranCode = this.docResponse.selectSingleNode("/ROOT/tranCode");
		
		response.put("tranCode", resTranCode.getText());
		
		Node resReqMsgId = this.docResponse.selectSingleNode("/ROOT/reqMsgId");
		
		if("auto".equals(resReqMsgId.getText())){
			resReqMsgId.setText(reqMsgId);
		}
		
		response.put("reqMsgId", resReqMsgId.getText());
		
		return response;
	}

}
