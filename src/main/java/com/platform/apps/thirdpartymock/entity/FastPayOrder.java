package com.platform.apps.thirdpartymock.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class FastPayOrder {
	
	@Id
	@GeneratedValue
	Integer id;
	String systemNo;
	String orderNo;
	
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
	
	
}
