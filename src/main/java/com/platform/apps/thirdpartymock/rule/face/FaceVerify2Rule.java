package com.platform.apps.thirdpartymock.rule.face;

import org.dom4j.Node;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONObject;

@Component("FaceVerify2Rule")
public class FaceVerify2Rule extends FaceRule {

	public FaceVerify2Rule() {
		// TODO Auto-generated constructor stub
		this.name = "FaceVerify2";
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
		
		JSONObject jsonOfFaceGenuineness = new JSONObject();
		
		Node nMaskConfidence = this.docResponse.selectSingleNode("/ROOT/face_genuineness/mask_confidence");
		
		if(null != nMaskConfidence){
			jsonOfFaceGenuineness.put("mask_confidence", Float.parseFloat(nMaskConfidence.getText()));
		}
		
		Node nScreenReplayConfidence = this.docResponse.selectSingleNode("/ROOT/face_genuineness/screen_replay_confidence");
		
		if(null != nScreenReplayConfidence){
			jsonOfFaceGenuineness.put("screen_replay_confidence", Float.parseFloat(nScreenReplayConfidence.getText()));
		}
		
		Node nMaskThreshold = this.docResponse.selectSingleNode("/ROOT/face_genuineness/mask_threshold");
		
		if(null != nMaskThreshold){
			jsonOfFaceGenuineness.put("mask_threshold", Float.parseFloat(nMaskThreshold.getText()));
		}
		
		Node nSyntheticFaceConfidence = this.docResponse.selectSingleNode("/ROOT/face_genuineness/synthetic_face_confidence");
		
		if(null != nSyntheticFaceConfidence){
			jsonOfFaceGenuineness.put("synthetic_face_confidence", Float.parseFloat(nSyntheticFaceConfidence.getText()));
		}
		
		Node nSyntheticFaceThreshold = this.docResponse.selectSingleNode("/ROOT/face_genuineness/synthetic_face_threshold");
		
		if(null != nSyntheticFaceThreshold){
			jsonOfFaceGenuineness.put("synthetic_face_threshold", Float.parseFloat(nSyntheticFaceThreshold.getText()));
		}
		
		Node nScreenReplayThreshold = this.docResponse.selectSingleNode("/ROOT/face_genuineness/screen_replay_threshold");
		
		if(null != nScreenReplayThreshold){
			jsonOfFaceGenuineness.put("screen_replay_threshold", Float.parseFloat(nScreenReplayThreshold.getText()));
		}
		
		Node nFaceReplaced = this.docResponse.selectSingleNode("/ROOT/face_genuineness/face_replaced");
		
		if(null != nFaceReplaced){
			jsonOfFaceGenuineness.put("face_replaced", Integer.parseInt(nFaceReplaced.getText()));
		}
		
		response.put("face_genuineness", jsonOfFaceGenuineness);
		
		return response;
	}
}
