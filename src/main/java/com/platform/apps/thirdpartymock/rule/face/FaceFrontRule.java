package com.platform.apps.thirdpartymock.rule.face;

import org.dom4j.Node;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONObject;
import com.platform.apps.thirdpartymock.util.IdCardGeneratorUtil;

@Component("FaceFrontRule")
public class FaceFrontRule extends FaceRule {
	
	int year;
	
	int month;
	
	int day;
	
	public FaceFrontRule() {
		// TODO Auto-generated constructor stub
		this.name = "FaceFront";
	}

	@Override
	public JSONObject getRuleResponse(JSONObject reqJson) {
		// TODO Auto-generated method stub
		
		super.init();
		
		JSONObject response = new JSONObject();
		
		Node nRace = this.docResponse.selectSingleNode("/ROOT/race");
		
		if(null != nRace){
			response.put("race", nRace.getText());
		}
		
		Node nName = this.docResponse.selectSingleNode("/ROOT/name");
		
		if(null != nName){
			response.put("name", nName.getText());
		}
		
		Node nTimeUsed = this.docResponse.selectSingleNode("/ROOT/time_used");
		
		if(null != nTimeUsed){
			response.put("time_used", nTimeUsed.getText());
		}
		
		Node nGender = this.docResponse.selectSingleNode("/ROOT/gender");
		
		if(null != nGender){
			response.put("gender", nGender.getText());
		}
		
		//
		Node nIdCardNumber = this.docResponse.selectSingleNode("/ROOT/id_card_number");
		
		if(null != nIdCardNumber){
			String id = nIdCardNumber.getText();
			
			if("auto".equals(id)){
				year = (int)(Math.random()*10);
				month = (int) (Math.random() * 12);
				day = (int) (Math.random() * 29);
				id = IdCardGeneratorUtil.generateByYearMonthDay(year+1985, month+1, day+1);
				response.put("id_card_number", id);
			}else if(null != id){
				response.put("id_card_number", id);
			}
		}
		
		//
		Node nRequestId = this.docResponse.selectSingleNode("/ROOT/request_id");
		
		if(null != nRequestId){
			response.put("request_id", nRequestId.getText());
		}
		
		//
		JSONObject jsonOfBirthday = new JSONObject();
		Node nYear = this.docResponse.selectSingleNode("/ROOT/birthday/year");
		
		if(null != nYear){
			if("auto".equals(nYear.getText())){
				jsonOfBirthday.put("year", year);
			}else if(null != nYear.getText()){
				jsonOfBirthday.put("year", nYear.getText());
			}
		}
		
		Node nDay = this.docResponse.selectSingleNode("/ROOT/birthday/day");
		
		if(null != nDay){
			if("auto".equals(nDay.getText())){
				jsonOfBirthday.put("day", day);
			}else if(null != nDay.getText()){
				jsonOfBirthday.put("day", nDay.getText());
			}
		}
		
		Node nMonth = this.docResponse.selectSingleNode("/ROOT/birthday/month");
		
		if(null != nMonth){
			if("auto".equals(nMonth.getText())){
				jsonOfBirthday.put("month", month);
			}else if(null != nMonth.getText()){
				jsonOfBirthday.put("month", nMonth.getText());
			}
		}
		
		response.put("birthday", jsonOfBirthday);
		
		JSONObject jsonOfLegality = new JSONObject();
		
		Node nEdited = this.docResponse.selectSingleNode("/ROOT/legality/Edited");
		
		if(null != nEdited){
			jsonOfLegality.put("Edited", nEdited.getText());
		}
		
		Node nPhotocopy = this.docResponse.selectSingleNode("/ROOT/legality/Photocopy");
		
		if(null != nEdited){
			jsonOfLegality.put("Photocopy", nPhotocopy.getText());
		}
		
		Node nID_Photo = this.docResponse.selectSingleNode("/ROOT/legality/ID_Photo");
		
		if(null != nID_Photo){
			jsonOfLegality.put("ID Photo", nID_Photo.getText());
		}
		
		Node nScreen = this.docResponse.selectSingleNode("/ROOT/legality/Screen");
		
		if(null != nScreen){
			jsonOfLegality.put("Screen", nScreen.getText());
		}
		
		Node nTemporaryIdPhoto = this.docResponse.selectSingleNode("/ROOT/legality/Temporary_ID_Photo");
		
		if(null != nTemporaryIdPhoto){
			jsonOfLegality.put("Temporary ID Photo", nTemporaryIdPhoto.getText());
		}
		
		response.put("legality", jsonOfLegality);
		
		Node nAddress = this.docResponse.selectSingleNode("/ROOT/address");
		
		if(null != nAddress){
			jsonOfLegality.put("address", nAddress.getText());
		}
		
		JSONObject jsonOfHeadRect = new JSONObject();
		
		JSONObject jsonOfRt = new JSONObject();
		
		Node nRTX = this.docResponse.selectSingleNode("/ROOT/head_rect/rt/x");
		
		if(null != nRTX){
			jsonOfRt.put("x", nRTX.getText());
		}
		
		Node nRTY = this.docResponse.selectSingleNode("/ROOT/head_rect/rt/y");
		
		if(null != nRTY){
			jsonOfRt.put("y", nRTY.getText());
		}
		
		JSONObject jsonOfLt = new JSONObject();
		
		Node nLTX = this.docResponse.selectSingleNode("/ROOT/head_rect/lt/x");
		
		if(null != nLTX){
			jsonOfLt.put("x", nLTX.getText());
		}
		
		Node nLTY = this.docResponse.selectSingleNode("/ROOT/head_rect/lt/y");
		
		if(null != nLTY){
			jsonOfLt.put("y", nLTY.getText());
		}
		
		JSONObject jsonOfLb = new JSONObject();
		
		Node nLBX = this.docResponse.selectSingleNode("/ROOT/head_rect/lb/x");
		
		if(null != nLBX){
			jsonOfLb.put("x", nLBX.getText());
		}
		
		Node nLBY = this.docResponse.selectSingleNode("/ROOT/head_rect/lb/y");
		
		if(null != nLBY){
			jsonOfLb.put("y", nLBY.getText());
		}
		
		JSONObject jsonOfRb = new JSONObject();
		
		Node nRBX = this.docResponse.selectSingleNode("/ROOT/head_rect/rb/x");
		
		if(null != nRBX){
			jsonOfRb.put("x", nRBX.getText());
		}
		
		Node nRBY = this.docResponse.selectSingleNode("/ROOT/head_rect/rb/y");
		
		if(null != nRBY){
			jsonOfRb.put("y", nRBY.getText());
		}
		
		jsonOfHeadRect.put("rt", jsonOfRt);
		
		jsonOfHeadRect.put("lt", jsonOfLt);
		
		jsonOfHeadRect.put("lb", jsonOfLt);
		
		jsonOfHeadRect.put("rb", jsonOfRb);
		
		response.put("head_rect", jsonOfHeadRect);
		
		Node nSide = this.docResponse.selectSingleNode("/ROOT/side");
		
		if(null != nSide){
			response.put("side", nSide.getText());
		}
		
		return response;
	}

}
