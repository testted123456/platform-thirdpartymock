package com.platform.apps.thirdpartymock.rule.face;

import org.dom4j.Node;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONObject;

@Component("FaceVerify1Rule")
public class FaceVerify1Rule extends FaceRule {

	public FaceVerify1Rule() {
		// TODO Auto-generated constructor stub
		this.name = "FaceVerify1";
	}
	
	@Override
	public JSONObject getRuleResponse(JSONObject reqJson) {
		// TODO Auto-generated method stub
		super.init();
		
		JSONObject response = new JSONObject();
		
		Node nTimeUsed = this.docResponse.selectSingleNode("/ROOT/time_used");
		
		if(null != nTimeUsed){
			response.put("time_used", Integer.parseInt(nTimeUsed.getText()));
		}
		
		Node nRequestId = this.docResponse.selectSingleNode("/ROOT/request_id");
		
		if(null != nRequestId){
			response.put("request_id", nRequestId.getText());
		}
		
		JSONObject jsonOfResultFaceid = new JSONObject();
		
		Node nConfidence = this.docResponse.selectSingleNode("/ROOT/result_faceid/confidence");

		if(null != nConfidence){
			jsonOfResultFaceid.put("confidence", Float.parseFloat(nConfidence.getText()));
		}
		
		JSONObject jsonOfThresholds = new JSONObject();
		
		Node n1e3 = this.docResponse.selectSingleNode("/ROOT/result_faceid/thresholds/le-3");
		
		jsonOfThresholds.put("1e-3", Float.parseFloat(n1e3.getText()));
		
		Node n1e4 = this.docResponse.selectSingleNode("/ROOT/result_faceid/thresholds/le-4");
		
		jsonOfThresholds.put("1e-4", Float.parseFloat(n1e4.getText()));
		
		Node n1e5 = this.docResponse.selectSingleNode("/ROOT/result_faceid/thresholds/le-5");
		
		jsonOfThresholds.put("1e-5", Float.parseFloat(n1e5.getText()));
		
		Node n1e6 = this.docResponse.selectSingleNode("/ROOT/result_faceid/thresholds/le-6");
		
		jsonOfThresholds.put("1e-6", Float.parseFloat(n1e6.getText()));
		
		jsonOfResultFaceid.put("thresholds", jsonOfThresholds);
		
		response.put("result_faceid", jsonOfResultFaceid);
		
		JSONObject jsonOfIdExceptions = new JSONObject();
		
		Node nIdPhotoMonochrome = this.docResponse.selectSingleNode("/ROOT/id_exceptions/id_photo_monochrome");
		
		jsonOfIdExceptions.put("id_photo_monochrome", Integer.parseInt(nIdPhotoMonochrome.getText()));

		Node nIdAttacked = this.docResponse.selectSingleNode("/ROOT/id_exceptions/id_attacked");
		
		jsonOfIdExceptions.put("id_attacked", Integer.parseInt(nIdAttacked.getText()));
		
		response.put("id_exceptions", jsonOfIdExceptions);
		
		return response;
	}

}
