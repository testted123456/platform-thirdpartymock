package com.platform.apps.thirdpartymock.service;

import java.util.List;

import com.platform.apps.thirdpartymock.entity.FastPayOrder;

public interface FastPayOrderService {

	public void add(String systemNo, String orderNo, String cardNo, Short optStatus);
	
	public void updateStatus(String systemNo, Short optStatus);
	
	public FastPayOrder getBySystemNo(String systemNo);
	
	public FastPayOrder getByOrderNo(String orderNo);
	
	public List<FastPayOrder> getFastPayCallBack();
}
