package com.platform.apps.thirdpartymock.rule.upzf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONObject;
import com.platform.apps.thirdpartymock.service.UPzfService;
import com.platform.apps.thirdpartymock.util.SMCryptUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.dom4j.Node;

/**
 * 扫码支付接口
 * @author xinxiang
 *
 */
@Component
public class SMZF002Rule extends UPZFRule {
	
	public static Logger logger = LoggerFactory.getLogger(SMZF002Rule.class);
	
	@Autowired
	UPzfService upzfService;
	
	public SMZF002Rule() {
		// TODO Auto-generated constructor stub
		this.name = "SMZF002";
	}

	@Override
	public JSONObject getRuleResponse(JSONObject reqJson) {
		// TODO Auto-generated method stub
		super.init();
		
		JSONObject response = new JSONObject();
		
		Node resVersion = this.docResponse.selectSingleNode("/ROOT/encryptData/version");
		
		Node resMsgType = this.docResponse.selectSingleNode("/ROOT/encryptData/msgType");
		
		Node resSmzfMsgId = this.docResponse.selectSingleNode("/ROOT/encryptData/smzfMsgId");
		
		if("auto".equals(resSmzfMsgId.getText())){//设置响应流水号
			String reqMsgId = reqJson.getString("reqMsgId");
			resSmzfMsgId.setText("B" + reqMsgId);
		}
		
		response.put("reqMsgId", reqJson.getString("reqMsgId"));
		
		Node resReqDate = this.docResponse.selectSingleNode("/ROOT/encryptData/reqDate");
		
		if("auto".equals(resReqDate.getText())){//设置请求日期
			String reqDate = reqJson.getJSONObject("encryptData").getString("reqDate");
			resSmzfMsgId.setText(reqDate);
		}
		
		Node resRespDate = this.docResponse.selectSingleNode("/ROOT/encryptData/respDate");
		
		if("auto".equals(resRespDate.getText())){//设置应答日期
			LocalDateTime now = LocalDateTime.now();
			String nowOfStr = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
			resSmzfMsgId.setText(nowOfStr);
		}
		
		Node resRespType = this.docResponse.selectSingleNode("/ROOT/encryptData/respType");
		
		Node resRespCode = this.docResponse.selectSingleNode("/ROOT/encryptData/respCode");
		
		Node resRespMsg = this.docResponse.selectSingleNode("/ROOT/encryptData/respMsg");
		
		Node resQrCode = this.docResponse.selectSingleNode("/ROOT/encryptData/data/qrCode");
		
		//拼装请求字段
		JSONObject resData = new JSONObject();
		
		resData.put("respCode", resRespCode.getText());
		resData.put("respDate", resRespDate.getText());
		resData.put("respMsg", resRespMsg.getText());
		resData.put("smzfMsgId", resSmzfMsgId.getText());
		resData.put("respType", resRespType.getText());
		resData.put("msgType", resMsgType.getText());
		resData.put("reqDate", resReqDate.getText());
		resData.put("version", resVersion.getText());
		
		JSONObject jsonData = new JSONObject();
		jsonData.put("qrCode", resQrCode.getText());
		
		resData.put("data", jsonData.toJSONString());
		
		Map<String, String> map = null;
		
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
		
		//请求交易金额
		String totalAmount = reqJson.getJSONObject("encryptData").getJSONObject("data").getString("totalAmount");
		
		//请求交易手续费
		String totalFee = reqJson.getJSONObject("encryptData").getJSONObject("data").getString("totalFee");
		
		//保存订单信息
		upzfService.add(reqJson.getString("reqMsgId"), resSmzfMsgId.getText(), reqJson.getString("callBack"), totalAmount, totalFee, (short)0);
		
		return response;
	}

}
