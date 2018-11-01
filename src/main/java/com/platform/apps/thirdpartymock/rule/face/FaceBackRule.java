package com.platform.apps.thirdpartymock.rule.face;

import org.dom4j.Node;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONObject;

@Component("FaceBackRule")
public class FaceBackRule extends FaceRule {
	
	public FaceBackRule() {
		// TODO Auto-generated constructor stub
		this.name = "FaceBack";
	}

	@Override
	public JSONObject getRuleResponse(JSONObject reqJson) {
		// TODO Auto-generated method stub
		
		super.init();
		
		JSONObject response = new JSONObject();
		
		Node nTimeUsed = this.docResponse.selectSingleNode("/ROOT/time_used");
		
		if(null != nTimeUsed){
			response.put("time_used", nTimeUsed.getText());
		}
		
		Node nRequestId = this.docResponse.selectSingleNode("/ROOT/request_id");
		
		if(null != nRequestId){
			response.put("request_id", nRequestId.getText());
		}
		
		Node nValidDate  = this.docResponse.selectSingleNode("/ROOT/valid_date");
		
		if(null != nValidDate){
			response.put("valid_date", nValidDate.getText());
		}
		
		Node nIssuedBy  = this.docResponse.selectSingleNode("/ROOT/issued_by");
		
		if(null != nIssuedBy){
			response.put("issued_by", nIssuedBy.getText());
		}
		
		Node nSide = this.docResponse.selectSingleNode("/ROOT/side");
		
		if(null != nSide){
			response.put("side", nSide.getText());
		}
		
		JSONObject jsonOfLegality = new JSONObject();
		
		Node nEdited = this.docResponse.selectSingleNode("/ROOT/legality/Edited");
		
		if(null != nEdited){
			jsonOfLegality.put("Edited", nEdited.getText());
		}
		
		Node nPhotocopy = this.docResponse.selectSingleNode("/ROOT/legality/Photocopy");
		
		if(null != nPhotocopy){
			jsonOfLegality.put("Photocopy", nPhotocopy.getText());
		}
		
		Node nIDPhoto = this.docResponse.selectSingleNode("/ROOT/legality/ID_Photo");
		
		if(null != nIDPhoto){
			jsonOfLegality.put("ID Photo", nIDPhoto.getText());
		}
		
		Node nScreen = this.docResponse.selectSingleNode("/ROOT/legality/Screen");
		
		if(null != nScreen){
			jsonOfLegality.put("Screen", nScreen.getText());
		}
		
		Node nTemporaryIDPhoto = this.docResponse.selectSingleNode("/ROOT/legality/Temporary_ID_Photo");
		
		if(null != nTemporaryIDPhoto){
			jsonOfLegality.put("Temporary ID Photo", nTemporaryIDPhoto.getText());
		}
		
		response.put("legality", jsonOfLegality);
		
		return response;
	}

}
