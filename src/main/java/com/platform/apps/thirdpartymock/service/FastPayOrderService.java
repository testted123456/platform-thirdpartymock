package com.platform.apps.thirdpartymock.service;

import com.platform.apps.thirdpartymock.entity.FastPayOrder;

public interface FastPayOrderService {

	public void add(String systemNo, String orderNo);
	
	public FastPayOrder getBySystemNo(String systemNo);
}
