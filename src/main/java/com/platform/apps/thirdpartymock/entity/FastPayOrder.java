package com.platform.apps.thirdpartymock.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class FastPayOrder {
	
	@Id
	@GeneratedValue
	Integer id;
	
	@Column(nullable=false, columnDefinition="varchar(255) COMMENT '流水号'")
	String systemNo;
	
	@Column(nullable=false, columnDefinition="varchar(255) COMMENT '快捷支付上送渠道的订单号'")
	String orderNo;
	
	@Column(nullable=true, columnDefinition="varchar(255) COMMENT '银行卡号'")
	String cardNo;
	
	@Column(nullable=true, columnDefinition="varchar(255) COMMENT '支付金额'")
	String tranAmt;
	
	@Column(nullable=true, columnDefinition="smallint(1) COMMENT '0：订单生成，1：订单确认状态为不确定，2：回调支付成功，3：回调支付失败'")
	Short optStatus;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSystemNo() {
		return systemNo;
	}
	
	public void setSystemNo(String systemNo) {
		this.systemNo = systemNo;
	}
	
	public String getOrderNo() {
		return orderNo;
	}
	
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getTranAmt() {
		return tranAmt;
	}

	public void setTranAmt(String tranAmt) {
		this.tranAmt = tranAmt;
	}

	public Short getOptStatus() {
		return optStatus;
	}

	public void setOptStatus(Short optStatus) {
		this.optStatus = optStatus;
	}
	
}
