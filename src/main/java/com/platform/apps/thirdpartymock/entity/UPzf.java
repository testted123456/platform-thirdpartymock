package com.platform.apps.thirdpartymock.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class UPzf {

	@Id
	@GeneratedValue
	Integer id;

	@Column(nullable = false, columnDefinition = "varchar(255) COMMENT '请求流水号'")
	String reqMsgId;

	@Column(nullable = true, columnDefinition = "varchar(255) COMMENT '扫码支付平台流水号'")
	String smzfMsgId;

	@Column(nullable = true, columnDefinition = "varchar(1024) COMMENT '回调地址'")
	String callBackUrl;
	
	@Column(nullable = true, columnDefinition = "varchar(255) COMMENT '交易金额'")
	String totalAmount;
	
	@Column(nullable = true, columnDefinition = "varchar(255) COMMENT '交易手续费'")
	String totalFee;

	@Column(nullable = false, columnDefinition = "smallint(1) COMMENT '订单状态，0：未处理，1：成功，2：失败'")
	Short optStatus;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getReqMsgId() {
		return reqMsgId;
	}

	public void setReqMsgId(String reqMsgId) {
		this.reqMsgId = reqMsgId;
	}

	public String getSmzfMsgId() {
		return smzfMsgId;
	}

	public void setSmzfMsgId(String smzfMsgId) {
		this.smzfMsgId = smzfMsgId;
	}

	public String getCallBackUrl() {
		return callBackUrl;
	}

	public void setCallBackUrl(String callBackUrl) {
		this.callBackUrl = callBackUrl;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public Short getOptStatus() {
		return optStatus;
	}

	public void setOptStatus(Short optStatus) {
		this.optStatus = optStatus;
	}

}
